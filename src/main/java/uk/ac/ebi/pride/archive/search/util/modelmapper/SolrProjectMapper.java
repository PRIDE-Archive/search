package uk.ac.ebi.pride.archive.search.util.modelmapper;

import uk.ac.ebi.pride.archive.dataprovider.assay.AssayProvider;
import uk.ac.ebi.pride.archive.dataprovider.identification.PeptideSequenceProvider;
import uk.ac.ebi.pride.archive.dataprovider.identification.ProteinReferenceProvider;
import uk.ac.ebi.pride.archive.dataprovider.project.ProjectProvider;
import uk.ac.ebi.pride.archive.dataprovider.reference.ReferenceProvider;
import uk.ac.ebi.pride.archive.search.model.SolrProject;
import uk.ac.ebi.pride.archive.search.util.CvParamHelperMethods;
import uk.ac.ebi.pride.archive.search.util.ProjectTagHelperMethods;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: dani, jadianes (adaptation to data-provider api, new fields)
 * Date: 07/12/12
 * Time: 12:40
 * To change this template use File | Settings | File Templates.
 */
public class SolrProjectMapper extends SolrProject {

    public static final String NODOI = "noDOI";

    public SolrProjectMapper(ProjectProvider project, Collection<? extends AssayProvider> assays) {
        initMethod(project, assays);
    }


    //initialize attributes from Project object
    private void initMethod(ProjectProvider project, Collection<? extends AssayProvider> assays){
        this.setAccession(project.getAccession());
        this.setDoi(project.getDoi());
        this.setTitle(project.getTitle());
        this.setProjectDescription(project.getProjectDescription());
        this.setSampleProcessingProtocol(project.getSampleProcessingProtocol());
        this.setDataProcessingProtocol(project.getDataProcessingProtocol());
        this.setKeywords(project.getKeywords());
        if (project.getSubmissionType() != null) this.setTypeSubmission(project.getSubmissionType().toString());
        if (project.getExperimentTypes() != null) this.setTypesExperimentsName(CvParamHelperMethods.listNameFromCvParam(project.getExperimentTypes()));
        if (project.getExperimentTypes() != null) this.setTypesExperimentsAccession(CvParamHelperMethods.listAccessionFromCvParam(project.getExperimentTypes()));
        if (project.getReferences() != null) this.setReferenceDois(getReferenceDois(project.getReferences()));
        if (project.getReferences() != null) this.setPubmedIds(getPubmedIds(project.getReferences()));
        if (project.getSamples() != null) this.setSampleNames(CvParamHelperMethods.listNameFromCvParamFiltered(project.getSamples()));
        if (project.getSamples() != null) this.setSampleAccessions(CvParamHelperMethods.listAccessionFromCvParamFiltered(project.getSamples()));
        if (project.getSamples() != null) this.setSampleLabels(CvParamHelperMethods.listLabelsFromCvParamFiltered(project.getSamples()));


        if (project.getSamples() != null) {
            // species-specific samples
            this.setSpeciesNames(CvParamHelperMethods.listSpeciesNamesFromCvParam(project.getSamples()));
            this.setSpeciesAccessions(CvParamHelperMethods.listSpeciesAccessionsFromCvParam(project.getSamples()));
            // tissue-specific samples
            this.setTissueNames(CvParamHelperMethods.listTissueNamesFromCvParam(project.getSamples()));
            this.setTissueAccessions(CvParamHelperMethods.listTissueAccessionsFromCvParam(project.getSamples()));
            // disease-specific samples
            this.setDiseaseNames(CvParamHelperMethods.listDiseaseNamesFromCvParam(project.getSamples()));
            this.setDiseaseAccessions(CvParamHelperMethods.listDiseaseAccessionsFromCvParam(project.getSamples()));
            // cell_type-specific samples
            this.setCellTypeNames(CvParamHelperMethods.listCellTypeNamesFromCvParam(project.getSamples()));
            this.setCellTypeAccessions(CvParamHelperMethods.listCellTypeAccessionsFromCvParam(project.getSamples()));
        }

        //tags
        if(project.getProjectTags() != null){
            this.setProjectTagNames(ProjectTagHelperMethods.listTagNames(project.getProjectTags()));
        }
        // instrument models
        if (project.getInstruments() != null)
            this.setInstrumentModels(CvParamHelperMethods.listProjectInstrumentModels(project.getInstruments()));

        // instrument accessions
        if (project.getInstruments() != null)
            this.setInstrumentAccessions(CvParamHelperMethods.listAssayInstrumentAccessions(project.getInstruments()));


        // instrument component names (from assays)
        if (assays!= null) {
            List<String> assaysAccession = new LinkedList<String>();
            for (AssayProvider assay: assays) {
                assaysAccession.add(assay.getAccession());
                if(project.getInstruments() != null) this.setInstrumentSourceNames(CvParamHelperMethods.listInstrumentSourceNames(assay.getInstruments()));
                if(project.getInstruments() != null) this.setInstrumentDetectorNames(CvParamHelperMethods.listInstrumentDetectorNames(assay.getInstruments()));
                if(project.getInstruments() != null) this.setInstrumentAnalyzerNames(CvParamHelperMethods.listInstrumentAnalyzerNames(assay.getInstruments()));
            }
            this.setAssaysAccession(assaysAccession);
        }

        // ptms
        if (project.getPtms() != null) this.setPtmNames(CvParamHelperMethods.listNameFromCvParam(project.getPtms()));
        if (project.getPtms() != null) this.setPtmAccessions(CvParamHelperMethods.listAccessionFromCvParam(project.getPtms()));

        // quantification methods
        if (project.getQuantificationMethods() != null) this.setQuantificationMethodNames(CvParamHelperMethods.listNameFromCvParam(project.getQuantificationMethods()));
        if (project.getQuantificationMethods() != null) this.setQuantificationMethodAccessions(CvParamHelperMethods.listAccessionFromCvParam(project.getQuantificationMethods()));

        // counting...
        this.setNumExperiments(project.getNumAssays());
        this.setNumProtein(calculateProteinCount(project));
        this.setNumPeptide(calculatePeptideCount(project));
        this.setNumUniquePeptide(calculateUniquePeptideCount(project));
        this.setNumIdentifiedSpectrum(calculateIdentifiedSpectrumCount(project));
        this.setNumTotalSpectrum(calculateTotalSpectrumCount(project));

        // publication date
        if (project.getPublicationDate() != null) this.setPublicationDate(project.getPublicationDate());
    }

    private int calculateTotalSpectrumCount(ProjectProvider project) {
        return 0;  //Todo
    }

    private int calculateIdentifiedSpectrumCount(ProjectProvider project) {
        return 0;  //Todo
    }

    private int calculateUniquePeptideCount(ProjectProvider project) {
        return 0;  //Todo
    }

    private int calculatePeptideCount(ProjectProvider project) {
        return 0;  //Todo
    }

    private int calculateProteinCount(ProjectProvider project) {
        return 0;  //Todo
    }



    public List<String> getReferenceDois(Collection<? extends ReferenceProvider> references) {
        List<String> dois = new ArrayList<String>();
        for (ReferenceProvider reference : references) {
            if (reference.getDoi() == null){
                dois.add(NODOI);
            }
            else{
                dois.add(reference.getDoi());
            }
        }
        return dois;    //To change body of overridden methods use File | Settings | File Templates.
    }

    public List<Integer> getPubmedIds(Collection<? extends ReferenceProvider> references) {
        List<Integer> pubmedIds = new ArrayList<Integer>();
        for (ReferenceProvider reference : references) {
            pubmedIds.add(reference.getPubmedId());
        }
        return pubmedIds;    //To change body of overridden methods use File | Settings | File Templates.
    }
}
