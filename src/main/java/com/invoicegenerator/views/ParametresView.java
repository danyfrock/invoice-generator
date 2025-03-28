package com.invoicegenerator.views;

import com.invoicegenerator.modeles.BillingProcessModel;
import com.invoicegenerator.services.ParametresService;
import com.invoicegenerator.utils.backend.LoggerFactory;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Vue pour la gestion des paramètres de l'application.
 * Permet de configurer le dossier de sortie, les années min/max et les codes d'activité.
 */
public class ParametresView extends Application {
    private static final Logger logger = LoggerFactory.getLogger(ParametresView.class.getName());

    private final BillingProcessModel source;
    private final TextField textAnneeMin = new TextField();
    private final TextField textAnneeMax = new TextField();
    private final TextField textDossier = new TextField();
    private final CheckBox  pleinEcranCheckBox = new CheckBox();
    private final ListView<String> listCodes = new ListView<>();

    /**
     * Constructeur avec un modèle de processus de facturation.
     *
     * @param source Le modèle BillingProcessModel contenant les données existantes
     */
    public ParametresView(BillingProcessModel source) {
        logger.log(Level.INFO, "Initialisation de ParametresView");
        this.source = source;
        this.chargerParametres();
    }

    /**
     * Méthode principale pour démarrer l'interface utilisateur JavaFX.
     *
     * @param primaryStage La fenêtre principale de l'application
     */
    @Override
    public void start(Stage primaryStage) {
        logger.log(Level.INFO, "Démarrage de l''interface ParametresView");
        primaryStage.setTitle("Paramètres");

        // grille
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        // composants
        Label labelDossier = new Label("Emplacement du dossier de sortie");
        textDossier.setEditable(false);
        Button btnDossier = new Button("Sélectionner");
        btnDossier.setOnAction(e -> selectOutputFolder());

        Label labelCodes = new Label("Codes d''activité");
        TextField textCode = new TextField();
        textCode.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("[\\w]{0,5}")) {
                return change;
            }
            logger.log(Level.FINE, "Entrée invalide pour le code d''activité : {0}", newText);
            return null;
        }));

        Label labelPleinEcran = new Label("Maximiser les fenêtres");

        Button btnAjouter = new Button("Ajouter");
        Button btnSupprimer = new Button("Supprimer");

        Label labelAnneeMin = new Label("Année minimum");
        textAnneeMin.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("[\\d]{0,4}")) {
                return change;
            }
            logger.log(Level.FINE, "Entrée invalide pour l'année minimum : {0}", newText);
            return null;
        }));

        Label labelAnneeMax = new Label("Année maximale");
        textAnneeMax.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("[\\d]{0,4}")) {
                return change;
            }
            logger.log(Level.FINE, "Entrée invalide pour l'année maximale : {0}", newText);
            return null;
        }));

        Button btnEnregistrer = new Button("Enregistrer");
        btnEnregistrer.setOnAction(e -> {
            logger.log(Level.INFO, "Tentative d'enregistrement des paramètres");
            this.enregistrerParametres();
            new FileSelectorView(source).start(new Stage());
            primaryStage.close();
        });

        Button btnAnnuler = new Button("Annuler");
        btnAnnuler.setOnAction(e -> {
            logger.log(Level.INFO, "Annulation des modifications et retour à FileSelectorView");
            this.chargerParametres();
            new FileSelectorView(source).start(new Stage());
            primaryStage.close();
        });

        // placer
        grid.add(labelDossier, 0, 0);
        grid.add(textDossier, 1, 0);
        grid.add(btnDossier, 2, 0);

        grid.add(labelCodes, 0, 1);
        grid.add(listCodes, 1, 1, 2, 3);
        grid.add(textCode, 1, 4);
        grid.add(btnAjouter, 2, 4);
        grid.add(btnSupprimer, 2, 5);

        grid.add(labelAnneeMin, 0, 6);
        grid.add(textAnneeMin, 1, 6);

        grid.add(labelAnneeMax, 0, 7);
        grid.add(textAnneeMax, 1, 7);

        grid.add(labelPleinEcran, 0, 9);
        grid.add(pleinEcranCheckBox,1,9);

        grid.add(btnEnregistrer, 1, 10);
        grid.add(btnAnnuler, 2, 10);

        // listeners
        btnAjouter.setOnAction(e -> {
            String code = textCode.getText().trim();
            if (!code.isEmpty() && !listCodes.getItems().contains(code)) {
                listCodes.getItems().add(code);
                textCode.clear();
                logger.log(Level.FINE, "Code d'activité ajouté : {0}", code);
            } else {
                logger.log(Level.FINE, "Code d'activité non ajouté (vide ou déjà présent) : {0}", code);
            }
        });

        btnSupprimer.setOnAction(e -> {
            String selected = listCodes.getSelectionModel().getSelectedItem();
            if (selected != null) {
                listCodes.getItems().remove(selected);
                logger.log(Level.FINE, "Code d'activité supprimé : {0}", selected);
            } else {
                logger.log(Level.FINE, "Aucun code d'activité sélectionné pour suppression");
            }
        });

        Scene scene = new Scene(grid, 500, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
        logger.log(Level.INFO, "Interface ParametresView affichée avec succès");
    }

    /**
     * Enregistre les paramètres modifiés dans le modèle et persiste les changements.
     */
    private void enregistrerParametres() {
        try {
            logger.log(Level.FINE, "Enregistrement des paramètres");

            //mettre à jour modele
            this.source.getParameters().setMaxYear(Integer.parseInt(this.textAnneeMax.getText()));
            this.source.getParameters().setMinYear(Integer.parseInt(this.textAnneeMin.getText()));
            this.source.getParameters().setOutputFolder(this.textDossier.getText());
            this.source.getParameters().setActivityCodes(listCodes.getItems());
            this.source.getParameters().setPleinEcran(this.pleinEcranCheckBox.isSelected());

            // enregistrer
            new ParametresService(this.source.getParameters().getParametersFileName()).enregistrerParametres(this.source.getParameters());

            logger.log(Level.INFO, "Paramètres enregistrés avec succès");
        } catch (NumberFormatException e) {
            logger.log(Level.SEVERE, "Erreur lors de la conversion des années min/max en nombres : {0}", e.getMessage());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erreur lors de l'enregistrement des paramètres : {0}", e.getMessage());
        }
    }

    /**
     * Charge les paramètres existants dans l'interface utilisateur.
     */
    private void chargerParametres() {
        logger.log(Level.FINE, "Chargement des paramètres");
        try {
            this.textAnneeMax.setText(String.valueOf(this.source.getParameters().getMaxYear()));
            this.textAnneeMin.setText(String.valueOf(this.source.getParameters().getMinYear()));
            this.textDossier.setText(this.source.getParameters().getOutputFolder());
            this.pleinEcranCheckBox.setSelected(this.source.getParameters().getPleinEcran());
            listCodes.setItems(FXCollections.observableArrayList(this.source.getParameters().getActivityCodes()));
            logger.log(Level.INFO, "Paramètres chargés avec succès");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erreur lors du chargement des paramètres : {0}", e.getMessage());
        }
    }

    /**
     * Ouvre un sélecteur de dossier pour choisir le dossier de sortie.
     */
    private void selectOutputFolder() {
        logger.log(Level.FINE, "Ouverture du sélecteur de dossier de sortie");
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File(this.source.getParameters().getOutputFolder()));
        directoryChooser.setTitle("Sélectionner un dossier de sortie");
        File selectedFolder = directoryChooser.showDialog(null);
        if (selectedFolder != null) {
            this.textDossier.setText(selectedFolder.getAbsolutePath());
            logger.log(Level.FINE, "Dossier de sortie sélectionné : {0}", selectedFolder.getAbsolutePath());
        } else {
            logger.log(Level.FINE, "Aucun dossier de sortie sélectionné");
        }
    }

    /**
     * Point d'entrée principal pour lancer l'application.
     *
     * @param args Arguments de la ligne de commande
     */
    public static void main(String[] args) {
        logger.log(Level.INFO, "Lancement de l'application ParametresView");
        launch(args);
    }
}