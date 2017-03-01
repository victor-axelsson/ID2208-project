package se.kth.webservice.project.parsing;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by victoraxelsson on 2017-03-01.
 */
public abstract class FileHandler {

    private static final String PROJECT_PATH = System.getProperty("PROJECT_PATH");
    protected OnCompare onCompare;
    private static DocumentBuilder dBuilder;
    private Set<String> blacklist;

    protected List<Document> docs;


    public abstract void startComparing();
    public abstract void process();
    public abstract String getFolderName();


    protected String getLastElement(String[] array) {
        if (array != null && array.length > 0) {
            return array[array.length - 1];
        }

        return null;
    }


    public void setup(){
        try {
            docs = new ArrayList<>();
            String[] names = getAllWSDLNames(getFolderName());
            for(int i = 0; i < names.length; i++){


                if(blacklist.contains(names[i])){
                    System.out.println("It's blacklisted, so skipping: " + names[i]);
                    continue;
                }

                System.out.println("Parsing: " + (i + 1) + "/" + names.length + ". " + names[i]);

                Document d = getDocument(getFolderName(), names[i]);

                if(d != null){
                    docs.add(d);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public FileHandler( OnCompare onCompare){
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
    }

    protected static List<Element> toList(NodeList nodes){
        List<Element> elements = new ArrayList<>();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node n = nodes.item(i);
            if(n instanceof Element){
                elements.add((Element)nodes.item(i));
            }
        }
        return elements;
    }

    protected static Document getDocument(String folderName, String fullPath) throws IOException {
        File fXmlFile = new File(PROJECT_PATH + "/" + folderName +"/" + fullPath);

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


    protected String[] getAllWSDLNames(String folderName){
        File folder = new File(PROJECT_PATH + "/" + folderName);
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
}
