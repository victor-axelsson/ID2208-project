package se.kth.webservice.project.parsing;

import se.kth.webservice.project.model.XMLModelMapping;

/**
 * Created by victoraxelsson on 2017-02-28.
 */
public interface IComparable {
    float getSimmilarityRating(XMLModelMapping a, XMLModelMapping b);
}
