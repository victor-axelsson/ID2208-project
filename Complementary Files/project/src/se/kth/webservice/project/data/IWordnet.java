package se.kth.webservice.project.data;

import se.kth.webservice.project.model.DictionaryLookup;

import java.util.List;

/**
 * Created by victoraxelsson on 2017-02-27.
 */
public interface IWordnet {
    List<DictionaryLookup> lookupInDictionary(String term);
    List<DictionaryLookup> lookupLinks(String term, String secondTerm);
}
