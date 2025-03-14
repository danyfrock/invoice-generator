package com.invoicegenerator.views;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

import com.invoicegenerator.modeles.BillingProcessModel;
import com.invoicegenerator.services.BillingProcessService;
import com.invoicegenerator.services.EntitePvService;
import com.invoicegenerator.modeles.PvEntityPvModel;
import com.invoicegenerator.services.ParametresService;
import com.invoicegenerator.utils.FileUtil;
import com.invoicegenerator.utils.LoggerFactory;
import com.invoicegenerator.viewModels.CommandeViewModel;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * Vue principale pour l'affichage et la gestion des commandes dans l'application.
 * Cette classe étend Application pour créer une interface utilisateur JavaFX.
 */
public class CommandesView extends Application {
    private static final Logger logger = LoggerFactory.getLogger(CommandesView.class.getName());

    private TableView<CommandeViewModel> fileTable;
    private String dossierSortie;
    private List<String> filePaths = new ArrayList<>();
    private Button previewButton;
    private final Label fichierSortieLabel = new Label();
    private final TextField complementField = new TextField();
    private BillingProcessModel source = new BillingProcessModel();;
    private final EntitePvService pvService = new EntitePvService();
    private BillingProcessService billingService = new BillingProcessService("billing_process.json");
    private final ParametresService parametresService = new ParametresService(this.source.getParameters().getParametersFileName());
    /**
     * Constructeur avec une liste de chemins de fichiers et un dossier de sortie.
     *
     * @param filePaths Liste des chemins des fichiers à traiter
     * @param dossierSortie Chemin du dossier de sortie
     */
    public CommandesView(List<String> filePaths, String dossierSortie) {
        logger.log(Level.INFO, "Initialisation de CommandesView avec {0} chemins de fichiers et dossier de sortie : {1}", new Object[]{filePaths.size(), dossierSortie});
        this.filePaths = filePaths;
        this.dossierSortie = dossierSortie;

        this.source = new BillingProcessModel();
        for (String path : filePaths) {
            PvEntityPvModel pv = convertToPv(path);
            CommandeViewModel commandeViewModel = new CommandeViewModel();
            commandeViewModel.setSource(pv);
            commandeViewModel.fillWidthProperty().setValue(true);
            source.getPvEntities().add(pv);
        }
    }

    /**
     * Constructeur avec un modèle de processus de facturation.
     *
     * @param source Le modèle BillingProcessModel contenant les données
     */
    public CommandesView(BillingProcessModel source) {
        logger.log(Level.INFO, "Initialisation de CommandesView avec un modèle BillingProcessModel");
        this.source = source;

        for (PvEntityPvModel pv : source.getPvEntities()) {
            this.pvService.FillPvFrom(pv.getFilePath(), pv);
        }

        this.dossierSortie = this.source.getParameters().getOutputFolder();
        this.filePaths.clear();
        this.filePaths.addAll(
                source.getPvEntities().stream()
                        .map(PvEntityPvModel::getFilePath)
                        .toList()
        );
        this.complementField.setText(this.source.getComplement());
        resetFichierSortie();
    }

    /**
     * Méthode principale pour démarrer l'interface utilisateur JavaFX.
     *
     * @param primaryStage La fenêtre principale de l'application
     */
    @Override
    public void start(Stage primaryStage) {
        logger.log(Level.INFO, "Démarrage de l'interface CommandesView");
        primaryStage.setTitle("Commandes UI");

        fileTable = new TableView<>();

        TableColumn<CommandeViewModel, String> fileNameColumn = new TableColumn<>("File Name");
        fileNameColumn.setCellValueFactory(cellData -> cellData.getValue().nomFichierProperty());

        TableColumn<CommandeViewModel, String> filePathColumn = new TableColumn<>("File Path");
        filePathColumn.setCellValueFactory(cellData -> cellData.getValue().pathFichierProperty());

        TableColumn<CommandeViewModel, Boolean> verifiedColumn = getCommandeViewModelBooleanTableColumn();

        TableColumn<CommandeViewModel, String> mustFillColumn = new TableColumn<>("À remplir");
        mustFillColumn.setCellValueFactory(cellData -> cellData.getValue().doitRemplirProperty());
        mustFillColumn.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null || item.isEmpty() ? "" : item);
            }
        });

        fileTable.getColumns().addAll(fileNameColumn, filePathColumn, verifiedColumn, mustFillColumn);

        for (PvEntityPvModel pv : source.getPvEntities()) {
            CommandeViewModel commandeViewModel = new CommandeViewModel();
            commandeViewModel.setSource(pv);
            commandeViewModel.fillWidthProperty().setValue(true);
            fileTable.getItems().add(commandeViewModel);
        }

        Button backButton = new Button("Retour au choix de fichiers");
        backButton.setOnAction(e -> {
            logger.log(Level.INFO, "Retour à FileSelectorView");
            new FileSelectorView(source).start(new Stage());
            primaryStage.close();
        });

        previewButton = new Button("Preview");
        previewButton.setDisable(true);
        previewButton.setOnAction(e -> {
            logger.log(Level.INFO, "Ouverture de NavettesFacturationView");
            new NavettesFacturationView(this.source).start(new Stage());
            primaryStage.close();
        });

        // Ajout du filtre de saisie pour complementField
        complementField.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\w*")) {
                return change;
            }
            return null;
        }));

        if (!fileTable.getItems().isEmpty()) {
            fileTable.getItems().forEach(item ->
                    item.estVerifieProperty().addListener((obs, wasVerified, isNowVerified) -> {
                        logger.log(Level.FINE, "Changement de vérification : {0} -> {1}", new Object[]{wasVerified, isNowVerified});
                        updatePreviewButtonState();
                    }));

            CommandeViewModel firstItem = fileTable.getItems().getFirst();
            firstItem.codeContratProperty().addListener((obs, oldVal, newVal) -> {
                logger.log(Level.FINE, "Changement de code contrat : {0} -> {1}", new Object[]{oldVal, newVal});
                updateCodeContract(oldVal, newVal);
            });

            this.complementField.textProperty().addListener((observable, oldValue, newValue) -> {
                logger.log(Level.FINE, "Changement de complément : {0} -> {1}", new Object[]{oldValue, newValue});
                this.source.setComplement(newValue);
                resetFichierSortie();
            });
        }

        Button previousButton = new Button("<");
        previousButton.setOnAction(e -> selectPreviousRow());
        Button nextButton = new Button(">");
        nextButton.setOnAction(e -> selectNextRow());
        HBox navigationBox = new HBox(10, previousButton, nextButton);

        HBox buttonBox = new HBox(10, backButton, previewButton);
        Label complementLibelle = new Label("Complément");
        HBox complementBox = new HBox(10, complementLibelle, this.complementField);
        VBox sortieBox = new VBox(10, this.fichierSortieLabel, complementBox, navigationBox);

        VBox commandePanel = new VBox();
        commandePanel.setMaxWidth(Double.MAX_VALUE);
        VBox.setVgrow(commandePanel, Priority.ALWAYS);
        commandePanel.setPrefWidth(Double.MAX_VALUE);
        commandePanel.setMaxWidth(Double.MAX_VALUE);

        fileTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            logger.log(Level.FINE, "Sélection changée dans fileTable : {0} -> {1}", new Object[]{oldValue != null ? oldValue.nomFichierProperty().get() : "null", newValue != null ? newValue.nomFichierProperty().get() : "null"});
            commandePanel.getChildren().clear();
            if (newValue != null) {
                commandePanel.getChildren().add(newValue);
                newValue.autosize();
                newValue.setMaxWidth(Double.MAX_VALUE);
                VBox.setVgrow(newValue, Priority.ALWAYS);
                newValue.setPrefWidth(Double.MAX_VALUE);
                newValue.setMaxWidth(Double.MAX_VALUE);
                newValue.setPrefWidth(Double.MAX_VALUE);
                VBox.setVgrow(newValue, Priority.ALWAYS);
            } else {
                commandePanel.getChildren().add(null);
            }
        });

        VBox mainContent = new VBox(fileTable, commandePanel);
        mainContent.setMaxWidth(Double.MAX_VALUE);
        VBox.setVgrow(mainContent, Priority.ALWAYS);
        mainContent.setPrefWidth(Double.MAX_VALUE);
        mainContent.setMaxWidth(Double.MAX_VALUE);

        // Ajout du MenuBar
        MenuBar menuBar = new MenuBar();
        Menu menu = new Menu("Menu");

        MenuItem saveProgressItem = new MenuItem("Sauvegarder progression (Ctrl+S)");
        saveProgressItem.setAccelerator(KeyCombination.keyCombination("Ctrl+S"));
        saveProgressItem.setOnAction(e -> {
            logger.log(Level.INFO, "Sauvegarde de la progression demandée");
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialDirectory(new File(this.source.getParameters().getDernierEmplacementConnuProgression()));
            fileChooser.setTitle("Sauvegarder progression");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers JSON (*.json)", "*.json"));
            File file = fileChooser.showSaveDialog(primaryStage);
            if (file != null) {
                this.source.getParameters().setDernierEmplacementConnuProgression(file.getParentFile().getAbsolutePath());
                this.parametresService.enregistrerParametres(this.source.getParameters());
                billingService = new BillingProcessService(file.getAbsolutePath());
                billingService.enregistrerBillingProcess(this.source);
            }
        });

        menu.getItems().add(saveProgressItem);
        menuBar.getMenus().add(menu);

        BorderPane root = new BorderPane();
        root.setTop(new VBox(menuBar, sortieBox));
        root.setCenter(mainContent);
        root.setBottom(buttonBox);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

        selectFirstRow();
        updatePreviewButtonState();
        logger.log(Level.INFO, "Interface CommandesView affichée avec succès");
    }

    private static TableColumn<CommandeViewModel, Boolean> getCommandeViewModelBooleanTableColumn() {
        TableColumn<CommandeViewModel, Boolean> verifiedColumn = new TableColumn<>("Vérifié");
        verifiedColumn.setCellValueFactory(cellData -> cellData.getValue().estVerifieProperty().asObject());
        verifiedColumn.setCellFactory(col -> new TableCell<>() {
            private final CheckBox checkBox = new CheckBox();

            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    checkBox.setSelected(item);
                    checkBox.setDisable(true);
                    setGraphic(checkBox);
                }
            }
        });
        return verifiedColumn;
    }

    /**
     * Met à jour le code du contrat pour tous les éléments de la table.
     *
     * @param oldVal Ancienne valeur du code contrat
     * @param newVal Nouvelle valeur du code contrat
     */
    private void updateCodeContract(String oldVal, String newVal) {
        fileTable.getItems().forEach(item -> {
            if (!oldVal.equals(newVal)) {
                item.getSource().getCommand().setContractCode(newVal);
                item.setSource(item.getSource());
                resetFichierSortie(newVal);
            }
        });
    }

    /**
     * Réinitialise le nom du fichier de sortie avec un code contrat donné.
     *
     * @param codeContrat Le code contrat à utiliser pour le suffixe
     */
    private void resetFichierSortie(String codeContrat) {
        String sortie = FileUtil.concat(dossierSortie, source.getParameters().getOutputFileDefaultName());
        sortie = FileUtil.addSuffixToFileName(sortie, codeContrat);
        sortie = FileUtil.addSuffixToFileName(sortie,
                this.complementField.getText() != null && !this.complementField.getText().isEmpty() ?
                        this.complementField.getText() : "");

        this.source.setOutputFileName(sortie);
        this.fichierSortieLabel.setText(sortie);
        logger.log(Level.FINE, "Fichier de sortie réinitialisé : {0}", sortie);
    }

    /**
     * Réinitialise le nom du fichier de sortie en utilisant le premier code contrat disponible.
     */
    private void resetFichierSortie() {
        String codeContrat = "";

        if (fileTable != null && !fileTable.getItems().isEmpty()) {
            var firstItem = fileTable.getItems().getFirst();
            if (firstItem != null && firstItem.getSource() != null && firstItem.getSource().getCommand() != null) {
                codeContrat = firstItem.getSource().getCommand().getContractCode();
            }
        } else if (source != null && source.getPvEntities() != null && !source.getPvEntities().isEmpty()) {
            var firstModel = source.getPvEntities().getFirst();
            if (firstModel != null && firstModel.getCommand() != null) {
                codeContrat = firstModel.getCommand().getContractCode() != null ? firstModel.getCommand().getContractCode() : "";
            }
        }

        resetFichierSortie(codeContrat);
    }

    /**
     * Sélectionne la ligne précédente dans la table.
     */
    private void selectPreviousRow() {
        int selectedIndex = fileTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex > 0) {
            fileTable.getSelectionModel().select(selectedIndex - 1);
            logger.log(Level.FINE, "Sélection de la ligne précédente : {0}", selectedIndex - 1);
        }
    }

    /**
     * Sélectionne la ligne suivante dans la table.
     */
    private void selectNextRow() {
        int selectedIndex = fileTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex < fileTable.getItems().size() - 1) {
            fileTable.getSelectionModel().select(selectedIndex + 1);
            logger.log(Level.FINE, "Sélection de la ligne suivante : {0}", selectedIndex + 1);
        }
    }

    /**
     * Sélectionne la première ligne de la table si elle existe.
     */
    private void selectFirstRow() {
        if (!fileTable.getItems().isEmpty()) {
            fileTable.getSelectionModel().select(0);
            logger.log(Level.FINE, "Sélection de la première ligne");
        }
    }

    /**
     * Convertit un chemin de fichier en un modèle PvEntityPvModel.
     *
     * @param path Le chemin du fichier
     * @return Le modèle PvEntityPvModel correspondant
     */
    private PvEntityPvModel convertToPv(String path) {
        PvEntityPvModel pv = this.pvService.GetPvFrom(path);
        logger.log(Level.FINE, "Conversion du fichier {0} en PvEntityPvModel", path);
        return pv;
    }

    /**
     * Met à jour l'état du bouton "Preview" en fonction de la vérification des éléments.
     */
    private void updatePreviewButtonState() {
        boolean allVerified = fileTable.getItems().stream()
                .allMatch(item -> item.estVerifieProperty().get());
        previewButton.setDisable(!allVerified);
        logger.log(Level.FINE, "État du bouton Preview mis à jour : désactivé = {0}", !allVerified);
    }

    /**
     * Point d'entrée principal pour lancer l'application.
     *
     * @param args Arguments de la ligne de commande
     */
    public static void main(String[] args) {
        List<String> filePaths = List.of("chemin/vers/fichier1.xlsx", "chemin/vers/fichier2.xlsx");
        String dossierSortie = "chemin/vers";
        CommandesView app = new CommandesView(filePaths, dossierSortie);
        app.start(new Stage());
    }
}