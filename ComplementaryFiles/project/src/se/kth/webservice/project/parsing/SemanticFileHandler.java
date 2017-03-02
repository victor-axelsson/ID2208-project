package se.kth.webservice.project.parsing;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import se.kth.webservice.project.model.SemanticModelMapping;
import se.kth.webservice.project.parsing.compare.OnCompare;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by victoraxelsson on 2017-03-01.
 */
public class SemanticFileHandler extends FileHandler {

    private List<SemanticModelMapping> modelMappings;

    public SemanticFileHandler(OnCompare<SemanticModelMapping> onCompare) {
        super(onCompare);
    }

    @Override
    public void startComparing() {
        System.out.println("starting to semantically compare");
        for (int i = 0; i < modelMappings.size() -1;  i++){
            for(int j = i +1; j < modelMappings.size(); j++){
                onCompare.compare(modelMappings.get(i), modelMappings.get(j));
                onCompare.compare(modelMappings.get(j), modelMappings.get(i));
            }

            System.out.println("---- Done with semantically comparing doc: " + (i +1) + "/" + (modelMappings.size() -1) + " ----");
        }

        onCompare.flush();
    }

    @Override
    public void process() {
        modelMappings = new ArrayList<>();
        System.out.println("Processing");
        for (Document doc : docs) {
            processDocument(doc);
        }
    }

    private void processDocument(Document doc) {
        SemanticModelMapping modelMapping = new SemanticModelMapping();
        modelMapping.setFullDocument(doc);
        modelMapping.setFilename(doc.getDocumentURI());

        NodeList outputNodes = doc.getElementsByTagName("wsdl:output");
        NodeList inputNodes = doc.getElementsByTagName("wsdl:input");

        List<Element> simpleTypes = toList(doc.getElementsByTagNameNS("*", "simpleType"));
        for (int z = 0; z < simpleTypes.size(); z++){
            String name = simpleTypes.get(z).getAttribute("name");
            modelMapping.getSimpleTypes().put(name, simpleTypes.get(z));
        }

        List<Element> complexTypes = toList(doc.getElementsByTagNameNS("*", "complexType"));
        for (int z = 0; z < complexTypes.size(); z++){
            String name = complexTypes.get(z).getAttribute("name");
            modelMapping.getComplexTypes().put(name, complexTypes.get(z));
        }

        modelMapping.setOutputs(toList(outputNodes));
        modelMapping.setInputs(toList(inputNodes));

        //Collect messages and such
        processModel(modelMapping, doc);

        modelMappings.add(modelMapping);
    }

    private void processModel(SemanticModelMapping model, Document doc) {
        //Add all messages
        NodeList messages = doc.getElementsByTagName("wsdl:message");
        for (int i = 0; i < messages.getLength(); i++) {
            Element e = (Element) messages.item(i);
            String name = e.getAttribute("name");
            model.getMessages().put(name, e);

            List<Element> parts = toList(e.getChildNodes());
            model.getMessageParts().put(name, parts);

        }

        //Collect all messages names that are actually used in the outputs
        for(int i = 0; i < model.getOutputs().size(); i++){
            Element o = model.getOutputs().get(i);

            String message = o.getAttribute("message");
            if(message != null && message.trim().length() > 0){
                String messageName = getLastElement( message.split(":"));
                model.getMessageOutputNames().add(messageName);
            }
        }

        //Collect all messages names that are actually used in the input
        for(int i = 0; i < model.getInputs().size(); i++){
            Element o = model.getInputs().get(i);

            String message = o.getAttribute("message");
            if(message != null && message.trim().length() > 0){
                //Here we add the actual messages
                String messageName = getLastElement( message.split(":"));
                model.getMessageInputNames().add(messageName);
            }
        }

        connectPartsToModelReferences(model, doc);
    }

    private void connectPartsToModelReferences(SemanticModelMapping model, Document doc) {
        for (List<Element> parts : model.getMessageParts().values()) {
            for (Element part : parts) {
//                String partName = part.getAttribute("name");
                String type = part.getAttribute("type");

                Element elem = null;
                if (model.getSimpleTypes().containsKey(type)){
                    elem = model.getSimpleTypes().get(type);
                }else if(model.getComplexTypes().containsKey(type)){
                    elem = model.getComplexTypes().get(type);
                }

                if(elem != null){
                    String modelReference = getLastElement(elem.getAttribute("sawsdl:modelReference").split("#"));
                    model.getPartToModelRef().put(part, modelReference);
                }
            }
        }
    }




    @Override
    public String getFolderName() {
        return "SAWSDL";
    }

}
