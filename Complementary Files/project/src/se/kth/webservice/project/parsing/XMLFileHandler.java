package se.kth.webservice.project.parsing;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import se.kth.webservice.project.model.XMLModelMapping;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by victoraxelsson on 2017-02-27.
 */
public class XMLFileHandler {


    private static final String WSDL_PATH = System.getProperty("WSDLPATH");
    private List<Document> docs;
    private List<XMLModelMapping> modelMappings;
    private OnCompare onCompare;
    private DocumentBuilder dBuilder;
    private static Set<String> BASE_TYPES;


    private Set<String> blacklist;
    public XMLFileHandler(OnCompare onCompare){
        this.onCompare = onCompare;
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        dbFactory.setValidating(false);
        dbFactory.setNamespaceAware(true);
        try {
            dBuilder = dbFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        blacklist = new HashSet<>();
        blacklist.add("BangoDirectBillingAPIProfile.wsdl");
        blacklist.add("compositeFlightStatsAPIProfile.wsdl");
        blacklist.add("DeveloperGardenClickandBuyAPIProfile.wsdl");
        blacklist.add("GoToBillingAPIProfile.wsdl");
        blacklist.add("InnovativeMerchantSolutionsAPIProfile.wsdl");
        blacklist.add("PaymentVisionPayAPIProfile.wsdl");

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

    public void setup(){

        try {
            docs = new ArrayList<>();
            String[] names = getAllWSDLNames();
            for(int i = 0; i < names.length; i++){


                if(blacklist.contains(names[i])){
                    System.out.println("It's blacklisted, so skipping: " + names[i]);
                    continue;
                }

                System.out.println("Parsing: " + (i + 1) + "/" + names.length + ". " + names[i]);

                Document d = getDocument(names[i]);

                if(d != null){
                    docs.add(d);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Done with parsing");

        //optional, but recommended
        //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
    }

    private Document getDocument(String fullPath) throws IOException{
        File fXmlFile = new File(WSDL_PATH + "/" + fullPath);

        Document doc = null;
        try {
            doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();
        } catch (SAXException e) {
            System.err.println(fullPath + " is not a valid XML doc");
            doc = null;
        }

        return doc;
    }

    private String[] getAllWSDLNames(){

        File folder = new File(WSDL_PATH);
        File[] listOfFiles = folder.listFiles();
        String[] names = new String[listOfFiles.length];

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                names[i] = listOfFiles[i].getName();
            } else if (listOfFiles[i].isDirectory()) {
                System.out.println("Directory " + listOfFiles[i].getName());
            }
        }

        return names;
    }

    public List<Document> getDocs() {
        return docs;
    }

    public List<XMLModelMapping> getModelMappings() {
        return modelMappings;
    }

    private static List<Element> toList(NodeList nodes){
        List<Element> elements = new ArrayList<>();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node n = nodes.item(i);
            if(n instanceof Element){
                elements.add((Element)nodes.item(i));
            }
        }
        return elements;
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

            model.setOutputs(toList(outputNodes));
            model.setInputs(toList(inputNodes));

            //Collect messages and such
            processModel(model, doc);

            modelMappings.add(model);
        }

        System.out.println("asd");
    }

    //Do we need this?
    private void processParts(List<Element> parts, String messageName, Document doc){
        for(int i = 0; i < parts.size(); i++){
            Element part = parts.get(i);

            if(part.hasAttribute("type")){
                String type = part.getAttribute("type").split(":")[1];

                //this is a basic type, like string or int
            }else if(part.hasAttribute("element")){
                //this is a complex custom type
            }

        }
    }

    public static boolean isComplexType(Element part){
        if(part.hasAttribute("type")){
            String type = part.getAttribute("type").split(":")[1];

            //this is a basic type, like string or int

            return !BASE_TYPES.contains(type);

        }else if(part.hasAttribute("element")){
            //this is a complex custom type
            return true;
        }

        return false;
    }

    private static List<Element> flattenComplex(Element complex, Document doc){
        List<Element> response = new ArrayList<>();

        if(complex == null){
            return response;
        }
        NodeList elements = complex.getElementsByTagName("s:element");

        for(int i = 0; i < elements.getLength(); i++){
            Element e = (Element) elements.item(i);

            if(isComplexType(e)){
                String[] name =  e.getAttribute("type").split(":");


                Element recursiveComplex = findElement(name[1], name[0] + ":" +  name[1], doc);

                //throw new RuntimeException("Not implemented");
                response.addAll(flattenComplex(recursiveComplex, doc));
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


    public static List<Element> flatten(List<Element> original, Document doc){
        List<Element> response = new ArrayList<>();

        for(int i = 0; i < original.size(); i++){
            Element curr = original.get(i);

            if(isComplexType(curr)){

                String[] elementNameParts = curr.getAttribute("element").split(":");
                String elementName = elementNameParts[elementNameParts.length -1];

                String fullName = curr.getAttribute("element");

                response.addAll(flattenComplex(findElement(elementName, fullName, doc), doc));
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
            }
        }
    }
}
