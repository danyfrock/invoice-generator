package com.invoicegenerator.views;

import com.invoicegenerator.modeles.PvEntityPvModel;
import com.invoicegenerator.modeles.BillingProcessModel;
import com.invoicegenerator.services.ParametresService;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.File;
import java.util.List;

public class FileSelectorView extends Application {
    private TableView<PvEntityPvModel> fileTable = new TableView<>();
    private Button nextButton = new Button("Suivant");
    private BillingProcessModel source = new BillingProcessModel();

    public FileSelectorView() {
        this.chargerParametres();
        updateNextButtonState();
    }

    public FileSelectorView(BillingProcessModel source) {
        this.source = source;
        this.chargerParametres();

        for(PvEntityPvModel pv : source.getPvEntities()) {
            fileTable.getItems().add(pv);
        }
        updateNextButtonState();
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("<<<POC>>> application d'enregistrement de navettes de facturation.");

        fileTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        TableColumn<PvEntityPvModel, String> fileNameColumn = new TableColumn<>("File Name");
        fileNameColumn.prefWidthProperty().bind(fileTable.widthProperty().multiply(0.5));
        fileNameColumn.setCellValueFactory(new PropertyValueFactory<>("fileName"));
        TableColumn<PvEntityPvModel, String> filePathColumn = new TableColumn<>("File Path");
        filePathColumn.prefWidthProperty().bind(fileTable.widthProperty().multiply(0.5));
        filePathColumn.setCellValueFactory(new PropertyValueFactory<>("filePath"));
        fileTable.getColumns().addAll(fileNameColumn, filePathColumn);

        // Enable multiple selection
        fileTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        Button selectButton = new Button("Select Files");
        selectButton.setOnAction(e -> selectFiles());

        Button deleteButton = new Button("Delete Selection");
        deleteButton.setOnAction(e -> deleteSelection());

        nextButton.setOnAction(e -> {
            source.getPvEntities().clear();
            source.getPvEntities().addAll(fileTable.getItems());
            new CommandesView(source).start(new Stage());
            primaryStage.close();
        });

        Button paramsButton = new Button("Paramètres");
        paramsButton.setOnAction(e -> {
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
    }

    private void selectFiles() {
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
                }
            }
            updateNextButtonState();
        }
    }

    private void deleteSelection() {
        List<PvEntityPvModel> selectedItems = fileTable.getSelectionModel().getSelectedItems();
        fileTable.getItems().removeAll(selectedItems);
        updateNextButtonState();
    }

    private void updateNextButtonState() {
        nextButton.setDisable(fileTable.getItems().isEmpty() ||
                source.getParameters().getOutputFolder() == null ||
                source.getParameters().getOutputFolder().isEmpty());
    }

    private void chargerParametres() {
        this.source.setParameters(new ParametresService(this.source.getParameters().getParametersFileName()).chargerParametres());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
