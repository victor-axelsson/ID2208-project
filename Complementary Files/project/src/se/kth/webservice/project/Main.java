package se.kth.webservice.project;

import se.kth.webservice.project.data.IWordnet;
import se.kth.webservice.project.data.WordnetSQL;
import se.kth.webservice.project.model.DictionaryLookup;

import java.util.List;

/**
 * Created by victoraxelsson on 2017-02-27.
 */
public class Main {

    public static void main (String arg[]){
        System.out.println("Flowers and kittens and wiskers");

        IWordnet repo = new WordnetSQL();
        List<DictionaryLookup> lookups =  repo.lookupInDictionary("gravy");

        System.out.println("done");

    }
}
