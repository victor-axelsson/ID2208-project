package se.kth.webservice.project.parsing.compare;

import ontology.MyOntManager;
import org.mindswap.pellet.owlapi.Reasoner;
import org.semanticweb.owl.model.*;
import org.w3c.dom.Element;
import se.kth.webservice.project.model.SemanticModelMapping;
import se.kth.webservice.project.output.ElementComparisonResult;
import se.kth.webservice.project.output.OperationComparisonResult;
import se.kth.webservice.project.output.WsdlComparisonResult;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertNotNull;

/**
 * Created by victoraxelsson on 2017-03-01.
 */
public class SemanticComparator extends UnicastRemoteObject implements IComparable<SemanticModelMapping> {

    private static OWLDataFactory df;
    private static OWLOntologyManager manager = null;
    private static OWLOntology ontology = null;
    private static Reasoner reasoner = null;
    private static String projectPath = System.getProperty("PROJECT_PATH");
    private static String ontLocation = "file://"+projectPath + "/project/data/SUMO.owl";
    private static MyOntManager ontsum = null;

    public SemanticComparator() throws RemoteException {
        ontsum = new  MyOntManager();
        manager = ontsum.initializeOntologyManager();
        ontology = ontsum.initializeOntology(manager, ontLocation);
        reasoner = ontsum.initializeReasoner(ontology, manager);
    }

    @Override
    public WsdlComparisonResult getSimmilarityRating(SemanticModelMapping a, SemanticModelMapping b) throws RemoteException {
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

    private OperationComparisonResult compareIO(String outputMessageA, String inputMessageB, SemanticModelMapping a, SemanticModelMapping b){


        List<Element> aParts = a.getMessageParts().get(outputMessageA);
        List<Element> bParts = b.getMessageParts().get(inputMessageB);


        double avg = 0.0;
        List<ElementComparisonResult> elementComparisonResults = new ArrayList<>();
        for (Element aElem : aParts) {
            ElementComparisonResult score = null;

            String aModelRef = a.getPartToModelRef().get(aElem);
            for (Element bElem : bParts) {
                String bModelRef = b.getPartToModelRef().get(bElem);
                ElementComparisonResult comparison = new ElementComparisonResult(aModelRef,
                        bModelRef, compareOwl(aModelRef, bModelRef));

                if (score == null || comparison.getScore() >= score.getScore()) {
                    score = comparison;
                }
            }


            if (score != null) {
                elementComparisonResults.add(score);
                avg += score.getScore();
            }
        }

        if (elementComparisonResults.size() > 0) {
            avg = avg / elementComparisonResults.size();
        }

//        if (avg <= minScore) minScore = avg;
//        if (avg < 5) {
//            System.out.println(outputMessageA + " and " + inputMessageB + " have a score of " + avg);
//            count++;
//            if (avg < 4) counter4++;
//            if (avg < 3) counter3++;
//            if (avg < 2) counter2++;
//            if (avg < 1) counter1++;
//        }

        return new OperationComparisonResult(outputMessageA, inputMessageB, avg, elementComparisonResults);
    }

    /*
    private ElementComparisonResult compareElements(Element a, Element b) {
        String aName = a.getAttribute("name");
        String bName = b.getAttribute("name");
        double res = compareOwl(aName, bName);
        //System.out.println("Compare " + aName + " and " + bName + " result is " + res);
        return new ElementComparisonResult(aName, bName, res);
    }
    */

    /**
     * @apiNote Its assumed that the first parameter is the output and the second is the input. This is needed
     * in order to differentiate between plugin and subsumption because they have different scores
     * */
    private double compareOwl(String first, String second) {HashMap<String, OWLClass> mapName_OWLClass = ontsum.loadClasses(reasoner);

        if(first != null && second != null){
            OWLClass cls1 = mapName_OWLClass.get(first.toLowerCase());
            OWLClass cls2 = mapName_OWLClass.get(second.toLowerCase());

            if(cls1 != null && cls2 != null){

                int c = cls1.compareTo(cls2);

                if(c == 0){
                    //the same, exact
                    System.out.println(first + " is the same as " + second);
                    return 1;
                }else if(reasoner.isSubClassOf(cls1, cls2)){

                    //Plugin
                    System.out.println(first + " is a subclass of " + second);

                    return 0.6;
                }else if(reasoner.isSubClassOf(cls2, cls1)){

                    //Subsumption
                    System.out.println(second + " is a subclass of " + first);

                    return 0.8;
                }else if(ontsum.findRelationship(cls1, cls2, reasoner).size() > 0){

                    //Structural
                    System.out.println(first + " have some arbitrary relation with " + second);

                    return 0.5;
                }
            }
        }

        return 0;
    }

}
