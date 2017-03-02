package se.kth.webservice.project.parsing.compare;

import ontology.EditDistance;
import org.w3c.dom.Element;
import se.kth.webservice.project.model.XMLModelMapping;
import se.kth.webservice.project.output.ElementComparisonResult;
import se.kth.webservice.project.output.OperationComparisonResult;
import se.kth.webservice.project.output.WsdlComparisonResult;
import se.kth.webservice.project.parsing.XMLFileHandler;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by victoraxelsson on 2017-02-28.
 */
public class SyntacticComparator extends UnicastRemoteObject implements IComparable<XMLModelMapping>{

    public SyntacticComparator() throws RemoteException {}

//    public static double minScore = 20;
//    public static int count = 0;
//    public static int counter4 = 0;
//    public static int counter3 = 0;
//    public static int counter2 = 0;
//    public static int counter1 = 0;

    private OperationComparisonResult compareIO(String outputMessageA, String inputMessageB, XMLModelMapping a, XMLModelMapping b){
        List<Element> aParts = XMLFileHandler.flatten(a.getMessageParts().get(outputMessageA), a.getFullDocument(), a);
        List<Element> bParts = XMLFileHandler.flatten(b.getMessageParts().get(inputMessageB), b.getFullDocument(), b);

        double avg = 0.0;
        List<ElementComparisonResult> elementComparisonResults = new ArrayList<>();
        for (Element aElem : aParts) {
            ElementComparisonResult score = null;
            for (Element bElem : bParts) {
                ElementComparisonResult comparison = compareElements(aElem, bElem);
                if (score == null || comparison.getScore() >= score.getScore()) {
                    score = comparison;
                }
            }

            if (score != null && !score.getFirst().isEmpty() && !score.getSecond().isEmpty()) {
                elementComparisonResults.add(score);
                avg += score.getScore();
            }

        }

        if (elementComparisonResults.size() > 0) {
            avg = avg / elementComparisonResults.size();
        }

        return new OperationComparisonResult(outputMessageA, inputMessageB, avg, elementComparisonResults);
    }

    private ElementComparisonResult compareElements(Element a, Element b) {
        String aName = a.getAttribute("name");
        String bName = b.getAttribute("name");
        int res = EditDistance.getEditDistance(aName, bName);
        //System.out.println("Compare " + aName + " and " + bName + " result is " + res);
        return new ElementComparisonResult(aName, bName, convertEditDistanceToScore(res));
    }


    private double convertEditDistanceToScore(int score) {
        if (score == 0) return 1.0;
        if (score == 1) return 0.95;
        if (score == 2) return 0.9;
        if (score == 3) return 0.8;
        if (score == 4) return 0.7;
        if (score == 5) return 0.5;
        return 0.0;
    }


    @Override
    public WsdlComparisonResult getSimmilarityRating(XMLModelMapping a, XMLModelMapping b) {
        System.out.println("---- Comparing ---- \n" + a.getFilename() + "\n" + b.getFilename() + "\n----");
        double score = 0;
        List<OperationComparisonResult> operationComparisonResults = new ArrayList<>();
        for(int i = 0; i < a.getMessageOutputNames().size(); i++){
            for(int j = 0; j < b.getMessageInputNames().size(); j++){
                OperationComparisonResult res =
                        compareIO(a.getMessageOutputNames().get(i), b.getMessageInputNames().get(j), a, b);
                score = score + res.getScore();
                operationComparisonResults.add(res);
            }

            System.out.println("Rating: i: " + (i + 1) + "/" + a.getMessageOutputNames().size());
        }

        if (operationComparisonResults.size() > 0) {
            score = score / (double) operationComparisonResults.size();
        }

        return new WsdlComparisonResult(a.getFilename(), b.getFilename(),
                score, operationComparisonResults);
    }
}
