package se.kth.webservice.project;

import se.kth.webservice.project.model.SemanticModelMapping;
import se.kth.webservice.project.model.XMLModelMapping;
import se.kth.webservice.project.output.ElementComparisonResult;
import se.kth.webservice.project.output.OperationComparisonResult;
import se.kth.webservice.project.output.WsdlComparisonResult;
import se.kth.webservice.project.output_model.*;
import se.kth.webservice.project.parsing.*;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
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
        String PROJECT_PATH = args[0];
        String JDBC_DRIVER = args[1];
        String DB_URL = args[2];
        String DB_USERNAME = args[3];
        String DB_USERPASSWORD = args[4];


        System.setProperty("PROJECT_PATH", PROJECT_PATH);
        System.setProperty("JDBC_DRIVER", JDBC_DRIVER);
        System.setProperty("DB_URL", DB_URL);
        System.setProperty("DB_USERNAME", DB_USERNAME);
        System.setProperty("DB_USERPASSWORD", DB_USERPASSWORD);
    }

    private static List<WsdlComparisonResult> remoteList;
    private static List<WsdlComparisonResult> localList;

    private static IComparable getRMIComparator(){

        //Instatiate the list to store the results in
        remoteList = new ArrayList<>();

        //Get the remote comparator
        IComparable remoteComparator = null;
        try {
            try {
                LocateRegistry.getRegistry(1099).list();
            } catch (RemoteException e) {
                LocateRegistry.createRegistry(1099);
            }
            remoteComparator = (IComparable) Naming.lookup("comparator");
        } catch (Exception e) {
            System.out.println("The runtime failed: " + e.getMessage());
            System.exit(0);
        }

        return remoteComparator;
    }

    private static IComparable getLocalComparator(){

        //Instatiate the list to store the results in
        localList = new ArrayList<>();

        IComparable localComparer = null;
        try {
            localComparer = new SyntacticComparator();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        return localComparer;
    }

    private static IComparable getLocalSemanticComparator(){

        //Instatiate the list to store the results in


        IComparable localComparer = null;
        try {
            localComparer = new SemanticComparator();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        return localComparer;
    }

    private static void doSyntacticComparisment(){
        IComparable localComparer = getLocalComparator();

        /*
        //There are 496 comparisments for 32 docs. A doc is not compared to itself.
        XMLFileHandler fileHandler = new XMLFileHandler(new OnCompare() {
            @Override
            public void compare(XMLModelMapping a, XMLModelMapping b) {

                // Only for remote
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            WsdlComparisonResult rating = finalRemoteComparator.getSimmilarityRating(a, b);
                            remoteList.add(rating);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();



                try {
                    WsdlComparisonResult rating = localComparer.getSimmilarityRating(b, a);
                    if (rating.getScore() > 0){
                        localList.add(rating);
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

            }
        });
        fileHandler.setup();
        fileHandler.process();
        fileHandler.startComparing();


        Collections.sort(localList, new Comparator<WsdlComparisonResult>() {
            @Override
            public int compare(WsdlComparisonResult o1, WsdlComparisonResult o2) {
                return Double.compare(o2.getScore(), o1.getScore());
            }
        });

        System.out.println(localList.size());
        output(localList);
        */

    }

    private static void doComparisment(IComparable comparable, FileHandler fileHandler){
        fileHandler.setup();
        fileHandler.process();
        fileHandler.startComparing();
    }

    public static void main (String args[]){

        setupSystemProps(args);

        //IWordnet repo = new WordnetSQL();
        //List<DictionaryLookup> lookups =  repo.lookupInDictionary("gravy");


        //IComparable remoteComparer = getRMIComparator();
        //IComparable localSemanticComparer = getLocalSemanticComparator();

        //doSyntacticComparisment();
        //doSemanticComparisment();


        doComparisment(getLocalSemanticComparator(), new SemanticFileHandler(new OnCompare<SemanticModelMapping>() {
            @Override
            public void compare(SemanticModelMapping a, SemanticModelMapping b) {
                System.out.println("I got some");
            }
        }));
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
