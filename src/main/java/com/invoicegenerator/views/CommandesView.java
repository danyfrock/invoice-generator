package com.invoicegenerator.views;

import java.util.ArrayList;
import java.util.List;

import com.invoicegenerator.modeles.BillingProcessModel;
import com.invoicegenerator.services.EntitePvService;
import com.invoicegenerator.modeles.PvEntityPvModel;

import com.invoicegenerator.utils.FileUtil;
import com.invoicegenerator.viewModels.CommandeViewModel;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class CommandesView extends Application {
    private TableView<CommandeViewModel> fileTable;
    private String dossierSortie;
    private List<String> filePaths = new ArrayList<>();
    private Button previewButton;
    private final Label fichierSortieLabel = new Label();
    private final TextField complementField = new TextField();
    private BillingProcessModel source;
    private EntitePvService pvService = new EntitePvService();


    public CommandesView(List<String> filePaths, String dossierSortie) {
        this.filePaths = filePaths;
        this.dossierSortie = dossierSortie;

        this.source = new BillingProcessModel();
        for (String path : filePaths) {
            PvEntityPvModel pv = convertToPv(path);
            CommandeViewModel commandeViewModel = new CommandeViewModel();
            commandeViewModel.setSource(pv);
            commandeViewModel.fillWidthProperty().setValue(true);
            source.getEntitePvModeles().add(pv);
        }
    }

    public CommandesView(BillingProcessModel source) {
        this.source = source;

        for(PvEntityPvModel pv : source.getEntitePvModeles()){
            this.pvService.FillPvFrom(pv.getPathFichier(),pv);
        }

        this.dossierSortie = this.source.getParametres().getEmplacementDossierSortie();
        this.filePaths.clear();
        this.filePaths.addAll(
                source.getEntitePvModeles().stream()
                        .map(PvEntityPvModel::getPathFichier)
                        .toList()
        );
        this.complementField.setText(this.source.getComplementNom());
        resetFichierSortie();
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Commandes UI");

        fileTable = new TableView<>();
        
        TableColumn<CommandeViewModel, String> fileNameColumn = new TableColumn<>("File Name");
        fileNameColumn.setCellValueFactory(cellData -> cellData.getValue().nomFichierProperty());
        
        TableColumn<CommandeViewModel, String> filePathColumn = new TableColumn<>("File Path");
        filePathColumn.setCellValueFactory(cellData -> cellData.getValue().pathFichierProperty());
        
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

        for (PvEntityPvModel pv: source.getEntitePvModeles()){
            CommandeViewModel commandeViewModel = new CommandeViewModel();
            commandeViewModel.setSource(pv);
            commandeViewModel.fillWidthProperty().setValue(true);
            fileTable.getItems().add(commandeViewModel);
        }
        
        Button backButton = new Button("Retour au choix de fichiers");
        backButton.setOnAction(e -> {
            new FileSelectorView(source).start(new Stage());
            primaryStage.close();
        });

        previewButton = new Button("Preview");
        previewButton.setDisable(true); // Désactiver le bouton par défaut
        previewButton.setOnAction(e -> {
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
            //observe prop est verifie
            fileTable.getItems().forEach(item ->
                    item.estVerifieProperty()
                            .addListener(
                                    (obs,
                                     wasVerified,
                                     isNowVerified) -> {
                updatePreviewButtonState();
            }));

            // observe le premier code contrat
            CommandeViewModel firstItem = fileTable.getItems().getFirst();
            firstItem.codeContratProperty()
                    .addListener((obs, oldVal, newVal) -> {
                updateCodeContract(oldVal, newVal);
            });

            this.complementField.textProperty().addListener((observable, oldValue, newValue) -> {
                this.source.setComplementNom(newValue);
                resetFichierSortie();
            });
        }

        //selection des CommandeViewModel
        Button previousButton = new Button("<");
        previousButton.setOnAction(e -> selectPreviousRow());
        Button nextButton = new Button(">");
        nextButton.setOnAction(e -> selectNextRow());
        HBox navigationBox = new HBox(10, previousButton, nextButton);

        // partie complement et fichier de sortie
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
            commandePanel.getChildren().clear();
            ////commandePanel.getChildren().add(fileTable);
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

        BorderPane root = new BorderPane();
        root.setTop(sortieBox);
        root.setCenter(mainContent);
        ////root.setCenter(commandePanel);
        root.setBottom(buttonBox);

        Scene scene = new Scene(root, 800, 600);
        //primaryStage.setFullScreen(true);
        primaryStage.setScene(scene);
        primaryStage.show();

        selectFirstRow();
        updatePreviewButtonState(); // Initial check
    }

    private void updateCodeContract(String oldVal, String newVal) {
        fileTable.getItems().forEach(item -> {
            if (!oldVal.equals(newVal)) {
                item.getSource().getCommande().setCodeContrat(newVal);
                item.setSource(item.getSource());
                resetFichierSortie(newVal);
            }
        });
    }

    private void resetFichierSortie(String codeContrat){
        String sortie = FileUtil.concat(dossierSortie, source.getParametres().getNomDefautFichierSortie());
        sortie = FileUtil.addSuffixToFileName(sortie, codeContrat);
        sortie = FileUtil.addSuffixToFileName(sortie, this.complementField.getText());

        this.source.setNomFichierSortie(sortie);
        this.fichierSortieLabel.setText(sortie);
    }

    private void resetFichierSortie() {
        String codeContrat = "";

        if (fileTable != null && !fileTable.getItems().isEmpty()) {
            var firstItem = fileTable.getItems().getFirst();
            if (firstItem != null && firstItem.getSource() != null && firstItem.getSource().getCommande() != null) {
                codeContrat = firstItem.getSource().getCommande().getCodeContrat();
            }
        } else if (source != null && source.getEntitePvModeles() != null && !source.getEntitePvModeles().isEmpty()) {
            var firstModel = source.getEntitePvModeles().getFirst();
            if (firstModel != null && firstModel.getCommande() != null) {
                codeContrat = firstModel.getCommande().getCodeContrat() != null ? firstModel.getCommande().getCodeContrat() : "";
            }
        }

        resetFichierSortie(codeContrat);
    }

    private void selectPreviousRow() {
        int selectedIndex = fileTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex > 0) {
            fileTable.getSelectionModel().select(selectedIndex - 1);
        }
    }

    private void selectNextRow() {
        int selectedIndex = fileTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex < fileTable.getItems().size() - 1) {
            fileTable.getSelectionModel().select(selectedIndex + 1);
        }
    }

    private void selectFirstRow() {
        if (!fileTable.getItems().isEmpty()) {
            fileTable.getSelectionModel().select(0);
        }
    }

    private PvEntityPvModel convertToPv(String path) {
        return this.pvService.GetPvFrom(path);
    }

    private void updatePreviewButtonState() {
        boolean allVerified = fileTable.getItems().stream()
                .allMatch(item -> item.estVerifieProperty().get());
        previewButton.setDisable(!allVerified);
    }

    public static void main(String[] args) {
        List<String> filePaths = List.of("chemin/vers/fichier1.xlsx", "chemin/vers/fichier2.xlsx");
        String dossierSortie = "chemin/vers";
        CommandesView app = new CommandesView(filePaths, dossierSortie);
        app.start(new Stage());
    }
}