package com.invoicegenerator.utils.ihm;

import com.invoicegenerator.utils.backend.LoggerFactory;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utilitaire pour gérer les dialogues de sélection de fichiers et dossiers dans JavaFX.
 */
public class FileChooserHelper {
    private static final Logger logger = LoggerFactory.getLogger(FileChooserHelper.class.getName());


    private  FileChooserHelper() { throw new IllegalStateException("Utility class");}

    /**
     * Affiche un dialogue pour sauvegarder un fichier JSON.
     *
     * @param stage      Stage parent du dialogue
     * @param initialDir Répertoire initial
     * @param title      Titre du dialogue
     * @return Fichier sélectionné ou null si annulé
     */
    public static File showSaveJsonDialog(Stage stage, String initialDir, String title) {
        logger.log(Level.FINE, "Configuration du FileChooser pour sauvegarde JSON : {0}", title);
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(initialDir));
        fileChooser.setTitle(title);
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Fichiers JSON (*.json)", "*.json")
        );
        File file = fileChooser.showSaveDialog(stage);
        logger.log(Level.FINE, "Résultat de la sauvegarde : {0}", file != null ? file.getAbsolutePath() : "Sauvegarde de JSON annulée");
        return file;
    }

    /**
     * Affiche un dialogue pour ouvrir un fichier JSON.
     *
     * @param stage      Stage parent du dialogue
     * @param initialDir Répertoire initial
     * @param title      Titre du dialogue
     * @return Fichier sélectionné ou null si annulé
     */
    public static File showOpenJsonDialog(Stage stage, String initialDir, String title) {
        logger.log(Level.FINE, "Configuration du FileChooser pour ouverture JSON : {0}", title);
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(initialDir));
        fileChooser.setTitle(title);
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Fichiers JSON (*.json)", "*.json")
        );
        File file = fileChooser.showOpenDialog(stage);
        logger.log(Level.FINE, "Résultat de l''ouverture : {0}", file != null ? file.getAbsolutePath() : "ouverture de JSON annulée");
        return file;
    }

    /**
     * Affiche un dialogue pour sélectionner plusieurs fichiers Excel.
     *
     * @param stage      Stage parent du dialogue
     * @param initialDir Répertoire initial
     * @param title      Titre du dialogue
     * @return Liste des fichiers sélectionnés ou null si annulé
     */
    public static List<File> showOpenExcelDialog(Stage stage, String initialDir, String title) {
        logger.log(Level.FINE, "Configuration du FileChooser pour sélection Excel : {0}", title);
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(initialDir));
        fileChooser.setTitle(title);
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Fichiers Excel (*.xls, *.xlsx)", "*.xls", "*.xlsx")
        );
        List<File> files = fileChooser.showOpenMultipleDialog(stage);
        logger.log(Level.FINE, "Résultat de la sélection : {0} fichier(s)", files != null ? files.size() : 0);
        return files;
    }

    /**
     * Affiche un dialogue pour sélectionner un dossier.
     *
     * @param stage      Stage parent du dialogue
     * @param initialDir Répertoire initial
     * @param title      Titre du dialogue
     * @return Dossier sélectionné ou null si annulé
     */
    public static File showDirectoryDialog(Stage stage, String initialDir, String title) {
        logger.log(Level.FINE, "Configuration du DirectoryChooser : {0}", title);
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File(initialDir));
        directoryChooser.setTitle(title);
        File folder = directoryChooser.showDialog(stage);
        logger.log(Level.FINE, "Résultat de la sélection : {0}", folder != null ? folder.getAbsolutePath() : "ouverture de dossier annulée");
        return folder;
    }
}