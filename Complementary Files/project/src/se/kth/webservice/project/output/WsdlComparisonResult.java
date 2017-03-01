package se.kth.webservice.project.output;

import java.util.List;

/**
 * Created by Nick on 3/1/2017.
 */
public class WsdlComparisonResult {
    private final String first;
    private final String second;
    private final double score;
    private final List<OperationComparisonResult> operationComparisonResults;

    public WsdlComparisonResult(String first, String second, double score, List<OperationComparisonResult> operationComparisonResults) {
        this.first = first;
        this.second = second;
        this.score = score;
        this.operationComparisonResults = operationComparisonResults;
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

    public List<OperationComparisonResult> getOperationComparisonResults() {
        return operationComparisonResults;
    }

    @Override
    public String toString() {
        return "WsdlComparisonResult{" +
                "first='" + first + '\'' +
                ", second='" + second + '\'' +
                ", score=" + score +
                ", operationComparisonResults=" + operationComparisonResults +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WsdlComparisonResult that = (WsdlComparisonResult) o;

        if (Double.compare(that.score, score) != 0) return false;
        if (first != null ? !first.equals(that.first) : that.first != null) return false;
        if (second != null ? !second.equals(that.second) : that.second != null) return false;
        return operationComparisonResults != null ? operationComparisonResults.equals(that.operationComparisonResults) : that.operationComparisonResults == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = first != null ? first.hashCode() : 0;
        result = 31 * result + (second != null ? second.hashCode() : 0);
        temp = Double.doubleToLongBits(score);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (operationComparisonResults != null ? operationComparisonResults.hashCode() : 0);
        return result;
    }
}
