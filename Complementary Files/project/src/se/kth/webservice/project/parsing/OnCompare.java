package se.kth.webservice.project.parsing;

import org.w3c.dom.Document;
import se.kth.webservice.project.model.XMLModelMapping;

import java.io.Serializable;

/**
 * Created by victoraxelsson on 2017-02-28.
 */
public interface OnCompare extends Serializable{
    void compare(XMLModelMapping a, XMLModelMapping b);
}
