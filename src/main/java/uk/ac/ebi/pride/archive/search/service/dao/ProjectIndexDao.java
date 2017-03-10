package uk.ac.ebi.pride.archive.search.service.dao;

import uk.ac.ebi.pride.archive.dataprovider.assay.AssayProvider;
import uk.ac.ebi.pride.archive.dataprovider.identification.PeptideSequenceProvider;
import uk.ac.ebi.pride.archive.dataprovider.identification.ProteinReferenceProvider;
import uk.ac.ebi.pride.archive.dataprovider.project.ProjectProvider;

import java.util.Collection;

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
                    Collection<? extends ProteinReferenceProvider> proteinReferences // a Map from assay accessions to protein references
    );


    void addProject(ProjectProvider project);
}
