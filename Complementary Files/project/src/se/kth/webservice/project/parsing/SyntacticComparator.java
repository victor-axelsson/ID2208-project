package se.kth.webservice.project.parsing;

import org.w3c.dom.Element;
import se.kth.webservice.project.model.XMLModelMapping;

import java.util.List;

/**
 * Created by victoraxelsson on 2017-02-28.
 */
public class SyntacticComparator implements IComparable {


    private void compareIO(String outputMessageA, String inputMessageB, XMLModelMapping a, XMLModelMapping b){
        List<Element> aParts = XMLFileHandler.flatten(a.getMessageParts().get(outputMessageA), a.getFullDocument(), a);
        List<Element> bParts = XMLFileHandler.flatten(b.getMessageParts().get(inputMessageB), b.getFullDocument(), b);
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
