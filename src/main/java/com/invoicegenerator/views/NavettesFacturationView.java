package com.invoicegenerator.views;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.stream.Stream;

import com.invoicegenerator.modeles.PvEntityPvModel;
import com.invoicegenerator.modeles.BillingShuttleModel;
import com.invoicegenerator.modeles.ActionResult;
import com.invoicegenerator.modeles.BillingProcessModel;
import com.invoicegenerator.services.PvToNavetteService;
import com.invoicegenerator.utils.ExcelNavetteWritterUtil;

import com.invoicegenerator.viewModels.NavetteFacturationViewModel;
import javafx.application.Application;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class NavettesFacturationView extends Application {
    private TableView<NavetteFacturationViewModel> table;
    private NavetteFacturationViewModel[] source;
    private final File fichierSortie;
    private Label resultLabel;
    BillingProcessModel sourceFacturation;

    public NavettesFacturationView(BillingShuttleModel[] source, String fichierSortie) {
        this.source = getNavetteFacturationViewModels(source);

        File outputFile;
        outputFile = new File(fichierSortie);
        this.fichierSortie = outputFile;
    }

    public NavettesFacturationView(BillingProcessModel source) {
        this.sourceFacturation = source;
        this.fichierSortie = new File(this.sourceFacturation.getNomFichierSortie());

        BillingShuttleModel[] aModeleSource;
        aModeleSource = this.getNavettes();
        this.source = getNavetteFacturationViewModels(aModeleSource);
    }

    public void start(Stage primaryStage) {
        primaryStage.setTitle("Navettes de Facturation");

        // Création de la barre de menu
        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");

        MenuItem openFileItem = new MenuItem("Ouvrir Fichier");
        openFileItem.setOnAction(e -> openFile(fichierSortie));

        MenuItem openFolderItem = new MenuItem("Ouvrir Dossier");
        openFolderItem.setOnAction(e -> openFolder(fichierSortie.getParentFile()));

        fileMenu.getItems().addAll(openFileItem, openFolderItem);
        menuBar.getMenus().add(fileMenu);

        // Création de la table et des colonnes
        table = new TableView<>();
        table.setEditable(false);

        TableColumn<NavetteFacturationViewModel, String> pcBuColumn = new TableColumn<>("PC BU");
        pcBuColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPcBu()));

        TableColumn<NavetteFacturationViewModel, String> projetColumn = new TableColumn<>("Projet");
        projetColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProjet()));

        TableColumn<NavetteFacturationViewModel, String> activiteColumn = new TableColumn<>("Activité");
        activiteColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getActivite()));

        TableColumn<NavetteFacturationViewModel, Integer> nombreFacturesColumn = new TableColumn<>("Nombre de Factures");
        nombreFacturesColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getNombreFactures()).asObject());

        TableColumn<NavetteFacturationViewModel, String> noteEvenementColumn = new TableColumn<>("Note de l'Événement");
        noteEvenementColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNoteEvenement()));

        TableColumn<NavetteFacturationViewModel, Double> quantiteColumn = new TableColumn<>("Quantité");
        quantiteColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getQuantite()).asObject());

        TableColumn<NavetteFacturationViewModel, String> uniteMesureColumn = new TableColumn<>("Unité de Mesure");
        uniteMesureColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUniteMesure()));

        TableColumn<NavetteFacturationViewModel, Double> prixUnitaireColumn = new TableColumn<>("Prix Unitaire");
        prixUnitaireColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrixUnitaireRound()).asObject());

        TableColumn<NavetteFacturationViewModel, Double> montantFacturationColumn = new TableColumn<>("Montant Facturation");
        montantFacturationColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getMontantFacturationRound()).asObject());

        TableColumn<NavetteFacturationViewModel, Double> montantEvenementCalculeColumn = new TableColumn<>("Montant Événement Calculé");
        montantEvenementCalculeColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getMontantEvenementCalculeRound()).asObject());

        TableColumn<NavetteFacturationViewModel, String> periodeFacturationDuColumn = new TableColumn<>("Période Facturation Du");
        periodeFacturationDuColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPeriodeFacturationDu()));

        TableColumn<NavetteFacturationViewModel, String> periodeFacturationAuColumn = new TableColumn<>("Période Facturation Au");
        periodeFacturationAuColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPeriodeFacturationAu()));

        TableColumn<NavetteFacturationViewModel, String> itemIdColumn = new TableColumn<>("Item ID");
        itemIdColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getItemId()));

        TableColumn<NavetteFacturationViewModel, String> factureInitialeColumn = new TableColumn<>("Facture Initiale");
        factureInitialeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFactureInitiale()));

        table.getColumns().addAll(pcBuColumn, projetColumn, activiteColumn, nombreFacturesColumn, noteEvenementColumn,
                quantiteColumn, uniteMesureColumn, prixUnitaireColumn, montantFacturationColumn, montantEvenementCalculeColumn,
                periodeFacturationDuColumn, periodeFacturationAuColumn, itemIdColumn, factureInitialeColumn);

        // Ajouter les éléments de la source à la table
        table.getItems().addAll(source);

        Button backButton = new Button("Retour");
        backButton.setOnAction(e -> {
            new CommandesView(sourceFacturation).start(new Stage());
            primaryStage.close();
        });

        Button writeButton = new Button("Écrire Navettes");
        writeButton.setOnAction(e -> {
        	ActionResult resultat = ExcelNavetteWritterUtil.writeNavette(Arrays.asList(source), fichierSortie.getAbsolutePath(), "Facturation uniquement");
            resultLabel.setText(resultat.message());
        });

        resultLabel = new Label();

        HBox buttonBox = new HBox(10, backButton, writeButton);

        BorderPane root = new BorderPane();
        root.setTop(menuBar);
        root.setCenter(table);
        root.setBottom(new VBox(buttonBox, resultLabel));

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private NavetteFacturationViewModel[] getNavetteFacturationViewModels(BillingShuttleModel... billingShuttleModels) {
        // Étape 1a : Conversion des modèles de facturation en vue de facturation
        Stream<NavetteFacturationViewModel> viewModelStream = getNavetteFacturationViewModelStream(billingShuttleModels);

        // Étape 1b : Conversion du flux en tableau
        NavetteFacturationViewModel[] viewModels;
        viewModels = viewModelStream.toArray(NavetteFacturationViewModel[]::new);

        // Étape 2 : Initialisation des variables d'instance
        this.source = viewModels;
        return source;
    }

    private static Stream<NavetteFacturationViewModel> getNavetteFacturationViewModelStream(BillingShuttleModel[] source) {
        Stream<NavetteFacturationViewModel> viewModelStream;
        viewModelStream = Arrays.stream(source)
                .map(NavetteFacturationViewModel::new);
        return viewModelStream;
    }

    private BillingShuttleModel[] getNavettes() {
        return new PvToNavetteService().Convertir(sourceFacturation.getEntitePvModeles().toArray(new PvEntityPvModel[0]));
    }

    private void openFile(File file) {
        if (file.exists()) {
            try {
                Desktop.getDesktop().open(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            resultLabel.setText("Le fichier n'existe pas.");
        }
    }

    private void openFolder(File folder) {
        if (folder.exists()) {
            try {
                Desktop.getDesktop().open(folder);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            resultLabel.setText("Le dossier n'existe pas.");
        }
    }

    public static void main(String[] args) {
        // Exemple de données pour tester
        BillingShuttleModel[] source = {
            new BillingShuttleModel("EU005", "Projet1", "Activité1", 1, "Note1", 10.0, "UNT", 100.0, 1000.0, 1000.0, LocalDate.now(), LocalDate.now(), "Item1", "Facture1"),
            new BillingShuttleModel("EU005", "Projet2", "Activité2", 2, "Note2", 20.0, "UNT", 200.0, 2000.0, 2000.0, LocalDate.now(), LocalDate.now(), "Item2", "Facture2")
        };
        String fichierSortie = "chemin/vers/fichierSortie.xlsx";
        NavettesFacturationView app = new NavettesFacturationView(source, fichierSortie);
        app.start(new Stage());
    }
}