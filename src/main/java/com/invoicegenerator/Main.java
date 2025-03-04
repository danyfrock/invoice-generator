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
        writeErrorToFile("just start");
        if (args.length > 0) {
            logger.info("Arguments reçus : " + String.join(", ", args));
        } else {
            logger.info("Aucun argument fourni au démarrage");
        }

        writeErrorToFile("configureLogging start");
        configureLogging();
        writeErrorToFile("configureLogging done");

        logger.info("Lancement de l'application JavaFX");
        launch(args);
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
                writeErrorToFile("Fichier logging.properties non trouvé dans les resources");
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
            writeErrorToFile("Erreur de configuration du logging : " + e.getMessage());
        }
    }

    /**
     * Écrit un message d'erreur dans un fichier texte dans le répertoire de l'utilisateur.
     * @param errorMessage Le message d'erreur à écrire.
     */
    private static void writeErrorToFile(String errorMessage) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ERROR_LOG_FILE, true))) {
            writer.write("Erreur survenue à " + java.time.LocalDateTime.now() + " : " + errorMessage);
            writer.newLine();
            logger.info("Erreur enregistrée dans le fichier : " + ERROR_LOG_FILE);
        } catch (IOException e) {
            logger.severe("Erreur lors de l'écriture du fichier d'erreur : " + e.getMessage());
        }
    }
}
