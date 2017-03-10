package uk.ac.ebi.pride.archive.search.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ebi.pride.archive.dataprovider.assay.AssayProvider;
import uk.ac.ebi.pride.archive.dataprovider.identification.PeptideSequenceProvider;
import uk.ac.ebi.pride.archive.dataprovider.identification.ProteinReferenceProvider;
import uk.ac.ebi.pride.archive.dataprovider.project.ProjectProvider;
import uk.ac.ebi.pride.archive.search.service.dao.ProjectIndexDao;

import java.util.Collection;

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
                         Collection<? extends ProteinReferenceProvider> proteinReferences) {
    this.projectIndexDao.addProject(project,assays, proteinReferences);
  }

  public void addProject(ProjectProvider project) {
    this.projectIndexDao.addProject(project);
  }
}
