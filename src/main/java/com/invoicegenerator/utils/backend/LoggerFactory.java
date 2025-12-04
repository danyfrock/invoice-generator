package com.invoicegenerator.utils.backend;

import java.util.logging.Logger;
import java.util.logging.LogManager;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Factory class for creating and managing logger instances.
 * Provides a centralized way to obtain loggers for different classes.
 */
public class LoggerFactory {
    private static final String LOGGING_CONFIG_FILE = "/logging.properties";
    private static final String DEFAULT_LOG_DIR = Paths.get(System.getenv("APPDATA"), "InvoiceGenerator", "logs").toString();
    private static final String DEFAULT_LOG_PATTERN = "invoice_%g.log";
    private static final String ERROR_LOG_FILE = Paths.get(System.getProperty("user.home"), "invoice_generator_error_log.txt").toString();
    private static FileHandler sharedFileHandler; // Handler unique partagé


    private  LoggerFactory() { throw new IllegalStateException("Utility class");}

    static {
        try {
            writeAdHocLog("Start LoggerFactory", false);
            Path logDirPath = Paths.get(DEFAULT_LOG_DIR);
            if (!Files.exists(logDirPath)) {
                Files.createDirectories(logDirPath);
                writeAdHocLog("Dossier de logs créé : " + DEFAULT_LOG_DIR);
            }
        } catch (IOException e) {
            writeAdHocLog("Erreur lors de la création du dossier de logs : " + e.getMessage());
        }

        try {
            InputStream configFile = LoggerFactory.class.getResourceAsStream(LOGGING_CONFIG_FILE);
            if (configFile != null) {
                LogManager.getLogManager().readConfiguration(configFile);
                writeAdHocLog("Configuration de logging chargée depuis " + LOGGING_CONFIG_FILE);
            } else {
                writeAdHocLog("Fichier logging.properties non trouvé, configuration par défaut utilisée");
            }
        } catch (Exception e) {
            writeAdHocLog("Erreur lors du chargement de logging.properties : " + e.getMessage());
        }

        configureDefaultHandler();
    }

    /**
     * Récupère la valeur de configuration pour une propriété donnée, ou retourne une valeur par défaut.
     * @param property La propriété de configuration.
     * @param defaultValue La valeur par défaut si la propriété n'est pas définie.
     * @return La valeur de la propriété ou la valeur par défaut.
     */
    private static String recupConfig(String property, String defaultValue) {
        String value = LogManager.getLogManager().getProperty(property);
        return (value != null && !value.isEmpty()) ? value : defaultValue;
    }

    /**
     * Récupère la valeur de configuration pour une propriété donnée, ou retourne une valeur par défaut.
     * @param property La propriété de configuration.
     * @param defaultValue La valeur par défaut si la propriété n'est pas définie.
     * @return La valeur de la propriété ou la valeur par défaut.
     */
    private static int recupConfig(String property, int defaultValue) {
        String value = LogManager.getLogManager().getProperty(property);
        if (value != null && !value.isEmpty()) {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {

                writeAdHocLog(
                        "Valeur invalide pour " + property + " : " + value +
                                " (" + e.getMessage() + "), utilisation de la valeur par défaut " + defaultValue
                );
            }
        }
        return defaultValue;
    }

    /**
     * Configure et retourne un FileHandler partagé pour la journalisation.
     * @return Le FileHandler configuré.
     */
    private static FileHandler configureFileHandler() {
        if (sharedFileHandler == null) {
            try {
                String logPatternRaw = recupConfig("java.util.logging.FileHandler.pattern", DEFAULT_LOG_PATTERN);
                String logPattern = Paths.get(logPatternRaw).getFileName().toString();
                int limit = recupConfig("java.util.logging.FileHandler.limit", 50000);
                int count = recupConfig("java.util.logging.FileHandler.count", 5);
                String logFilePath = Paths.get(DEFAULT_LOG_DIR, logPattern).toString();

                sharedFileHandler = new FileHandler(logFilePath, limit, count, true);
                sharedFileHandler.setFormatter(new java.util.logging.SimpleFormatter());
                sharedFileHandler.setLevel(Level.INFO);
                writeAdHocLog("FileHandler configuré pour : " + logFilePath + " (limit=" + limit + ", count=" + count + ")");
            } catch (Exception e) {
                writeAdHocLog("Erreur lors de la configuration du FileHandler : " + e.getMessage());
            }
        }
        return sharedFileHandler;
    }

    /**
     * Obtient un logger pour le nom spécifié.
     * @param name Le nom du logger.
     * @return Le logger configuré.
     */
    public static Logger getLogger(String name) {
        Logger logger = Logger.getLogger(name);
        logger.setUseParentHandlers(false);

        for (Handler handler : logger.getHandlers()) {
            logger.removeHandler(handler);
        }

        FileHandler fileHandler = configureFileHandler();
        if (fileHandler != null) {
            logger.addHandler(fileHandler);
        }

        logger.setLevel(Level.INFO);
        return logger;
    }

    /**
     * Configure le handler par défaut pour le logger racine.
     */
    private static void configureDefaultHandler() {
        try {
            Logger rootLogger = LogManager.getLogManager().getLogger("");
            for (Handler handler : rootLogger.getHandlers()) {
                rootLogger.removeHandler(handler);
            }

            FileHandler fileHandler = configureFileHandler();
            if (fileHandler != null) {
                rootLogger.addHandler(fileHandler);
            }
            rootLogger.setLevel(Level.INFO);
        } catch (Exception e) {
            writeAdHocLog("Erreur lors de la configuration du handler par défaut : " + e.getMessage());
        }
    }

    /**
     * Écrit un log ad hoc dans un fichier de secours.
     * @param message Le message à écrire.
     * @param append Indique si le message doit être ajouté à la fin du fichier.
     */
    public static void writeAdHocLog(String message, boolean append) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ERROR_LOG_FILE, append))) {
            writer.write("log de secours survenue à " + java.time.LocalDateTime.now() + " : " + message);
            writer.newLine();
        } catch (IOException e) {
            writeAdHocLog("Erreur lors de l'écriture du log de secours : " + e.getMessage());
        }
    }

    /**
     * Écrit un log ad hoc dans un fichier de secours.
     * @param message Le message à écrire.
     */
    public static void writeAdHocLog(String message) {
        writeAdHocLog(message, true);
    }
}