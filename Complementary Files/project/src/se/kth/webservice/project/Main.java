package se.kth.webservice.project;

import se.kth.webservice.project.data.IWordnet;
import se.kth.webservice.project.data.WordnetSQL;
import se.kth.webservice.project.model.DictionaryLookup;
import se.kth.webservice.project.model.XMLModelMapping;
import se.kth.webservice.project.parsing.OnCompare;
import se.kth.webservice.project.parsing.XMLFileHandler;

import java.util.List;

/**
 * Created by victoraxelsson on 2017-02-27.
 */
public class Main {

    private static int counter = 0;

    public static void main (String args[]){

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


        System.out.println("Flowers and kittens and wiskers");

        IWordnet repo = new WordnetSQL();
        List<DictionaryLookup> lookups =  repo.lookupInDictionary("gravy");

        XMLFileHandler fileHandler = new XMLFileHandler(new OnCompare() {
            @Override
            public void compare(XMLModelMapping a, XMLModelMapping b) {
                counter++;
            }
        });
        fileHandler.setup();
        fileHandler.process();
        fileHandler.startComparing();
        System.out.println("Counter: " + counter);

        System.out.println("done");

    }
}
