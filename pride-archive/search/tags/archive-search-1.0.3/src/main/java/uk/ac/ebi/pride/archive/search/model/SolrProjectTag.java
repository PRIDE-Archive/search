package uk.ac.ebi.pride.archive.search.model;

import uk.ac.ebi.pride.archive.dataprovider.project.ProjectTagProvider;

/**
 * @author Jose A. Dianes
 * @version $Id$
 */
public class SolrProjectTag implements ProjectTagProvider {

    private String projectTag;

    public void setProjectTag(String projectTag) {
        this.projectTag = projectTag;
    }

    @Override
    public String getTag() {
        return projectTag;
    }

    @Override
    public Long getId() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
