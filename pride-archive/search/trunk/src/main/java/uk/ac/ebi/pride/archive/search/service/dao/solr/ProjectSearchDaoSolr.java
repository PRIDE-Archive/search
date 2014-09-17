package uk.ac.ebi.pride.archive.search.service.dao.solr;


import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import uk.ac.ebi.pride.archive.dataprovider.project.ProjectProvider;
import uk.ac.ebi.pride.archive.search.model.SolrProject;
import uk.ac.ebi.pride.archive.search.service.dao.ProjectSearchDao;
import uk.ac.ebi.pride.archive.search.util.SearchFields;
import uk.ac.ebi.pride.archive.search.util.SolrQueryFactory;

import java.util.*;

/**
 * @author Jose A. Dianes
 * @version $Id$
 */
public class ProjectSearchDaoSolr implements ProjectSearchDao {

    private static final String DEFFAULT_CRITERIA = "score";
    private static final String DEFAULT_ORDER = "asc";
    private static final String QMETHODS_NAMES_FIELD = "quantification_methods_names";
    private static final String EXPERIMENT_TYPES_NAMES_FIELD = "experiment_types_name";
    private static final String PTM_FACET_NAMES_FIELD = "ptm_facet_names";
    private static final String INSTRUMENT_FACET_NAMES_FIELD = "instruments_facet_names";
    public static final String HIGHLIGHT_PRE_FRAGMENT = "<span class='search-hit'>";
    public static final String HIGHLIGHT_POST_FRAGMENT = "</span>";

    /*
          HttpSolrServer is thread-safe and if you are using the following constructor,
          you *MUST* re-use the same instance for all requests.  If instances are created on
          the fly, it can cause a connection leak. The recommended practice is to keep a
          static instance of HttpSolrServer per solr projectServer url and share it for all requests.
          See https://issues.apache.org/jira/browse/SOLR-861 for more details
     */
    private SolrServer projectServer;

    public ProjectSearchDaoSolr(SolrServer projectServer){
        this.projectServer = projectServer;
    }

    private SolrQuery prepareBasicQuery(String queryString, String queryFields, String[] filters) {
        // prepare the query
        SolrQuery query = SolrQueryFactory.getBasicEDismaxQuery(queryString, queryFields, filters);
        query.setHighlight(true);
        query.setParam("f.project_title.hl.fragsize", "0"); // title highlights must include the whole title
        query.setParam("hl.fl",
                SearchFields.TITLE.getIndexName() + " " +
                        SearchFields.PROJECT_DESCRIPTION.getIndexName() + " " +
                        SearchFields.PROJECT_TAGS_AS_TEXT.getIndexName() + " " +
                        SearchFields.SPECIES_NAMES_AS_TEXT.getIndexName() + " " +
                        SearchFields.SPECIES_ASCENDANTS_AS_TEXT.getIndexName() + " " +
                        SearchFields.DISEASE_AS_TEXT.getIndexName() + " " +
                        SearchFields.DISEASE_ASCENDANTS_AS_TEXT.getIndexName() + " " +
                        SearchFields.TISSUE_AS_TEXT.getIndexName() + " " +
                        SearchFields.TISSUE_ASCENDANTS_AS_TEXT.getIndexName() + " " +
                        SearchFields.CELL_TYPE_AS_TEXT.getIndexName() + " " +
                        SearchFields.CELL_TYPE_ASCENDANTS_AS_TEXT.getIndexName() + " " +
                        SearchFields.PTM_NAMES_AS_TEXT.getIndexName() + " " +
                        SearchFields.PTM_ACCESSIONS.getIndexName() + " " +
                        SearchFields.INSTRUMENT_MODELS_AS_TEXT.getIndexName() + " " +
                        SearchFields.EXPERIMENT_TYPES_AS_TEXT.getIndexName() + " " +
                        SearchFields.QUANTIFICATION_METHODS_AS_TEXT.getIndexName() + " " +
                        SearchFields.SAMPLE_AS_TEXT.getIndexName() + " " +
                        SearchFields.SUBMISSION_TYPE.getIndexName()
        );
        query.setParam("hl.highlightMultiTerm", true);
        query.setParam("hl.snippets", "5");
        query.setParam("hl.simple.pre", HIGHLIGHT_PRE_FRAGMENT);
        query.setParam("hl.simple.post", HIGHLIGHT_POST_FRAGMENT);

        return query;
    }

    @Override
    public Collection<? extends ProjectProvider> searchProjectAny(String queryString, String queryFields, String[] filters, int start, int rows, String sortBy, String order) {

        // prepare the query
        SolrQuery query = prepareBasicQuery(queryString, queryFields, filters);
        query.addSortField(sortBy, SolrQuery.ORDER.valueOf(order));
        query.setStart(start);
        if (rows > 0) query.setRows(rows);

        // perform the query
        try {
            // Query the projectServer
            QueryResponse rsp = projectServer.query(query);
            // get the results
            // To read Documents as beans, the bean must be annotated
            List<SolrProject> projects = rsp.getBeans(SolrProject.class);

            // add highlights
            Map<String, Map<String, List<String>>> highlights = rsp.getHighlighting();
            for (SolrProject solrProject : projects) {
                solrProject.setHighlights(highlights.get(solrProject.getAccession()));
            }

            return projects;

        } catch (SolrServerException e) {
            e.printStackTrace();
        }

        return null;

    }

    public Collection<? extends ProjectProvider> searchProjectAny(String queryString, String queryFields, String[] filters) {
        // negative number of rows will indicate you want to return all results
        return searchProjectAny(queryString, queryFields, filters, 1, -1, DEFFAULT_CRITERIA, DEFAULT_ORDER);
    }

    @Override
    public long numDocuments(String queryString, String queryFields, String[] filters) {

        SolrQuery query = SolrQueryFactory.getBasicEDismaxQuery(queryString,queryFields,filters);
        query.setRows(0); //setting the rows to 0 will only return number, not results

        // perform the query
        try{
            // Query the projectServer
            QueryResponse rsp = projectServer.query(query);
            // get the results
            SolrDocumentList docs = rsp.getResults();
            return docs.getNumFound();
        }  catch (SolrServerException e) {
            e.printStackTrace();
        }

        return 0;
    }

    @Override
    public Map<String, Long> getSpeciesCount(String queryString, String queryFields, String[] filters) {

        Map<String, Long> res = new HashMap<String, Long>();

        // prepare the query
        SolrQuery query = prepareBasicQuery(queryString,queryFields,filters);
        query.setFacet(true);
        query.setFacetLimit(-1); // unlimited facets for counts
        query.addFacetField(SearchFields.SPECIES_ASCENDANTS_NAMES.getIndexName());
        query.addFacetField(SearchFields.SPECIES_NAMES.getIndexName());
        query.setRows(0); //setting the rows to 0 will only return number, not results

        // perform the query
        try{
            // Query the projectServer
            QueryResponse rsp = projectServer.query(query);

            // get the facets
            FacetField speciesFacetField = rsp.getFacetField(SearchFields.SPECIES_ASCENDANTS_NAMES.getIndexName());
            FacetField speciesFacetFieldNoAscendants = rsp.getFacetField(SearchFields.SPECIES_NAMES.getIndexName());
            if (speciesFacetField != null) {
                for (FacetField.Count count : speciesFacetField.getValues()) {
                    if (containsCount(speciesFacetFieldNoAscendants.getValues(), count))
                        res.put(count.getName(), count.getCount());
                }
            }

            return res;
        } catch (SolrServerException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Map<String, Long> getTissueCount(String queryString, String queryFields, String[] filters) {

        Map<String, Long> res = new HashMap<String, Long>();

        // prepare the query
        SolrQuery query = prepareBasicQuery(queryString, queryFields, filters);
        query.setFacet(true);
        query.setFacetLimit(-1); // unlimited facets for counts
        query.addFacetField(SearchFields.TISSUE_NAMES.getIndexName());
        query.addFacetField(SearchFields.TISSUE_ASCENDANTS_NAMES.getIndexName());
        query.setRows(0); //setting the rows to 0 will only return number, not results

        // perform the query
        try {
            // Query the projectServer
            QueryResponse rsp = projectServer.query(query);

            // get the facets
            FacetField tissueFacetField = rsp.getFacetField(SearchFields.TISSUE_ASCENDANTS_NAMES.getIndexName());
            FacetField tissueFacetFieldNoAscendants = rsp.getFacetField(SearchFields.TISSUE_NAMES.getIndexName());
            if (tissueFacetField != null) {
                for (FacetField.Count count : tissueFacetField.getValues()) {
                    if (containsCount(tissueFacetFieldNoAscendants.getValues(), count))
                        res.put(count.getName(), count.getCount());
                }
            }

            return res;
        } catch (SolrServerException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Map<String, Long> getDiseaseCount(String queryString, String queryFields, String[] filters) {

        Map<String, Long> res = new HashMap<String, Long>();

        // prepare the query
        SolrQuery query = SolrQueryFactory.getBasicEDismaxQuery(queryString, queryFields, filters);
        query.setFacet(true);
        query.addFacetField(SearchFields.DISEASE_NAMES.getIndexName());
        query.addFacetField(SearchFields.DISEASE_ASCENDANTS_NAMES.getIndexName());
        query.setRows(0); //setting the rows to 0 will only return number, not results

        // perform the query
        try {
            // Query the projectServer
            QueryResponse rsp = projectServer.query(query);

            // get the facets
            FacetField diseaseFacetField = rsp.getFacetField(SearchFields.DISEASE_NAMES.getIndexName());
            FacetField diseaseFacetFieldNoAscendants = rsp.getFacetField(SearchFields.DISEASE_ASCENDANTS_NAMES.getIndexName());
            if (diseaseFacetField != null) {
                for (FacetField.Count count : diseaseFacetField.getValues()) {
                    if (containsCount(diseaseFacetFieldNoAscendants.getValues(), count))
                        res.put(count.getName(), count.getCount());
                }
            }

            return res;
        } catch (SolrServerException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Map<String, Long> getInstrumentModelCount(String queryString, String queryFields, String[] filters) {

        Map<String, Long> res = new HashMap<String, Long>();

        // prepare the query
        SolrQuery query = SolrQueryFactory.getBasicEDismaxQuery(queryString, queryFields, filters);
        query.setFacet(true);
        query.addFacetField(INSTRUMENT_FACET_NAMES_FIELD);
        query.setRows(0); //setting the rows to 0 will only return number, not results

        try {
            // Query the projectServer
            QueryResponse rsp = projectServer.query(query);

            // get the facets
            FacetField instrumentFacetField = rsp.getFacetField(INSTRUMENT_FACET_NAMES_FIELD);
            if (instrumentFacetField != null) {
                for (FacetField.Count count : instrumentFacetField.getValues()) {
                    res.put(count.getName(), count.getCount());
                }
            }
            return res;
        } catch (SolrServerException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Map<String, Long> getQuantificationMethodsCount(String queryString, String queryFields, String[] filters) {

        Map<String, Long> res = new HashMap<String, Long>();

        // prepare the query
        SolrQuery query = SolrQueryFactory.getBasicEDismaxQuery(queryString, queryFields, filters);
        query.setFacet(true);
        query.addFacetField(QMETHODS_NAMES_FIELD);
        query.setRows(0); //setting the rows to 0 will only return number, not results

        try {
            // Query the projectServer
            QueryResponse rsp = projectServer.query(query);

            // get the facets
            FacetField qmethodsFacetField = rsp.getFacetField(QMETHODS_NAMES_FIELD);
            if (qmethodsFacetField != null) {
                for (FacetField.Count count : qmethodsFacetField.getValues()) {
                    res.put(count.getName(), count.getCount());
                }
            }
            return res;
        } catch (SolrServerException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Map<String, Long> getExperimentTypesCount(String queryString, String queryFields, String[] filters) {

        Map<String, Long> res = new HashMap<String, Long>();

        // prepare the query
        SolrQuery query = SolrQueryFactory.getBasicEDismaxQuery(queryString, queryFields, filters);
        query.setFacet(true);
        query.addFacetField(EXPERIMENT_TYPES_NAMES_FIELD);
        query.setRows(0); //setting the rows to 0 will only return number, not results

        try {
            // Query the projectServer
            QueryResponse rsp = projectServer.query(query);
            // get the facets
            FacetField experimentTypesFacetField = rsp.getFacetField(EXPERIMENT_TYPES_NAMES_FIELD);
            if (experimentTypesFacetField != null) {
                for (FacetField.Count count : experimentTypesFacetField.getValues()) {
                    res.put(count.getName(), count.getCount());
                }
            }
            return res;
        } catch (SolrServerException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Map<String, Long> getPtmCount(String queryString, String queryFields, String[] filters) {

        Map<String, Long> res = new HashMap<String, Long>();

        // prepare the query
        SolrQuery query = SolrQueryFactory.getBasicEDismaxQuery(queryString, queryFields, filters);
        query.setFacet(true);
        query.addFacetField(PTM_FACET_NAMES_FIELD);
        query.setRows(0); //setting the rows to 0 will only return number, not results

        try {
            // Query the projectServer
            QueryResponse rsp = projectServer.query(query);

            // get the facets
            FacetField ptmsFacetField = rsp.getFacetField(PTM_FACET_NAMES_FIELD);
            if (ptmsFacetField != null) {
                for (FacetField.Count count : ptmsFacetField.getValues()) {
                    res.put(count.getName(), count.getCount());
                }
            }
            return res;
        } catch (SolrServerException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Map<String, Long> getProjectTagCount(String queryString, String queryFields, String[] filters) {

        Map<String, Long> res = new HashMap<String, Long>();

        // prepare the query
        SolrQuery query = SolrQueryFactory.getBasicEDismaxQuery(queryString, queryFields, filters);
        query.setFacet(true);
        query.addFacetField(SearchFields.PROJECT_TAGS.getIndexName());
        query.setRows(0); //setting the rows to 0 will only return number, not results

        // perform the query
        try {
            // Query the projectServer
            QueryResponse rsp = projectServer.query(query);

            // get the facets
            FacetField projectTagFacetField = rsp.getFacetField(SearchFields.PROJECT_TAGS.getIndexName());

            if (projectTagFacetField != null) {
                for (FacetField.Count count : projectTagFacetField.getValues()) {
                    res.put(count.getName(), count.getCount());
                }
            }

            return res;
        } catch (SolrServerException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Map<String, Long> getSubmissionTypeCount(String queryString, String queryFields, String[] filters) {

        Map<String, Long> res = new HashMap<String, Long>();

        // prepare the query
        SolrQuery query = SolrQueryFactory.getBasicEDismaxQuery(queryString, queryFields, filters);
        query.setFacet(true);
        query.addFacetField(SearchFields.SUBMISSION_TYPE.getIndexName());
        query.setRows(0); //setting the rows to 0 will only return number, not results

        // perform the query
        try {
            // Query the projectServer
            QueryResponse rsp = projectServer.query(query);

            // get the facets
            FacetField projectTagFacetField = rsp.getFacetField(SearchFields.SUBMISSION_TYPE.getIndexName());

            if (projectTagFacetField != null) {
                for (FacetField.Count count : projectTagFacetField.getValues()) {
                    res.put(count.getName(), count.getCount());
                }
            }
            return res;
        } catch (SolrServerException e) {
            e.printStackTrace();
        }

        return null;
    }

    private boolean containsCount(List<FacetField.Count> values, FacetField.Count count) {
        boolean found = false;

        Iterator<FacetField.Count> it = values.iterator();
        while (!found && it.hasNext()) {
            found = it.next().getName().equals(count.getName());
        }

        return found;
    }
}
