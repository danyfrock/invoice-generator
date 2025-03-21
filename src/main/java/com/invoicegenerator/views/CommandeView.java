package com.invoicegenerator.views;

import com.invoicegenerator.utils.backend.LoggerFactory;
import com.invoicegenerator.viewModels.CommandeViewModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import com.invoicegenerator.modeles.*;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * View pour la gestion des commandes dans l'interface utilisateur.
 * Cette classe encapsule les contrôles UI et la logique pour afficher et modifier les données d'une commande.
 */
public class CommandeView extends VBox {
    private static final Logger logger = LoggerFactory.getLogger(CommandeViewModel.class.getName());

    private final DatePicker dateDebutPicker = new DatePicker();
    private final DatePicker dateFinPicker = new DatePicker();

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    StringConverter<LocalDate> converter = new StringConverter<LocalDate>() {
        @Override
        public String toString(LocalDate date) {
            if (date != null) {
                return formatter.format(date);
            }
            return "";
        }

        @Override
        public LocalDate fromString(String string) {
            if (string != null && !string.isEmpty()) {
                return LocalDate.parse(string, formatter);
            }
            return null;
        }
    };

    private final TextField codeContratField = new TextField();
    private final ComboBox<String> codeActiviteComboBox = new ComboBox<>(FXCollections.observableArrayList("30002", "30005", "30007", "30009"));
    private final Label nomFichierLabel = new Label();
    private final Label pathFichierLabel = new Label();
    private CommandeViewModel source;

    private final TableView<UoCommandLineModel> tableView = new TableView<>();


    /**
     * Constructeur par défaut. Initialise l'interface utilisateur sans viewmodel.
     */
    public CommandeView() {
        logger.log(Level.INFO, "Initialisation de CommandeView sans viewmodel");
        initUI();
        logger.log(Level.INFO, "Interface utilisateur initialisée avec succès");
    }

    /**
     * Constructeur avec un viewmodel. Initialise l'interface utilisateur et associe le viewmodel.
     * @param source Le CommandeViewModel à associer
     */
    public CommandeView(CommandeViewModel source) {
        this.source = source;
        logger.log(Level.INFO, "Initialisation de CommandeView avec viewmodel");
        initUI();
        logger.log(Level.INFO, "Interface utilisateur initialisée avec succès");
    }

    /**
     * Initialise les composants de l'interface utilisateur communs aux deux constructeurs.
     */
    private void initUI() {
        dateDebutPicker.setConverter(converter);
        dateFinPicker.setConverter(converter);
        tableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        GridPane grid = new GridPane();
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

        // Filtre de saisie pour codeContratField
        codeContratField.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            return newText.matches("[\\w]{0,15}") ? change : null;
        }));

        grid.add(new Label("Nom Fichier:"), 0, 4);
        grid.add(nomFichierLabel, 1, 4);
        grid.add(new Label("Path Fichier:"), 0, 5);
        grid.add(pathFichierLabel, 1, 5);

        // Configuration du TableView (colonnes)
        TableColumn<UoCommandLineModel, String> libelleCol = new TableColumn<>("Libellé");
        libelleCol.setCellValueFactory(new PropertyValueFactory<>("commandLabel"));
        TableColumn<UoCommandLineModel, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("uoType"));
        TableColumn<UoCommandLineModel, String> prixUnitaireCol = new TableColumn<>("Prix Unitaire");
        prixUnitaireCol.setCellValueFactory(cellData -> new SimpleStringProperty(String.format("%.2f", cellData.getValue().getUnitPrice())));
        TableColumn<UoCommandLineModel, String> totalNombreCol = new TableColumn<>("total nombre d'UO");
        totalNombreCol.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getUoTotal().getNumber())));
        TableColumn<UoCommandLineModel, String> totalHTCol = new TableColumn<>("total HT");
        totalHTCol.setCellValueFactory(cellData -> new SimpleStringProperty(String.format("%.2f", cellData.getValue().getUoTotal().getTotalHT())));
        TableColumn<UoCommandLineModel, String> totalTTCCol = new TableColumn<>("total TTC");
        totalTTCCol.setCellValueFactory(cellData -> new SimpleStringProperty(String.format("%.2f", cellData.getValue().getUoTotal().getTotalTTC())));
        TableColumn<UoCommandLineModel, String> doneNombreCol = new TableColumn<>("depensé nombre d'UO");
        doneNombreCol.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getUoCost().getNumber())));
        TableColumn<UoCommandLineModel, String> doneHTCol = new TableColumn<>("depensé HT");
        doneHTCol.setCellValueFactory(cellData -> new SimpleStringProperty(String.format("%.2f", cellData.getValue().getUoCost().getTotalHT())));
        TableColumn<UoCommandLineModel, String> doneTTCCol = new TableColumn<>("depensé TTC");
        doneTTCCol.setCellValueFactory(cellData -> new SimpleStringProperty(String.format("%.2f", cellData.getValue().getUoCost().getTotalTTC())));
        TableColumn<UoCommandLineModel, String> restNombreCol = new TableColumn<>("reste nombre d'UO");
        restNombreCol.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getUoToSpend().getNumber())));
        TableColumn<UoCommandLineModel, String> restHTCol = new TableColumn<>("reste HT");
        restHTCol.setCellValueFactory(cellData -> new SimpleStringProperty(String.format("%.2f", cellData.getValue().getUoToSpend().getTotalHT())));
        TableColumn<UoCommandLineModel, String> restTTCCol = new TableColumn<>("reste TTC");
        restTTCCol.setCellValueFactory(cellData -> new SimpleStringProperty(String.format("%.2f", cellData.getValue().getUoToSpend().getTotalTTC())));

        tableView.getColumns().addAll(libelleCol, typeCol, prixUnitaireCol, totalNombreCol, totalHTCol, totalTTCCol,
                doneNombreCol, doneHTCol, doneTTCCol, restNombreCol, restHTCol, restTTCCol);

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
    }

    /**
     * Vide la vue en supprimant les bindings et en réinitialisant les champs.
     */
    private void Clear() {
        logger.log(Level.FINE, "Suppression des bindings et réinitialisation des champs");

        if (this.source != null) {
            dateDebutPicker.valueProperty().unbindBidirectional(this.source.getDateDebutProperty());
            dateFinPicker.valueProperty().unbindBidirectional(this.source.getDateFinProrperty());
            codeContratField.textProperty().unbindBidirectional(this.source.codeContratProperty());
            codeActiviteComboBox.valueProperty().unbindBidirectional(this.source.CodeActiviteProperty());
            nomFichierLabel.textProperty().unbind();
            pathFichierLabel.textProperty().unbind();
            tableView.itemsProperty().unbind();
        }

        dateDebutPicker.setValue(null);
        dateFinPicker.setValue(null);
        codeContratField.setText("");
        codeActiviteComboBox.setValue(null);
        nomFichierLabel.setText("");
        pathFichierLabel.setText("");
        tableView.setItems(null);
    }

    /**
     * Associe un nouveau ViewModel à cette vue et configure les bindings.
     * @param viewModel Le CommandeViewModel à binder
     */
    public void setViewModel(CommandeViewModel viewModel) {
        logger.log(Level.FINE, "Association d'un nouveau ViewModel à la vue");

        this.Clear(); // Nettoie les anciens bindings
        this.source = viewModel;

        // Bindings bidirectionnels pour les champs modifiables
        dateDebutPicker.valueProperty().bindBidirectional(this.source.getDateDebutProperty());
        dateFinPicker.valueProperty().bindBidirectional(this.source.getDateFinProrperty());
        codeContratField.textProperty().bindBidirectional(this.source.codeContratProperty());
        codeActiviteComboBox.valueProperty().bindBidirectional(this.source.CodeActiviteProperty());

        // Bindings unidirectionnels pour les champs en lecture seule
        nomFichierLabel.textProperty().bind(this.source.nomFichierProperty());
        pathFichierLabel.textProperty().bind(this.source.pathFichierProperty());

        // Remplissage de la ComboBox
        fillCodeActiviteComboBox();

        // Remplissage du TableView
        tableView.setItems(FXCollections.observableArrayList(this.source.getSource().getCommand().getCommandLines()));
    }

    /**
     * Remplit la ComboBox des codes d'activité avec les valeurs du modèle de paramètres.
     */
    private void fillCodeActiviteComboBox() {
        logger.log(Level.FINE, "Remplissage de la ComboBox des codes d'activité");
        List<String> activityCodes = this.source.getBackReference().getSource().getParameters().getActivityCodes();
        if (activityCodes != null && !activityCodes.isEmpty()) {
            codeActiviteComboBox.setItems(FXCollections.observableArrayList(activityCodes));
        } else {
            logger.log(Level.WARNING, "Aucun code d'activité disponible");
        }
    }

}