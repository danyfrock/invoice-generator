package com.invoicegenerator.views;

import com.invoicegenerator.modeles.PvEntityPvModel;
import com.invoicegenerator.modeles.BillingProcessModel;
import com.invoicegenerator.services.BillingProcessService;
import com.invoicegenerator.services.ParametresService;
import com.invoicegenerator.utils.backend.LoggerFactory;
import com.invoicegenerator.utils.ihm.FileChooserHelper;
import com.invoicegenerator.utils.ihm.MenuBuilder;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.function.Supplier;

/**
 * Vue pour la sélection des fichiers dans l'application de gestion des navettes de facturation.
 * Permet à l'utilisateur de choisir des fichiers Excel, de les supprimer et de passer à l'étape suivante.
 */
public class FileSelectorView extends Application {
    private static final Logger logger = LoggerFactory.getLogger(FileSelectorView.class.getName());

    private TableView<PvEntityPvModel> fileTable = new TableView<>();
    private Button nextButton = new Button("Suivant");
    private BillingProcessModel source = new BillingProcessModel();
    private BillingProcessService billingService = new BillingProcessService("billing_process.json"); // Instance par défaut
    private final ParametresService parametresService = new ParametresService(this.source.getParameters().getParametersFileName());
    private boolean canGoNext = false;

    public FileSelectorView() {
        logger.log(Level.INFO, "Initialisation de FileSelectorView sans modèle source");
        this.chargerParametres();
        updateNextButtonState();
    }

    public FileSelectorView(BillingProcessModel source) {
        logger.log(Level.INFO, "Initialisation de FileSelectorView avec modèle source");
        this.source = source;
        for (PvEntityPvModel pv : source.getPvEntities()) {
            fileTable.getItems().add(pv);
        }
        updateNextButtonState();
    }

    @Override
    public void start(Stage primaryStage) {
        logger.log(Level.INFO, "Démarrage de l'interface FileSelectorView");
        primaryStage.setTitle("Application d'enregistrement de navettes de facturation.");

        fileTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        this.source.setParameters(new ParametresService(this.source.getParameters().getParametersFileName()).chargerParametres());

        TableColumn<PvEntityPvModel, String> fileNameColumn = new TableColumn<>("File Name");
        fileNameColumn.prefWidthProperty().bind(fileTable.widthProperty().multiply(0.5));
        fileNameColumn.setCellValueFactory(new PropertyValueFactory<>("fileName"));
        TableColumn<PvEntityPvModel, String> filePathColumn = new TableColumn<>("File Path");
        filePathColumn.prefWidthProperty().bind(fileTable.widthProperty().multiply(0.5));
        filePathColumn.setCellValueFactory(new PropertyValueFactory<>("filePath"));
        fileTable.getColumns().addAll(fileNameColumn, filePathColumn);

        fileTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        Button selectButton = new Button("Sélectionner fichier (Ctrl+O)");
        selectButton.setOnAction(e -> selectFiles());

        Button deleteButton = new Button("Delete Selection");
        deleteButton.setOnAction(e -> deleteSelection());

        nextButton.setOnAction(e -> goNext(primaryStage));

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

        // Ajout du MenuBar avec MenuBuilder
        MenuBar menuBar = new MenuBar();
        Supplier<String> dernierEmplacementSupplier = () -> this.source.getParameters().getDernierEmplacementConnuProgression();
        Menu fileMenu = new MenuBuilder("Menu", primaryStage)
                .avecSelectionExcel(this::selectFiles)
                .avecChargementJson(
                        "Charger sauvegarde + paramètres (Ctrl+L)", "Ctrl+L",
                        dernierEmplacementSupplier,
                        "Charger sauvegarde",
                        file -> chargerUneProgression(primaryStage, file, true)
                )
                .avecChargementJson(
                        "Charger une sauvegarde sans paramètres (Ctrl+M)", "Ctrl+M",
                        dernierEmplacementSupplier,
                        "Charger sauvegarde sans paramètres",
                        file -> chargerUneProgression(primaryStage, file, false)
                )
                .avecSauvegardeJson(
                        dernierEmplacementSupplier,
                        "Sauvegarder progression",
                        this::sauvegarderProgression
                )
                .silVousPlait();

        Menu navigateMenu = new MenuBuilder("Naviguer", primaryStage)
                .avecNavigationSuivant("Suivant (Ctrl+Flèche Droite)", "Ctrl+Right", e -> goNext(primaryStage))
                .silVousPlait();

        Menu helpMenu = new MenuBuilder("Help", primaryStage)
                .avecAide() // Utilisation de la méthode par défaut pour help.html
                .silVousPlait();

        menuBar.getMenus().addAll(fileMenu, navigateMenu, helpMenu);

        // Placement des contrôles
        BorderPane root = new BorderPane();
        root.setTop(new VBox(menuBar, outputBox));
        root.setCenter(fileTable);
        root.setBottom(buttonBox);

        Scene scene = new Scene(root, 800, 600);
        scene.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.DELETE) {
                deleteSelection();
            }
        });
        primaryStage.setScene(scene);
        primaryStage.setMaximized(this.source.getParameters().getPleinEcran());
        primaryStage.show();

        logger.log(Level.INFO, "Interface FileSelectorView affichée avec succès");
    }

    private void goNext(Stage primaryStage) {
        if (this.canGoNext) {
            logger.log(Level.INFO, "Passage à CommandesView avec {0} éléments", fileTable.getItems().size());
            source.getPvEntities().clear();
            source.getPvEntities().addAll(fileTable.getItems());
            new CommandesView(source).start(new Stage());
            primaryStage.close();
        }
    }

    private void chargerUneProgression(Stage primaryStage, File file, boolean avecParametres) {
        logger.log(Level.INFO, "Chargement d'une sauvegarde depuis : {0}", file.getAbsolutePath());
        this.source.getParameters().setDernierEmplacementConnuProgression(file.getParentFile().getAbsolutePath());
        billingService = new BillingProcessService(file.getAbsolutePath());
        BillingProcessModel loadedModel = billingService.chargerBillingProcess();

        if (avecParametres) {
            this.parametresService.enregistrerParametres(loadedModel.getParameters());
        } else {
            loadedModel.setParameters(this.parametresService.chargerParametres());
            loadedModel.getParameters().setDernierEmplacementConnuProgression(file.getParentFile().getAbsolutePath());
            this.parametresService.enregistrerParametres(loadedModel.getParameters());
        }

        new FileSelectorView(loadedModel).start(new Stage());
        primaryStage.close();
    }

    private void sauvegarderProgression(File file) {
        logger.log(Level.INFO, "Sauvegarde de la progression dans : {0}", file.getAbsolutePath());
        this.source.getParameters().setDernierEmplacementConnuProgression(file.getParentFile().getAbsolutePath());
        this.parametresService.enregistrerParametres(this.source.getParameters());
        billingService = new BillingProcessService(file.getAbsolutePath());
        billingService.enregistrerBillingProcess(this.source);
    }

    private void selectFiles(List<File> files) {
        logger.log(Level.FINE, "Ajout de {0} fichiers sélectionnés", files.size());
        this.source.getParameters().setDernierEmplacementConnuEntrees(files.getFirst().getParentFile().getAbsolutePath());
        this.parametresService.enregistrerParametres(this.source.getParameters());
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
    }

    private void selectFiles() {
        List<File> files = FileChooserHelper.showOpenExcelDialog(null, this.source.getParameters().getDernierEmplacementConnuEntrees(), "Sélectionner fichier");
        if (files != null) {
            selectFiles(files);
        } else {
            logger.log(Level.FINE, "Aucun fichier sélectionné");
        }
    }

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

    private void updateNextButtonState() {
        boolean isDisabled = fileTable.getItems().isEmpty() ||
                source.getParameters().getOutputFolder() == null ||
                source.getParameters().getOutputFolder().isEmpty();
        nextButton.setDisable(isDisabled);
        this.canGoNext = !isDisabled;
        logger.log(Level.FINE, "État du bouton Suivant mis à jour : désactivé = {0}", isDisabled);
    }

    private void chargerParametres() {
        logger.log(Level.FINE, "Chargement des paramètres");
        this.source.setParameters(new ParametresService(this.source.getParameters().getParametersFileName()).chargerParametres());
        logger.log(Level.FINE, "Paramètres chargés avec succès");
    }

    public static void main(String[] args) {
        logger.log(Level.INFO, "Lancement de l'application FileSelectorView");
        launch(args);
    }
}