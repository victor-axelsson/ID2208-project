package se.kth.webservice.project.parsing;

import org.w3c.dom.Element;
import se.kth.webservice.project.model.XMLModelMapping;

import java.util.List;

/**
 * Created by victoraxelsson on 2017-02-28.
 */
public class SyntacticComparator implements IComparable {


    private void compareIO(String outputMessageA, String inputMessageB, XMLModelMapping a, XMLModelMapping b){

        List<Element> aParts = XMLFileHandler.flatten(a.getMessageParts().get(outputMessageA), a.getFullDocument());
        List<Element> bParts = XMLFileHandler.flatten(b.getMessageParts().get(inputMessageB), b.getFullDocument());




        System.out.println("asd");
    }

    @Override
    public float getSimmilarityRating(XMLModelMapping a, XMLModelMapping b) {

        for(int i = 0; i < a.getMessageOutputNames().size(); i++){
            for(int j = 0; j < b.getMessageInputNames().size(); j++){
                compareIO(a.getMessageOutputNames().get(i), b.getMessageInputNames().get(j), a, b);
            }
        }


        //TODO: DO reverse comparsiment

        return 0.0f;
    }
}
