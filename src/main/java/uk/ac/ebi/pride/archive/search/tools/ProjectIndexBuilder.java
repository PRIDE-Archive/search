package uk.ac.ebi.pride.archive.search.tools;

import org.apache.solr.client.solrj.SolrServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;
import uk.ac.ebi.pride.archive.ontology.search.service.OntologyTermSearchService;
import uk.ac.ebi.pride.archive.repo.assay.AssayRepository;
import uk.ac.ebi.pride.archive.repo.project.ProjectRepository;
import uk.ac.ebi.pride.archive.search.service.ProjectIndexService;
import uk.ac.ebi.pride.archive.search.service.dao.solr.ProjectIndexDaoSolr;
import uk.ac.ebi.pride.proteinidentificationindex.search.service.ProteinIdentificationSearchService;

/**
 * @author Jose A. Dianes
 * @version $Id$
 */
@Component
public class ProjectIndexBuilder {

  //DB Repositories
  @Autowired
  private ProjectRepository projectRepository;
  @Autowired
  private AssayRepository assayRepository;

  //Solr Repositories
  @Autowired
  private OntologyTermSearchService ontologyTermSearchService;
  @Autowired
  private ProteinIdentificationSearchService proteinIdentificationSearchService;

    /*
    HttpSolrServer is thread-safe and if you are using the following constructor,
    you *MUST* re-use the same instance for all requests.  If instances are created on
    the fly, it can cause a connection leak. The recommended practice is to keep a
    static instance of HttpSolrServer per solr server url and share it for all requests.
    See https://issues.apache.org/jira/browse/SOLR-861 for more details
    */

  @Autowired
  private SolrServer solrProjectServer;

  public static void main(String[] args) {
    ApplicationContext context = new ClassPathXmlApplicationContext("spring/app-context.xml");
    ProjectIndexBuilder projectIndexBuilder = context.getBean(ProjectIndexBuilder.class);
    if (args.length>0 && "inc".equals(args[0].toLowerCase())) {
      indexNonExistingPublicProjects(projectIndexBuilder);
    } else if (args.length>0 && "all".equals(args[0].toLowerCase())) {
      indexPublicProjects(projectIndexBuilder);
    } else {
      System.out.println("Arguments:");
      System.out.println("   inc   - index projects not already in the index");
      System.out.println("   all   - deletes the index and index all projects");
    }

  }

  public static void indexPublicProjects(ProjectIndexBuilder projectIndexBuilder) {
    ProjectIndexService projectIndexService = new ProjectIndexService(new ProjectIndexDaoSolr(
        projectIndexBuilder.solrProjectServer,
        projectIndexBuilder.projectRepository,
        projectIndexBuilder.assayRepository,
        projectIndexBuilder.ontologyTermSearchService,
        projectIndexBuilder.proteinIdentificationSearchService
    ));

    projectIndexService.indexAllPublicProjects();

  }

  public static void indexNonExistingPublicProjects(ProjectIndexBuilder projectIndexBuilder) {
    ProjectIndexService projectIndexService = new ProjectIndexService(new ProjectIndexDaoSolr(
        projectIndexBuilder.solrProjectServer,
        projectIndexBuilder.projectRepository,
        projectIndexBuilder.assayRepository,
        projectIndexBuilder.ontologyTermSearchService,
        projectIndexBuilder.proteinIdentificationSearchService
    ));

    projectIndexService.indexAllNonExistingPublicProjects();

  }

}
