package uk.ac.ebi.pride.archive.search.model;

import uk.ac.ebi.pride.archive.dataprovider.identification.ProteinIdentificationProvider;

import java.util.Set;

/**
 * @author Jose A. Dianes
 * @version $Id$
 */
public class SolrProteinIdentification implements ProteinIdentificationProvider {

    private String accession;
    private Set<String> synonyms;
    private String projectAccession;
    private String assayAccession;

    public void setAccession(String accession) {
        this.accession = accession;
    }

    public void setSynonyms(Set<String> synonyms) {
        this.synonyms = synonyms;
    }

    public void setProjectAccession(String projectAccession) {
        this.projectAccession = projectAccession;
    }

    public void setAssayAccession(String assayAccession) {
        this.assayAccession = assayAccession;
    }

    @Override
    public String getAccession() {
        return accession;
    }

    @Override
    public Set<String> getSynonyms() {
        return synonyms;
    }

    @Override
    public String getProjectAccession() {
        return projectAccession;
    }

    @Override
    public String getAssayAccession() {
        return assayAccession;
    }

}
