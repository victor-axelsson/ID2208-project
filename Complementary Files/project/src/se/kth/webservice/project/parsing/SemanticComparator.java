package se.kth.webservice.project.parsing;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import se.kth.webservice.project.model.XMLModelMapping;
import se.kth.webservice.project.output.WsdlComparisonResult;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by victoraxelsson on 2017-03-01.
 */
public class SemanticComparator extends UnicastRemoteObject implements IComparable{

        private OWLOntology ontology;
        private OWLDataFactory df;

    public SemanticComparator() throws RemoteException {}



    public void parse(){
            String x = "/Users/victoraxelsson/Desktop/intellijProjects/ID2208-project/Complementary Files/project/data/SUMO.owl";

            IRI documentIRI = IRI.create(x);
            OWLOntologyManager manager = OWLManager.createOWLOntologyManager();

            try {
                ontology = manager.loadOntology(documentIRI);
                df = manager.getOWLDataFactory();

            } catch (OWLOntologyCreationException e) {
                e.printStackTrace();
            }
        }


        public void parseOntology() throws OWLOntologyCreationException {


            for (OWLClass cls : ontology.getClassesInSignature()) {
                String id = cls.getIRI().toString();
                //String label = get(cls, RDFS_LABEL.toString()).get(0);
                //System.out.println(label + " [" + id + "]");
            }
        }

    @Override
    public WsdlComparisonResult getSimmilarityRating(XMLModelMapping a, XMLModelMapping b) throws RemoteException {
        return null;
    }
}
