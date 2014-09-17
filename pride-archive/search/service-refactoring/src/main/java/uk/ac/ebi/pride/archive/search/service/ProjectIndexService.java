package uk.ac.ebi.pride.archive.search.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ebi.pride.archive.dataprovider.assay.AssayProvider;
import uk.ac.ebi.pride.archive.dataprovider.project.ProjectProvider;
import uk.ac.ebi.pride.archive.search.service.dao.ProjectIndexDao;
import uk.ac.ebi.pride.archive.dataprovider.identification.PeptideSequenceProvider;
import uk.ac.ebi.pride.archive.dataprovider.identification.ProteinReferenceProvider;

import java.util.Collection;
import java.util.Map;

/**
 * @author Jose A. Dianes
 * @version $Id$
 *
 */
public class ProjectIndexService {

    private static Logger logger = LoggerFactory.getLogger(ProjectIndexService.class.getName());
    private ProjectIndexDao projectIndexDao;

    public ProjectIndexService(ProjectIndexDao projectIndexDao) {
        this.projectIndexDao = projectIndexDao;
    }


    public void indexAllPublicProjects() {
        this.projectIndexDao.indexAllPublicProjects();
    }

    public void indexAllNonExistingPublicProjects() {
        this.projectIndexDao.indexAllNonExistingPublicProjects();
    }

    public void addProject(ProjectProvider project,
                    Collection<? extends AssayProvider> assays,
                    Map<String, ? extends Map<String, ? extends ProteinReferenceProvider>> proteinReferences // a Map from assay accessions to protein references
    ) {
        this.projectIndexDao.addProject(project,assays,proteinReferences);
    }

    public void addProject(ProjectProvider project,
                    Collection<? extends AssayProvider> assays,
                    Map<String, ? extends Map<String, ? extends ProteinReferenceProvider>> proteinReferences, // a Map from assay accessions to protein references
                    Collection<? extends PeptideSequenceProvider> peptideSequences
    ) {
        this.addProject(project,assays,proteinReferences,peptideSequences);
    }

    public void addProject(ProjectProvider project,
                    Collection<? extends AssayProvider> assays
    ) {
        this.addProject(project,assays);
    }
}
