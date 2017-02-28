package ontology;

/**
 * Created by Nick on 2/28/2017.
 */
public class Test
{
    public static void main(String[] args) {
        System.out.println(EditDistance.getEditDistance("SendACHTransSoapOut", "GetCancellationsSoapIn"));
        System.out.println(EditDistance.getEditDistance("SendACHTransSoapOut", "CancelSubscriptionSoapIn"));
        System.out.println(EditDistance.getEditDistance("same", "different"));
    }
}
