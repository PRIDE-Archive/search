package uk.ac.ebi.pride.archive.search.service;

import java.util.*;

/**
 * @author Rui Wang
 * @version $Id$
 */
public class ProjectSearchSummary {
    private String projectAccession;
    private String title;
    private String projectDescription;

    private int numExperiments;
    private int numProtein;
    private int numPeptide;
    private int numUniquePeptide;
    private int numIdentifiedSpectrum;
    private int numTotalSpectrum;
    private Date publicationDate;
    private String submissionType;

    private Collection<String> ptmNames;
    private Set<String> speciesNames;
    private Set<String> tissueNames;
    private Set<String> diseaseNames;
    private Set<String> cellTypeNames;
    private Set<String> instrumentModels;
    private Set<String> quantificationMethods;
    private Set<String> experimentTypes;
    private List<String> assayAccessions;
    private Set<String> projectTagNames;
    private Map<String, Long> proteinIdentifications;

    private Map<String, List<String>> highlights;


    public String getProjectDescription() {
        return projectDescription;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    public Map<String, List<String>> getHighlights() {
        return highlights;
    }

    public void setHighlights(Map<String, List<String>> highlights) {
        this.highlights = highlights;
    }

    public Set<String> getInstrumentModels() {
        return instrumentModels;
    }

    public void setInstrumentModels(Set<String> instrumentModels) {
        this.instrumentModels = instrumentModels;
    }

    public Set<String> getQuantificationMethods() {
        return quantificationMethods;
    }

    public void setQuantificationMethods(Set<String> quantificationMethods) {
        this.quantificationMethods = quantificationMethods;
    }

    public Set<String> getExperimentTypes() {
        return experimentTypes;
    }

    public void setExperimentTypes(Set<String> experimentTypes) {
        this.experimentTypes = experimentTypes;
    }

    public Set<String> getTissueNames() {
        return tissueNames;
    }

    public void setTissueNames(Set<String> tissueNames) {
        this.tissueNames = tissueNames;
    }

    public Set<String> getDiseaseNames() {
        return diseaseNames;
    }

    public void setDiseaseNames(Set<String> diseaseNames) {
        this.diseaseNames = diseaseNames;
    }

    public Set<String> getCellTypeNames() {
        return cellTypeNames;
    }

    public void setCellTypeNames(Set<String> cellTypeNames) {
        this.cellTypeNames = cellTypeNames;
    }

    public String getProjectAccession() {
        return projectAccession;
    }

    public void setProjectAccession(String projectAccession) {
        this.projectAccession = projectAccession;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Collection<String> getSpeciesNames() {
        return speciesNames;
    }

    public void setSpeciesNames(Set<String> speciesNames) {
        this.speciesNames = speciesNames;
    }

    public int getNumExperiments() {
        return numExperiments;
    }

    public void setNumExperiments(int numExperiments) {
        this.numExperiments = numExperiments;
    }

    public int getNumProtein() {
        return numProtein;
    }

    public void setNumProtein(int numProtein) {
        this.numProtein = numProtein;
    }

    public int getNumPeptide() {
        return numPeptide;
    }

    public void setNumPeptide(int numPeptide) {
        this.numPeptide = numPeptide;
    }

    public int getNumUniquePeptide() {
        return numUniquePeptide;
    }

    public void setNumUniquePeptide(int numUniquePeptide) {
        this.numUniquePeptide = numUniquePeptide;
    }

    public int getNumIdentifiedSpectrum() {
        return numIdentifiedSpectrum;
    }

    public void setNumIdentifiedSpectrum(int numIdentifiedSpectrum) {
        this.numIdentifiedSpectrum = numIdentifiedSpectrum;
    }

    public int getNumTotalSpectrum() {
        return numTotalSpectrum;
    }

    public void setNumTotalSpectrum(int numTotalSpectrum) {
        this.numTotalSpectrum = numTotalSpectrum;
    }

    public Date getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getSubmissionType() {
        return submissionType;
    }

    public void setSubmissionType(String submissionType) {
        this.submissionType = submissionType;
    }

    public Collection<String> getPtmNames() {
        return ptmNames;
    }

    public void setPtmNames(Collection<String> ptmNames) {
        this.ptmNames = ptmNames;
    }

    public List<String> getAssayAccessions() {
        return assayAccessions;
    }

    public void setAssayAccessions(List<String> assayAccessions) {
        this.assayAccessions = assayAccessions;
    }

    public Map<String, Long> getProteinIdentifications() {
        return proteinIdentifications;
    }

    public void setProteinIdentifications(Map<String, Long> proteinIdentifications) {
        this.proteinIdentifications = proteinIdentifications;
    }

    public Set<String> getProjectTagNames() {
        return projectTagNames;
    }

    public void setProjectTagNames(Set<String> projectTagNames) {
        this.projectTagNames = projectTagNames;
    }

}
