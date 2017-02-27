package se.kth.webservice.project.model;

import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by victoraxelsson on 2017-02-27.
 */
public class XMLModelMapping {
    private List<Element> outputs;
    private List<Element> inputs;
    private List<String> messageOutputNames;
    private Map<String, Element> messages;
    private Map<String, List<Element>> messageParts;

    public XMLModelMapping() {
        this.outputs = new ArrayList<>();
        this.inputs = new ArrayList<>();
        this.messageOutputNames = new ArrayList<>();
        this.messages = new HashMap<>();
        this.messageParts = new HashMap<>();
    }

    public Map<String, List<Element>> getMessageParts() {
        return messageParts;
    }

    public void setMessageParts(Map<String, List<Element>> messageParts) {
        this.messageParts = messageParts;
    }

    public Map<String, Element> getMessages() {
        return messages;
    }

    public void setMessages(Map<String, Element> messages) {
        this.messages = messages;
    }

    public List<String> getMessageOutputNames() {
        return messageOutputNames;
    }

    public void setMessageOutputNames(List<String> messageOutputNames) {
        this.messageOutputNames = messageOutputNames;
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
}
