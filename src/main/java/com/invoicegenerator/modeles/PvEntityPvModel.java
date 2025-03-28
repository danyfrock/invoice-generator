package com.invoicegenerator.modeles;

import com.invoicegenerator.utils.backend.LoggerFactory;

import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Représente le modèle d'entité PV qui contient les détails du fichier et un modèle de commande.
 * Ce modèle est conçu pour être sérialisé/désérialisé en JSON avec Gson.
 */
public class PvEntityPvModel {
    private static final Logger logger = LoggerFactory.getLogger(PvEntityPvModel.class.getName());

    // Champs privés avec valeurs par défaut pour éviter les nulls lors de la sérialisation
    private String fileName = "";
    private String filePath = "";
    private CommandModel command = new CommandModel();

    /**
     * Constructeur par défaut pour PvEntityPvModel.
     * Initialise le nom et le chemin du fichier à des chaînes vides et crée une nouvelle instance de CommandModel.
     */
    public PvEntityPvModel() {
        logger.log(Level.INFO, "Création d''une nouvelle instance de PvEntityPvModel avec valeurs par défaut : fileName={0}, filePath={1}",
                new Object[]{fileName, filePath});
    }

    /**
     * Récupère le nom du fichier.
     *
     * @return Le nom du fichier (jamais null grâce à l'initialisation par défaut).
     */
    public String getFileName() {
        logger.log(Level.FINE, "Récupération du nom du fichier : {0}", fileName);
        return fileName;
    }

    /**
     * Définit le nom du fichier.
     * Protège contre les valeurs null en remplaçant par une chaîne vide si nécessaire.
     *
     * @param fileName Le nom du fichier à définir.
     * @return L'instance actuelle de PvEntityPvModel pour chaînage (fluent API).
     */
    public PvEntityPvModel setFileName(String fileName) {
        this.fileName = (fileName != null) ? fileName : "";
        logger.log(Level.FINE, "Nom du fichier défini : {0}", this.fileName);
        return this;
    }

    /**
     * Récupère le chemin du fichier.
     *
     * @return Le chemin du fichier (jamais null grâce à l'initialisation par défaut).
     */
    public String getFilePath() {
        logger.log(Level.FINE, "Récupération du chemin du fichier : {0}", filePath);
        return filePath;
    }

    /**
     * Définit le chemin du fichier.
     * Protège contre les valeurs null en remplaçant par une chaîne vide si nécessaire.
     *
     * @param filePath Le chemin du fichier à définir.
     * @return L'instance actuelle de PvEntityPvModel pour chaînage (fluent API).
     */
    public PvEntityPvModel setFilePath(String filePath) {
        this.filePath = (filePath != null) ? filePath : "";
        logger.log(Level.FINE, "Chemin du fichier défini : {0}", this.filePath);
        return this;
    }

    /**
     * Récupère le modèle de commande associé.
     *
     * @return Le modèle de commande (jamais null grâce à l'initialisation par défaut).
     */
    public CommandModel getCommand() {
        logger.log(Level.FINE, "Récupération du modèle de commande");
        return command;
    }

    /**
     * Définit le modèle de commande associé.
     * Protège contre les valeurs null en conservant l'instance par défaut si nécessaire.
     *
     * @param command Le modèle de commande à définir.
     */
    public void setCommand(CommandModel command) {
        this.command = (command != null) ? command : new CommandModel();
        logger.log(Level.FINE, "Modèle de commande défini");
    }
}