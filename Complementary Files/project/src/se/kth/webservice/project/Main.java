package se.kth.webservice.project;

import se.kth.webservice.project.data.IWordnet;
import se.kth.webservice.project.data.WordnetSQL;
import se.kth.webservice.project.model.DictionaryLookup;
import se.kth.webservice.project.model.XMLModelMapping;
import se.kth.webservice.project.output.ElementComparisonResult;
import se.kth.webservice.project.output.OperationComparisonResult;
import se.kth.webservice.project.output.WsdlComparisonResult;
import se.kth.webservice.project.output_model.*;
import se.kth.webservice.project.parsing.IComparable;
import se.kth.webservice.project.parsing.OnCompare;
import se.kth.webservice.project.parsing.SyntacticComparator;
import se.kth.webservice.project.parsing.XMLFileHandler;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by victoraxelsson on 2017-02-27.
 */
public class Main {

    private static int counter = 0;

    private static void setupSystemProps(String[] args){
        //Setup system properties
        String WSDLPATH = args[0];
        String JDBC_DRIVER = args[1];
        String DB_URL = args[2];
        String DB_USERNAME = args[3];
        String DB_USERPASSWORD = args[4];

        System.setProperty("WSDLPATH", WSDLPATH);
        System.setProperty("JDBC_DRIVER", JDBC_DRIVER);
        System.setProperty("DB_URL", DB_URL);
        System.setProperty("DB_USERNAME", DB_USERNAME);
        System.setProperty("DB_USERPASSWORD", DB_USERPASSWORD);
    }

    public static void main (String args[]){

        setupSystemProps(args);

        IComparable comparator = new SyntacticComparator();

        IWordnet repo = new WordnetSQL();
        List<DictionaryLookup> lookups =  repo.lookupInDictionary("gravy");

        List<WsdlComparisonResult> results = new ArrayList<>();

        //There are 496 comparisments for 32 docs. A doc is not compared to itself.
        XMLFileHandler fileHandler = new XMLFileHandler(new OnCompare() {
            @Override
            public void compare(XMLModelMapping a, XMLModelMapping b) {
                WsdlComparisonResult rating = comparator.getSimmilarityRating(a, b);
                results.add(rating);
                System.out.println(rating);
            }
        });
        fileHandler.setup();
        fileHandler.process();
        fileHandler.startComparing();

        System.out.println(results.size());
        output(results);

//        System.out.println("done, minScore is " + SyntacticComparator.minScore +
//                " number of operation pairs with less than 5 is " + SyntacticComparator.count);
//        System.out.println("less than 4 " + SyntacticComparator.counter4);
//        System.out.println("less than 3 " + SyntacticComparator.counter3);
//        System.out.println("less than 2 " + SyntacticComparator.counter2);
//        System.out.println("less than 1 " + SyntacticComparator.counter1);

    }

    private static void output(List<WsdlComparisonResult> results) {
        ObjectFactory objectFactory = new ObjectFactory();
        WSMatchingType wsMatchingType = objectFactory.createWSMatchingType();
        for (WsdlComparisonResult wsdlComparisonResult : results) {
            MatchedWebServiceType matchedWebServiceType = objectFactory.createMatchedWebServiceType();
            matchedWebServiceType.setOutputServiceName(wsdlComparisonResult.getFirst());
            matchedWebServiceType.setInputServiceName(wsdlComparisonResult.getSecond());
            matchedWebServiceType.setWsScore(wsdlComparisonResult.getScore());
            for (OperationComparisonResult operationComparisonResult : wsdlComparisonResult.getOperationComparisonResults()) {
                MatchedOperationType matchedOperationType = objectFactory.createMatchedOperationType();
                matchedOperationType.setInputOperationName(operationComparisonResult.getFirst());
                matchedOperationType.setOutputOperationName(operationComparisonResult.getSecond());
                matchedOperationType.setOpScore(operationComparisonResult.getScore());

                for (ElementComparisonResult elementComparisonResult : operationComparisonResult.getElementComparisonResults()) {
                    MatchedElementType matchedElementType = objectFactory.createMatchedElementType();
                    matchedElementType.setInputElement(elementComparisonResult.getFirst());
                    matchedElementType.setOutputElement(elementComparisonResult.getSecond());
                    matchedElementType.setScore(elementComparisonResult.getScore());
                    matchedOperationType.getMacthedElement().add(matchedElementType);
                }
                matchedWebServiceType.getMacthedOperation().add(matchedOperationType);
            }
            wsMatchingType.getMacthing().add(matchedWebServiceType);
        }
        saveToFile(wsMatchingType);
    }

    private static void saveToFile(WSMatchingType wsMatchingType) {
        try {
            javax.xml.bind.JAXBContext jaxbCtx = javax.xml.bind.JAXBContext.newInstance(wsMatchingType.getClass().getPackage().getName());
            javax.xml.bind.Marshaller marshaller = jaxbCtx.createMarshaller();
            marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_ENCODING, "UTF-8"); //NOI18N
            marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            OutputStream os = new FileOutputStream( new File("output.xml" ));
            marshaller.marshal( wsMatchingType, os );
            os.close();
        } catch (javax.xml.bind.JAXBException ex) {
            java.util.logging.Logger.getLogger("global").log(java.util.logging.Level.SEVERE, null, ex); //NOI18N
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
