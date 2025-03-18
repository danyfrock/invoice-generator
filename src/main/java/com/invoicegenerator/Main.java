package com.invoicegenerator;

import com.invoicegenerator.utils.backend.LoggerFactory;
import com.invoicegenerator.views.FileSelectorView;
import javafx.application.Application;
import javafx.stage.Stage;
import java.util.logging.Logger;

/**
 * Classe principale de l'application Invoice Generator.
 * Cette classe initialise et lance l'application JavaFX.
 */
public class Main extends Application {
    private static final Logger logger = LoggerFactory.getLogger(Main.class.getName());
    private static final String CURRENT_DIR = System.getProperty("user.dir");

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
        if (args.length > 0) {
            logger.info("Arguments reçus : " + String.join(", ", args));
        } else {
            logger.info("Aucun argument fourni au démarrage");
        }

        logger.info("Lancement de l'application JavaFX");
        launch(args);
    }
}