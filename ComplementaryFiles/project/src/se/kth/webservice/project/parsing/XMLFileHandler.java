package se.kth.webservice.project.parsing;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import se.kth.webservice.project.model.XMLModelMapping;
import se.kth.webservice.project.parsing.compare.OnCompare;

import java.util.*;

/**
 * Created by victoraxelsson on 2017-02-27.
 */
public class XMLFileHandler extends FileHandler{

    private List<XMLModelMapping> modelMappings;

    private static Set<String> BASE_TYPES;


    public XMLFileHandler(OnCompare onCompare){
        super(onCompare);
    }

    private static Set<String> getBaseTypes(){
        if(BASE_TYPES == null){
            BASE_TYPES = new HashSet<>();
            BASE_TYPES.add("string");
            BASE_TYPES.add("decimal");
            BASE_TYPES.add("integer");
            BASE_TYPES.add("boolean");
            BASE_TYPES.add("date");
            BASE_TYPES.add("time");
            BASE_TYPES.add("long");
            BASE_TYPES.add("byte");
            BASE_TYPES.add("time");
            BASE_TYPES.add("short");
            BASE_TYPES.add("unsignedLong");
            BASE_TYPES.add("unsignedInt");
            BASE_TYPES.add("unsignedShort");
            BASE_TYPES.add("unsignedByte");
            BASE_TYPES.add("anyURI");
            BASE_TYPES.add("float");
            BASE_TYPES.add("double");
            BASE_TYPES.add("QName");
            BASE_TYPES.add("dateTime");
            BASE_TYPES.add("int");
            BASE_TYPES.add("base64Binary");
        }

        return BASE_TYPES;
    }

    public void process(){

        modelMappings = new ArrayList<>();

        for (int i = 0; i < docs.size(); i++){
            Document doc = docs.get(i);
            XMLModelMapping model = new XMLModelMapping();
            model.setFilename(doc.getDocumentURI());
            model.setFullDocument(doc);

            NodeList outputNodes = doc.getElementsByTagName("wsdl:output");
            NodeList inputNodes = doc.getElementsByTagName("wsdl:input");

            List<Element> simpleTypes = toList(doc.getElementsByTagNameNS("*", "simpleType"));
            for (int z = 0; z < simpleTypes.size(); z++){
                String name = simpleTypes.get(z).getAttribute("name");
                model.getSimpleTypes().put(name, simpleTypes.get(z));
            }

            model.setOutputs(toList(outputNodes));
            model.setInputs(toList(inputNodes));

            //Collect messages and such
            processModel(model, doc);

            modelMappings.add(model);
        }

        //saveCache();

        //System.out.println("asd");
    }

    @Override
    public String getFolderName() {
        return "WSDLs";
    }


    public static boolean isComplexType(Element part, XMLModelMapping model){

       if(part.hasAttribute("type")){

            String type = part.getAttribute("type").split(":")[1];

            //this is a basic type, like string or int

           if(getBaseTypes().contains(type)){
               return false;
           }

           if(part.hasAttribute("name")){
               String withNS = part.getAttribute("name");
               String[] withoutNSParts = part.getAttribute("name").split(":");
               String withoutNs = "";

               if(withoutNSParts.length > 1){
                   withoutNs = withoutNSParts[1];
               }

               return model.getSimpleTypes().containsKey(withNS) || model.getSimpleTypes().containsKey(withoutNs);
           }

        }else if(part.hasAttribute("element")){
            //this is a complex custom type
            return true;
        }

        return false;
    }

    private static List<Element> flattenComplex(Element complex, Document doc, XMLModelMapping model){
        List<Element> response = new ArrayList<>();

        if(complex == null){
            return response;
        }
        NodeList elements = complex.getElementsByTagNameNS("*", "element");

        for(int i = 0; i < elements.getLength(); i++){
            Element e = (Element) elements.item(i);

            if(isComplexType(e, model)){
                String[] name =  e.getAttribute("type").split(":");


                Element recursiveComplex = findElement(name[1], name[0] + ":" +  name[1], doc);

                //throw new RuntimeException("Not implemented");
                response.addAll(flattenComplex(recursiveComplex, doc, model));
            }else{
                response.add(e);
            }
        }

        return response;
    }

    private static Element findElement(String name, String fullName, Document doc){
        NodeList complexType = doc.getElementsByTagNameNS("*","complexType");
        NodeList elements = doc.getElementsByTagNameNS("*","element");

        List<Element> elementsList = new ArrayList<>();
        elementsList.addAll(toList(complexType));
        elementsList.addAll(toList(elements));

        for(int z = 0; z < elementsList.size(); z++){
            Element e = elementsList.get(z);
            String n = e.getAttribute("name");

            if(e.hasAttribute("name") && (e.getAttribute("name").equals(fullName) || e.getAttribute("name").equals(name))){
                return e;
            }
        }

        System.out.println(fullName + " was not found");
        return null;
    }


    public static List<Element> flatten(List<Element> original, Document doc, XMLModelMapping model){
        List<Element> response = new ArrayList<>();

        for(int i = 0; i < original.size(); i++){
            Element curr = original.get(i);

            if(isComplexType(curr, model)){

                String[] elementNameParts = curr.getAttribute("element").split(":");
                String elementName = elementNameParts[elementNameParts.length -1];

                String fullName = curr.getAttribute("element");

                if (fullName.isEmpty()) {
                    elementNameParts = curr.getAttribute("type").split(":");
                    elementName = elementNameParts[elementNameParts.length -1];
                    fullName = curr.getAttribute("type");
                }

                response.addAll(flattenComplex(findElement(elementName, fullName, doc), doc, model));
            }else{
                response.add(curr); 
            }
        }

        return response;
    }

    private void processModel(XMLModelMapping model, Document doc){

        //Add all messages
        NodeList messages = doc.getElementsByTagName("wsdl:message");
        for(int i = 0; i < messages.getLength(); i++){
            Element e = (Element)messages.item(i);
            String name = e.getAttribute("name");
            model.getMessages().put(name, e);

            List<Element> parts =  toList(e.getChildNodes());
            model.getMessageParts().put(name, parts);

            //Don't know if we need this?
            //processParts(parts, name, doc);
        }

        //Collect all messages names that are actually used in the outputs
        for(int i = 0; i < model.getOutputs().size(); i++){
            Element o = model.getOutputs().get(i);

            String message = o.getAttribute("message");
            if(message != null && message.trim().length() > 0){
                String[] parts = message.split(":");

                //Here we add the actual messages
                String messageName = parts[1];
                model.getMessageOutputNames().add(messageName);
            }
        }

        //Collect all messages names that are actually used in the input
        for(int i = 0; i < model.getInputs().size(); i++){
            Element o = model.getInputs().get(i);

            String message = o.getAttribute("message");
            if(message != null && message.trim().length() > 0){
                String[] parts = message.split(":");

                //Here we add the actual messages
                String messageName = parts[1];
                model.getMessageInputNames().add(messageName);
            }
        }
    }

    public void startComparing(){
        for (int i = 0; i < modelMappings.size() -1;  i++){
            for(int j = i +1; j < modelMappings.size(); j++){
                onCompare.compare(modelMappings.get(i), modelMappings.get(j));
                onCompare.compare(modelMappings.get(j), modelMappings.get(i));
            }

            System.out.println("---- Done with comparing doc: " + (i +1) + "/" + (modelMappings.size() -1) + " ----");
        }

        onCompare.flush();
    }
}
