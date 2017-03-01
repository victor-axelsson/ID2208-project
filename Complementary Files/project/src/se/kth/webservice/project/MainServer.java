package se.kth.webservice.project;

import se.kth.webservice.project.parsing.SyntacticComparator;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

/**
 * Created by victoraxelsson on 2017-03-01.
 */
public class MainServer {

    public static void main(String[] args){
        try {
            SyntacticComparator comparator = new SyntacticComparator();
            // Register the newly created object at rmiregistry.
            try {
                LocateRegistry.getRegistry(1099).list();
            } catch (RemoteException e) {
                LocateRegistry.createRegistry(1099);
            }

            Naming.rebind("comparator", comparator);
            System.out.println("My <body> is ready");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
