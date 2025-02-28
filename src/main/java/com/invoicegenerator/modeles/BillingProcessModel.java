package com.invoicegenerator.modeles;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Représente le modèle du processus de facturation qui contient les paramètres,
 * une liste de modèles d'entités PV et les détails du nom de fichier.
 */
public class BillingProcessModel {
    private static final Logger logger = Logger.getLogger(BillingProcessModel.class.getName());

    private ParametersModel parameters = new ParametersModel();
    private final List<PvEntityPvModel> pvEntityPvModels = new ArrayList<>();
    private String outputFileName;
    private String complement;

    /**
     * Constructeur par défaut. Initialise les paramètres et la liste d'entités PV.
     */
    public BillingProcessModel() {
        logger.log(Level.INFO, "Création d'une nouvelle instance de BillingProcessModel");
    }

    /**
     * Récupère la liste des modèles d'entités PV.
     *
     * @return Une liste de PvEntityPvModel.
     */
    public List<PvEntityPvModel> getPvEntities() {
        logger.log(Level.FINE, "Récupération de la liste des entités PV, taille : {0}", pvEntityPvModels.size());
        return pvEntityPvModels;
    }

    /**
     * Récupère le modèle de paramètres.
     *
     * @return Le ParametersModel.
     */
    public ParametersModel getParameters() {
        logger.log(Level.FINE, "Récupération du modèle de paramètres");
        return parameters;
    }

    /**
     * Définit le modèle de paramètres.
     *
     * @param parameters Le ParametersModel à définir.
     */
    public void setParameters(ParametersModel parameters) {
        if (parameters == null) {
            logger.log(Level.WARNING, "Tentative de définition d'un modèle de paramètres null, ignorée");
            return;
        }
        this.parameters = parameters;
        logger.log(Level.FINE, "Modèle de paramètres défini avec succès");
    }

    /**
     * Récupère le nom du fichier de sortie.
     *
     * @return Le nom du fichier de sortie.
     */
    public String getOutputFileName() {
        logger.log(Level.FINE, "Récupération du nom du fichier de sortie : {0}", outputFileName);
        return outputFileName;
    }

    /**
     * Définit le nom du fichier de sortie.
     *
     * @param outputFileName Le nom du fichier de sortie à définir.
     */
    public void setOutputFileName(String outputFileName) {
        this.outputFileName = outputFileName;
        logger.log(Level.FINE, "Nom du fichier de sortie défini : {0}", outputFileName);
    }

    /**
     * Récupère le complément du nom.
     *
     * @return Le complément du nom.
     */
    public String getComplement() {
        logger.log(Level.FINE, "Récupération du complément du nom : {0}", complement);
        return complement;
    }

    /**
     * Définit le complément du nom.
     *
     * @param complement Le complément du nom à définir.
     */
    public void setComplement(String complement) {
        this.complement = complement;
        logger.log(Level.FINE, "Complément du nom défini : {0}", complement);
    }
}