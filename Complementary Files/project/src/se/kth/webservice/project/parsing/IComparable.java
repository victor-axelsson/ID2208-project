package se.kth.webservice.project.parsing;

import se.kth.webservice.project.model.XMLModelMapping;
import se.kth.webservice.project.output.WsdlComparisonResult;

/**
 * Created by victoraxelsson on 2017-02-28.
 */
public interface IComparable {
    WsdlComparisonResult getSimmilarityRating(XMLModelMapping a, XMLModelMapping b);
}
