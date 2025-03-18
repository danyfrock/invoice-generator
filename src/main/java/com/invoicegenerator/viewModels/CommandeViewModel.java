package com.invoicegenerator.viewModels;

import com.invoicegenerator.services.ParametresService;
import com.invoicegenerator.utils.backend.LoggerFactory;
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
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * ViewModel pour la gestion des commandes dans l'interface utilisateur.
 * Cette classe encapsule les contrôles UI et la logique pour afficher et modifier les données d'une commande.
 */
public class CommandeViewModel extends VBox {
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
    private PvEntityPvModel source;

    private final SimpleBooleanProperty estVerifie = new SimpleBooleanProperty(false);
    private final SimpleStringProperty doitRemplir = new SimpleStringProperty("");
    private final SimpleStringProperty nomFichier = new SimpleStringProperty("");
    private final SimpleStringProperty pathFichier = new SimpleStringProperty("");
    private final SimpleStringProperty codeContrat = new SimpleStringProperty("");

    private final TableView<UoCommandLineModel> tableView = new TableView<>();
    private boolean listen = true;
    private ParametersModel parametreModele = new ParametersModel();

    /**
     * Constructeur par défaut. Initialise l'interface utilisateur et charge les paramètres.
     */
    public CommandeViewModel() {
        logger.log(Level.INFO, "Initialisation de CommandeViewModel");
        chargerParametres();

        dateDebutPicker.setConverter(converter);
        dateFinPicker.setConverter(converter);

        tableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        GridPane grid = new GridPane();
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
        tableView.getColumns().addAll(libelleCol, typeCol, prixUnitaireCol, totalNombreCol, totalHTCol, totalTTCCol,
                doneNombreCol, doneHTCol, doneTTCCol, restNombreCol, restHTCol, restTTCCol);

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
        logger.log(Level.INFO, "Interface utilisateur initialisée avec succès");
    }

    /**
     * Charge les paramètres à partir du service de paramètres.
     */
    private void chargerParametres() {
        logger.log(Level.FINE, "Chargement des paramètres");
        this.setParametres(new ParametresService(this.parametreModele.getParametersFileName()).chargerParametres());
        logger.log(Level.FINE, "Paramètres chargés avec succès");
    }

    /**
     * Définit le modèle de paramètres.
     *
     * @param parametersModel Le modèle de paramètres à définir
     */
    private void setParametres(ParametersModel parametersModel) {
        this.parametreModele = parametersModel;
    }

    /**
     * Ajoute des listeners aux propriétés des contrôles pour mettre à jour les données.
     */
    private void AddListeners() {
        logger.log(Level.FINE, "Ajout des listeners aux contrôles");
        dateDebutPicker.valueProperty().addListener((obs, oldVal, newVal) -> updateProperties());
        dateFinPicker.valueProperty().addListener((obs, oldVal, newVal) -> updateProperties());
        codeContratField.textProperty().addListener((obs, oldVal, newVal) -> updateProperties());
        codeActiviteComboBox.valueProperty().addListener((obs, oldVal, newVal) -> updateProperties());
        listen = true;
    }

    /**
     * Supprime les listeners des propriétés des contrôles.
     */
    private void removeListeners() {
        logger.log(Level.FINE, "Suppression des listeners des contrôles");
        dateDebutPicker.valueProperty().removeListener((obs, oldVal, newVal) -> updateProperties());
        dateFinPicker.valueProperty().removeListener((obs, oldVal, newVal) -> updateProperties());
        codeContratField.textProperty().removeListener((obs, oldVal, newVal) -> updateProperties());
        codeActiviteComboBox.valueProperty().removeListener((obs, oldVal, newVal) -> updateProperties());
        listen = false;
    }

    /**
     * Remplit la ComboBox des codes d'activité avec les valeurs du modèle de paramètres.
     */
    private void fillCodeActiviteComboBox() {
        logger.log(Level.FINE, "Remplissage de la ComboBox des codes d'activité");
        List<String> activityCodes = this.parametreModele.getActivityCodes();
        if (activityCodes != null) {
            codeActiviteComboBox.setItems(FXCollections.observableArrayList(activityCodes));
        } else {
            logger.log(Level.WARNING, "Impossible de récupérer les codes d'activité");
            System.out.println("Impossible de récupérer les codes d'activité.");
        }
    }

    /**
     * Retourne la source de données actuelle.
     *
     * @return L'entité PvEntityPvModel source
     */
    public PvEntityPvModel getSource() {
        return this.source;
    }

    /**
     * Définit la source de données et met à jour l'interface utilisateur.
     *
     * @param pv L'entité PvEntityPvModel à définir comme source
     */
    public void setSource(PvEntityPvModel pv) {
        logger.log(Level.INFO, "Définition de la source PvEntityPvModel");
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

            ObservableList<UoCommandLineModel> lignesCommande = FXCollections.observableArrayList(commande.getCommandLines());
            tableView.setItems(lignesCommande);
            logger.log(Level.FINE, "TableView rempli avec {0} lignes de commande", lignesCommande.size());
        } else {
            clearFields();
        }
        AddListeners();
        updateProperties();
    }

    /**
     * Met à jour les propriétés en fonction des valeurs des contrôles.
     */
    private void updateProperties() {
        if (!listen) return;

        logger.log(Level.FINE, "Mise à jour des propriétés");
        this.source.getCommand().setActivityCode(codeActiviteComboBox.getValue());
        this.source.getCommand().setContractCode(codeContratField.getText());
        this.source.getCommand().setDateDebut(dateDebutPicker.getValue());
        this.source.getCommand().setDateFin(dateFinPicker.getValue());
        estVerifie.set(isEstVerifie());
        doitRemplir.set(getDoitRemplir());
        codeContrat.set(codeContratField.getText());
    }

    /**
     * Vérifie si tous les champs requis sont correctement remplis.
     *
     * @return true si tout est vérifié, false sinon
     */
    public boolean isEstVerifie() {
        return getDoitRemplir().isEmpty();
    }

    /**
     * Retourne un message indiquant ce qui doit être rempli ou corrigé.
     *
     * @return Une chaîne de caractères décrivant les erreurs ou une chaîne vide si tout est correct
     */
    public String getDoitRemplir() {
        if (dateDebutPicker.getValue() == null) {
            return "Remplir le contrôle Date Début";
        }
        if (dateFinPicker.getValue() == null) {
            return "Remplir le contrôle Date Fin";
        }
        if (codeActiviteComboBox.getValue() == null || codeActiviteComboBox.getValue().isEmpty()) {
            return "Remplir le contrôle Code Activité";
        }
        if (!codeActiviteComboBox.getItems().contains(codeActiviteComboBox.getValue())) {
            return "La valeur du Code Activité n'est pas valide.";
        }
        if (codeContratField.getText().isEmpty()) {
            return "Remplir le contrôle Code Contrat";
        }
        if (dateDebutPicker.getValue().isEqual(dateFinPicker.getValue())) {
            return "La date de début doit être différente de la date de fin";
        }
        if (dateDebutPicker.getValue().isAfter(dateFinPicker.getValue())) {
            return "La date de début doit être avant la date de fin";
        }

        int yearMin = this.parametreModele.getMinYear();
        if (dateDebutPicker.getValue().getYear() < yearMin) {
            return "La date de début doit être après " + yearMin;
        }
        if (dateFinPicker.getValue().getYear() < yearMin) {
            return "La date de fin doit être après " + yearMin;
        }

        int yearMax = this.parametreModele.getMaxYear();
        if (dateDebutPicker.getValue().getYear() > yearMax) {
            return "La date de début doit être avant " + yearMax;
        }
        if (dateFinPicker.getValue().getYear() > yearMax) {
            return "La date de fin doit être avant " + yearMax;
        }

        return "";
    }

    /**
     * Réinitialise tous les champs de l'interface utilisateur.
     */
    private void clearFields() {
        logger.log(Level.FINE, "Réinitialisation des champs");
        dateDebutPicker.setValue(null);
        dateFinPicker.setValue(null);
        codeContratField.setText("");
        codeActiviteComboBox.setValue("");
        nomFichier.set("");
        pathFichier.set("");
        tableView.getItems().clear();
        updateProperties();
    }

    /**
     * Retourne la propriété indiquant si les données sont vérifiées.
     *
     * @return SimpleBooleanProperty pour estVerifie
     */
    public SimpleBooleanProperty estVerifieProperty() {
        return estVerifie;
    }

    /**
     * Retourne la propriété contenant le message de ce qui doit être rempli.
     *
     * @return SimpleStringProperty pour doitRemplir
     */
    public SimpleStringProperty doitRemplirProperty() {
        return doitRemplir;
    }

    /**
     * Retourne la propriété contenant le nom du fichier.
     *
     * @return SimpleStringProperty pour nomFichier
     */
    public SimpleStringProperty nomFichierProperty() {
        return nomFichier;
    }

    /**
     * Retourne la propriété contenant le chemin du fichier.
     *
     * @return SimpleStringProperty pour pathFichier
     */
    public SimpleStringProperty pathFichierProperty() {
        return pathFichier;
    }

    /**
     * Retourne la propriété contenant le code du contrat.
     *
     * @return SimpleStringProperty pour codeContrat
     */
    public SimpleStringProperty codeContratProperty() {
        return codeContrat;
    }
}