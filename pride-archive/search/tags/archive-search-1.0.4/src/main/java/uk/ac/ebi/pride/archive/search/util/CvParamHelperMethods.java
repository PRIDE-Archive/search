package uk.ac.ebi.pride.archive.search.util;

import org.apache.commons.lang.StringUtils;
import uk.ac.ebi.pride.archive.dataprovider.assay.instrument.InstrumentComponentProvider;
import uk.ac.ebi.pride.archive.dataprovider.assay.instrument.InstrumentProvider;
import uk.ac.ebi.pride.archive.dataprovider.param.CvParamProvider;
import uk.ac.ebi.pride.archive.dataprovider.param.ParamProvider;
import uk.ac.ebi.pride.archive.ontology.model.OntologyTerm;
import uk.ac.ebi.pride.archive.ontology.search.service.OntologyTermSearchService;
import uk.ac.ebi.pride.archive.search.util.modelmapper.SolrProjectMapper;

import java.util.*;

/**
 * @author Jose A. Dianes
 * @version $Id$
 */
public class CvParamHelperMethods {

    public static List<String> listSpeciesNamesFromCvParam(Collection<? extends CvParamProvider> list){
        List<String> stringList = new ArrayList<String>();
        for (CvParamProvider cvParam : list) {
            if ("NEWT".equals(cvParam.getCvLabel())) {
                stringList.add(cvParam.getName());
            }
        }
        return stringList;
    }

    public static List<String> listSpeciesAccessionsFromCvParam(Collection<? extends CvParamProvider> list){
        List<String> stringList = new ArrayList<String>();
        for (CvParamProvider cvParam : list) {
            if ("NEWT".equals(cvParam.getCvLabel())) {
                stringList.add(cvParam.getAccession());
            }
        }
        return stringList;
    }

    public static List<String> listLabelsFromCvParam(Collection<? extends CvParamProvider> list){
        List<String> stringList = new ArrayList<String>();
        for (CvParamProvider cvParam : list) {
            stringList.add(cvParam.getCvLabel());
        }
        return stringList;
    }

    public static List<String> listAccessionFromCvParam(Collection<? extends CvParamProvider> list){
        List<String> stringList = new ArrayList<String>();
        for (CvParamProvider cvParam : list) {
            stringList.add(cvParam.getAccession());
        }
        return stringList;
    }

    public static List<String> listNameFromCvParam(Collection<? extends CvParamProvider> list){
        List<String> stringList = new ArrayList<String>();
        for (CvParamProvider cvParam : list) {
            stringList.add(cvParam.getName());
        }
        return stringList;
    }

    public static List<String> listProjectInstrumentModels(Collection<? extends CvParamProvider> instruments) {
        List<String> instrumentList = new ArrayList<String>();
        for (CvParamProvider instrument : instruments) {
            if (instrument.getValue() != null)
                instrumentList.add(instrument.getValue());
            else
                instrumentList.add(instrument.getName());
        }
        return instrumentList;    //To change body of overridden methods use File | Settings | File Templates.
    }

    public static List<String> listAssayInstrumentModels(Collection<? extends InstrumentProvider> instruments) {
        List<String> instrumentList = new ArrayList<String>();
        for (InstrumentProvider instrument : instruments) {
            if (instrument.getModel().getValue() != null)
                instrumentList.add(instrument.getModel().getValue());
            else
                instrumentList.add(instrument.getModel().getName());
        }
        return instrumentList;
    }

    public static List<String> listInstrumentSourceNames(Collection<? extends InstrumentProvider> instruments){
        Set<String> instrumentList = new HashSet<String>();
        List<String> res = new LinkedList<String>();
        List<String> instrumentNames = new ArrayList<String>();
        for (InstrumentProvider instrument : instruments) {
            //create a comma separated list with all names
            for (InstrumentComponentProvider instrumentComponent : instrument.getSources()) {
                instrumentNames = listNamesFromParams(instrumentComponent.getParams());
            }
            if (instrumentNames.size() != 0) instrumentList.add(StringUtils.join(instrumentNames, ','));
        }
        res.addAll(instrumentList);
        return res;
    }

    public static List<String> listInstrumentDetectorNames(Collection<? extends InstrumentProvider> instruments){
        Set<String> instrumentList = new HashSet<String>();
        List<String> res = new LinkedList<String>();
        List<String> instrumentNames = new ArrayList<String>();
        for (InstrumentProvider instrument : instruments) {
            //create a comma separated list with all names
            for (InstrumentComponentProvider instrumentComponent : instrument.getDetectors()) {
                instrumentNames = listNamesFromParams(instrumentComponent.getParams());
            }
            if (instrumentNames.size() != 0) instrumentList.add(StringUtils.join(instrumentNames,','));
        }

        res.addAll(instrumentList);
        return res;
    }

    public static List<String> listInstrumentAnalyzerNames(Collection<? extends InstrumentProvider> instruments){
        Set<String> instrumentList = new HashSet<String>();
        List<String> res = new LinkedList<String>();
        List<String> instrumentNames = new ArrayList<String>();
        for (InstrumentProvider instrument : instruments) {
            //create a comma separated list with all names
            for (InstrumentComponentProvider instrumentComponent : instrument.getAnalyzers()) {
                instrumentNames = listNamesFromParams(instrumentComponent.getParams());
            }
            if (instrumentNames.size() != 0) instrumentList.add(StringUtils.join(instrumentNames,','));
        }
        res.addAll(instrumentList);
        return res;
    }

    public static List<String> listNamesFromParams(Collection<? extends ParamProvider> params){
        List<String> paramNames = new ArrayList<String>();
        for (ParamProvider param : params) {
            paramNames.add(param.getName());
        }
        return paramNames;
    }

    public static List<String> listAssayInstrumentAccessions(Collection<? extends CvParamProvider> instruments) {
        List<String> instrumentList = new ArrayList<String>();
        for (CvParamProvider instrument : instruments) {
            if (instrument.getAccession() != null)
                instrumentList.add(instrument.getAccession());
            else
                instrumentList.add(instrument.getName());
        }
        return instrumentList;
    }

    public static List<String> listTissueNamesFromCvParam(Collection<? extends CvParamProvider> samples) {
        List<String> stringList = new ArrayList<String>();
        for (CvParamProvider cvParam : samples) {
            if ("BTO".equals(cvParam.getCvLabel())) {
                stringList.add(cvParam.getName());
            }
        }
        return stringList;
    }

    public static List<String> listTissueAccessionsFromCvParam(Collection<? extends CvParamProvider> samples) {
        List<String> stringList = new ArrayList<String>();
        for (CvParamProvider cvParam : samples) {
            if ("BTO".equals(cvParam.getCvLabel())) {
                stringList.add(cvParam.getAccession());
            }
        }
        return stringList;
    }

    public static List<String> listDiseaseNamesFromCvParam(Collection<? extends CvParamProvider> samples) {
        List<String> stringList = new ArrayList<String>();
        for (CvParamProvider cvParam : samples) {
            if ("DOID".equals(cvParam.getCvLabel())) {
                stringList.add(cvParam.getName());
            }
        }
        return stringList;
    }

    public static List<String> listDiseaseAccessionsFromCvParam(Collection<? extends CvParamProvider> samples) {
        List<String> stringList = new ArrayList<String>();
        for (CvParamProvider cvParam : samples) {
            if ("DOID".equals(cvParam.getCvLabel())) {
                stringList.add(cvParam.getAccession());
            }
        }
        return stringList;
    }

    public static List<String> listCellTypeNamesFromCvParam(Collection<? extends CvParamProvider> samples) {
        List<String> stringList = new ArrayList<String>();
        for (CvParamProvider cvParam : samples) {
            if ("CL".equals(cvParam.getCvLabel())) {
                stringList.add(cvParam.getName());
            }
        }
        return stringList;
    }

    public static List<String> listCellTypeAccessionsFromCvParam(Collection<? extends CvParamProvider> samples) {
        List<String> stringList = new ArrayList<String>();
        for (CvParamProvider cvParam : samples) {
            if ("CL".equals(cvParam.getCvLabel())) {
                stringList.add(cvParam.getAccession());
            }
        }
        return stringList;

    }

    public static List<String> listNameFromCvParamFiltered(Collection<? extends CvParamProvider> list) {
        List<String> stringList = new ArrayList<String>();
        for (CvParamProvider cvParam : list) {
            if (
                    (!"CL".equals(cvParam.getCvLabel()))
                 && (!"DOID".equals(cvParam.getCvLabel()))
                 && (!"NEWT".equals(cvParam.getCvLabel()))
                 && (!"BTO".equals(cvParam.getCvLabel()))
            ) {
                stringList.add(cvParam.getName());
            }
        }
        return stringList;
    }

    public static List<String> listAccessionFromCvParamFiltered(Collection<? extends CvParamProvider> list) {
        List<String> stringList = new ArrayList<String>();
        for (CvParamProvider cvParam : list) {
            if (
                   (!"CL".equals(cvParam.getCvLabel()))
                && (!"DOID".equals(cvParam.getCvLabel()))
                && (!"NEWT".equals(cvParam.getCvLabel()))
                && (!"BTO".equals(cvParam.getCvLabel()))
            ) {
                stringList.add(cvParam.getAccession());
            }
        }
        return stringList;
    }

    public static List<String> listLabelsFromCvParamFiltered(Collection<? extends CvParamProvider> list) {
        List<String> stringList = new ArrayList<String>();
        for (CvParamProvider cvParam : list) {
            if (
                    (!"CL".equals(cvParam.getCvLabel()))
                            && (!"DOID".equals(cvParam.getCvLabel()))
                            && (!"NEWT".equals(cvParam.getCvLabel()))
                            && (!"BTO".equals(cvParam.getCvLabel()))
                    ) {
                stringList.add(cvParam.getCvLabel());
            }
        }
        return stringList;
    }

    public static void populateRelatives(SolrProjectMapper solrProject, OntologyTermSearchService ontologyTermSearchService) {

        // tissues
        solrProject.setTissueAscendantsAccessions(new LinkedList<String>());
        solrProject.setTissueAscendantsNames(new LinkedList<String>());
        // for each tissue accession, get the related ontology terms (ascendants) from the ontology index, and keep the accession and name as relatives
        // these will be then used as search fields in order to find projects using related ontology terms
        for (int i=0; i<solrProject.getTissueAccessions().size(); i++) {
            String tissueAccession = solrProject.getTissueAccessions().get(i);
            String tissueName = solrProject.getTissueNames().get(i);
            List<OntologyTerm> relativeTissues = ontologyTermSearchService.findAllByDescendant(tissueAccession);
            for (OntologyTerm relativeOntologyTerm: relativeTissues) {
                if ( !solrProject.getTissueAscendantsAccessions().contains(relativeOntologyTerm.getAccession()) )
                    solrProject.getTissueAscendantsAccessions().add(relativeOntologyTerm.getAccession());
                if ( !solrProject.getTissueAscendantsNames().contains(relativeOntologyTerm.getName()) )
                    solrProject.getTissueAscendantsNames().add( relativeOntologyTerm.getName() );
            }
            // finally, we add the term itself as a relative
            solrProject.getTissueAscendantsAccessions().add(tissueAccession);
            solrProject.getTissueAscendantsNames().add(tissueName);

        }

        // diseases
        solrProject.setDiseaseAscendantsAccessions(new LinkedList<String>());
        solrProject.setDiseaseAscendantsNames(new LinkedList<String>());
        for (int i=0; i<solrProject.getDiseaseAccessions().size(); i++) {
            String diseaseAccession = solrProject.getDiseaseAccessions().get(i);
            String diseaseName = solrProject.getDiseaseNames().get(i);
            List<OntologyTerm> relativeDiseases = ontologyTermSearchService.findAllByDescendant(diseaseAccession);
            for (OntologyTerm relativeOntologyTerm: relativeDiseases) {
                if ( !solrProject.getDiseaseAscendantsAccessions().contains(relativeOntologyTerm.getAccession()) )
                    solrProject.getDiseaseAscendantsAccessions().add(relativeOntologyTerm.getAccession());
                if ( !solrProject.getDiseaseAscendantsNames().contains(relativeOntologyTerm.getName()) )
                    solrProject.getDiseaseAscendantsNames().add( relativeOntologyTerm.getName() );
            }
            // finally, we add the term itself as a relative
            solrProject.getDiseaseAscendantsAccessions().add(diseaseAccession);
            solrProject.getDiseaseAscendantsNames().add(diseaseName);

        }

        // species
        solrProject.setSpeciesAscendantsAccessions(new LinkedList<String>());
        solrProject.setSpeciesAscendantsNames(new LinkedList<String>());
        for (int i=0; i<solrProject.getSpeciesAccessions().size(); i++) {
            String speciesAccession = solrProject.getSpeciesAccessions().get(i);
            String speciesName = solrProject.getSpeciesNames().get(i);
            List<OntologyTerm> relativeSpecies = ontologyTermSearchService.findAllByDescendant(speciesAccession);
            for (OntologyTerm relativeOntologyTerm: relativeSpecies) {
                if ( !solrProject.getSpeciesAscendantsAccessions().contains(relativeOntologyTerm.getAccession()) )
                    solrProject.getSpeciesAscendantsAccessions().add(relativeOntologyTerm.getAccession());
                if ( !solrProject.getSpeciesAscendantsNames().contains(relativeOntologyTerm.getName()) )
                    solrProject.getSpeciesAscendantsNames().add( relativeOntologyTerm.getName() );
            }
            // finally, we add the term itself as a relative
            solrProject.getSpeciesAscendantsAccessions().add(speciesAccession);
            solrProject.getSpeciesAscendantsNames().add(speciesName);
        }

        // cell types
        solrProject.setCellTypeAscendantsAccessions(new LinkedList<String>());
        solrProject.setCellTypeAscendantsNames(new LinkedList<String>());
        for (int i=0; i<solrProject.getCellTypeAccessions().size(); i++) {
            String cellTypeAccession = solrProject.getCellTypeAccessions().get(i);
            String cellTypeName = solrProject.getCellTypeNames().get(i);
            List<OntologyTerm> relativeCellTypes = ontologyTermSearchService.findAllByDescendant(cellTypeAccession);
            for (OntologyTerm relativeOntologyTerm: relativeCellTypes) {
                if ( !solrProject.getCellTypeAscendantsAccessions().contains(relativeOntologyTerm.getAccession()) )
                    solrProject.getCellTypeAscendantsAccessions().add(relativeOntologyTerm.getAccession());
                if ( !solrProject.getCellTypeAscendantsNames().contains(relativeOntologyTerm.getName()) )
                    solrProject.getCellTypeAscendantsNames().add( relativeOntologyTerm.getName() );
            }
            // finally, we add the term itself as a relative
            solrProject.getCellTypeAscendantsAccessions().add(cellTypeAccession);
            solrProject.getCellTypeAscendantsNames().add(cellTypeName);
        }
    }
}
