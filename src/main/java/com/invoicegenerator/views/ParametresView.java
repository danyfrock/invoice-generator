package com.invoicegenerator.views;

import com.invoicegenerator.modeles.BillingProcessModel;
import com.invoicegenerator.services.ParametresService;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;

public class ParametresView extends Application {
    private final BillingProcessModel source;
    private final TextField textAnneeMin = new TextField();
    private final TextField textAnneeMax = new TextField();
    private final TextField textDossier = new TextField();
    ListView<String> listCodes = new ListView<>();

    public ParametresView(BillingProcessModel source) {
        this.source = source;
        this.chargerParametres();
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Paramètres");

        // Layout
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        // Emplacement du dossier
        Label labelDossier = new Label("Emplacement du dossier de sortie");
        textDossier.setEditable(false);
        Button btnDossier = new Button("Sélectionner");
        btnDossier.setOnAction(e -> selectOutputFolder());

        // Codes d'activité
        Label labelCodes = new Label("Codes d'activité");
        TextField textCode = new TextField();
        // Ajout du filtre
        textCode.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("[\\w]{0,5}")) {
                return change;
            }
            return null;
        }));
        Button btnAjouter = new Button("Ajouter");
        Button btnSupprimer = new Button("Supprimer");

        // Années min/max
        Label labelAnneeMin = new Label("année minimum");
        // Ajout du filtre
        textAnneeMin.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("[\\d]{0,4}")) {
                return change;
            }
            return null;
        }));

        Label labelAnneeMax = new Label("année maximale");
        // Ajout du filtre
        textAnneeMax.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("[\\d]{0,4}")) {
                return change;
            }
            return null;
        }));

        // Boutons action
        Button btnEnregistrer;
        btnEnregistrer = new Button("Enregistrer");
        btnEnregistrer.setOnAction(e -> {
            this.enregistrerParametres();
            new FileSelectorView(source).start(new Stage());
            primaryStage.close();
        });

        Button btnAnnuler;
        btnAnnuler = new Button("Annuler");
        btnAnnuler.setOnAction(e -> {
            this.chargerParametres();
            new FileSelectorView(source).start(new Stage());
            primaryStage.close();
        });

        // Positionnement
        grid.add(labelDossier, 0, 0);
        grid.add(textDossier, 1, 0);
        grid.add(btnDossier, 2, 0);

        grid.add(labelCodes, 0, 1);
        grid.add(listCodes, 1, 1, 2, 3);
        grid.add(textCode, 1, 4);
        grid.add(btnAjouter, 2, 4);
        grid.add(btnSupprimer, 2, 5);

        grid.add(textAnneeMin, 1, 6);
        grid.add(labelAnneeMin, 2, 6);

        grid.add(textAnneeMax, 1, 7);
        grid.add(labelAnneeMax, 2, 7);

        grid.add(btnEnregistrer, 1, 8);
        grid.add(btnAnnuler, 2, 8);

        // Actions des boutons
        btnAjouter.setOnAction(e -> {
            String code = textCode.getText().trim();
            if (!code.isEmpty() && !listCodes.getItems().contains(code)) {
                listCodes.getItems().add(code);
                textCode.clear();
            }
        });

        btnSupprimer.setOnAction(e -> {
            String selected = listCodes.getSelectionModel().getSelectedItem();
            if (selected != null) {
                listCodes.getItems().remove(selected);
            }
        });

        // Affichage
        Scene scene = new Scene(grid, 500, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void enregistrerParametres() {
        this.source.getParameters().setMaxYear(Integer.parseInt(this.textAnneeMax.getText()));
        this.source.getParameters().setMinYear(Integer.parseInt(this.textAnneeMin.getText()));
        this.source.getParameters().setOutputFolder(this.textDossier.getText());
        this.source.getParameters().getActivityCodes().clear();
        this.source.getParameters().getActivityCodes().addAll(listCodes.getItems());
        new ParametresService(this.source.getParameters().getParametersFileName()).enregistrerParametres(this.source.getParameters());
    }

    private void chargerParametres() {
        this.source.setParameters(new ParametresService(this.source.getParameters().getParametersFileName()).chargerParametres());

        this.textAnneeMax.setText("" + this.source.getParameters().getMaxYear());
        this.textAnneeMin.setText("" + this.source.getParameters().getMinYear());
        this.textDossier.setText((this.source.getParameters().getOutputFolder()));
        listCodes.setItems(FXCollections.observableArrayList(this.source.getParameters().getActivityCodes()));
    }

    private void selectOutputFolder() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Sélectionner un dossier de sortie");
        File selectedFolder = directoryChooser.showDialog(null);
        if (selectedFolder != null) {
            this.textDossier.setText(selectedFolder.getAbsolutePath());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
