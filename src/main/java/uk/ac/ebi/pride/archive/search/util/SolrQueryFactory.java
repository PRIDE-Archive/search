package uk.ac.ebi.pride.archive.search.util;

import org.apache.solr.client.solrj.SolrQuery;

/**
 * @author Jose A. Dianes
 * @version $Id$
 */
public class SolrQueryFactory {

    private static final String QUERY_DEF_TYPE = "edismax";
    private static final String EDISMAX_TIE_FACTOR = "0.8";  // 0.0 means that just the maximum scoring field is used. 1.0 means that all scoring fields are summed

    public static SolrQuery getBasicEDismaxQuery(String queryString, String queryFields, String[] filters) {
        SolrQuery query = new SolrQuery();

        query.setParam("defType", QUERY_DEF_TYPE); // use DisMax query parser
        query.setParam("tie", EDISMAX_TIE_FACTOR); // 0.0 means that just the maximum scoring field is used. 1.0 means that all scoring fields are summed
        query.setParam("qf", queryFields);
        query.setParam("q", queryString);
        query.setParam("fq", filters);

        return query;
    }
}
