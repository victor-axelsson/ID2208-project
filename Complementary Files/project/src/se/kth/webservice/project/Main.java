package se.kth.webservice.project;

import se.kth.webservice.project.data.IWordnet;
import se.kth.webservice.project.data.WordnetSQL;
import se.kth.webservice.project.model.DictionaryLookup;
import se.kth.webservice.project.model.XMLModelMapping;
import se.kth.webservice.project.output.WsdlComparisonResult;
import se.kth.webservice.project.parsing.IComparable;
import se.kth.webservice.project.parsing.OnCompare;
import se.kth.webservice.project.parsing.SyntacticComparator;
import se.kth.webservice.project.parsing.XMLFileHandler;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;
import java.util.List;

import static com.hp.hpl.jena.sparql.junit.QueryTestSuiteFactory.results;

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

    public static void main (String args[]){

        setupSystemProps(args);

        //IWordnet repo = new WordnetSQL();
        //List<DictionaryLookup> lookups =  repo.lookupInDictionary("gravy");


        //IComparable remoteComparer = getRMIComparator();
        IComparable localComparer = getLocalComparator();


        //There are 496 comparisments for 32 docs. A doc is not compared to itself.
        XMLFileHandler fileHandler = new XMLFileHandler(new OnCompare() {
            @Override
            public void compare(XMLModelMapping a, XMLModelMapping b) {

                /*
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
                */

                try {
                    WsdlComparisonResult rating = localComparer.getSimmilarityRating(b, a);
                    localList.add(rating);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
        fileHandler.setup();
        fileHandler.process();
        fileHandler.startComparing();

        System.out.println(results.size());

//        System.out.println("done, minScore is " + SyntacticComparator.minScore +
//                " number of operation pairs with less than 5 is " + SyntacticComparator.count);
//        System.out.println("less than 4 " + SyntacticComparator.counter4);
//        System.out.println("less than 3 " + SyntacticComparator.counter3);
//        System.out.println("less than 2 " + SyntacticComparator.counter2);
//        System.out.println("less than 1 " + SyntacticComparator.counter1);

    }
}
