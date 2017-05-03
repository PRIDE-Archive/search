package uk.ac.ebi.pride.archive.search.service.dao.solr;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.method.P;
import uk.ac.ebi.pride.archive.dataprovider.assay.AssayProvider;
import uk.ac.ebi.pride.archive.dataprovider.identification.IdentificationProvider;
import uk.ac.ebi.pride.archive.dataprovider.identification.PeptideSequenceProvider;
import uk.ac.ebi.pride.archive.dataprovider.identification.ProteinIdentificationProvider;
import uk.ac.ebi.pride.archive.dataprovider.identification.ProteinReferenceProvider;
import uk.ac.ebi.pride.archive.dataprovider.project.ProjectProvider;
import uk.ac.ebi.pride.archive.ontology.search.service.OntologyTermSearchService;
import uk.ac.ebi.pride.archive.repo.assay.AssayRepository;
import uk.ac.ebi.pride.archive.repo.project.ProjectRepository;
import uk.ac.ebi.pride.archive.search.model.SolrPeptideSequence;
import uk.ac.ebi.pride.archive.search.model.SolrProject;
import uk.ac.ebi.pride.archive.search.service.dao.ProjectIndexDao;
import uk.ac.ebi.pride.archive.search.util.CvParamHelperMethods;
import uk.ac.ebi.pride.archive.search.util.SolrQueryFactory;
import uk.ac.ebi.pride.archive.search.util.modelmapper.SolrProjectMapper;
import uk.ac.ebi.pride.proteinidentificationindex.search.model.ProteinIdentification;
import uk.ac.ebi.pride.proteinidentificationindex.search.service.ProteinIdentificationSearchService;
import uk.ac.ebi.pride.psmindex.search.model.Psm;
import uk.ac.ebi.pride.psmindex.search.service.PsmSearchService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Jose A. Dianes
 * @version $Id$
 */
public class ProjectIndexDaoSolr implements ProjectIndexDao {

  private static Logger logger = LoggerFactory.getLogger(ProjectIndexDaoSolr.class.getName());

  private SolrServer projectServer;
  private ProjectRepository projectRepository;
  private AssayRepository assayRepository;
  private OntologyTermSearchService ontologyTermSearchService;

  public ProjectIndexDaoSolr(SolrServer projectServer,
                             ProjectRepository projectRepository,
                             AssayRepository assayRepository,
                             OntologyTermSearchService ontologyTermSearchService) {
    this.projectServer = projectServer;
    this.projectRepository = projectRepository;
    this.assayRepository = assayRepository;
    this.ontologyTermSearchService = ontologyTermSearchService;

  }

  @Override
  public void indexAllPublicProjects() {
    Iterable<? extends ProjectProvider> projects = projectRepository.findAll();
    deleteIndex();
    try {
      for (ProjectProvider project : projects) {
        if (project.getPublicationDate() != null) { // we are going to index just public projects
          createAndIndexSolrProject(project);
        }
      }
    } catch (SolrServerException|IOException e) {
      logger.error("Exception indexing Solr", e);
    }
  }

  @Override
  public void indexAllNonExistingPublicProjects() {
    Iterable<? extends ProjectProvider> projects = projectRepository.findAll();
    try {
      for (ProjectProvider project : projects) {
        if (project.getPublicationDate() != null) { // we are going to index just public projects not previously indexed
          SolrQuery projectExistsSolrQuery = SolrQueryFactory.getBasicEDismaxQuery("id:" + project.getAccession(), null, null);
          QueryResponse res = projectServer.query(projectExistsSolrQuery);
          if (res.getBeans(SolrProject.class).size()==0) {
            createAndIndexSolrProject(project);
          } else {
            logger.info("Project " + project.getAccession() + " already in the index. SKIPPING...");
          }
        }
      }
    } catch (SolrServerException|IOException e) {
      logger.error("Exception indexing Solr", e);
    }
  }

  @Override
  public void addProject(ProjectProvider project) {
    try {
      createAndIndexSolrProject(project);
    } catch (SolrServerException|IOException e) {
      logger.error("Exception indexing Solr", e);
    }
  }

  @Override
  public void addProject(ProjectProvider project,
                         Collection<? extends AssayProvider> assays) {
    try {
      SolrProjectMapper solrProject = new SolrProjectMapper(project, assays);
      if (ontologyTermSearchService != null) {
        CvParamHelperMethods.populateRelatives(solrProject, ontologyTermSearchService);
      }
      projectServer.addBean(solrProject);
      projectServer.commit();
    } catch (SolrServerException | IOException e) {
      logger.error("Exception indexing Solr", e);
    }
  }

  private void deleteIndex() {
    try {  //WARNING: deletes ALL entries from index
      projectServer.deleteByQuery("*:*");
      projectServer.commit();
      logger.info("All index entries deleted!");
    } catch (SolrServerException|IOException e) {
      logger.error("Exception indexing Solr", e);
    }
  }

  public void deleteProjectFromIndexAndReindex(String projectAccession) {
    try {
      deleteProjectFromIndex(projectAccession);
      ProjectProvider project = projectRepository.findByAccession(projectAccession);
      if (project.getPublicationDate() != null) { // we are going to index just public projects
        createAndIndexSolrProject(project);
      } else {
        logger.error("Not indexing project, it does not have a publication date.");
      }
    } catch (SolrServerException|IOException e) {
      logger.error("Exception indexing Solr", e);
    }
  }

  public void deleteProjectFromIndex(String projectAccession) throws IOException, SolrServerException {
    projectServer.deleteByQuery("id:" + projectAccession);
    projectServer.commit();
    logger.info("Project " + projectAccession + " deleted from index!");
  }


  private void createAndIndexSolrProject(ProjectProvider project) throws IOException, SolrServerException {
    //create the solr object with proteins and peptide sequences
    SolrProjectMapper solrProject = new SolrProjectMapper(project, assayRepository.findAllByProjectId(project.getId()));

    // populate relatives
    CvParamHelperMethods.populateRelatives(solrProject, ontologyTermSearchService);

    //Reminder: solrProject.getPeptideSequences() and solrProject.getProteinIdentifications() will return null
    //because the field is indexed but not stored
    logger.info("Project accession: " + solrProject.getAccession());
    projectServer.addBean(solrProject);
    projectServer.commit();
    logger.info("Indexed project " + solrProject.getAccession());
  }
}
