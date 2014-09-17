package uk.ac.ebi.pride.archive.search.integration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ebi.pride.archive.search.util.ErrorLogOutputStream;
import uk.ac.ebi.pride.jmztab.model.MZTabFile;
import uk.ac.ebi.pride.jmztab.model.Protein;
import uk.ac.ebi.pride.jmztab.utils.MZTabFileParser;
import uk.ac.ebi.pride.jmztab.utils.errors.MZTabException;
import uk.ac.ebi.pride.archive.dataprovider.identification.ProteinReferenceProvider;
import uk.ac.ebi.pride.archive.dataprovider.project.ProjectProvider;
import uk.ac.ebi.pride.archive.search.model.SolrProteinIdentification;
import uk.ac.ebi.pride.psmindex.search.model.Psm;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author Jose A. Dianes
 * @version $Id$
 */
public class MzTabDataProviderReader {

    private static Logger logger = LoggerFactory.getLogger(MzTabDataProviderReader.class.getName());

    private static ErrorLogOutputStream errorLogOutputStream = new ErrorLogOutputStream(logger);

    private static final String INTERNAL_FOLDER_NAME = "internal";

    /**
     * mzTab files in the directory will have names such as PRIDE_Exp_Complete_Ac_28654.submissions. We are interested in the
     * assay accession, the last bit if we split by '_'.
     * @return A map of assay accessions to peptide identifications
     * @throws java.io.IOException
     */
    public static Map<String, Map<String,ProteinReferenceProvider>> readProteinIdentificationsFromMzTabFilesDirectory(File mzTabFilesDirectory) throws IOException, MZTabException {

        Map<String, Map<String, ProteinReferenceProvider>> res = new HashMap<String, Map<String, ProteinReferenceProvider>>();

        File[] mzTabFilesInDirectory = mzTabFilesDirectory.listFiles(new MzTabFileNameFilter());
        if (mzTabFilesInDirectory != null) {
            for (File tabFile: mzTabFilesInDirectory) {
                MZTabFileParser mzTabFileParser = new MZTabFileParser(tabFile, errorLogOutputStream);
                if (mzTabFileParser != null) {
                    // get all the peptide identifications from the file
                    MZTabFile mzTabFile = mzTabFileParser.getMZTabFile();
                    if (mzTabFile != null) {
                        // get assay accession
                        String assayAccession = tabFile.getName().split("[_\\.]")[4];
                        // get proteins
                        HashMap<String, ProteinReferenceProvider> assayProteinIdentifications = new HashMap<String, ProteinReferenceProvider>();
                        Collection<Protein> mzTabProteins = mzTabFile.getProteins();
                        for (Protein mzTabProtein: mzTabProteins) {
                            SolrProteinIdentification solrProteinIdentification = new SolrProteinIdentification();
                            solrProteinIdentification.setAccession(mzTabProtein.getAccession());
                            assayProteinIdentifications.put(solrProteinIdentification.getAccession(), solrProteinIdentification);
                        }
                        // add assay proteins to the result
                        res.put(assayAccession, assayProteinIdentifications);
                        logger.info("Found " + assayProteinIdentifications.size() + " protein identifications for Assay " + assayAccession + " in file " + tabFile.getAbsolutePath());
                    } else {
                        mzTabFileParser.getErrorList().print(errorLogOutputStream);
                    }
                }
            }
        }

        return res;
    }

    /**
     * mzTab files in the directory will have names such as PRIDE_Exp_Complete_Ac_28654.submissions. We are interested in the
     * assay accession, the last bit if we split by '_'. For testing purposes only.
     *
     * @return A map of assay accessions to PSMs
     * @throws java.io.IOException
     */
    public static LinkedList<Psm> readPsmsFromMzTabFilesDirectory(String projectAccession, File mzTabFilesDirectory) throws IOException, MZTabException {

        LinkedList<Psm> res = new LinkedList<Psm>();

        File[] mzTabFilesInDirectory = mzTabFilesDirectory.listFiles(new uk.ac.ebi.pride.psmindex.search.util.MzTabFileNameFilter());
        if (mzTabFilesInDirectory != null) {
            for (File tabFile : mzTabFilesInDirectory) {

                MZTabFileParser mzTabFileParser = new MZTabFileParser(tabFile, errorLogOutputStream);
                MZTabFile mzTabFile = mzTabFileParser.getMZTabFile();

                if (mzTabFile != null) {
                    // get assay accession
                    String assayAccession = tabFile.getName().split("[_\\.]")[4];

                    // get all psms from the file
                    LinkedList<Psm> assayPsms = uk.ac.ebi.pride.psmindex.search.util.MzTabDataProviderReader.readPsmsFromMzTabFile(projectAccession, assayAccession, mzTabFile);

                    // add assay psms to the result
                    res.addAll(assayPsms);
                    logger.debug("Found " + assayPsms.size() + " psms for Assay " + assayAccession + " in file " + tabFile.getAbsolutePath());
                } else {
                    mzTabFileParser.getErrorList().print(errorLogOutputStream);
                }

            }
        }

        return res;
    }


    /**
     * For testing purposes only. We should use the database to retrieve the names of the files to index
     *
     */
    @Deprecated //
    public static String buildGeneratedDirectoryFilePath(String prefix, ProjectProvider project) {
        if (project.isPublicProject()) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(project.getPublicationDate());
            int month = calendar.get(Calendar.MONTH) + 1;

            return prefix
                    + File.separator + calendar.get(Calendar.YEAR)
                    + File.separator + (month < 10 ? "0" : "") + month
                    + File.separator + project.getAccession()
                    + File.separator + INTERNAL_FOLDER_NAME;
        } else {
            return prefix
                    + File.separator + project.getAccession()
                    + File.separator + INTERNAL_FOLDER_NAME;
        }

    }
}
