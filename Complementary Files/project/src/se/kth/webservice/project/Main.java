package se.kth.webservice.project;

import se.kth.webservice.project.data.IWordnet;
import se.kth.webservice.project.data.WordnetSQL;
import se.kth.webservice.project.model.DictionaryLookup;
import se.kth.webservice.project.model.XMLModelMapping;
import se.kth.webservice.project.parsing.*;

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

        SemanticParser p = new SemanticParser();
        p.parse();

        /*
        IComparable comparator = new SyntacticComparator();


        IWordnet repo = new WordnetSQL();
        List<DictionaryLookup> lookups =  repo.lookupInDictionary("gravy");

        //There are 496 comparisments for 32 docs. A doc is not compared to itself.
        XMLFileHandler fileHandler = new XMLFileHandler(new OnCompare() {
            @Override
            public void compare(XMLModelMapping a, XMLModelMapping b) {
                float rating = comparator.getSimmilarityRating(a, b);
                System.out.println("Rating: " + rating);
            }
        });
        fileHandler.setup();
        fileHandler.process();
        fileHandler.startComparing();


        System.out.println("done, minScore is " + SyntacticComparator.minScore +
                " number of operation pairs with less than 5 is " + SyntacticComparator.count);
        System.out.println("less than 4 " + SyntacticComparator.counter4);
        System.out.println("less than 3 " + SyntacticComparator.counter3);
        System.out.println("less than 2 " + SyntacticComparator.counter2);
        System.out.println("less than 1 " + SyntacticComparator.counter1);
        */
    }
}
