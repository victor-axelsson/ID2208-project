
package service.se.kth.webservice.project.parsing;

import se.kth.webservice.project.parsing.CalculateSimmilarityRatingResponse;
import se.kth.webservice.project.parsing.XmlModelMapping;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the se.kth.webservice.project.parsing package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _CalculateSimmilarityRating_QNAME = new QName("http://parsing.project.webservice.kth.se/", "calculateSimmilarityRating");
    private final static QName _CalculateSimmilarityRatingResponse_QNAME = new QName("http://parsing.project.webservice.kth.se/", "calculateSimmilarityRatingResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: se.kth.webservice.project.parsing
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link CalculateSimmilarityRating }
     * 
     */
    public CalculateSimmilarityRating createCalculateSimmilarityRating() {
        return new CalculateSimmilarityRating();
    }

    /**
     * Create an instance of {@link CalculateSimmilarityRatingResponse }
     * 
     */
    public CalculateSimmilarityRatingResponse createCalculateSimmilarityRatingResponse() {
        return new CalculateSimmilarityRatingResponse();
    }

    /**
     * Create an instance of {@link XmlModelMapping }
     * 
     */
    public XmlModelMapping createXmlModelMapping() {
        return new XmlModelMapping();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CalculateSimmilarityRating }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://parsing.project.webservice.kth.se/", name = "calculateSimmilarityRating")
    public JAXBElement<CalculateSimmilarityRating> createCalculateSimmilarityRating(CalculateSimmilarityRating value) {
        return new JAXBElement<CalculateSimmilarityRating>(_CalculateSimmilarityRating_QNAME, CalculateSimmilarityRating.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CalculateSimmilarityRatingResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://parsing.project.webservice.kth.se/", name = "calculateSimmilarityRatingResponse")
    public JAXBElement<CalculateSimmilarityRatingResponse> createCalculateSimmilarityRatingResponse(CalculateSimmilarityRatingResponse value) {
        return new JAXBElement<CalculateSimmilarityRatingResponse>(_CalculateSimmilarityRatingResponse_QNAME, CalculateSimmilarityRatingResponse.class, null, value);
    }

}
