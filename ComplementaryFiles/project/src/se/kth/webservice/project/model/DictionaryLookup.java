package se.kth.webservice.project.model;

/**
 * Created by victoraxelsson on 2017-02-27.
 */
public class DictionaryLookup {
    int ssensenum;
    String lemma;
    String link;
    String linkedLemma;
    String decription;

    public DictionaryLookup(int ssensenum, String lemma, String link, String linkedLemma, String decription) {
        this.ssensenum = ssensenum;
        this.lemma = lemma;
        this.link = link;
        this.linkedLemma = linkedLemma;
        this.decription = decription;
    }

    @Override
    public String toString() {
        return "DictionaryLookup{" +
                "ssensenum=" + ssensenum +
                ", lemma='" + lemma + '\'' +
                ", link='" + link + '\'' +
                ", linkedLemma='" + linkedLemma + '\'' +
                ", decription='" + decription + '\'' +
                '}';
    }

    public int getSsensenum() {
        return ssensenum;
    }

    public String getLemma() {
        return lemma;
    }

    public String getLink() {
        return link;
    }

    public String getLinkedLemma() {
        return linkedLemma;
    }

    public String getDecription() {
        return decription;
    }
}
