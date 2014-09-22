package uk.ac.ebi.pride.archive.search.model;

import org.apache.solr.client.solrj.beans.Field;
import uk.ac.ebi.pride.archive.dataprovider.assay.AssayProvider;
import uk.ac.ebi.pride.archive.dataprovider.identification.PeptideSequenceProvider;
import uk.ac.ebi.pride.archive.dataprovider.identification.ProteinIdentificationProvider;
import uk.ac.ebi.pride.archive.dataprovider.identification.ProteinReferenceProvider;
import uk.ac.ebi.pride.archive.dataprovider.param.CvParamProvider;
import uk.ac.ebi.pride.archive.dataprovider.param.ParamProvider;
import uk.ac.ebi.pride.archive.dataprovider.person.ContactProvider;
import uk.ac.ebi.pride.archive.dataprovider.person.UserProvider;
import uk.ac.ebi.pride.archive.dataprovider.project.ProjectProvider;
import uk.ac.ebi.pride.archive.dataprovider.project.ProjectTagProvider;
import uk.ac.ebi.pride.archive.dataprovider.project.SubmissionType;
import uk.ac.ebi.pride.archive.dataprovider.reference.ReferenceProvider;
import uk.ac.ebi.pride.archive.repo.param.CvParam;
import uk.ac.ebi.pride.archive.repo.project.ProjectInstrumentCvParam;
import uk.ac.ebi.pride.archive.repo.project.Reference;

import java.util.*;

/**
 * @author Jose A. Dianes
 * @version $Id$
 *
 * TODO: we need search-specific implementations of the following data-provider interfaces: Intrument*Provider,
 *       ReferenceProvider, CvParamProvider. Right now we are instantiating pride-repo entities that implement those
 *       Interfaces but this is not the right thing to do.
 */
public class SolrProject implements ProjectProvider {


    //we will be using the accession to identify the documents in the index
    @Field("id")
    private String accession;

    @Field("doi")
    private String doi;

    @Field("project_title")
    private String title;

    @Field("project_description")
    private String projectDescription;

    @Field("sample_processing_protocol")
    private String sampleProcessingProtocol;

    @Field("data_processing_protocol")
    private String dataProcessingProtocol;

    @Field
    private String keywords;

    @Field("assays_accession")
    private List<String> assaysAccession;

    @Field("experiment_types_accession")
    private List<String> typesExperimentsAccession;

    @Field("experiment_types_name")
    private List<String> typesExperimentsName;

    @Field("submission_type")
    private String typeSubmission;

    @Field("pubmed_ids")
    private List<Integer> pubmedIds;

    @Field("reference_dois")
    private List<String> referenceDois;

    @Field("sample_names")
    private List<String> sampleNames;

    @Field("sample_accessions")
    private List<String> sampleAccessions;

    @Field("sample_labels")
    private List<String> sampleLabels;

    @Field("species_names")
    private List<String> speciesNames;

    @Field("species_accessions")
    private List<String> speciesAccessions;

    @Field("species_descendants_accessions")
    private List<String> speciesAscendantsAccessions;

    @Field("species_descendants_names")
    private List<String> speciesAscendantsNames;

    @Field("tissue_names")
    private List<String> tissueNames;

    @Field("tissue_accessions")
    private List<String> tissueAccessions;

    @Field("tissue_descendants_accessions")
    private List<String> tissueAscendantsAccessions;

    @Field("tissue_descendants_names")
    private List<String> tissueAscendantsNames;

    @Field("disease_names")
    private List<String> diseaseNames;

    @Field("disease_accessions")
    private List<String> diseaseAccessions;

    @Field("disease_descendants_accessions")
    private List<String> diseaseAscendantsAccessions;

    @Field("disease_descendants_names")
    private List<String> diseaseAscendantsNames;

    @Field("cell_type_names")
    private List<String> cellTypeNames;

    @Field("cell_type_accessions")
    private List<String> cellTypeAccessions;

    @Field("cell_type_descendants_accessions")
    private List<String> cellTypeAscendantsAccessions;

    @Field("cell_type_descendants_names")
    private List<String> cellTypeAscendantsNames;

    @Field("instruments_models")
    private List<String> instrumentModels;

    @Field("instruments_accessions")
    private List<String> instrumentAccessions;

    @Field("instruments_source_names")
    private List<String> instrumentSourceNames;

    @Field("instruments_detector_names")
    private List<String> instrumentDetectorNames;

    @Field("instruments_analyzer_names")
    private List<String> instrumentAnalyzerNames;

    @Field("ptm_names")
    private List<String> ptmNames;

    @Field("ptm_accessions")
    private List<String> ptmAccessions;

    @Field("quantification_methods_names")
    private List<String> quantificationMethodNames;

    @Field("quantification_methods_accessions")
    private List<String> quantificationMethodAccessions;

    private List<String> softwareNames;

    private List<String> softwareAccessions;

    @Field("assays_count")
    private int numExperiments;

    @Field("protein_count")
    private int numProtein;

    @Field("peptide_count")
    private int numPeptide;

    @Field("unique_peptide_count")
    private int numUniquePeptide;

    @Field("identified_spectrum_count")
    private int numIdentifiedSpectrum;

    @Field("total_spectrum_count")
    private int numTotalSpectrum;

    @Field("publication_date")
    private Date publicationDate;

    //This field is not store, so when you retrieve the value from solr is always null
    @Field("protein_identifications")
    private Set<String> proteinIdentifications;

    //This field is not store, so when you retrieve the value from solr is always null
    @Field("peptide_sequences")
    private Set<String> peptideSequences;

    private Map<String, List<String>> highlights;

    @Field("project_tags")
    private List<String> projectTagNames;

    private List<String> labHeads;


    public Map<String, List<String>> getHighlights() {
        return highlights;
    }

    public void setHighlights(Map<String, List<String>> highlights) {
        this.highlights = highlights;
    }

    public String getAccession() {
        return accession;
    }

    public void setAccession(String accession) {
        this.accession = accession;
    }

    public String getDoi() {
        return doi;
    }

    public void setDoi(String doi) {
        this.doi = doi;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    public String getSampleProcessingProtocol() {
        return sampleProcessingProtocol;
    }

    public void setSampleProcessingProtocol(String sampleProcessingProtocol) {
        this.sampleProcessingProtocol = sampleProcessingProtocol;
    }

    public String getDataProcessingProtocol() {
        return dataProcessingProtocol;
    }

    public void setDataProcessingProtocol(String dataProcessingProtocol) {
        this.dataProcessingProtocol = dataProcessingProtocol;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public List<String> getAssaysAccession() {
        return assaysAccession;
    }

    public void setAssaysAccession(List<String> assaysAccession) {
        this.assaysAccession = assaysAccession;
    }

    public List<String> getTypesExperimentsAccession() {
        return typesExperimentsAccession;
    }

    public void setTypesExperimentsAccession(List<String> typesExperimentsAccession) {
        this.typesExperimentsAccession = typesExperimentsAccession;
    }

    public List<String> getTypesExperimentsName() {
        return typesExperimentsName;
    }

    public void setTypesExperimentsName(List<String> typesExperimentsName) {
        this.typesExperimentsName = typesExperimentsName;
    }

    public String getTypeSubmission() {
        return typeSubmission;
    }

    public void setTypeSubmission(String typeSubmission) {
        this.typeSubmission = typeSubmission;
    }

    public List<Integer> getPubmedIds() {
        return pubmedIds;
    }

    public void setPubmedIds(List<Integer> pubmedIds) {
        this.pubmedIds = pubmedIds;
    }

    public List<String> getReferenceDois() {
        return referenceDois;
    }

    public void setReferenceDois(List<String> referenceDois) {
        this.referenceDois = referenceDois;
    }

    public List<String> getSampleLabels() {
        return sampleLabels;
    }

    public void setSampleLabels(List<String> sampleLabels) {
        this.sampleLabels = sampleLabels;
    }

    public List<String> getSampleNames() {
        return sampleNames;
    }

    public void setSampleNames(List<String> sampleNames) {
        this.sampleNames = sampleNames;
    }

    public List<String> getSampleAccessions() {
        return sampleAccessions;
    }

    public List<String> getSpeciesNames() {
        return speciesNames;
    }

    public void setSpeciesNames(List<String> speciesNames) {
        this.speciesNames = speciesNames;
    }

    public List<String> getSpeciesAccessions() {
        return speciesAccessions;
    }

    public void setSpeciesAccessions(List<String> speciesAccessions) {
        this.speciesAccessions = speciesAccessions;
    }

    public List<String> getTissueNames() {
        return tissueNames;
    }

    public void setTissueNames(List<String> tissueNames) {
        this.tissueNames = tissueNames;
    }

    public List<String> getTissueAccessions() {
        return tissueAccessions;
    }

    public void setTissueAccessions(List<String> tissueAccessions) {
        this.tissueAccessions = tissueAccessions;
    }

    public List<String> getTissueAscendantsNames() {
        return tissueAscendantsNames;
    }

    public void setTissueAscendantsNames(List<String> tissueAscendantsNames) {
        this.tissueAscendantsNames = tissueAscendantsNames;
    }

    public List<String> getTissueAscendantsAccessions() {
        return tissueAscendantsAccessions;
    }

    public void setTissueAscendantsAccessions(List<String> tissueAscendantsAccessions) {
        this.tissueAscendantsAccessions = tissueAscendantsAccessions;
    }

    public List<String> getDiseaseNames() {
        return diseaseNames;
    }

    public void setDiseaseNames(List<String> diseaseNames) {
        this.diseaseNames = diseaseNames;
    }

    public List<String> getDiseaseAccessions() {
        return diseaseAccessions;
    }

    public void setDiseaseAccessions(List<String> diseaseAccessions) {
        this.diseaseAccessions = diseaseAccessions;
    }

    public List<String> getDiseaseAscendantsAccessions() {
        return diseaseAscendantsAccessions;
    }

    public void setDiseaseAscendantsAccessions(List<String> diseaseAscendantsAccessions) {
        this.diseaseAscendantsAccessions = diseaseAscendantsAccessions;
    }

    public List<String> getDiseaseAscendantsNames() {
        return diseaseAscendantsNames;
    }

    public void setDiseaseAscendantsNames(List<String> diseaseAscendantsNames) {
        this.diseaseAscendantsNames = diseaseAscendantsNames;
    }

    public void setSampleAccessions(List<String> sampleAccessions) {
        this.sampleAccessions = sampleAccessions;
    }

    public List<String> getInstrumentModels() {
        return instrumentModels;
    }

    public void setInstrumentModels(List<String> instrumentModels) {
        this.instrumentModels = instrumentModels;
    }

    public List<String> getInstrumentAccessions() {
        return instrumentAccessions;
    }

    public void setInstrumentAccessions(List<String> instrumentAccessions) {
        this.instrumentAccessions = instrumentAccessions;
    }

    public List<String> getInstrumentSourceNames() {
        return instrumentSourceNames;
    }

    public void setInstrumentSourceNames(List<String> instrumentSourceNames) {
        this.instrumentSourceNames = instrumentSourceNames;
    }

    public List<String> getInstrumentDetectorNames() {
        return instrumentDetectorNames;
    }

    public void setInstrumentDetectorNames(List<String> instrumentDetectorNames) {
        this.instrumentDetectorNames = instrumentDetectorNames;
    }

    public List<String> getInstrumentAnalyzerNames() {
        return instrumentAnalyzerNames;
    }

    public void setInstrumentAnalyzerNames(List<String> instrumentAnalyzerNames) {
        this.instrumentAnalyzerNames = instrumentAnalyzerNames;
    }

    public List<String> getPtmNames() {
        return ptmNames;
    }

    public void setPtmNames(List<String> ptmNames) {
        this.ptmNames = ptmNames;
    }

    public List<String> getPtmAccessions() {
        return ptmAccessions;
    }

    public void setPtmAccessions(List<String> ptmAccessions) {
        this.ptmAccessions = ptmAccessions;
    }

    public List<String> getQuantificationMethodNames() {
        return quantificationMethodNames;
    }

    public void setQuantificationMethodNames(List<String> quantificationMethodNames) {
        this.quantificationMethodNames = quantificationMethodNames;
    }

    public List<String> getQuantificationMethodAccessions() {
        return quantificationMethodAccessions;
    }

    public void setQuantificationMethodAccessions(List<String> quantificationMethodAccessions) {
        this.quantificationMethodAccessions = quantificationMethodAccessions;
    }

    public List<String> getSoftwareNames() {
        return softwareNames;
    }

    public void setSoftwareNames(List<String> softwareNames) {
        this.softwareNames = softwareNames;
    }

    public List<String> getSoftwareAccessions() {
        return softwareAccessions;
    }

    public void setSoftwareAccessions(List<String> softwareAccessions) {
        this.softwareAccessions = softwareAccessions;
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

    public void setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
    }

    @Override
    public Date getPublicationDate() {
        return this.publicationDate;
    }

    @Override
    public String getOtherOmicsLink() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public <T extends UserProvider> T getSubmitter() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Collection<? extends UserProvider>  getUsers() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getNumAssays() {
        return numExperiments;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getReanalysis() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    //will get both lists, with name and accession, and return the CVParam for it
    public Collection<? extends CvParamProvider> getExperimentTypes() {
        List<CvParam> experimentTypes = new ArrayList<CvParam>();
        //iterate through both arrays at the same time, check sizes are equal first !
        if (typesExperimentsAccession!=null && typesExperimentsName!= null && typesExperimentsAccession.size() == typesExperimentsName.size()) {
            for (int i = 0; i < typesExperimentsAccession.size(); i++) {
                CvParam cvParam = new CvParam();
                cvParam.setAccession(typesExperimentsAccession.get(i));
                cvParam.setName(typesExperimentsName.get(i));
                experimentTypes.add(cvParam);
            }
            return experimentTypes;  //To change body of implemented methods use File | Settings | File Templates.
        }
        return null;
    }

    @Override
    public SubmissionType getSubmissionType() {
        return SubmissionType.fromString(typeSubmission);  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Date getSubmissionDate() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Date getUpdateDate() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    //iterate both lists to create the reference
    public Collection<? extends ReferenceProvider> getReferences() {
        List<Reference> references = new ArrayList<Reference>();
        if (referenceDois!= null && referenceDois.size() == pubmedIds.size()){
            for(int i = 0; i< pubmedIds.size(); i++){
                Reference reference = new Reference();
                reference.setPubmedId(pubmedIds.get(i));
                reference.setDoi(referenceDois.get(i));
                references.add(reference);
            }
        }
        return references;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Collection<? extends ProjectTagProvider> getProjectTags() {
        List<SolrProjectTag> tags = new ArrayList<SolrProjectTag>();
        if (projectTagNames != null){
            for (String projectTagName : projectTagNames) {
                SolrProjectTag newTag = new SolrProjectTag();
                newTag.setProjectTag(projectTagName);
                tags.add(newTag);
            }
        }
        return tags;
    }

    @Override
    public Collection<? extends ContactProvider> getLabHeads() {
        return null;
    }

    @Override
    //iterate both lists simultaneously (name and accession)
    public Collection<? extends CvParamProvider> getPtms() {
        List<CvParam> ptms = new ArrayList<CvParam>();
        if (ptmNames != null && ptmNames.size() == ptmAccessions.size()){
            for (int i=0; i< ptmNames.size(); i++){
                CvParam cvParam = new CvParam();
                cvParam.setAccession(ptmAccessions.get(i));
                cvParam.setName(ptmNames.get(i));
                ptms.add(cvParam);
            }
        }
        return ptms;
    }

    @Override
    public Collection<? extends CvParamProvider> getSamples() {
        List<CvParam> samples = new ArrayList<CvParam>();
        if (sampleAccessions != null && sampleAccessions.size() == sampleNames.size()){
            for (int i=0 ; i < sampleNames.size(); i ++){
                CvParam cvParam = new CvParam();
                cvParam.setAccession(sampleAccessions.get(i));
                cvParam.setName(sampleNames.get(i));
                cvParam.setCvLabel(sampleLabels.get(i));
                samples.add(cvParam);
            }
        }
        return samples;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Collection<? extends CvParamProvider> getInstruments() {
        List<CvParamProvider> instruments = new ArrayList<CvParamProvider>();
        if (instrumentModels != null && instrumentModels.size() == instrumentAnalyzerNames.size() &&
                instrumentModels.size() == instrumentDetectorNames.size() &&
                instrumentModels.size() == instrumentSourceNames.size()){
                for (int i=0; i < instrumentModels.size(); i++){
                    ProjectInstrumentCvParam instrument = new ProjectInstrumentCvParam();
                    //create Model Param from the name
                    CvParam instrumentModel = new CvParam();
                    instrumentModel.setName(instrumentModels.get(i));
//                    instrumentModel.setValue(INSTRUMENT_MODEL_VALUE); TODO
//                    instrumentModel.setCvLabel(INSTRUMENT_MODEL_LABEL); TODO
//                    instrumentModel.setAccession(INSTRUMENT_MODEL_ACCESSION); TODO
                    instrument.setCvParam(instrumentModel);
                    instrument.setValue(instrumentModels.get(i));
                    instruments.add(instrument);
                }
        }
        return instruments;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Collection<? extends CvParamProvider> getSoftware() {
        List<CvParam> software = new ArrayList<CvParam>();
        if (softwareAccessions != null && softwareAccessions.size() == softwareNames.size()){
            for(int i = 0; i< softwareNames.size(); i++){
                CvParam cvParam = new CvParam();
                cvParam.setAccession(softwareAccessions.get(i));
                cvParam.setName(softwareNames.get(i));
                software.add(cvParam);
            }
        }
        return software;
    }

    @Override
    public Collection<? extends CvParamProvider> getQuantificationMethods() {
        List<CvParam> quantificationMethods = new ArrayList<CvParam>();
        if (quantificationMethodAccessions != null && quantificationMethodAccessions.size() == quantificationMethodNames.size()){
            for(int i = 0; i< quantificationMethodNames.size(); i++){
                CvParam cvParam = new CvParam();
                cvParam.setAccession(quantificationMethodAccessions.get(i));
                cvParam.setName(quantificationMethodNames.get(i));
                quantificationMethods.add(cvParam);
            }
        }
        return quantificationMethods;
    }

    @Override
    public Map<String, Collection<ProteinIdentificationProvider>> getProteinIdentifications() {
        //The protein identification accession can not be retrieved because the field is not store in solr
        return null;
    }

    @Override
    public Collection<PeptideSequenceProvider> getPeptideSequences() {
        //The peptide sequences can not be retrieved because the field is not store in solr
        return null;
    }

    public void setProteinIdentifications(Collection<? extends ProteinReferenceProvider> proteinIdentifications) {

        Set<String> proteinIdentificationFields = new TreeSet<String> ();
        for (ProteinReferenceProvider proteinIdentification: proteinIdentifications) {

            // add accession
            if(proteinIdentification.getAccession() != null)
                proteinIdentificationFields.add(proteinIdentification.getAccession());

            // add uniprot mapping
            if(proteinIdentification.getUniprotMapping() != null)
                proteinIdentificationFields.add(proteinIdentification.getUniprotMapping());

            // add ensemble mapping
            if(proteinIdentification.getEnsemblMapping() != null)
                proteinIdentificationFields.add(proteinIdentification.getEnsemblMapping());

            // add other mappings
            if (proteinIdentification.getOtherMappings() != null) {
                for (String mapping : proteinIdentification.getOtherMappings()) {
                    proteinIdentificationFields.add(mapping);
                }
            }
        }

        this.proteinIdentifications = proteinIdentificationFields;
    }

    public void setPeptideSequences(Collection<? extends PeptideSequenceProvider> peptideSequences){

        Set<String> peptideSequenceFields = new TreeSet<String> ();

        for (PeptideSequenceProvider peptideSequence : peptideSequences) {
            peptideSequenceFields.add(peptideSequence.getPeptideSequence());
        }

        this.peptideSequences = peptideSequenceFields;
    }

    @Override
    public boolean isPublicProject() {
        return this.publicationDate != null;
    }

    @Override
    public Long getId() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Collection<? extends ParamProvider> getParams() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Collection<? extends AssayProvider> getAssays() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public int getProteinCount() {
        return numProtein;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public int getPeptideCount() {
        return numPeptide;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public int getUniquePeptideCount() {
        return numUniquePeptide;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public int getIdentifiedSpectrumCount() {
        return numIdentifiedSpectrum;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public int getTotalSpectrumCount() {
        return numTotalSpectrum;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setSpeciesAscendantsAccessions(LinkedList<String> speciesAscendantsAccessions) {
        this.speciesAscendantsAccessions = speciesAscendantsAccessions;
    }

    public List<String> getSpeciesAscendantsAccessions() {
        return speciesAscendantsAccessions;
    }

    public void setSpeciesAscendantsNames(LinkedList<String> speciesAscendantsNames) {
        this.speciesAscendantsNames = speciesAscendantsNames;
    }

    public List<String> getSpeciesAscendantsNames() {
        return speciesAscendantsNames;
    }

    public List<String> getCellTypeNames() {
        return cellTypeNames;
    }

    public void setCellTypeNames(List<String> cellTypeNames) {
        this.cellTypeNames = cellTypeNames;
    }

    public List<String> getCellTypeAccessions() {
        return cellTypeAccessions;
    }

    public void setCellTypeAccessions(List<String> cellTypeAccessions) {
        this.cellTypeAccessions = cellTypeAccessions;
    }

    public List<String> getCellTypeAscendantsAccessions() {
        return cellTypeAscendantsAccessions;
    }

    public void setCellTypeAscendantsAccessions(List<String> cellTypeAscendantsAccessions) {
        this.cellTypeAscendantsAccessions = cellTypeAscendantsAccessions;
    }

    public List<String> getCellTypeAscendantsNames() {
        return cellTypeAscendantsNames;
    }

    public void setCellTypeAscendantsNames(List<String> cellTypeAscendantsNames) {
        this.cellTypeAscendantsNames = cellTypeAscendantsNames;
    }

    public List<String> getProjectTagNames() {
        return projectTagNames;
    }

    public void setProjectTagNames(List<String> projectTagNames) {
        this.projectTagNames = projectTagNames;
    }
}
