package com.invoicegenerator.modeles;

import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Représente le modèle d'entité PV qui contient les détails du fichier et un modèle de commande.
 */
public class PvEntityPvModel {
    private static final Logger logger = Logger.getLogger(PvEntityPvModel.class.getName());

    private String fileName = "";
    private String filePath = "";
    private final CommandModel command = new CommandModel();

    /**
     * Constructeur par défaut pour PvEntityPvModel.
     * Initialise le nom et le chemin du fichier à des chaînes vides et crée une nouvelle instance de CommandModel.
     */
    public PvEntityPvModel() {
        logger.log(Level.INFO, "Création d'une nouvelle instance de PvEntityPvModel avec valeurs par défaut");
    }

    /**
     * Récupère le nom du fichier.
     *
     * @return Le nom du fichier.
     */
    public String getFileName() {
        logger.log(Level.FINE, "Récupération du nom du fichier : {0}", fileName);
        return fileName;
    }

    /**
     * Définit le nom du fichier.
     *
     * @param fileName Le nom du fichier à définir.
     * @return L'instance actuelle de PvEntityPvModel.
     */
    public PvEntityPvModel setFileName(String fileName) {
        this.fileName = (fileName != null) ? fileName : "";
        logger.log(Level.FINE, "Nom du fichier défini : {0}", this.fileName);
        return this;
    }

    /**
     * Récupère le chemin du fichier.
     *
     * @return Le chemin du fichier.
     */
    public String getFilePath() {
        logger.log(Level.FINE, "Récupération du chemin du fichier : {0}", filePath);
        return filePath;
    }

    /**
     * Définit le chemin du fichier.
     *
     * @param filePath Le chemin du fichier à définir.
     * @return L'instance actuelle de PvEntityPvModel.
     */
    public PvEntityPvModel setFilePath(String filePath) {
        this.filePath = (filePath != null) ? filePath : "";
        logger.log(Level.FINE, "Chemin du fichier défini : {0}", this.filePath);
        return this;
    }

    /**
     * Récupère le modèle de commande.
     *
     * @return Le modèle de commande.
     */
    public CommandModel getCommand() {
        logger.log(Level.FINE, "Récupération du modèle de commande");
        return command;
    }
}