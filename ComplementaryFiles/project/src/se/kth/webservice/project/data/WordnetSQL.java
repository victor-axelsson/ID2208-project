package se.kth.webservice.project.data;

import se.kth.webservice.project.model.DictionaryLookup;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by victoraxelsson on 2017-02-27.
 */
public class WordnetSQL extends  Database implements IWordnet{

    private static final String DICTIONARY_QUERY = "SELECT ssensenum,sw.lemma,link,dw.lemma AS linkedlemma,SUBSTRING(sdefinition FROM 1 FOR 60) FROM sensesXsemlinksXsenses AS l LEFT JOIN words AS sw ON l.swordid = sw.wordid LEFT JOIN words AS dw ON l.dwordid = dw.wordid LEFT JOIN linktypes USING (linkid) WHERE sw.lemma = ? ORDER BY linkid,ssensenum";

    private static final String LINK = "SELECT ssensenum,sw.lemma,link,dw.lemma AS linkedlemma,SUBSTRING(sdefinition FROM 1 FOR 60) FROM sensesXsemlinksXsenses AS l LEFT JOIN words AS sw ON l.swordid = sw.wordid LEFT JOIN words AS dw ON l.dwordid = dw.wordid LEFT JOIN linktypes USING (linkid) WHERE sw.lemma = ? and dw.lemma= ? ORDER BY linkid,ssensenum";

    @Override
    public List<DictionaryLookup> lookupInDictionary(String term) {
        List<DictionaryLookup> lookups = new ArrayList<>();

        try {
            PreparedStatement prepared = getPreparedStatement(DICTIONARY_QUERY);
            prepared.setString(1, term);

            ResultSet rs = prepared.executeQuery();

            while(rs.next()){
                lookups.add(new DictionaryLookup(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5)
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            safeCloseConnection();
        }

        return lookups;
    }

    @Override
    public List<DictionaryLookup> lookupLinks(String term, String secondTerm) {
        List<DictionaryLookup> lookups = new ArrayList<>();

        try {
            PreparedStatement prepared = getPreparedStatement(LINK);
            prepared.setString(1, term);
            prepared.setString(2, secondTerm);

            ResultSet rs = prepared.executeQuery();

            while(rs.next()){
                lookups.add(new DictionaryLookup(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5)
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            safeCloseConnection();
        }

        return lookups;
    }
}
