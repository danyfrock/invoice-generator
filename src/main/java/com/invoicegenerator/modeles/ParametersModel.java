package com.invoicegenerator.modeles;

import com.invoicegenerator.utils.backend.LoggerFactory;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Représente le modèle de paramètres qui contient diverses configurations pour le générateur de factures.
 */
public class ParametersModel {
    private static final Logger logger = LoggerFactory.getLogger(ParametersModel.class.getName());

    private String outputFolder = Paths.get(System.getProperty("user.home"), "Desktop").toString();
    private List<String> activityCodes = Arrays.asList("30001", "30003", "30005", "30007", "30009");
    private int minYear = 2024;
    private int maxYear = 2029;
    private String outputFileDefaultName = "WST-CO_.xlsm";
    private String parametersFileName = Paths.get(System.getProperty("user.home"), "") + "\\parametres.json";
    private String dernierEmplacementConnuEntrees = Paths.get(System.getProperty("user.home"), "") + "\\";
    private String dernierEmplacementConnuProgression = Paths.get(System.getProperty("user.home"), "") + "\\";

    /**
     * Constructeur par défaut pour ParametersModel.
     */
    public ParametersModel() {
        logger.log(Level.INFO, "Création d'une nouvelle instance de ParametersModel avec valeurs par défaut");
    }

    /**
     * Récupère l'emplacement du dossier de sortie.
     *
     * @return L'emplacement du dossier de sortie.
     */
    public String getOutputFolder() {
        logger.log(Level.FINE, "Récupération de l'emplacement du dossier de sortie : {0}", outputFolder);
        return outputFolder;
    }

    /**
     * Définit l'emplacement du dossier de sortie.
     *
     * @param outputFolder L'emplacement du dossier de sortie à définir.
     */
    public void setOutputFolder(String outputFolder) {
        this.outputFolder = outputFolder;
        logger.log(Level.FINE, "Emplacement du dossier de sortie défini : {0}", outputFolder);
    }

    /**
     * Récupère la liste des codes d'activité.
     *
     * @return Une liste des codes d'activité.
     */
    public List<String> getActivityCodes() {
        List<String> result = (activityCodes == null) ? Collections.emptyList() : new ArrayList<>(activityCodes);
        logger.log(Level.FINE, "Récupération de la liste des codes d'activité, taille : {0}", result.size());
        return result;
    }

    /**
     * Définit la liste des codes d'activité.
     *
     * @param codes La liste des codes d'activité à définir.
     */
    public void setActivityCodes(List<String> codes) {
        this.activityCodes = (codes == null) ? new ArrayList<>() : new ArrayList<>(codes);
        logger.log(Level.FINE, "Liste des codes d'activité définie, taille : {0}", this.activityCodes.size());
    }

    /**
     * Récupère l'année minimum autorisée.
     *
     * @return L'année minimum.
     */
    public int getMinYear() {
        logger.log(Level.FINE, "Récupération de l'année minimum : {0}", minYear);
        return minYear;
    }

    /**
     * Définit l'année minimum autorisée.
     *
     * @param minYear L'année minimum à définir.
     */
    public void setMinYear(int minYear) {
        this.minYear = minYear;
        logger.log(Level.FINE, "Année minimum définie : {0}", minYear);
    }

    /**
     * Récupère l'année maximale autorisée.
     *
     * @return L'année maximale.
     */
    public int getMaxYear() {
        logger.log(Level.FINE, "Récupération de l'année maximale : {0}", maxYear);
        return maxYear;
    }

    /**
     * Définit l'année maximale autorisée.
     *
     * @param maxYear L'année maximale à définir.
     */
    public void setMaxYear(int maxYear) {
        this.maxYear = maxYear;
        logger.log(Level.FINE, "Année maximale définie : {0}", maxYear);
    }

    /**
     * Récupère le nom par défaut du fichier de sortie.
     *
     * @return Le nom par défaut du fichier de sortie.
     */
    public String getOutputFileDefaultName() {
        logger.log(Level.FINE, "Récupération du nom par défaut du fichier de sortie : {0}", outputFileDefaultName);
        return outputFileDefaultName;
    }

    /**
     * Récupère le chemin du fichier de paramètres.
     *
     * @return Le chemin du fichier de paramètres.
     */
    public String getParametersFileName() {
        logger.log(Level.FINE, "Récupération du nom du fichier de paramètres : {0}", parametersFileName);
        return parametersFileName;
    }

    /**
     * Retourne une représentation sous forme de chaîne du ParametersModel.
     *
     * @return Une représentation sous forme de chaîne du ParametersModel.
     */
    @Override
    public String toString() {
        String result = "ParametresModele{" +
                "emplacementDossierSortie='" + outputFolder + '\'' +
                ", codesActivite=" + activityCodes +
                ", anneeMin=" + minYear +
                ", anneeMax=" + maxYear +
                '}';
        logger.log(Level.FINE, "Génération de la représentation textuelle : {0}", result);
        return result;
    }

    /**
     * Récupère le dernier emplacement connu pour les fichiers d'entrée Excel.
     *
     * @return Le chemin du dernier dossier connu pour les fichiers d'entrée.
     */
    public String getDernierEmplacementConnuEntrees() {
        return dernierEmplacementConnuEntrees;
    }

    /**
     * Définit le dernier emplacement connu pour les fichiers d'entrée Excel.
     *
     * @param dernierEmplacementConnuEntrees Le chemin du dernier dossier connu.
     */
    public void setDernierEmplacementConnuEntrees(String dernierEmplacementConnuEntrees) {
        this.dernierEmplacementConnuEntrees = dernierEmplacementConnuEntrees;
    }

    /**
     * Récupère le dernier emplacement connu pour les fichiers de sauvegarde.
     *
     * @return Le chemin du dernier dossier connu pour les fichiers de sauvegarde.
     */
    public String getDernierEmplacementConnuProgression() {
        return dernierEmplacementConnuProgression;
    }

    /**
     * Définit le dernier emplacement connu pour les fichiers de sauvegarde.
     *
     * @param dernierEmplacementConnuProgression Le chemin du dernier dossier connu.
     */
    public void setDernierEmplacementConnuProgression(String dernierEmplacementConnuProgression) {
        this.dernierEmplacementConnuProgression = dernierEmplacementConnuProgression;
    }
}
