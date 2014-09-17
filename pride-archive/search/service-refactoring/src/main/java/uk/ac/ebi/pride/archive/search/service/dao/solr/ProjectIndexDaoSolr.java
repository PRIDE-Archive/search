package uk.ac.ebi.pride.archive.search.service.dao.solr;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ebi.pride.archive.dataprovider.assay.AssayProvider;
import uk.ac.ebi.pride.archive.dataprovider.identification.PeptideSequenceProvider;
import uk.ac.ebi.pride.archive.dataprovider.identification.ProteinIdentificationProvider;
import uk.ac.ebi.pride.archive.dataprovider.identification.ProteinReferenceProvider;
import uk.ac.ebi.pride.archive.dataprovider.project.ProjectProvider;
import uk.ac.ebi.pride.archive.ontology.search.service.OntologyTermSearchService;
import uk.ac.ebi.pride.archive.repo.assay.AssayRepository;
import uk.ac.ebi.pride.archive.repo.project.ProjectRepository;
import uk.ac.ebi.pride.archive.search.model.SolrPeptideSequence;
import uk.ac.ebi.pride.archive.search.model.SolrProject;
import uk.ac.ebi.pride.archive.search.util.modelmapper.SolrProjectMapper;
import uk.ac.ebi.pride.archive.search.model.SolrProteinIdentification;
import uk.ac.ebi.pride.archive.search.service.dao.ProjectIndexDao;
import uk.ac.ebi.pride.archive.search.util.CvParamHelperMethods;
import uk.ac.ebi.pride.archive.search.util.SolrQueryFactory;

import uk.ac.ebi.pride.proteinidentificationindex.search.model.ProteinIdentification;
import uk.ac.ebi.pride.proteinidentificationindex.search.service.ProteinIdentificationSearchService;
import uk.ac.ebi.pride.proteinindex.search.synonyms.ProteinAccessionSynonymsFinder;
import uk.ac.ebi.pride.psmindex.search.service.PsmSearchService;

import java.io.IOException;
import java.util.*;

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
    private ProteinIdentificationSearchService proteinIdentificationSearchService;
    private PsmSearchService psmSearchService;

    private final static int STEP = 89;

    @Deprecated
    public ProjectIndexDaoSolr(SolrServer projectServer,
                               ProjectRepository projectRepository,
                               AssayRepository assayRepository,
                               OntologyTermSearchService ontologyTermSearchService,
                               ProteinIdentificationSearchService proteinIdentificationSearchService) {
        this.projectServer = projectServer;
        this.projectRepository = projectRepository;
        this.assayRepository = assayRepository;
        this.ontologyTermSearchService = ontologyTermSearchService;
        this.proteinIdentificationSearchService = proteinIdentificationSearchService;

    }

    public ProjectIndexDaoSolr(SolrServer projectServer,
                               ProjectRepository projectRepository,
                               AssayRepository assayRepository,
                               OntologyTermSearchService ontologyTermSearchService,
                               ProteinIdentificationSearchService proteinIdentificationSearchService,
                               PsmSearchService psmSearchService) {
        this(projectServer, projectRepository, assayRepository, ontologyTermSearchService, proteinIdentificationSearchService);
        this.psmSearchService = psmSearchService;

    }


    @Override
    public void indexAllPublicProjects() {

        // get all projects on repository
        Iterable<? extends ProjectProvider> projects = projectRepository.findAll();

        deleteIndex();

        try {
            for (ProjectProvider project : projects) {
                if (project.getPublicationDate() != null) { // we are going to index just public projects
                    createAndIndexSolrProject(project);
                }
            }
        } catch (SolrServerException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    @Override
    public void indexAllNonExistingPublicProjects() {

        // get all projects on repository
        Iterable<? extends ProjectProvider> projects = projectRepository.findAll();

        try {
            for (ProjectProvider project : projects) {
                if (project.getPublicationDate() != null) { // we are going to index just public projects
                    // just index projects not previously indexed
                    SolrQuery projectExistsSolrQuery = SolrQueryFactory.getBasicEDismaxQuery("id:" + project.getAccession(), null, null);
                    QueryResponse res = projectServer.query(projectExistsSolrQuery);
                    if (res.getBeans(SolrProject.class).size()==0) {
                        createAndIndexSolrProject(project);
                    } else {
                        logger.info("Project " + project.getAccession() + " already in the index. SKIPPING...");
                    }
                }
            }
        } catch (SolrServerException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


    }

    @Override
    public void addProject(ProjectProvider project,
                           Collection<? extends AssayProvider> assays,
                           Map<String, ? extends Map<String, ? extends ProteinReferenceProvider>> proteinReferences // a Map from assay accessions to protein references
    ) {
        try {

            SolrProjectMapper solrProject = new SolrProjectMapper(project, assays, createProteinIdentificationListFromReferencesMap(proteinReferences,project));
            // populate relatives
            if (ontologyTermSearchService != null)
                CvParamHelperMethods.populateRelatives(solrProject, ontologyTermSearchService);
            projectServer.addBean(solrProject);
            projectServer.commit();
        } catch (SolrServerException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Override
    public void addProject(ProjectProvider project,
                           Collection<? extends AssayProvider> assays,
                           Map<String, ? extends Map<String, ? extends ProteinReferenceProvider>> proteinReferences,
                           Collection<? extends PeptideSequenceProvider> peptideSequences) {

        //TODO Check if need to be deleted
        try {
            SolrProjectMapper solrProject = new SolrProjectMapper(project, assays,
                    createProteinIdentificationListFromReferencesMap(proteinReferences,project),
                    peptideSequences);

            // populate relatives
            if (ontologyTermSearchService != null)
                CvParamHelperMethods.populateRelatives(solrProject, ontologyTermSearchService);

            // TODO - get synonyms?

            projectServer.addBean(solrProject);
            projectServer.commit();
        } catch (SolrServerException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    @Override
    public void addProject(ProjectProvider project,
                           Collection<? extends AssayProvider> assays) {
        try {
            //create the solr object with proteins and peptide sequences
            SolrProjectMapper solrProject = new SolrProjectMapper(project, assays,
                    getProteinIdentificationsForProject(project),
                    getPeptideSequencesForProject(project));

            // populate relatives
            if (ontologyTermSearchService != null)
                CvParamHelperMethods.populateRelatives(solrProject, ontologyTermSearchService);

            // TODO - get synonyms?

            projectServer.addBean(solrProject);
            projectServer.commit();
        } catch (SolrServerException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private long getTotalProteinCount(Map<String, Collection<ProteinIdentificationProvider>> proteinReferences) {
        long res = 0;
        if (proteinReferences != null) {
            for (Map.Entry<String, Collection<ProteinIdentificationProvider>> proteinIdentificationEntry : proteinReferences.entrySet()) {
                res = res + proteinIdentificationEntry.getValue().size();
            }
        }
        return res;
    }

    private long getTotalProteinSynonymCount(Map<String, Collection<ProteinIdentificationProvider>> proteinReferences) {
        long res = 0;

        if (proteinReferences != null) {
            for (Map.Entry<String, Collection<ProteinIdentificationProvider>> proteinIdentificationEntry : proteinReferences.entrySet()) {
                for (ProteinIdentificationProvider proteinIdentificationProvider : proteinIdentificationEntry.getValue()) {
                    if (proteinIdentificationProvider.getSynonyms() != null) {
                        res = res + proteinIdentificationProvider.getSynonyms().size();
                    }
                }
            }
        }

        return res;
    }

    private void deleteIndex() {
        try {
            //WARNING: deletes ALL entries from index
            projectServer.deleteByQuery("*:*");
            projectServer.commit();
            logger.info("All index entries deleted!");
        } catch (SolrServerException e) {
            throw new RuntimeException("Failed to delete data in Solr. "
                    + e.getMessage(), e);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete data in Solr. "
                    + e.getMessage(), e);
        }
    }

    private void createAndIndexSolrProject(ProjectProvider project) throws IOException, SolrServerException {

        List<? extends ProteinIdentificationProvider> proteinIdentifications = getProteinIdentificationsForProject(project);
        Collection<PeptideSequenceProvider> peptideSequences = getPeptideSequencesForProject(project);

        logger.info("Got " + proteinIdentifications.size() + " protein identifications from Protein index for project " + project.getAccession());
        logger.info("Got " + peptideSequences.size() + " peptide sequences from PSM index for project " + project.getAccession());

        //create the solr object with proteins and peptide sequences
        SolrProjectMapper solrProject = new SolrProjectMapper(
                project,
                assayRepository.findAllByProjectId(project.getId()),
                proteinIdentifications,
                peptideSequences
        );

        // populate relatives
        CvParamHelperMethods.populateRelatives(solrProject, ontologyTermSearchService);

        //Reminder: solrProject.getPeptideSequences() and solrProject.getProteinIdentifications() will return null
        //because the field is indexed but not stored

        logger.info("Project accession: " + solrProject.getAccession());
//        logger.info("Protein identifications count: " + getTotalProteinCount(solrProject.getProteinIdentifications()));
//        logger.info("Protein synonyms count: " + getTotalProteinSynonymCount(solrProject.getProteinIdentifications()));

        projectServer.addBean(solrProject);
        projectServer.commit();
        logger.info("Indexed project " + solrProject.getAccession());
    }

//    private List<ProteinIdentificationProvider> buildProteinIdentificationItems(List<ProteinIdentification> proteinIdentification, ProjectProvider project) {
//        LinkedList<ProteinIdentificationProvider> res = new LinkedList<ProteinIdentificationProvider>();
//
//        for (ProteinIdentification protein: proteinIdentification) {
//            for (String assayAccession: protein.getAssayAccessions()) {
//                SolrProteinIdentification proteinIdentification = new SolrProteinIdentification();
//                proteinIdentification.setAccession(protein.getAccession());
//                proteinIdentification.setProjectAccession(project.getAccession());
//                proteinIdentification.setAssayAccession(assayAccession);
//                proteinIdentification.setSynonyms(protein.getSynonyms());
//                if (proteinIdentification.getSynonyms() == null) {
//                    proteinIdentification.setSynonyms(new LinkedHashSet<String>());
//                }
//                proteinIdentification.getSynonyms().add(proteinIdentification.getAccession()); // add the accession, for safety (this should be added already in the synonyms)
//                res.add(proteinIdentification);
//            }
//        }
//
//        return res;
//    }

    private List<? extends ProteinIdentificationProvider> getProteinIdentificationsForProject(ProjectProvider project) {
        List<ProteinIdentification> proteinIdentifications = new LinkedList<ProteinIdentification>();
        // build protein identifications from protein identification service
        if (proteinIdentificationSearchService != null) {
            proteinIdentifications = proteinIdentificationSearchService.findByProjectAccession(project.getAccession());
        } else {
            logger.warn("Protein identification index not available. Protein identifications and synonyms will not be added.");
        }
        return proteinIdentifications;
    }

    /**
     *  Retrieves peptide sequences from the psm store
     * @param project Project to query the psm store and retrieve the peptides
     * @return peptide sequences
     */
    private Collection<PeptideSequenceProvider> getPeptideSequencesForProject(ProjectProvider project) {
        Collection<PeptideSequenceProvider> peptideSequences = new LinkedList<PeptideSequenceProvider>();
        // build protein identifications from psm service
        if (psmSearchService != null) {
            peptideSequences = buildPeptideSequenceProviderItems(psmSearchService.findPeptideSequencesByProjectAccession(project.getAccession()));
        } else {
            logger.warn("Psm index not available. Peptide sequences will not be added.");
        }
        return peptideSequences;
    }

    private Collection<PeptideSequenceProvider> buildPeptideSequenceProviderItems(List<String> peptideSequences) {

        Collection<PeptideSequenceProvider> res = new LinkedList<PeptideSequenceProvider>();

        if(peptideSequences!=null) {
            for (String sequence: peptideSequences) {
                SolrPeptideSequence peptideSequence = new SolrPeptideSequence();
                peptideSequence.setPeptideSequence(sequence);
                res.add(peptideSequence);
            }
        }
        return res;
    }

    private Collection<? extends ProteinIdentificationProvider> createProteinIdentificationListFromReferencesMap(
            Map<String, ? extends Map<String, ? extends ProteinReferenceProvider>> proteinReferences,
            ProjectProvider project
    ) {
        Collection<ProteinIdentificationProvider> res = new LinkedList<ProteinIdentificationProvider>();

        if(proteinReferences!= null) {
            for (Map.Entry<String, ? extends Map<String, ? extends ProteinReferenceProvider>> proteinReferencesEntry : proteinReferences.entrySet()) {
                String assayAccession = proteinReferencesEntry.getKey();
                // get synonyms
                ProteinAccessionSynonymsFinder.getAllSynonyms(proteinReferencesEntry.getValue());
                // create the protein identification provider instance
                for (ProteinReferenceProvider proteinReference : proteinReferencesEntry.getValue().values()) {
                    SolrProteinIdentification solrProteinIdentification = new SolrProteinIdentification();
                    solrProteinIdentification.setAccession(proteinReference.getAccession());
                    solrProteinIdentification.setProjectAccession(project.getAccession());
                    solrProteinIdentification.setAssayAccession(assayAccession);
                    solrProteinIdentification.setSynonyms(proteinReference.getSynonyms());

                    res.add(solrProteinIdentification);
                }
            }
        }

        return res;
    }

}
