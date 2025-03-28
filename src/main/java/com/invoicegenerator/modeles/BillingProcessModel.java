package com.invoicegenerator.modeles;

import com.invoicegenerator.utils.backend.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Représente le modèle du processus de facturation qui contient les paramètres,
 * une liste de modèles d'entités PV et les détails du nom de fichier.
 * Ce modèle est conçu pour être sérialisé/désérialisé en JSON avec Gson.
 */
public class BillingProcessModel {
    private static final Logger logger = LoggerFactory.getLogger(BillingProcessModel.class.getName());

    // Champs privés avec valeurs par défaut pour éviter les nulls lors de la sérialisation
    private ParametersModel parameters = new ParametersModel();
    private List<PvEntityPvModel> pvEntityPvModels = new ArrayList<>(); // Retiré 'final' pour permettre la désérialisation
    private String outputFileName = ""; // Valeur par défaut pour éviter null
    private String complement = ""; // Valeur par défaut pour éviter null

    /**
     * Constructeur par défaut. Initialise les paramètres et la liste d'entités PV.
     */
    public BillingProcessModel() {
        logger.log(Level.INFO, "Création d''une nouvelle instance de BillingProcessModel avec valeurs par défaut : pvEntityPvModels.size={0}, outputFileName={1}, complement={2}",
                new Object[]{0, outputFileName, complement});
    }

    /**
     * Récupère la liste des modèles d'entités PV.
     *
     * @return Une liste de PvEntityPvModel (jamais null grâce à l'initialisation par défaut).
     */
    public List<PvEntityPvModel> getPvEntities() {
        logger.log(Level.FINE, "Récupération de la liste des entités PV, taille : {0}", pvEntityPvModels.size());
        return pvEntityPvModels;
    }

    /**
     * Définit la liste des modèles d'entités PV.
     * Protège contre les valeurs null en initialisant une nouvelle liste vide si nécessaire.
     *
     * @param pvEntities La liste des modèles d'entités PV à définir.
     */
    public void setPvEntities(List<PvEntityPvModel> pvEntities) {
        this.pvEntityPvModels = (pvEntities != null) ? pvEntities : new ArrayList<>();
        logger.log(Level.FINE, "Liste des entités PV définie, taille : {0}", this.pvEntityPvModels.size());
    }

    /**
     * Récupère le modèle de paramètres.
     *
     * @return Le ParametersModel (jamais null grâce à l'initialisation par défaut).
     */
    public ParametersModel getParameters() {
        logger.log(Level.FINE, "Récupération du modèle de paramètres");
        return parameters;
    }

    /**
     * Définit le modèle de paramètres.
     * Protège contre les valeurs null en conservant l'instance par défaut si nécessaire.
     *
     * @param parameters Le ParametersModel à définir.
     */
    public void setParameters(ParametersModel parameters) {
        this.parameters = (parameters != null) ? parameters : new ParametersModel();
        logger.log(Level.FINE, "Modèle de paramètres défini avec succès");
    }

    /**
     * Récupère le nom du fichier de sortie.
     *
     * @return Le nom du fichier de sortie (jamais null grâce à l'initialisation par défaut).
     */
    public String getOutputFileName() {
        logger.log(Level.FINE, "Récupération du nom du fichier de sortie : {0}", outputFileName);
        return outputFileName;
    }

    /**
     * Définit le nom du fichier de sortie.
     * Protège contre les valeurs null en remplaçant par une chaîne vide si nécessaire.
     *
     * @param outputFileName Le nom du fichier de sortie à définir.
     */
    public void setOutputFileName(String outputFileName) {
        this.outputFileName = (outputFileName != null) ? outputFileName : "";
        logger.log(Level.FINE, "Nom du fichier de sortie défini : {0}", this.outputFileName);
    }

    /**
     * Récupère le complément du nom.
     *
     * @return Le complément du nom (jamais null grâce à l'initialisation par défaut).
     */
    public String getComplement() {
        logger.log(Level.FINE, "Récupération du complément du nom : {0}", complement);
        return complement;
    }

    /**
     * Définit le complément du nom.
     * Protège contre les valeurs null en remplaçant par une chaîne vide si nécessaire.
     *
     * @param complement Le complément du nom à définir.
     */
    public void setComplement(String complement) {
        this.complement = (complement != null) ? complement : "";
        logger.log(Level.FINE, "Complément du nom défini : {0}", this.complement);
    }
}