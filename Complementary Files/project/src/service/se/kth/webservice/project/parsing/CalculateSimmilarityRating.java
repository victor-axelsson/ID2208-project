
package service.se.kth.webservice.project.parsing;

import se.kth.webservice.project.parsing.XmlModelMapping;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for calculateSimmilarityRating complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="calculateSimmilarityRating">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="arg0" type="{http://parsing.project.webservice.kth.se/}xmlModelMapping" minOccurs="0"/>
 *         &lt;element name="arg1" type="{http://parsing.project.webservice.kth.se/}xmlModelMapping" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "calculateSimmilarityRating", propOrder = {
    "arg0",
    "arg1"
})
public class CalculateSimmilarityRating {

    protected XmlModelMapping arg0;
    protected XmlModelMapping arg1;

    /**
     * Gets the value of the arg0 property.
     * 
     * @return
     *     possible object is
     *     {@link XmlModelMapping }
     *     
     */
    public XmlModelMapping getArg0() {
        return arg0;
    }

    /**
     * Sets the value of the arg0 property.
     * 
     * @param value
     *     allowed object is
     *     {@link XmlModelMapping }
     *     
     */
    public void setArg0(XmlModelMapping value) {
        this.arg0 = value;
    }

    /**
     * Gets the value of the arg1 property.
     * 
     * @return
     *     possible object is
     *     {@link XmlModelMapping }
     *     
     */
    public XmlModelMapping getArg1() {
        return arg1;
    }

    /**
     * Sets the value of the arg1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link XmlModelMapping }
     *     
     */
    public void setArg1(XmlModelMapping value) {
        this.arg1 = value;
    }

}
