package com.invoicegenerator.views;

import com.invoicegenerator.modeles.PvEntityPvModel;
import com.invoicegenerator.modeles.BillingProcessModel;
import com.invoicegenerator.services.ParametresService;
import com.invoicegenerator.utils.LoggerFactory;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Vue pour la sélection des fichiers dans l'application de gestion des navettes de facturation.
 * Permet à l'utilisateur de choisir des fichiers Excel, de les supprimer et de passer à l'étape suivante.
 */
public class FileSelectorView extends Application {
    private static final Logger logger = LoggerFactory.getLogger(FileSelectorView.class.getName());

    private TableView<PvEntityPvModel> fileTable = new TableView<>();
    private Button nextButton = new Button("Suivant");
    private BillingProcessModel source = new BillingProcessModel();

    /**
     * Constructeur par défaut. Initialise le modèle et charge les paramètres.
     */
    public FileSelectorView() {
        logger.log(Level.INFO, "Initialisation de FileSelectorView sans modèle source");
        this.chargerParametres();
        updateNextButtonState();
    }

    /**
     * Constructeur avec un modèle de processus de facturation.
     *
     * @param source Le modèle BillingProcessModel contenant les données existantes
     */
    public FileSelectorView(BillingProcessModel source) {
        logger.log(Level.INFO, "Initialisation de FileSelectorView avec modèle source");
        this.source = source;
        this.chargerParametres();

        for (PvEntityPvModel pv : source.getPvEntities()) {
            fileTable.getItems().add(pv);
        }
        updateNextButtonState();
    }

    /**
     * Méthode principale pour démarrer l'interface utilisateur JavaFX.
     *
     * @param primaryStage La fenêtre principale de l'application
     */
    @Override
    public void start(Stage primaryStage) {
        logger.log(Level.INFO, "Démarrage de l'interface FileSelectorView");
        primaryStage.setTitle("<<<POC>>> application d'enregistrement de navettes de facturation.");

        fileTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        TableColumn<PvEntityPvModel, String> fileNameColumn = new TableColumn<>("File Name");
        fileNameColumn.prefWidthProperty().bind(fileTable.widthProperty().multiply(0.5));
        fileNameColumn.setCellValueFactory(new PropertyValueFactory<>("fileName"));
        TableColumn<PvEntityPvModel, String> filePathColumn = new TableColumn<>("File Path");
        filePathColumn.prefWidthProperty().bind(fileTable.widthProperty().multiply(0.5));
        filePathColumn.setCellValueFactory(new PropertyValueFactory<>("filePath"));
        fileTable.getColumns().addAll(fileNameColumn, filePathColumn);

        fileTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        Button selectButton = new Button("Select Files");
        selectButton.setOnAction(e -> selectFiles());

        Button deleteButton = new Button("Delete Selection");
        deleteButton.setOnAction(e -> deleteSelection());

        nextButton.setOnAction(e -> {
            logger.log(Level.INFO, "Passage à CommandesView avec {0} éléments", fileTable.getItems().size());
            source.getPvEntities().clear();
            source.getPvEntities().addAll(fileTable.getItems());
            new CommandesView(source).start(new Stage());
            primaryStage.close();
        });

        Button paramsButton = new Button("Paramètres");
        paramsButton.setOnAction(e -> {
            logger.log(Level.INFO, "Passage à ParametresView avec {0} éléments", fileTable.getItems().size());
            source.getPvEntities().clear();
            source.getPvEntities().addAll(fileTable.getItems());
            new ParametresView(source).start(new Stage());
            primaryStage.close();
        });

        HBox buttonBox = new HBox(10, selectButton, deleteButton, nextButton);
        Label outputFolderPathLabel = new Label("Dossier de sortie: " + source.getParameters().getOutputFolder());
        HBox outputBox = new HBox(10, outputFolderPathLabel, paramsButton);

        BorderPane root = new BorderPane();
        root.setCenter(fileTable);
        root.setBottom(buttonBox);
        root.setTop(outputBox);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

        logger.log(Level.INFO, "Interface FileSelectorView affichée avec succès");
    }

    /**
     * Ouvre un sélecteur de fichiers pour ajouter des fichiers Excel à la table.
     */
    private void selectFiles() {
        logger.log(Level.FINE, "Ouverture du sélecteur de fichiers");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Files");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xls", "*.xlsx", "*.xlsm", "*.xlam"));
        List<File> files = fileChooser.showOpenMultipleDialog(null);
        if (files != null) {
            for (File file : files) {
                boolean exists = fileTable.getItems().stream()
                        .anyMatch(data -> data.getFilePath().equals(file.getAbsolutePath()));
                if (!exists) {
                    PvEntityPvModel entite = new PvEntityPvModel().setFileName(file.getName()).setFilePath(file.getAbsolutePath());
                    fileTable.getItems().add(entite);
                    logger.log(Level.FINE, "Fichier ajouté : {0}", file.getAbsolutePath());
                } else {
                    logger.log(Level.FINE, "Fichier déjà existant ignoré : {0}", file.getAbsolutePath());
                }
            }
            updateNextButtonState();
        } else {
            logger.log(Level.FINE, "Aucun fichier sélectionné");
        }
    }

    /**
     * Supprime les éléments sélectionnés de la table.
     */
    private void deleteSelection() {
        List<PvEntityPvModel> selectedItems = fileTable.getSelectionModel().getSelectedItems();
        if (!selectedItems.isEmpty()) {
            fileTable.getItems().removeAll(selectedItems);
            logger.log(Level.FINE, "Suppression de {0} éléments sélectionnés", selectedItems.size());
            updateNextButtonState();
        } else {
            logger.log(Level.FINE, "Aucune sélection à supprimer");
        }
    }

    /**
     * Met à jour l'état du bouton "Suivant" en fonction des éléments et des paramètres.
     */
    private void updateNextButtonState() {
        boolean isDisabled = fileTable.getItems().isEmpty() ||
                source.getParameters().getOutputFolder() == null ||
                source.getParameters().getOutputFolder().isEmpty();
        nextButton.setDisable(isDisabled);
        logger.log(Level.FINE, "État du bouton Suivant mis à jour : désactivé = {0}", isDisabled);
    }

    /**
     * Charge les paramètres à partir du service de paramètres.
     */
    private void chargerParametres() {
        logger.log(Level.FINE, "Chargement des paramètres");
        this.source.setParameters(new ParametresService(this.source.getParameters().getParametersFileName()).chargerParametres());
        logger.log(Level.FINE, "Paramètres chargés avec succès");
    }

    /**
     * Point d'entrée principal pour lancer l'application.
     *
     * @param args Arguments de la ligne de commande
     */
    public static void main(String[] args) {
        logger.log(Level.INFO, "Lancement de l'application FileSelectorView");
        launch(args);
    }
}