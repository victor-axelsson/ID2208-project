package se.kth.webservice.project.output;

import java.io.Serializable;

/**
 * Created by Nick on 3/1/2017.
 */
public class ElementComparisonResult implements Serializable{
    private final String first;
    private final String second;
    private final double score;

    public ElementComparisonResult(String first, String second, double score) {
        this.first = first;
        this.second = second;
        this.score = score;
    }

    public String getFirst() {
        return first;
    }

    public String getSecond() {
        return second;
    }

    public double getScore() {
        return score;
    }

    @Override
    public String toString() {
        return "ElementComparisonResult{" +
                "first='" + first + '\'' +
                ", second='" + second + '\'' +
                ", score=" + score +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ElementComparisonResult that = (ElementComparisonResult) o;

        if (score != that.score) return false;
        if (first != null ? !first.equals(that.first) : that.first != null) return false;
        return second != null ? second.equals(that.second) : that.second == null;
    }

    @Override
    public int hashCode() {
        int result = first != null ? first.hashCode() : 0;
        result = 31 * result + (second != null ? second.hashCode() : 0);
        result = (int) (31 * result + score);
        return result;
    }
}

