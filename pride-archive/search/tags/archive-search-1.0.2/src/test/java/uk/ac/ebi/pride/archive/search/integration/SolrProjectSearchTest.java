package uk.ac.ebi.pride.archive.search.integration;

import org.apache.solr.SolrTestCaseJ4;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.params.SolrParams;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import uk.ac.ebi.pride.archive.dataprovider.identification.PeptideSequenceProvider;
import uk.ac.ebi.pride.archive.dataprovider.identification.ProteinReferenceProvider;
import uk.ac.ebi.pride.archive.dataprovider.project.ProjectProvider;
import uk.ac.ebi.pride.archive.search.model.SolrProject;
import uk.ac.ebi.pride.archive.search.service.dao.ProjectIndexDao;
import uk.ac.ebi.pride.archive.search.service.dao.ProjectSearchDao;
import uk.ac.ebi.pride.archive.search.service.dao.solr.ProjectIndexDaoSolr;
import uk.ac.ebi.pride.archive.search.service.dao.solr.ProjectSearchDaoSolr;
import uk.ac.ebi.pride.archive.search.util.SearchFields;
import uk.ac.ebi.pride.jmztab.utils.errors.MZTabException;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Jose A. Dianes
 * @version $Id$
 */
public class SolrProjectSearchTest extends SolrTestCaseJ4 {

    private static final String PROTEIN_A5A5Z5_ACCESSION = "A5A5Z5";

    private static final String UNIPROT_ACCESSION = "A5A5Z5";
    private static final String ENSEMBL_ACCESSION = "A5A5Z5";
    private static final String OTHER_ACCESSION = "UPI0001509A10";

    private static final String FIRST_PEPTIDE_SEQUENCE_PXD000581 = "FLTDRYPISGIFSGK";
    private static final String SECOND_PEPTIDE_SEQUENCE_PXD000581 = "LLSQTTSVHFHGQVQR";
    private static final String THIRD_PEPTIDE_SEQUENCE_PXD000581 = "LYGILNHANAPVTR";
    private static final String FOUR_PEPTIDE_SEQUENCE_PXD000581 = "VHAASTAGPFPDNAVVAR";

    private static final String PROJECT_PXD000433_ACCESSION = "PXD000433";
    private static final String PROJECT_PXD000581_ACCESSION = "PXD000581";
    private static final String PROJECT_PXTEST1_ACCESSION = "PXTEST1";

    private static final String PROJECT_1_DOI = "doi123.456";
    private static final String PROJECT_1_TITLE = "doi123.456";
    private static final String PUBLICATION_DATE_AS_STRING = "02/13/14";

    private SolrServer server;
    private SolrProject solrProject;

    public static final long ZERO_DOCS = 0L;
    public static final long SINGLE_DOC = 1L;

    private static LinkedList<ProteinReferenceProvider> proteinReferencesPXD000433;
    private static LinkedList<PeptideSequenceProvider> peptideSequencesPXD000433;
    private static LinkedList<ProteinReferenceProvider> proteinReferencesPXD000581;
    private static LinkedList<PeptideSequenceProvider> peptideSequencesPXD000581;

    @BeforeClass
    public static void initialise() throws Exception {
        initCore(
                "src/test/resources/solr/collection1/conf/solrconfig.xml",
                "src/test/resources/solr/collection1/conf/schema.xml",
                "src/test/resources/solr"
        );
    }


    @Before
    @Override
    public void setUp() throws Exception {

        super.setUp();
        server = new EmbeddedSolrServer(h.getCoreContainer(), h.getCore().getName());
        server.deleteByQuery("*:*");

    }

    @Test
    public void testThatNoResultsAreReturned() throws SolrServerException {

        SolrParams params = new SolrQuery("text that is not found");
        QueryResponse response = server.query(params);
        assertEquals(ZERO_DOCS, response.getResults().getNumFound());
    }


    @Test
    public void testThatDocumentIsFound() throws SolrServerException, IOException {
        addProjectPXTEST1ToIndex();

        SolrParams params = new SolrQuery("id:PXTEST1");
        QueryResponse response = server.query(params);
        assertEquals(SINGLE_DOC, response.getResults().getNumFound());
        assertEquals(PROJECT_PXTEST1_ACCESSION, response.getResults().get(0).get("id"));
    }

    @Test
    public void testProjectTagsIsFound() throws SolrServerException, IOException {
        addProjectPXTEST1ToIndex();

        SolrParams params = new SolrQuery("id:PXTEST1");
        QueryResponse response = server.query(params);
        assertEquals(SINGLE_DOC, response.getResults().getNumFound());
        assertEquals(PROJECT_PXTEST1_ACCESSION, response.getResults().get(0).get("id"));
        assertEquals(Arrays.asList("Biological","Technical"), response.getResults().get(0).get("project_tags"));
    }

    @Test
    public void testSearchByProjectAccession() throws SolrServerException, IOException, URISyntaxException, ParseException, MZTabException {
        addProjectPXD000433ToIndex();
        addProjectPXD000581ToIndex();

        ProjectSearchDao projectSearchDao = new ProjectSearchDaoSolr(server);

        // search for PXD000433
        Collection<? extends ProjectProvider> res =
                projectSearchDao.searchProjectAny(
                        PROJECT_PXD000433_ACCESSION,
                        SearchFields.ACCESSION.getIndexName(),
                        null,
                        0,
                        1,
                        SearchFields.ACCESSION.getIndexName(),
                        "desc"
                );
        assertEquals(1, res.size());
        checkIsProjectPXD000433(res.iterator().next());

        // search for PXD000581
        res =  projectSearchDao.searchProjectAny(
                        PROJECT_PXD000581_ACCESSION,
                        SearchFields.ACCESSION.getIndexName(),
                        null,
                        0,
                        1,
                        SearchFields.ACCESSION.getIndexName(),
                        "desc"
                );
        assertEquals(1, res.size());
        checkIsProjectPXD000581(res.iterator().next());

    }

    @Test
    public void testSearchByProteinAccession() throws SolrServerException, IOException, URISyntaxException, ParseException, MZTabException {
        addProjectPXD000433ToIndex();

        ProjectSearchDao projectSearchDao = new ProjectSearchDaoSolr(server);
        Collection<? extends ProjectProvider> res =
                projectSearchDao.searchProjectAny(
                        SearchFields.PROTEIN_IDENTIFICATIONS.getIndexName() + ":" + PROTEIN_A5A5Z5_ACCESSION,
                        SearchFields.PROTEIN_IDENTIFICATIONS.getIndexName(),
                        null,
                        0,
                        1,
                        SearchFields.ACCESSION.getIndexName(),
                        "desc"
                );
        // check we found one project
        assertEquals(1, res.size());
        // check that is PXD000433
        checkIsProjectPXD000433(res.iterator().next());

    }

    @Test
     public void testSearchByOtherProteinAccession() throws SolrServerException, IOException, URISyntaxException, ParseException, MZTabException {
        addProjectPXD000433ToIndex();

        ProjectSearchDao projectSearchDao = new ProjectSearchDaoSolr(server);
        Collection<? extends ProjectProvider> res =
                projectSearchDao.searchProjectAny(
                        SearchFields.PROTEIN_IDENTIFICATIONS.getIndexName() + ":" + UNIPROT_ACCESSION,
                        SearchFields.PROTEIN_IDENTIFICATIONS.getIndexName(),
                        null,
                        0,
                        1,
                        SearchFields.ACCESSION.getIndexName(),
                        "desc"
                );
        // check we found one project
        assertEquals(1, res.size());
        // check that is PXD000433
        checkIsProjectPXD000433(res.iterator().next());

    }

    @Test
    public void testSearchByUniprotProteinAccession() throws SolrServerException, IOException, URISyntaxException, ParseException, MZTabException {
        addProjectPXD000433ToIndex();

        ProjectSearchDao projectSearchDao = new ProjectSearchDaoSolr(server);
        Collection<? extends ProjectProvider> res =
                projectSearchDao.searchProjectAny(
                        SearchFields.PROTEIN_IDENTIFICATIONS.getIndexName() + ":" + OTHER_ACCESSION,
                        SearchFields.PROTEIN_IDENTIFICATIONS.getIndexName(),
                        null,
                        0,
                        1,
                        SearchFields.ACCESSION.getIndexName(),
                        "desc"
                );
        // check we found one project
        assertEquals(1, res.size());
        // check that is PXD000433
        checkIsProjectPXD000433(res.iterator().next());

    }


    @Test
    public void testSearchByPeptide() throws SolrServerException, IOException, URISyntaxException, ParseException, MZTabException {
        addProjectPXD000581ToIndex();

        ProjectSearchDao projectSearchDao = new ProjectSearchDaoSolr(server);

        Collection<? extends ProjectProvider> all =
                projectSearchDao.searchProjectAny(
                        SearchFields.PEPTIDE_SEQUENCES.getIndexName() + ":" + "*",
                        SearchFields.PEPTIDE_SEQUENCES.getIndexName(),
                        null,
                        0,
                        1,
                        SearchFields.ACCESSION.getIndexName(),
                        "desc"
                );

        Collection<? extends ProjectProvider> res =
                projectSearchDao.searchProjectAny(
                        SearchFields.PEPTIDE_SEQUENCES.getIndexName() + ":" + FIRST_PEPTIDE_SEQUENCE_PXD000581,
                        SearchFields.PEPTIDE_SEQUENCES.getIndexName(),
                        null,
                        0,
                        1,
                        SearchFields.ACCESSION.getIndexName(),
                        "desc"
                );
        // check we found one project
        assertEquals(1, res.size());
        // check that is PXD000581
        checkIsProjectPXD000581(res.iterator().next());

        res = projectSearchDao.searchProjectAny(
                SearchFields.PEPTIDE_SEQUENCES.getIndexName() + ":" + SECOND_PEPTIDE_SEQUENCE_PXD000581,
                SearchFields.PEPTIDE_SEQUENCES.getIndexName(),
                null,
                0,
                1,
                SearchFields.ACCESSION.getIndexName(),
                "desc"
        );
        // check we found one project
        assertEquals(1, res.size());
        // check that is PXD000433
        checkIsProjectPXD000581(res.iterator().next());

        res = projectSearchDao.searchProjectAny(
                SearchFields.PEPTIDE_SEQUENCES.getIndexName() + ":" + THIRD_PEPTIDE_SEQUENCE_PXD000581,
                SearchFields.PEPTIDE_SEQUENCES.getIndexName(),
                null,
                0,
                1,
                SearchFields.ACCESSION.getIndexName(),
                "desc"
        );
        // check we found one project
        assertEquals(1, res.size());
        // check that is PXD000433
        checkIsProjectPXD000581(res.iterator().next());

        res = projectSearchDao.searchProjectAny(
                SearchFields.PEPTIDE_SEQUENCES.getIndexName() + ":" + FOUR_PEPTIDE_SEQUENCE_PXD000581,
                SearchFields.PEPTIDE_SEQUENCES.getIndexName(),
                null,
                0,
                1,
                SearchFields.ACCESSION.getIndexName(),
                "desc"
        );
        // check we found one project
        assertEquals(1, res.size());
        // check that is PXD000433
        checkIsProjectPXD000581(res.iterator().next());
    }

    private void checkIsProjectPXD000433(ProjectProvider searchResult) {

        SolrProject resSolrProject = (SolrProject) searchResult;

        assertEquals(PROJECT_PXD000433_ACCESSION, resSolrProject.getAccession());
        assertEquals(PROJECT_1_DOI, resSolrProject.getDoi());
        assertEquals(PROJECT_1_TITLE, resSolrProject.getTitle());
        assertNull(resSolrProject.getProteinIdentifications());
        assertNull(resSolrProject.getPeptideSequences());

        //We can not check the number of protein identifications or peptide sequences because these field are not store.

    }

    private void checkIsProjectPXD000581(ProjectProvider searchResult) {

        SolrProject resSolrProject = (SolrProject) searchResult;

        assertEquals(PROJECT_PXD000581_ACCESSION, resSolrProject.getAccession());
        assertEquals(PROJECT_1_DOI, resSolrProject.getDoi());
        assertEquals(PROJECT_1_TITLE, resSolrProject.getTitle());
        assertNull(resSolrProject.getProteinIdentifications());
        assertNull(resSolrProject.getPeptideSequences());

        //We can not check the number of protein identifications or peptide sequences because these field are not store.
    }

    private void addProjectPXTEST1ToIndex() {

        solrProject = new SolrProject();
        solrProject.setAccession(PROJECT_PXTEST1_ACCESSION);
        solrProject.setDoi(PROJECT_1_DOI);
        solrProject.setTitle(PROJECT_1_TITLE);
        solrProject.setProjectTagNames(Arrays.asList("Biological","Technical"));

        //add a new project to index
        ProjectIndexDao projectIndexDao = new ProjectIndexDaoSolr(server, null, null, null, null, null);
        projectIndexDao.addProject(solrProject, null, null, null );

    }

    private void addProjectPXD000433ToIndex() throws SolrServerException, IOException, URISyntaxException, ParseException, MZTabException {
        solrProject = new SolrProject();
        solrProject.setAccession(PROJECT_PXD000433_ACCESSION);
        solrProject.setDoi(PROJECT_1_DOI);
        solrProject.setTitle(PROJECT_1_TITLE);

        File submissionDirectory = new File(SolrProjectSearchTest.class.getClassLoader().getResource("submissions").toURI());
        File generatedDirectory = new File(
                MzTabDataProviderReader.buildGeneratedDirectoryFilePath(
                        submissionDirectory.getAbsolutePath(),
                        solrProject
                )
        );

        if(proteinReferencesPXD000433 == null){
          proteinReferencesPXD000433 = MzTabDataProviderReader.readProteinIdentificationsFromMzTabFilesDirectory(
                  PROJECT_PXD000433_ACCESSION,
                  generatedDirectory
          );
        }


        if(peptideSequencesPXD000433 == null){
            peptideSequencesPXD000433 = MzTabDataProviderReader.readPsmsFromMzTabFilesDirectory(
                PROJECT_PXD000433_ACCESSION,
                generatedDirectory);
        }


        //add a new project to index
        ProjectIndexDao projectIndexDao = new ProjectIndexDaoSolr(server, null, null, null, null, null);
        projectIndexDao.addProject(solrProject, null, proteinReferencesPXD000433, peptideSequencesPXD000433);

    }

    private void addProjectPXD000581ToIndex() throws SolrServerException, IOException, URISyntaxException, ParseException, MZTabException {
        solrProject = new SolrProject();
        solrProject.setAccession(PROJECT_PXD000581_ACCESSION);
        solrProject.setDoi(PROJECT_1_DOI);
        solrProject.setTitle(PROJECT_1_TITLE);

        DateFormat formatter = new SimpleDateFormat("MM/dd/yy");
        Date publicationDate = formatter.parse(PUBLICATION_DATE_AS_STRING);
        solrProject.setPublicationDate(publicationDate);

        File submissionDirectory = new File(SolrProjectSearchTest.class.getClassLoader().getResource("submissions").toURI());
        File generatedDirectory = new File(
                MzTabDataProviderReader.buildGeneratedDirectoryFilePath(
                        submissionDirectory.getAbsolutePath(),
                        solrProject
                )
        );

        if (proteinReferencesPXD000581 == null) {
            proteinReferencesPXD000581 = MzTabDataProviderReader.readProteinIdentificationsFromMzTabFilesDirectory(
                    PROJECT_PXD000581_ACCESSION,
                    generatedDirectory
            );
        }

        if (peptideSequencesPXD000581 == null) {
            peptideSequencesPXD000581 = MzTabDataProviderReader.readPsmsFromMzTabFilesDirectory(
                    PROJECT_PXD000581_ACCESSION,
                    generatedDirectory
            );
        }

        //add a new project to index
        ProjectIndexDao projectIndexDao = new ProjectIndexDaoSolr(server, null, null, null, null, null);
        projectIndexDao.addProject(solrProject, null, proteinReferencesPXD000581, peptideSequencesPXD000581);

    }
}
