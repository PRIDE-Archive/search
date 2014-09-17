package uk.ac.ebi.pride.archive.search.service.dao;

import uk.ac.ebi.pride.archive.dataprovider.assay.AssayProvider;
import uk.ac.ebi.pride.archive.dataprovider.identification.PeptideSequenceProvider;
import uk.ac.ebi.pride.archive.dataprovider.identification.ProteinReferenceProvider;
import uk.ac.ebi.pride.archive.dataprovider.project.ProjectProvider;

import java.util.Collection;
import java.util.Map;

/**
 * @author Jose A. Dianes
 * @version $Id$
 */
public interface ProjectIndexDao {
    //    void indexAllProjects();
    void indexAllPublicProjects();

    void indexAllNonExistingPublicProjects();

    void addProject(ProjectProvider project,
                    Collection<? extends AssayProvider> assays,
                    Map<String, ? extends Map<String, ? extends ProteinReferenceProvider>> proteinReferences // a Map from assay accessions to protein references
    );

    void addProject(ProjectProvider project,
                    Collection<? extends AssayProvider> assays,
                    Map<String, ? extends Map<String, ? extends ProteinReferenceProvider>> proteinReferences, // a Map from assay accessions to protein references
                    Collection<? extends PeptideSequenceProvider> peptideSequences
    );

    void addProject(ProjectProvider project,
                    Collection<? extends AssayProvider> assays
    );
}
