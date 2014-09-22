package uk.ac.ebi.pride.archive.search.model;

import uk.ac.ebi.pride.archive.dataprovider.identification.ProteinReferenceProvider;

import java.util.Set;

/**
 * @author Jose A. Dianes
 * @version $Id$
 */
public class SolrProteinIdentification implements ProteinReferenceProvider {

    private String accession;
    private String uniprotMapping;
    private String ensemblMapping;
    private Set<String> otherMappings;
    private String inferredSequence;
    private String submittedSequence;

    public void setAccession(String accession) {
        this.accession = accession;
    }

    public String getAccession() {
        return accession;
    }

    public String getUniprotMapping() {
        return uniprotMapping;
    }

    public void setUniprotMapping(String uniprotMapping) {
        this.uniprotMapping = uniprotMapping;
    }

    public String getEnsemblMapping() {
        return ensemblMapping;
    }

    public void setEnsemblMapping(String ensemblMapping) {
        this.ensemblMapping = ensemblMapping;
    }

    public Set<String> getOtherMappings() {
        return otherMappings;
    }

    public void setOtherMappings(Set<String> otherMappings) {
        this.otherMappings = otherMappings;
    }

    public String getInferredSequence() {
        return inferredSequence;
    }

    public void setInferredSequence(String inferredSequence) {
        this.inferredSequence = inferredSequence;
    }

    public String getSubmittedSequence() {
        return submittedSequence;
    }

    public void setSubmittedSequence(String submittedSequence) {
        this.submittedSequence = submittedSequence;
    }


}
