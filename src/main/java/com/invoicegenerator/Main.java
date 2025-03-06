package com.invoicegenerator;

import com.invoicegenerator.views.FileSelectorView;
import javafx.application.Application;
import javafx.stage.Stage;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.io.InputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.FileWriter;
import java.io.BufferedWriter;

/**
 * Classe principale de l'application Invoice Generator.
 * Cette classe initialise et lance l'application JavaFX.
 */
public class Main extends Application {
    private static final Logger logger = Logger.getLogger(Main.class.getName());
    private static final String LOGGING_CONFIG_FILE = "/logging.properties";
    private static final String CURRENT_DIR = System.getProperty("user.dir");
    private static final String LOG_DIR = "logs";
    private static final String ERROR_LOG_FILE = Paths.get(System.getProperty("user.home"), "invoice_generator_error_log.txt").toString();

    /**
     * Point d'entrée de l'application JavaFX.
     * @param primaryStage Le stage principal de l'application.
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            logger.info("Démarrage de l'application depuis : " + CURRENT_DIR);
            FileSelectorView fileSelectorView = new FileSelectorView();
            fileSelectorView.start(primaryStage);
            logger.info("FileSelectorView initialisé avec succès");
        } catch (Exception e) {
            logger.severe("Erreur au démarrage de FileSelectorView : " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Constructeur de la classe Main.
     */
    public Main() {
        logger.fine("Nouvelle instance de Main créée");
    }

    /**
     * Méthode principale de l'application.
     * @param args Arguments de la ligne de commande.
     */
    public static void main(String[] args) {
        writeAdHocLog("just start", false);

        if (args.length > 0) {
            logger.info("Arguments reçus : " + String.join(", ", args));
        } else {
            logger.info("Aucun argument fourni au démarrage");
        }

        writeAdHocLog("configureLogging start");
        configureLogging();
        writeAdHocLog("configureLogging done");
        logTheLogs();

        logger.info("Lancement de l'application JavaFX");
        launch(args);
    }

    private static void logTheLogs() {
        logger.info("Vérification de l'emplacement des logs");

        // Récupérer la propriété du pattern des logs
        String logPattern = System.getProperty("java.util.logging.FileHandler.pattern");

        if (logPattern != null && !logPattern.isEmpty()) {
            try {
                // Construire le chemin absolu à partir du pattern
                Path logPath = Paths.get(CURRENT_DIR, logPattern).normalize().toAbsolutePath();
                writeAdHocLog("Logs configurés pour être écrits dans : " + logPath);
                logger.info("Emplacement des logs : " + logPath);

                // Vérifier si le dossier parent existe
                Path parentDir = logPath.getParent();
                if (parentDir != null && Files.exists(parentDir)) {
                    writeAdHocLog("Dossier de logs vérifié comme existant : " + parentDir);
                } else {
                    writeAdHocLog("Attention : le dossier de logs n'existe pas encore : " + parentDir);
                }
            } catch (Exception e) {
                writeAdHocLog("Erreur lors de la vérification du chemin des logs : " + e.getMessage());
            }
        } else {
            // Cas par défaut quand le pattern n'est pas défini
            Path defaultLogPath = Paths.get(CURRENT_DIR, LOG_DIR).toAbsolutePath();
            writeAdHocLog("Pattern de log non défini. Utilisation du chemin par défaut : " + defaultLogPath);
            logger.info("Pattern de log non défini. Utilisation du chemin par défaut : " + defaultLogPath);

            try {
                if (Files.exists(defaultLogPath)) {
                    writeAdHocLog("Dossier de logs par défaut existe : " + defaultLogPath);
                } else {
                    writeAdHocLog("Dossier de logs par défaut n'existe pas encore : " + defaultLogPath);
                }
            } catch (Exception e) {
                writeAdHocLog("Erreur lors de la vérification du dossier par défaut : " + e.getMessage());
            }
        }
    }

    /**
     * Configure le système de logging pour l'application.
     */
    private static void configureLogging() {
        try {
            // Créer le dossier logs s'il n'existe pas
            Path logDirPath = Paths.get(CURRENT_DIR, LOG_DIR);
            if (!Files.exists(logDirPath)) {
                Files.createDirectories(logDirPath);
                logger.info("Dossier logs créé : " + logDirPath);
            }

            InputStream configFile = Main.class.getResourceAsStream(LOGGING_CONFIG_FILE);
            if (configFile == null) {
                writeAdHocLog("Fichier logging.properties non trouvé dans les resources");
                throw new IllegalStateException("Fichier logging.properties non trouvé dans les resources");
            }

            logger.info("Tentative de chargement du fichier de config : " + LOGGING_CONFIG_FILE);
            LogManager.getLogManager().readConfiguration(configFile);
            logger.info("Configuration de logging chargée avec succès");
        } catch (Exception e) {
            logger.severe("Erreur de configuration du logging : " + e.getMessage());
            logger.severe("Chemin actuel : " + CURRENT_DIR);
            logger.severe("Chemin attendu dans resources : " + LOGGING_CONFIG_FILE);
            e.printStackTrace();

            // Si l'erreur persiste, on écrit l'erreur dans un fichier texte simple
            writeAdHocLog("Erreur de configuration du logging : " + e.getMessage());
        }
    }

    /**
     * Écrit un message dans un fichier texte dans le répertoire de l'utilisateur.
     * @param errorMessage Le message d'erreur à écrire.
     */
    private static void writeAdHocLog(String errorMessage, boolean append) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ERROR_LOG_FILE, append))) {
            writer.write("Erreur survenue à " + java.time.LocalDateTime.now() + " : " + errorMessage);
            writer.newLine();
            logger.info("Erreur enregistrée dans le fichier : " + ERROR_LOG_FILE);
        } catch (IOException e) {
            logger.severe("Erreur lors de l'écriture du fichier d'erreur : " + e.getMessage());
        }
    }

    /**
     * Écrit un message dans un fichier texte dans le répertoire de l'utilisateur.
     * @param errorMessage Le message d'erreur à écrire.
     */
    private static void writeAdHocLog(String errorMessage) {
        writeAdHocLog(errorMessage, true);
    }
}
