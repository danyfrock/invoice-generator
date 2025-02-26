package com.invoicegenerator.viewModels;

import com.invoicegenerator.services.ParametresService;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import com.invoicegenerator.modeles.*;

import java.util.List;

public class CommandeViewModel extends VBox {
    private final DatePicker dateDebutPicker = new DatePicker();
    private final DatePicker dateFinPicker = new DatePicker();
    private final TextField codeContratField = new TextField();
    private final ComboBox<String> codeActiviteComboBox = new ComboBox<>(FXCollections.observableArrayList("30002", "30005", "30007", "30009"));
    private final Label nomFichierLabel = new Label();
    private final Label pathFichierLabel = new Label();
    private PvEntityPvModel source;

    private final SimpleBooleanProperty estVerifie = new SimpleBooleanProperty(false);
    private final SimpleStringProperty doitRemplir = new SimpleStringProperty("");
    private final SimpleStringProperty nomFichier = new SimpleStringProperty("");
    private final SimpleStringProperty pathFichier = new SimpleStringProperty("");
    private final SimpleStringProperty codeContrat = new SimpleStringProperty("");

    private final TableView<UoCommandLineModel> tableView = new TableView<>();
    private boolean listen = true;
    private ParametersModel parametreModele = new ParametersModel();

    public CommandeViewModel() {
        chargerParametres();
        tableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        GridPane grid = new GridPane();
        //grid.setGridLinesVisible(true);
        //grid.setStyle("-fx-border-color: black; -fx-border-width: 5px;");
        grid.autosize();
        grid.setPrefWidth(Double.MAX_VALUE);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));

        grid.add(new Label("Date Début:"), 0, 0);
        grid.add(dateDebutPicker, 1, 0);

        grid.add(new Label("Date Fin:"), 0, 1);
        grid.add(dateFinPicker, 1, 1);

        grid.add(new Label("Code Activité:"), 0, 2);
        grid.add(codeActiviteComboBox, 1, 2);

        grid.add(new Label("Code Contrat:"), 0, 3);
        grid.add(codeContratField, 1, 3);

        // Ajout du filtre de saisie pour codeContratField
        codeContratField.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("[\\w]{0,15}")) {
                return change;
            }
            return null;
        }));

        grid.add(new Label("Nom Fichier:"), 0, 4);
        grid.add(nomFichierLabel, 1, 4);

        grid.add(new Label("Path Fichier:"), 0, 5);
        grid.add(pathFichierLabel, 1, 5);

        this.fillCodeActiviteComboBox();

        // Création des colonnes
        TableColumn<UoCommandLineModel, String> libelleCol = new TableColumn<>("Libellé");
        libelleCol.setCellValueFactory(new PropertyValueFactory<>("commandLabel"));

        TableColumn<UoCommandLineModel, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("uoType"));

        TableColumn<UoCommandLineModel, String> prixUnitaireCol = new TableColumn<>("Prix Unitaire");
        prixUnitaireCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.format("%.2f", cellData.getValue().getUnitPrice())));

        TableColumn<UoCommandLineModel, String> totalNombreCol = new TableColumn<>("total nombre d'UO");
        totalNombreCol.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getUoTotal().getNumber())));

        TableColumn<UoCommandLineModel, String> totalHTCol = new TableColumn<>("total HT");
        totalHTCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.format("%.2f", cellData.getValue().getUoTotal().getTotalHT())));

        TableColumn<UoCommandLineModel, String> totalTTCCol = new TableColumn<>("total TTC");
        totalTTCCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.format("%.2f", cellData.getValue().getUoTotal().getTotalTTC()))
        );

        TableColumn<UoCommandLineModel, String> doneNombreCol = new TableColumn<>("depensé nombre d'UO");
        doneNombreCol.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getUoCost().getNumber())));

        TableColumn<UoCommandLineModel, String> doneHTCol = new TableColumn<>("depensé HT");
        doneHTCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.format("%.2f", cellData.getValue().getUoCost().getTotalHT())));

        TableColumn<UoCommandLineModel, String> doneTTCCol = new TableColumn<>("depensé TTC");
        doneTTCCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.format("%.2f", cellData.getValue().getUoCost().getTotalTTC())));

        TableColumn<UoCommandLineModel, String> restNombreCol = new TableColumn<>("reste nombre d'UO");
        restNombreCol.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getUoToSpend().getNumber())));

        TableColumn<UoCommandLineModel, String> restHTCol = new TableColumn<>("reste HT");
        restHTCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.format("%.2f", cellData.getValue().getUoToSpend().getTotalHT())));

        TableColumn<UoCommandLineModel, String> restTTCCol = new TableColumn<>("reste TTC");
        restTTCCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.format("%.2f", cellData.getValue().getUoToSpend().getTotalTTC())));

        // Ajout des colonnes au TableView
        tableView.getColumns().add(libelleCol);
        tableView.getColumns().add(typeCol);
        tableView.getColumns().add(prixUnitaireCol);


        tableView.getColumns().add(totalNombreCol);
        tableView.getColumns().add(totalHTCol);
        tableView.getColumns().add(totalTTCCol);

        tableView.getColumns().add(doneNombreCol);
        tableView.getColumns().add(doneHTCol);
        tableView.getColumns().add(doneTTCCol);

        tableView.getColumns().add(restNombreCol);
        tableView.getColumns().add(restHTCol);
        tableView.getColumns().add(restTTCCol);

        // Ajout du TableView au GridPane
        GridPane.setHgrow(tableView, Priority.ALWAYS);
        GridPane.setVgrow(tableView, Priority.ALWAYS);
        tableView.setMaxWidth(Double.MAX_VALUE);
        tableView.setMaxHeight(Double.MAX_VALUE);
        tableView.setPrefWidth(Double.MAX_VALUE);
        grid.add(tableView, 0, 6, 4, 1);


        this.getChildren().add(grid);
        this.setMaxWidth(Double.MAX_VALUE);
        VBox.setVgrow(this, Priority.ALWAYS);
        this.setPrefWidth(Double.MAX_VALUE);
        this.setMaxWidth(Double.MAX_VALUE);

        AddListeners();
    }

    private void chargerParametres() {
        this.setParametres(new ParametresService(this.parametreModele.getParametersFileName()).chargerParametres());
    }

    private void setParametres(ParametersModel parametersModel){
        this.parametreModele = parametersModel;
    }

    private void AddListeners() {
        dateDebutPicker.valueProperty().addListener((obs, oldVal, newVal) -> updateProperties());
        dateFinPicker.valueProperty().addListener((obs, oldVal, newVal) -> updateProperties());
        codeContratField.textProperty().addListener((obs, oldVal, newVal) -> updateProperties());
        codeActiviteComboBox.valueProperty().addListener((obs, oldVal, newVal) -> updateProperties());
        listen = true;
    }

    private void removeListeners() {
        dateDebutPicker.valueProperty().removeListener((obs, oldVal, newVal) -> updateProperties());
        dateFinPicker.valueProperty().removeListener((obs, oldVal, newVal) -> updateProperties());
        codeContratField.textProperty().removeListener((obs, oldVal, newVal) -> updateProperties());
        codeActiviteComboBox.valueProperty().removeListener((obs, oldVal, newVal) -> updateProperties());
        listen = false;
    }

    private void fillCodeActiviteComboBox() {
        List<String> activityCodes = this.parametreModele.getActivityCodes();
        if (activityCodes != null) {
            codeActiviteComboBox.setItems(FXCollections.observableArrayList(activityCodes));
        } else {
            System.out.println("Impossible de récupérer les codes d'activité.");
        }
    }

    public PvEntityPvModel getSource() {
        return this.source;
    }

    public void setSource(PvEntityPvModel pv) {
        removeListeners();
        this.source = pv;
        if (pv != null && pv.getCommand() != null) {
            CommandModel commande = pv.getCommand();
            dateDebutPicker.setValue(commande.getDateDebut());
            dateFinPicker.setValue(commande.getDateFin());
            codeContratField.setText(commande.getContractCode() != null ? commande.getContractCode() : "");
            codeActiviteComboBox.setValue(commande.getActivityCode() != null ? commande.getActivityCode() : "");
            nomFichier.set(pv.getFileName());
            pathFichier.set(pv.getFilePath());
            nomFichierLabel.setText(nomFichier.getValue());
            pathFichierLabel.setText(pathFichier.getValue());

            // Remplir le TableView avec les lignes de commande
            ObservableList<UoCommandLineModel> lignesCommande = FXCollections.observableArrayList(commande.getCommandLines());
            tableView.setItems(lignesCommande);
        } else {
            clearFields();
        }
        AddListeners();
        updateProperties();
    }

    private void updateProperties() {
        if(!listen) return;

        this.source.getCommand().setActivityCode(codeActiviteComboBox.getValue());
        this.source.getCommand().setContractCode(codeContratField.getText());
        this.source.getCommand().setDateDebut(dateDebutPicker.getValue());
        this.source.getCommand().setDateFin(dateFinPicker.getValue());
        estVerifie.set(isEstVerifie());
        doitRemplir.set(getDoitRemplir());
        codeContrat.set(codeContratField.getText());
    }

    public boolean isEstVerifie() {
        return getDoitRemplir().isEmpty();
    }

    public String getDoitRemplir() {
        if (dateDebutPicker.getValue() == null) {
            return "Remplir le contrôle Date Début";
        }
        if (dateFinPicker.getValue() == null) {
            return "Remplir le contrôle Date Fin";
        }
        if (codeActiviteComboBox.getValue().isEmpty()) {
            return "Remplir le contrôle Code Activité";
        }
        if (!codeActiviteComboBox.getItems().contains(codeActiviteComboBox.getValue())) {
            return "La valeur du Code Activité n'est pas valide.";
        }
        if (codeContratField.getText().isEmpty()) {
            return "Remplir le contrôle Code Contrat";
        }
        if(dateDebutPicker.getValue().isEqual(dateFinPicker.getValue())){
            return "La date de début doit être différente de la date de fin";
        }
        if(dateDebutPicker.getValue().isAfter(dateFinPicker.getValue())){
            return "La date de début doit être avant la date de fin";
        }

        //dates minimales
        ////int yearMin = LocalDate.now().getYear() - 1;
        int yearMin = this.parametreModele.getMinYear();
        if(dateDebutPicker.getValue().getYear() < yearMin){
            return "La date de début doit être après " + yearMin;
        }
        if(dateFinPicker.getValue().getYear() < yearMin){
            return "La date de fin doit être après " + yearMin;
        }

        //dates maximales
        ////int yearMax = LocalDate.now().getYear() + 4;
        int yearMax = this.parametreModele.getMaxYear();
        if(dateDebutPicker.getValue().getYear() > yearMax){
            return "La date de début doit être avant " + yearMax;
        }
        if(dateFinPicker.getValue().getYear() > yearMax){
            return "La date de fin doit être avant " + yearMax;
        }

        return "";
    }

    private void clearFields() {
        dateDebutPicker.setValue(null);
        dateFinPicker.setValue(null);
        codeContratField.setText("");
        codeActiviteComboBox.setValue("");
        nomFichier.set("");
        pathFichier.set("");
        tableView.getItems().clear();
        updateProperties();
    }

    public SimpleBooleanProperty estVerifieProperty() {
        return estVerifie;
    }

    public SimpleStringProperty doitRemplirProperty() {
        return doitRemplir;
    }

    public SimpleStringProperty nomFichierProperty() {
        return nomFichier;
    }

    public SimpleStringProperty pathFichierProperty() {
        return pathFichier;
    }

    public SimpleStringProperty codeContratProperty() {
        return codeContrat;
    }
}
