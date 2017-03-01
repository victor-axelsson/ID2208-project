package se.kth.webservice.project.parsing;

import se.kth.webservice.project.model.XMLModelMapping;
import se.kth.webservice.project.output.WsdlComparisonResult;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by victoraxelsson on 2017-02-28.
 */
public interface IComparable extends Remote{
    WsdlComparisonResult getSimmilarityRating(XMLModelMapping a, XMLModelMapping b) throws RemoteException;
}
