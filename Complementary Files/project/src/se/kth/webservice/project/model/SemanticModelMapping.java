package se.kth.webservice.project.model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by victoraxelsson on 2017-03-01.
 */
public class SemanticModelMapping implements Serializable {
    private List<Element> outputs;
    private List<Element> inputs;
    private List<String> messageOutputNames;
    private List<String> messageInputNames;
    private Map<String, Element> messages;
    private Map<String, List<Element>> messageParts;
    private Map<String, Element> simpleTypes;
    private Map<String, Element> complexTypes;
    private String filename;
    private Document fullDocument;

    private Map<String, String> partToModelRef;

    public SemanticModelMapping() {
        this.outputs = new ArrayList<>();
        this.inputs = new ArrayList<>();
        this.messageOutputNames = new ArrayList<>();
        this.messages = new HashMap<>();
        this.messageParts = new HashMap<>();
        this.messageInputNames = new ArrayList<>();
        this.simpleTypes = new HashMap<>();
        this.complexTypes = new HashMap<>();

        partToModelRef = new HashMap<>();
    }

    public List<Element> getOutputs() {
        return outputs;
    }

    public void setOutputs(List<Element> outputs) {
        this.outputs = outputs;
    }

    public List<Element> getInputs() {
        return inputs;
    }

    public void setInputs(List<Element> inputs) {
        this.inputs = inputs;
    }

    public List<String> getMessageOutputNames() {
        return messageOutputNames;
    }

    public void setMessageOutputNames(List<String> messageOutputNames) {
        this.messageOutputNames = messageOutputNames;
    }

    public List<String> getMessageInputNames() {
        return messageInputNames;
    }

    public void setMessageInputNames(List<String> messageInputNames) {
        this.messageInputNames = messageInputNames;
    }

    public Map<String, Element> getMessages() {
        return messages;
    }

    public void setMessages(Map<String, Element> messages) {
        this.messages = messages;
    }

    public Map<String, List<Element>> getMessageParts() {
        return messageParts;
    }

    public void setMessageParts(Map<String, List<Element>> messageParts) {
        this.messageParts = messageParts;
    }

    public Map<String, String> getPartToModelRef() {
        return partToModelRef;
    }

    public void setPartToModelRef(Map<String, String> partToModelRef) {
        this.partToModelRef = partToModelRef;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Document getFullDocument() {
        return fullDocument;
    }

    public void setFullDocument(Document fullDocument) {
        this.fullDocument = fullDocument;
    }

    public Map<String, Element> getSimpleTypes() {
        return simpleTypes;
    }

    public void setSimpleTypes(Map<String, Element> simpleTypes) {
        this.simpleTypes = simpleTypes;
    }

    public Map<String, Element> getComplexTypes() {
        return complexTypes;
    }

    public void setComplexTypes(Map<String, Element> complexTypes) {
        this.complexTypes = complexTypes;
    }
}
