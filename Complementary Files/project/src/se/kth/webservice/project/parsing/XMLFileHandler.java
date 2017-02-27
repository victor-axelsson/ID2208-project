package se.kth.webservice.project.parsing;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

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


    private static final String WSDL_PATH = "/Users/victoraxelsson/Desktop/intellijProjects/ID2208-project/Complementary Files/WSDLs";
    private List<Document> docs;

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
        return new String[] {
                WSDL_PATH + "/BangoSubscriptionBillingAPIProfile.wsdl"
        };
    }
}
