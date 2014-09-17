package uk.ac.ebi.pride.archive.search.model;

import uk.ac.ebi.pride.archive.dataprovider.identification.PeptideSequenceProvider;

/**
 * @author Jose A. Dianes
 * @version $Id$
 */
public class SolrPeptideSequence implements PeptideSequenceProvider {

    private String peptideSequence;

    //TODO  Noe: I need to think if I really need them in this layer
//    private String projectAccession;
//    private String assayAccession;

    public void setPeptideSequence(String peptideSequence) {
        this.peptideSequence = peptideSequence;
    }

//    public void setProjectAccession(String projectAccession) {
//        this.projectAccession = projectAccession;
//    }
//
//    public void setAssayAccession(String assayAccession) {
//        this.assayAccession = assayAccession;
//    }

    @Override
    public String getPeptideSequence() {
        return peptideSequence;
    }

//    @Override
//    public String getProjectAccession() {
//        return projectAccession;
//    }
//
//    @Override
//    public String getAssayAccession() {
//        return assayAccession;
//    }

}
