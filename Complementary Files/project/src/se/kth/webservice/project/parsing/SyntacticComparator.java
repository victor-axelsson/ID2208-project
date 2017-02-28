package se.kth.webservice.project.parsing;

import ontology.EditDistance;
import org.w3c.dom.Element;
import se.kth.webservice.project.model.XMLModelMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by victoraxelsson on 2017-02-28.
 */
public class SyntacticComparator implements IComparable {
    public static double minScore = 20;
    public static int count = 0;
    public static int counter4 = 0;
    public static int counter3 = 0;
    public static int counter2 = 0;
    public static int counter1 = 0;

    private void compareIO(String outputMessageA, String inputMessageB, XMLModelMapping a, XMLModelMapping b){
        List<Element> aParts = XMLFileHandler.flatten(a.getMessageParts().get(outputMessageA), a.getFullDocument(), a);
        List<Element> bParts = XMLFileHandler.flatten(b.getMessageParts().get(inputMessageB), b.getFullDocument(), b);

        List<Integer> scores = new ArrayList<>();
        double avg = 0.0;
        for (Element aElem : aParts) {
            int score = 20;
            String aName = aElem.getAttribute("name");
            String bName = "";
            for (Element bElem : bParts) {
                int comparison = compareElements(aElem, bElem);
                if (comparison <= score) {
                    score = comparison;
                    bName = bElem.getAttribute("name");
                }
            }
            //System.out.println(aName + " has top score of " + score + " with elem " + bName);
            scores.add(score);
        }

        for (int score : scores) avg += score;
        avg = avg / scores.size();

        if (avg <= minScore) minScore = avg;
        if (avg < 5) {
            System.out.println(outputMessageA + " and " + inputMessageB + " have a score of " + avg);
            count++;
            if (avg < 4) counter4++;
            if (avg < 3) counter3++;
            if (avg < 2) counter2++;
            if (avg < 1) counter1++;
        }
    }

    private int compareElements(Element a, Element b) {
        String aName = a.getAttribute("name");
        String bName = b.getAttribute("name");
        int res = EditDistance.getEditDistance(aName, bName);
        //System.out.println("Compare " + aName + " and " + bName + " result is " + res);
        return res;
    }

    @Override
    public float getSimmilarityRating(XMLModelMapping a, XMLModelMapping b) {

        System.out.println("---- Comparing ---- \n" + a.getFilename() + "\n" + b.getFilename() + "\n----");
        for(int i = 0; i < a.getMessageOutputNames().size(); i++){
            for(int j = 0; j < b.getMessageInputNames().size(); j++){
                compareIO(a.getMessageOutputNames().get(i), b.getMessageInputNames().get(j), a, b);
            }

            System.out.println("Rating: i: " + (i + 1) + "/" + a.getMessageOutputNames().size());
        }


        //TODO: DO reverse comparsiment

        return 0.0f;
    }
}
