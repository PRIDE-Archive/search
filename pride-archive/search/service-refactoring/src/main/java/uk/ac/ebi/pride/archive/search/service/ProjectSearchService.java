package uk.ac.ebi.pride.archive.search.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.ac.ebi.pride.archive.dataprovider.param.CvParamProvider;
import uk.ac.ebi.pride.archive.dataprovider.project.ProjectProvider;
import uk.ac.ebi.pride.archive.search.model.SolrProject;
import uk.ac.ebi.pride.archive.search.service.dao.ProjectSearchDao;
import uk.ac.ebi.pride.archive.search.util.SearchFields;

import java.util.*;

/**
 * @author Jose A. Dianes
 * @version $Id$
 *
 */
@Component
public class ProjectSearchService {

    private static Logger logger = LoggerFactory.getLogger(ProjectSearchService.class.getName());

    private static final int MAX_DESCIRPTION_LENGTH = 64;
    private ProjectSearchDao projectSearchDao;

    public ProjectSearchService(ProjectSearchDao projectSearchDao) {
        this.projectSearchDao = projectSearchDao;
    }

    /**
     * Get a list of ProjectSummary instances satisfying the search criteria.
     *
     * @param query
     * @return
     */
    public Collection<ProjectSearchSummary> searchProjects(String query, String queryFields, String[] queryFilters) {
        //a negative number of rows will return all rows in search
        return searchProjects(query, queryFields, queryFilters, 0, -1, "id", "asc");
    }

    public Collection<ProjectSearchSummary> searchProjects(String query, String queryFields, String[] queryFilters, int start, int rows, String sort, String order) {
        Collection<ProjectSearchSummary> res = new LinkedList<ProjectSearchSummary>();

        Collection<ProjectProvider> projects = (Collection<ProjectProvider>) projectSearchDao.searchProjectAny(query, queryFields, queryFilters, start, rows, sort, order);

        for (ProjectProvider solrProject : projects) {
            res.add(createSearchSummary((SolrProject)solrProject));
        }

        return res;
    }

    public long numSearchResults(String query, String queryFields, String[] queryFilters) {
        return projectSearchDao.numDocuments(query, queryFields, queryFilters);
    }

    public Map<String, Long> getSpeciesCount(String query, String queryFields, String[] queryFilters) {
        return projectSearchDao.getSpeciesCount(query, queryFields, queryFilters);
    }

    public Map<String, Long> getTissueCount(String query, String queryFields, String[] queryFilters) {
        return projectSearchDao.getTissueCount(query, queryFields, queryFilters);
    }

    public Map<String, Long> getDiseaseCount(String query, String queryFields, String[] queryFilters) {
        return projectSearchDao.getDiseaseCount(query, queryFields, queryFilters);
    }

    public Map<String, Long> getInstrumentModelCount(String query, String queryFields, String[] queryFilters) {
        return projectSearchDao.getInstrumentModelCount(query, queryFields, queryFilters);
    }

    public Map<String, Long> getQuantificationMethodsCount(String query, String queryFields, String[] queryFilters) {
        return projectSearchDao.getQuantificationMethodsCount(query, queryFields, queryFilters);
    }

    public Map<String, Long> getExperimentTypesCount(String query, String queryFields, String[] queryFilters) {
        return projectSearchDao.getExperimentTypesCount(query, queryFields, queryFilters);
    }

    public Map<String, Long> getPtmCount(String query, String queryFields, String[] queryFilters) {
        return projectSearchDao.getPtmCount(query, queryFields, queryFilters);
    }

    public Map<String, Long> getProjectTagCount(String query, String queryFields, String[] queryFilters) {
        return projectSearchDao.getProjectTagCount(query, queryFields, queryFilters);
    }

    public Map<String, Long> getSubmissionTypeCount(String query, String queryFields, String[] queryFilters) {
        return projectSearchDao.getSubmissionTypeCount(query, queryFields, queryFilters);
    }

    private ProjectSearchSummary createSearchSummary(SolrProject solrProject) {
        ProjectSearchSummary projectSearchSummary = new ProjectSearchSummary();
        projectSearchSummary.setProjectAccession(solrProject.getAccession());
        projectSearchSummary.setTitle(solrProject.getTitle());
        if (solrProject.getProjectDescription() != null) {
            projectSearchSummary.setProjectDescription(
                    solrProject.getProjectDescription().substring(0, Math.min(MAX_DESCIRPTION_LENGTH, solrProject.getProjectDescription().length()))
            );
        }
        if (solrProject.getSpeciesNames() != null)
            projectSearchSummary.setSpeciesNames(new HashSet<String>(solrProject.getSpeciesNames()));
        if (solrProject.getTissueNames() != null)
            projectSearchSummary.setTissueNames(new HashSet<String>(solrProject.getTissueNames()));
        if (solrProject.getDiseaseNames() != null)
            projectSearchSummary.setDiseaseNames(new HashSet<String>(solrProject.getDiseaseNames()));
        if (solrProject.getCellTypeNames() != null)
            projectSearchSummary.setCellTypeNames(new HashSet<String>(solrProject.getCellTypeNames()));
        if (solrProject.getInstrumentModels() != null)
            projectSearchSummary.setInstrumentModels(new HashSet<String>(solrProject.getInstrumentModels()));
        if (solrProject.getQuantificationMethodNames() != null)
            projectSearchSummary.setQuantificationMethods(new HashSet<String>(solrProject.getQuantificationMethodNames()));
        if (solrProject.getTypesExperimentsName() != null)
            projectSearchSummary.setExperimentTypes(new HashSet<String>(solrProject.getTypesExperimentsName()));
        projectSearchSummary.setNumExperiments(solrProject.getNumAssays());
        projectSearchSummary.setNumProtein(solrProject.getProteinCount());
        projectSearchSummary.setNumPeptide(solrProject.getPeptideCount());
        projectSearchSummary.setNumUniquePeptide(solrProject.getUniquePeptideCount());
        projectSearchSummary.setNumIdentifiedSpectrum(solrProject.getIdentifiedSpectrumCount());
        projectSearchSummary.setNumTotalSpectrum(solrProject.getTotalSpectrumCount());
        projectSearchSummary.setPtmNames(getPtmNames((Collection<CvParamProvider>) solrProject.getPtms()));
        projectSearchSummary.setAssayAccessions(solrProject.getAssaysAccession());
        projectSearchSummary.setPublicationDate(solrProject.getPublicationDate());
        projectSearchSummary.setSubmissionType(solrProject.getTypeSubmission());
        projectSearchSummary.setHighlights(solrProject.getHighlights());
        addProteinIdentificationsToSummary(projectSearchSummary,solrProject);


        //tags names
        if (solrProject.getProjectTagNames() != null)
             projectSearchSummary.setProjectTagNames(new HashSet<String>(solrProject.getProjectTagNames()));


        return projectSearchSummary;
    }

    /**
     * Build a list of protein identifications based on hits/highlights. Proteins can be very numerous and we don't want
     * to carry all of them to the front end
     * @param projectSearchSummary
     * @param solrProject
     */
    //TODO Noe: I think this method is not used because of the field is not store in the project index, but I need to double check
    @Deprecated
    private void addProteinIdentificationsToSummary(ProjectSearchSummary projectSearchSummary, SolrProject solrProject) {
        if (solrProject.getHighlights() != null && solrProject.getHighlights().containsKey(SearchFields.PROTEIN_IDENTIFICATIONS.getIndexName())) {
            if (projectSearchSummary.getProteinIdentifications() == null)
                projectSearchSummary.setProteinIdentifications(new HashMap<String,Long>());
            for (String proteinHighlight: solrProject.getHighlights().get(SearchFields.PROTEIN_IDENTIFICATIONS.getIndexName())) {
                String proteinAccession = proteinHighlight.substring(proteinHighlight.indexOf(' '));
                if (projectSearchSummary.getProteinIdentifications().containsKey(proteinAccession))
                    projectSearchSummary.getProteinIdentifications().put(proteinAccession,
                            projectSearchSummary.getProteinIdentifications().get(proteinAccession)+1
                    );
                else
                    projectSearchSummary.getProteinIdentifications().put(proteinAccession, new Long(1));
            }
        }
    }

    private Collection<String> getPtmNames(Collection<CvParamProvider> ptms) {
        List<String> ptmNames = new ArrayList<String>();
        for (CvParamProvider ptm : ptms) {
            ptmNames.add(ptm.getName());
        }
        return ptmNames;
    }

    private String getSpeciesFromSamples(Collection<CvParamProvider> samples) {
        String species = "";
        for (CvParamProvider sample : samples) {
            //TODO: way to identify species in index
//            if (sample.getCvLabel().equals(SPECIES_CV_LABEL)) {
            species = sample.getName();
//            }
        }
        return species;
    }
}
