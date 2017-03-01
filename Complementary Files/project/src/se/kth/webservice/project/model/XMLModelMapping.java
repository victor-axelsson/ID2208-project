package se.kth.webservice.project.model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by victoraxelsson on 2017-02-27.
 */
public class XMLModelMapping implements Serializable{
    private List<Element> outputs;
    private List<Element> inputs;
    private List<String> messageOutputNames;
    private List<String> messageInputNames;
    private Map<String, Element> messages;
    private Map<String, List<Element>> messageParts;
    private String filename;
    private Document fullDocument;
    private Map<String, Element> simpleTypes;
    private String wsdlName;
    private DocumentBuilder dBuilder;

    public XMLModelMapping() {
        this.outputs = new ArrayList<>();
        this.inputs = new ArrayList<>();
        this.messageOutputNames = new ArrayList<>();
        this.messages = new HashMap<>();
        this.messageParts = new HashMap<>();
        this.messageInputNames = new ArrayList<>();
        this.simpleTypes = new HashMap<>();

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        dbFactory.setValidating(false);
        dbFactory.setNamespaceAware(true);
        try {
            dBuilder = dbFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    public Document fullDocumentGetter() {

        return fullDocument;

        /*
        String wsdlPath = System.getProperty("WSDL_PATH");
        File fXmlFile = new File(wsdlPath + "/" + wsdlName);

        Document doc = null;
        try {
            doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();
        } catch (SAXException e) {
            System.err.println(wsdlPath + "/" + wsdlName + " is not a valid XML doc");
            doc = null;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return doc;
        */
    }

    public Map<String, Element> simpleTypesGetter() {
        return simpleTypes;
    }

    public void setSimpleTypes(Map<String, Element> simpleTypes) {
        this.simpleTypes = simpleTypes;
    }

    public void fullDocumentSetter(Document fullDocument) {
        String[] p = fullDocument.getDocumentURI().split("/");
        wsdlName = p[p.length-1];
        this.fullDocument = fullDocument;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public List<String> messageInputNamesGetter() {
        return messageInputNames;
    }

    public void setMessageInputNames(List<String> messageInputNames) {
        this.messageInputNames = messageInputNames;
    }

    public Map<String, List<Element>> messagePartsGetter() {
        return messageParts;
    }

    public void setMessageParts(Map<String, List<Element>> messageParts) {
        this.messageParts = messageParts;
    }

    public Map<String, Element> messagesGetter() {
        return messages;
    }

    public void setMessages(Map<String, Element> messages) {
        this.messages = messages;
    }

    public List<String> messageOutputNamesGetter() {
        return messageOutputNames;
    }

    public void setMessageOutputNames(List<String> messageOutputNames) {
        this.messageOutputNames = messageOutputNames;
    }

    public List<Element> outputsGetter() {
        return outputs;
    }

    public void setOutputs(List<Element> outputs) {
        this.outputs = outputs;
    }

    public List<Element> inputsGetter() {
        return inputs;
    }

    public void setInputs(List<Element> inputs) {
        this.inputs = inputs;
    }
}
