package uk.ac.ebi.pride.archive.search.util;

/**
 * @author Jose A. Dianes
 * @version $Id$
 */
public enum SearchFields {
    ACCESSION("id"),
    TITLE("project_title"),
    PROJECT_DESCRIPTION("project_description"),
    PUBMED_IDS("pubmed_ids"),
    PTM_NAMES_AS_TEXT("ptm_as_text"),
    PTM_ACCESSIONS("ptm_accessions"),
    PTM_FACET_NAMES("ptm_facet_names"),
    INSTRUMENT_MODELS_AS_TEXT("instrument_models_as_text"),
    INSTRUMENT_FACETS_NAMES("instruments_facet_names"),
    QUANTIFICATION_METHODS_AS_TEXT("quantification_methods_as_text"),
    QUANTIFICATION_METHODS_ACCESSIONS("quantification_methods_accessions"),
    EXPERIMENT_TYPES_AS_TEXT("experiment_types_as_text"),
    EXPERIMENT_TYPES_ACCESSIONS("experiment_types_accession"),
    SPECIES_ACCESSIONS("species_accessions"),
    SPECIES_NAMES("species_names"),
    SPECIES_NAMES_AS_TEXT("species_as_text"),
    SPECIES_ASCENDANTS_AS_TEXT("species_descendants_as_text"),
    SPECIES_ASCENDANTS_NAMES("species_descendants_names"),
    TISSUE_ACCESSIONS("tissue_accessions"),
    TISSUE_NAMES("tissue_names"),
    TISSUE_AS_TEXT("tissue_as_text"),
    TISSUE_ASCENDANTS_AS_TEXT("tissue_descendants_as_text"),
    TISSUE_ASCENDANTS_NAMES("tissue_descendants_names"),
    DISEASE_ACCESSIONS("disease_accessions"),
    DISEASE_NAMES("disease_names"),
    DISEASE_AS_TEXT("disease_as_text"),
    DISEASE_ASCENDANTS_NAMES("disease_descendants_names"),
    DISEASE_ASCENDANTS_AS_TEXT("disease_descendants_as_text"),
    CELL_TYPE_ACCESSIONS("cell_type_accessions"),
    CELL_TYPE_AS_TEXT("cell_type_as_text"),
    CELL_TYPE_ASCENDANTS_AS_TEXT("cell_type_descendants_as_text"),
    CELL_TYPE_ASCENDANTS_NAMES("cell_type_descendants_names"),
    SAMPLE_AS_TEXT("sample_as_text"),
    SAMPLE_ACCESSIONS("sample_accessions"),
    ASSAYS_ACCESSIONS("assays_accession"),
    PROJECT_TAGS("project_tags"),
    PROJECT_TAGS_AS_TEXT("project_tags_as_text"),
    PROTEIN_IDENTIFICATIONS("protein_identifications"),
    PEPTIDE_SEQUENCES("peptide_sequences"),
    SUBMISSION_TYPE("submission_type");

    private String indexName;

    private SearchFields(String indexName) {
        this.indexName = indexName;
    }

    public String getIndexName() {
        return indexName;
    }

}
