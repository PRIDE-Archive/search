package uk.ac.ebi.pride.archive.search.util;

import uk.ac.ebi.pride.archive.dataprovider.project.ProjectTagProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * User: ntoro
 * Date: 28/03/2014
 * Time: 10:31
 */
public class ProjectTagHelperMethods {
    public static List<String> listTagNames(Collection<? extends ProjectTagProvider> projectTags) {
        List<String> stringList = new ArrayList<String>();
        for (ProjectTagProvider tag : projectTags) {
            stringList.add(tag.getTag());
        }
        return stringList;
    }
}
