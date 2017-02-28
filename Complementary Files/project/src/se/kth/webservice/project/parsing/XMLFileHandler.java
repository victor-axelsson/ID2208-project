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

    private Set<String> blacklist;
    public XMLFileHandler(OnCompare onCompare){
        this.onCompare = onCompare;
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        dbFactory.setValidating(false);
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

    private List<Element> toList(NodeList nodes){
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
