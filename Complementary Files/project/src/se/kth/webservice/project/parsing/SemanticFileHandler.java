package se.kth.webservice.project.parsing;

import se.kth.webservice.project.model.SemanticModelMapping;

/**
 * Created by victoraxelsson on 2017-03-01.
 */
public class SemanticFileHandler extends FileHandler {


    public SemanticFileHandler(OnCompare<SemanticModelMapping> onCompare){
        super(onCompare);
    }

    @Override
    public void startComparing() {
        System.out.println("starting to compare");
        onCompare.compare(null, null);
    }

    @Override
    public void process() {
        System.out.println("Processing");
    }

    @Override
    public String getFolderName() {
        return "SAWSDL";
    }

}
