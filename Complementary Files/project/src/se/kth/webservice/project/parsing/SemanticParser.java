package se.kth.webservice.project.parsing;

import static org.semanticweb.owlapi.vocab.OWLRDFVocabulary.RDFS_LABEL;

import java.util.ArrayList;
import java.util.List;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLException;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
/**
 * Created by victoraxelsson on 2017-03-01.
 */
public class SemanticParser {

    private OWLOntology ontology;
    private OWLDataFactory df;

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
}
