package uk.ac.ebi.pride.archive.search.integration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ebi.pride.archive.dataprovider.identification.PeptideSequenceProvider;
import uk.ac.ebi.pride.archive.dataprovider.identification.ProteinReferenceProvider;
import uk.ac.ebi.pride.archive.dataprovider.project.ProjectProvider;
import uk.ac.ebi.pride.archive.search.util.ErrorLogOutputStream;
import uk.ac.ebi.pride.jmztab.model.MZTabFile;
import uk.ac.ebi.pride.jmztab.utils.MZTabFileParser;
import uk.ac.ebi.pride.jmztab.utils.errors.MZTabException;
import uk.ac.ebi.pride.proteincatalogindex.search.mappings.ProteinAccessionMappingsFinder;
import uk.ac.ebi.pride.proteinidentificationindex.search.model.ProteinIdentification;
import uk.ac.ebi.pride.proteinidentificationindex.search.util.ProteinIdentificationMzTabBuilder;
import uk.ac.ebi.pride.psmindex.search.util.PsmMzTabBuilder;

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
     * For testing purposes only. We should use the database to retrieve the names of the files to index
     *
     * mzTab files in the directory will have names such as PRIDE_Exp_Complete_Ac_28654.submissions. We are interested in the
     * assay accession, the last bit if we split by '_'.
     * @return A map of assay accessions to peptide identifications
     * @throws java.io.IOException
     */
    public static LinkedList<ProteinReferenceProvider> readProteinIdentificationsFromMzTabFilesDirectory(String projectAccession, File mzTabFilesDirectory) throws IOException, MZTabException {

        LinkedList<ProteinReferenceProvider> res = new LinkedList<ProteinReferenceProvider>();

        File[] mzTabFilesInDirectory = mzTabFilesDirectory.listFiles(new MzTabFileNameFilter());
        if (mzTabFilesInDirectory != null) {
            for (File tabFile: mzTabFilesInDirectory) {

                MZTabFileParser mzTabFileParser = new MZTabFileParser(tabFile, errorLogOutputStream);
                MZTabFile mzTabFile = mzTabFileParser.getMZTabFile();

                if (mzTabFile != null) {
                    // get assay accession
                    String assayAccession = tabFile.getName().split("[_\\.]")[4];
                    // get proteins
                    List<ProteinIdentification> proteinIdentifications = ProteinIdentificationMzTabBuilder.readProteinIdentificationsFromMzTabFile(projectAccession, assayAccession, mzTabFile);
                    enrichProteinIdentificationListWithOtherMappings(proteinIdentifications);

                    // add assay proteins to the result
                    res.addAll(proteinIdentifications);
                    logger.info("Found " + proteinIdentifications.size() + " protein identifications for Assay " + assayAccession + " in file " + tabFile.getAbsolutePath());
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
     * mzTab files in the directory will have names such as PRIDE_Exp_Complete_Ac_28654.submissions. We are interested in the
     * assay accession, the last bit if we split by '_'. For testing purposes only.
     *
     * @return A map of assay accessions to PSMs
     * @throws java.io.IOException
     */
    public static LinkedList<PeptideSequenceProvider> readPsmsFromMzTabFilesDirectory(String projectAccession, File mzTabFilesDirectory) throws IOException, MZTabException {

        LinkedList<PeptideSequenceProvider> res = new LinkedList<PeptideSequenceProvider>();

        File[] mzTabFilesInDirectory = mzTabFilesDirectory.listFiles(new MzTabFileNameFilter());
        if (mzTabFilesInDirectory != null) {
            for (File tabFile : mzTabFilesInDirectory) {

                MZTabFileParser mzTabFileParser = new MZTabFileParser(tabFile, errorLogOutputStream);
                MZTabFile mzTabFile = mzTabFileParser.getMZTabFile();

                if (mzTabFile != null) {
                    // get assay accession
                    String assayAccession = tabFile.getName().split("[_\\.]")[4];

                    // get all psms from the file
                    List<? extends PeptideSequenceProvider> psms = PsmMzTabBuilder.readPsmsFromMzTabFile(projectAccession, assayAccession, mzTabFile);

                    // add unique sequences to the result
                    res.addAll(psms);
                    logger.debug("Found " + psms.size() + " psms for Assay " + assayAccession + " in file " + tabFile.getAbsolutePath());
                } else {
                    mzTabFileParser.getErrorList().print(errorLogOutputStream);
                }
            }
        }

        return res;
    }


    /**
     * For testing purposes only.
     * In the general case, we don't read the identifications from mzTab, we read them
     * from the protein identification index that contains the other mappings.
     * @param proteinReferences list of protein references to be enriched
     */
    private static void enrichProteinIdentificationListWithOtherMappings(List<ProteinIdentification> proteinReferences) {

        logger.debug("Processing " + proteinReferences.size() + " proteins");

        Set<String> accessions = new TreeSet<String>();
        Map<String, String> uniprotMappings;
        Map<String, String> ensemblMappings;
        Map<String, TreeSet<String>> otherMappings;

        for (ProteinIdentification protein : proteinReferences) {
            accessions.add(protein.getAccession());
        }

        try {
            uniprotMappings = ProteinAccessionMappingsFinder.findProteinUniprotMappingsForAccession(accessions);
            ensemblMappings = ProteinAccessionMappingsFinder.findProteinEnsemblMappingsForAccession(accessions);
            otherMappings = ProteinAccessionMappingsFinder.findProteinOtherMappingsForAccession(accessions);

            for (ProteinIdentification proteinReference : proteinReferences) {
                String proteinAccession = proteinReference.getAccession();
                proteinReference.setUniprotMapping(uniprotMappings.get(proteinAccession));
                proteinReference.setEnsemblMapping(ensemblMappings.get(proteinAccession));
                proteinReference.setOtherMappings(otherMappings.get(proteinAccession));
            }

        } catch (IOException e) {
            logger.error("Cannot get mappings");
        }

    }

    /**
     * For testing purposes only. We should use the database to retrieve the names of the files to index
     *
     */
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
