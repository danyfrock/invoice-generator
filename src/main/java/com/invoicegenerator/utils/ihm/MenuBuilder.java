package com.invoicegenerator.utils.ihm;

import com.invoicegenerator.utils.backend.LoggerFactory;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Builder fluide pour créer des menus JavaFX avec des actions prédéfinies.
 * Permet de construire des menus avec des items personnalisés de manière réutilisable.
 */
public class MenuBuilder {
    private static final Logger logger = LoggerFactory.getLogger(MenuBuilder.class.getName());
    private final Menu menu;
    private final Stage stage;

    /**
     * Construit un nouveau MenuBuilder pour un menu donné.
     *
     * @param menuName Nom du menu à créer
     * @param stage    Stage associé pour les dialogues (ex. FileChooser)
     */
    public MenuBuilder(String menuName, Stage stage) {
        this.menu = new Menu(menuName);
        this.stage = stage;
    }

    /**
     * Ajoute un item générique avec un texte, un raccourci clavier et une action.
     *
     * @param text    Texte de l'item (inclut généralement le raccourci en clair)
     * @param shortcut Raccourci clavier (ex. "Ctrl+S")
     * @param action   Action à exécuter lors du clic
     * @return MenuBuilder pour chaînage fluide
     */
    public MenuBuilder avecAction(String text, String shortcut, EventHandler<ActionEvent> action) {
        logger.log(Level.FINE, "Ajout de l'action {0} avec raccourci {1}", new Object[]{text, shortcut});
        MenuItem item = new MenuItem(text);
        item.setAccelerator(KeyCombination.keyCombination(shortcut));
        item.setOnAction(action);
        menu.getItems().add(item);
        return this;
    }

    /**
     * Ajoute un item pour sélectionner plusieurs fichiers Excel.
     *
     * @param initialDir   Répertoire initial pour le FileChooser
     * @param onFilesSelected Action à exécuter avec la liste des fichiers sélectionnés
     * @return MenuBuilder pour chaînage fluide
     */
    public MenuBuilder avecSelectionExcel(String initialDir, Consumer<List<File>> onFilesSelected) {
        return avecAction("Sélectionner fichier (Ctrl+O)", "Ctrl+O", e -> {
            logger.log(Level.INFO, "Ouverture du dialogue pour sélectionner des fichiers Excel");
            List<File> files = FileChooserHelper.showOpenExcelDialog(stage, initialDir, "Sélectionner des fichiers Excel");
            if (files != null && !files.isEmpty()) {
                logger.log(Level.INFO, "{0} fichier(s) Excel sélectionné(s)", files.size());
                onFilesSelected.accept(files);
            } else {
                logger.log(Level.FINE, "Aucun fichier Excel sélectionné");
            }
        });
    }

    /**
     * Ajoute un item pour charger un fichier JSON avec un titre personnalisé.
     *
     * @param text        Texte de l'item
     * @param shortcut    Raccourci clavier
     * @param initialDir  Répertoire initial
     * @param title       Titre du dialogue
     * @param onFileLoaded Action à exécuter avec le fichier chargé
     * @return MenuBuilder pour chaînage fluide
     */
    public MenuBuilder avecChargementJson(String text, String shortcut, String initialDir, String title, Consumer<File> onFileLoaded) {
        return avecAction(text, shortcut, e -> {
            logger.log(Level.INFO, "Ouverture du dialogue pour charger un JSON : {0}", title);
            File file = FileChooserHelper.showOpenJsonDialog(stage, initialDir, title);
            if (file != null) {
                logger.log(Level.INFO, "Fichier JSON chargé : {0}", file.getAbsolutePath());
                onFileLoaded.accept(file);
            } else {
                logger.log(Level.FINE, "Aucun fichier JSON sélectionné");
            }
        });
    }

    /**
     * Ajoute un item pour sauvegarder un fichier JSON.
     *
     * @param initialDir  Répertoire initial
     * @param title       Titre du dialogue
     * @param onFileSaved Action à exécuter avec le fichier sauvegardé
     * @return MenuBuilder pour chaînage fluide
     */
    public MenuBuilder avecSauvegardeJson(String initialDir, String title, Consumer<File> onFileSaved) {
        return avecAction("Sauvegarder progression (Ctrl+S)", "Ctrl+S", e -> {
            logger.log(Level.INFO, "Ouverture du dialogue pour sauvegarder un JSON : {0}", title);
            File file = FileChooserHelper.showSaveJsonDialog(stage, initialDir, title);
            if (file != null) {
                logger.log(Level.INFO, "Fichier JSON sauvegardé : {0}", file.getAbsolutePath());
                onFileSaved.accept(file);
            } else {
                logger.log(Level.FINE, "Sauvegarde annulée");
            }
        });
    }

    /**
     * Ajoute un item pour ouvrir un dossier.
     *
     * @param initialDir    Répertoire initial
     * @param onFolderOpened Action à exécuter avec le dossier sélectionné
     * @return MenuBuilder pour chaînage fluide
     */
    public MenuBuilder avecOuvrirDossier(String initialDir, Consumer<File> onFolderOpened) {
        return avecAction("Ouvrir Dossier (Ctrl+D)", "Ctrl+D", e -> {
            logger.log(Level.INFO, "Ouverture du dialogue pour sélectionner un dossier");
            File folder = FileChooserHelper.showDirectoryDialog(stage, initialDir, "Ouvrir un dossier");
            if (folder != null) {
                logger.log(Level.INFO, "Dossier ouvert : {0}", folder.getAbsolutePath());
                onFolderOpened.accept(folder);
            } else {
                logger.log(Level.FINE, "Aucun dossier sélectionné");
            }
        });
    }

    /**
     * Ajoute un item de navigation "Suivant".
     *
     * @param text    Texte de l'item
     * @param shortcut Raccourci clavier
     * @param onNext   Action à exécuter
     * @return MenuBuilder pour chaînage fluide
     */
    public MenuBuilder avecNavigationSuivant(String text, String shortcut, EventHandler<ActionEvent> onNext) {
        return avecAction(text, shortcut, e -> {
            logger.log(Level.FINE, "Navigation suivante demandée : {0}", text);
            onNext.handle(e);
        });
    }

    /**
     * Ajoute un item de navigation "Précédent".
     *
     * @param text      Texte de l'item
     * @param shortcut   Raccourci clavier
     * @param onPrevious Action à exécuter
     * @return MenuBuilder pour chaînage fluide
     */
    public MenuBuilder avecNavigationPrecedent(String text, String shortcut, EventHandler<ActionEvent> onPrevious) {
        return avecAction(text, shortcut, e -> {
            logger.log(Level.FINE, "Navigation précédente demandée : {0}", text);
            onPrevious.handle(e);
        });
    }

    /**
     * Ajoute un item d'aide avec une action personnalisée.
     *
     * @param onHelp Action à exécuter
     * @return MenuBuilder pour chaînage fluide
     */
    public MenuBuilder avecAide(EventHandler<ActionEvent> onHelp) {
        return avecAction("Aide (F11)", "F11", onHelp);
    }

    /**
     * Ajoute un item d'aide par défaut qui ouvre help.html.
     *
     * @return MenuBuilder pour chaînage fluide
     */
    public MenuBuilder avecAide() {
        return avecAction("Aide (F11)", "F11", e -> tryShowHelp());
    }

    /**
     * Retourne le menu construit.
     *
     * @return Menu JavaFX prêt à l'emploi
     */
    public Menu silVousPlait() {
        logger.log(Level.FINE, "Menu {0} construit avec {1} items", new Object[]{menu.getText(), menu.getItems().size()});
        return menu;
    }

    private void tryShowHelp() {
        try {
            logger.log(Level.INFO, "Tentative d'affichage de l'aide");
            showHelp();
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Échec de l'ouverture de help.html : {0}", ex.getMessage());
        }
    }

    private void showHelp() throws IOException {
        logger.log(Level.FINE, "Récupération de help.html depuis le classpath");
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("help.html");
        if (inputStream == null) {
            throw new IOException("Resource help.html not found in classpath");
        }

        File tempFile = File.createTempFile("help", ".html");
        tempFile.deleteOnExit();
        logger.log(Level.FINE, "Création d'un fichier temporaire : {0}", tempFile.getAbsolutePath());

        try (java.io.FileOutputStream outputStream = new java.io.FileOutputStream(tempFile)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }

        logger.log(Level.INFO, "Ouverture de l'aide dans le navigateur");
        java.awt.Desktop.getDesktop().browse(tempFile.toURI());
    }
}