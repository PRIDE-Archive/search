package uk.ac.ebi.pride.archive.search.service.dao;

import uk.ac.ebi.pride.archive.dataprovider.project.ProjectProvider;

import java.util.Collection;
import java.util.Map;

/**
 * @author Jose A. Dianes
 * @version $Id$
 */
public interface ProjectSearchDao {

    Collection<? extends ProjectProvider> searchProjectAny(String queryString, String queryFields, String[] filters, int start, int rows, String sortBy, String order);
    Collection<? extends ProjectProvider> searchProjectAny(String queryString, String queryFields, String[] filters);
    long numDocuments(String query, String queryFields, String[] filters);
    Map<String, Long> getSpeciesCount(String queryString, String queryFields, String[] filters);
    Map<String, Long> getTissueCount(String queryString, String queryFields, String[] filters);
    Map<String, Long> getDiseaseCount(String queryString, String queryFields, String[] filters);
    Map<String, Long> getQuantificationMethodsCount(String queryString, String queryFields, String[] filters);
    Map<String, Long> getExperimentTypesCount(String queryString, String queryFields, String[] filters);
    Map<String, Long> getInstrumentModelCount(String queryString, String queryFields, String[] filters);
    Map<String, Long> getPtmCount(String queryString, String queryFields, String[] filters);
    Map<String, Long> getProjectTagCount(String queryString, String queryFields, String[] filters);
    Map<String, Long> getSubmissionTypeCount(String queryString, String queryFields, String[] filters);
}
