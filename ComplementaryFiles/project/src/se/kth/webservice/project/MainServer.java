package se.kth.webservice.project;

import se.kth.webservice.project.parsing.compare.SemanticComparator;
import se.kth.webservice.project.parsing.compare.SyntacticComparator;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

/**
 * Created by victoraxelsson on 2017-03-01.
 */
public class MainServer {

    public static void main(String[] args){

        Main.setupSystemProps(args);

        try {
            SyntacticComparator comparator = new SyntacticComparator();
            SemanticComparator semanticComparator = new SemanticComparator();

            // Register the newly created object at rmiregistry.
            try {
                LocateRegistry.getRegistry(1099).list();
            } catch (RemoteException e) {
                LocateRegistry.createRegistry(1099);
            }

            Naming.rebind("comparator", comparator);
            Naming.rebind("semantic_comparator", semanticComparator);
            System.out.println("My <body> is ready");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
