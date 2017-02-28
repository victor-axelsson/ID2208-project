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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by victoraxelsson on 2017-02-27.
 */
public class XMLFileHandler {


    private static final String WSDL_PATH = System.getProperty("WSDLPATH");
    private List<Document> docs;
    private List<XMLModelMapping> modelMappings;
    private OnCompare onCompare;

    public XMLFileHandler(OnCompare onCompare){
        this.onCompare = onCompare;
    }

    public void setup(){

        try {
            docs = new ArrayList<>();
            String[] names = getAllWSDLNames();
            for(int i = 0; i < names.length; i++){
                docs.add(getDocument(names[i]));
            }

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("done");

        //optional, but recommended
        //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
    }

    private Document getDocument(String fullPath) throws ParserConfigurationException, IOException, SAXException {
        File fXmlFile = new File(WSDL_PATH + "/BangoSubscriptionBillingAPIProfile.wsdl");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = null;
        Document doc = null;
        dBuilder = dbFactory.newDocumentBuilder();
        doc = dBuilder.parse(fXmlFile);
        doc.getDocumentElement().normalize();
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

            NodeList outputNodes = doc.getElementsByTagName("wsdl:output");
            NodeList inputNodes = doc.getElementsByTagName("wsdl:input");

            model.setInputs(toList(outputNodes));
            model.setOutputs(toList(inputNodes));

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
    }

    public void startComparing(){
        for (int i = 0; i < modelMappings.size() -1;  i++){
            for(int j = i +1; j < modelMappings.size(); j++){
                onCompare.compare(modelMappings.get(i), modelMappings.get(j));
            }
        }
    }
}
