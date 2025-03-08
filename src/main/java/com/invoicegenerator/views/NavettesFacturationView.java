package com.invoicegenerator.views;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

import com.invoicegenerator.modeles.PvEntityPvModel;
import com.invoicegenerator.modeles.BillingShuttleModel;
import com.invoicegenerator.modeles.ActionResult;
import com.invoicegenerator.modeles.BillingProcessModel;
import com.invoicegenerator.services.PvToNavetteService;
import com.invoicegenerator.utils.ExcelNavetteWritterUtil;
import com.invoicegenerator.utils.LoggerFactory;
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

/**
 * Vue pour l'affichage et la gestion des navettes de facturation dans l'application.
 * Permet de visualiser les données, écrire dans un fichier Excel, et ouvrir des fichiers/dossiers.
 */
public class NavettesFacturationView extends Application {
    private static final Logger logger = LoggerFactory.getLogger(NavettesFacturationView.class.getName());

    private TableView<NavetteFacturationViewModel> table;
    private NavetteFacturationViewModel[] source;
    private final File fichierSortie;
    private Label resultLabel;
    private BillingProcessModel sourceFacturation;

    /**
     * Constructeur avec un tableau de modèles de navette et un chemin de fichier de sortie.
     *
     * @param source Tableau de BillingShuttleModel
     * @param fichierSortie Chemin du fichier de sortie
     */
    public NavettesFacturationView(BillingShuttleModel[] source, String fichierSortie) {
        logger.log(Level.INFO, "Initialisation de NavettesFacturationView avec {0} éléments et fichier de sortie : {1}", new Object[]{source.length, fichierSortie});
        this.source = getNavetteFacturationViewModels(source);
        this.fichierSortie = new File(fichierSortie);
    }

    /**
     * Constructeur avec un modèle de processus de facturation.
     *
     * @param source Le modèle BillingProcessModel contenant les données
     */
    public NavettesFacturationView(BillingProcessModel source) {
        logger.log(Level.INFO, "Initialisation de NavettesFacturationView avec BillingProcessModel");
        this.sourceFacturation = source;
        if (source == null || source.getOutputFileName() == null) {
            logger.log(Level.WARNING, "sourceFacturation ou son fichier de sortie est null, utilisation d'un fichier par défaut");
            this.fichierSortie = new File("default_output.xlsx");
        } else {
            this.fichierSortie = new File(source.getOutputFileName());
        }

        BillingShuttleModel[] aModeleSource = this.getNavettes();
        this.source = getNavetteFacturationViewModels(aModeleSource);
    }

    /**
     * Méthode principale pour démarrer l'interface utilisateur JavaFX.
     *
     * @param primaryStage La fenêtre principale de l'application
     */
    @Override
    public void start(Stage primaryStage) {
        logger.log(Level.INFO, "Démarrage de l'interface NavettesFacturationView");
        primaryStage.setTitle("Navettes de Facturation");

        try {
            MenuBar menuBar = new MenuBar();
            Menu fileMenu = new Menu("File");

            MenuItem openFileItem = new MenuItem("Ouvrir Fichier");
            openFileItem.setOnAction(e -> openFile(fichierSortie));

            MenuItem openFolderItem = new MenuItem("Ouvrir Dossier");
            openFolderItem.setOnAction(e -> openFolder(fichierSortie.getParentFile()));

            fileMenu.getItems().addAll(openFileItem, openFolderItem);
            menuBar.getMenus().add(fileMenu);

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

            // Alternative si la quantité peux être décimale.
            ////TableColumn<NavetteFacturationViewModel, Double> quantiteColumn = new TableColumn<>("Quantité");
            ////quantiteColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getQuantite()).asObject());

            // Alternative si la quantité ne peux être que entière.
            TableColumn<NavetteFacturationViewModel, Integer> quantiteColumn = new TableColumn<>("Quantité");
            quantiteColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getQuantiteAsInt()).asObject());

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

            table.getItems().addAll(source);

            Button backButton = new Button("Retour");
            backButton.setOnAction(e -> {
                logger.log(Level.INFO, "Retour à CommandesView");
                new CommandesView(sourceFacturation).start(new Stage());
                primaryStage.close();
            });

            Button writeButton = new Button("Écrire Navettes");
            writeButton.setOnAction(e -> {
                try {
                    logger.log(Level.INFO, "Écriture des navettes dans le fichier : {0}", fichierSortie.getAbsolutePath());
                    ActionResult resultat = ExcelNavetteWritterUtil.writeNavette(Arrays.asList(source), fichierSortie.getAbsolutePath(), "Facturation uniquement");
                    resultLabel.setText(resultat.message());
                    logger.log(Level.FINE, "Résultat de l'écriture : {0}", resultat.message());
                } catch (Exception ex) {
                    logger.log(Level.SEVERE, "Erreur lors de l'écriture des navettes : {0}", ex.getMessage());
                    resultLabel.setText("Erreur lors de l'écriture : " + ex.getMessage());
                }
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

            logger.log(Level.INFO, "Interface NavettesFacturationView affichée avec succès");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erreur lors du démarrage de l'interface : {0}", e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR, "Erreur lors de l'initialisation : " + e.getMessage());
            alert.showAndWait();
        }
    }

    /**
     * Convertit un tableau de BillingShuttleModel en tableau de NavetteFacturationViewModel.
     *
     * @param billingShuttleModels Les modèles de navette à convertir
     * @return Un tableau de NavetteFacturationViewModel
     */
    private NavetteFacturationViewModel[] getNavetteFacturationViewModels(BillingShuttleModel... billingShuttleModels) {
        logger.log(Level.FINE, "Conversion de {0} BillingShuttleModel en NavetteFacturationViewModel", billingShuttleModels.length);
        try {
            Stream<NavetteFacturationViewModel> viewModelStream = getNavetteFacturationViewModelStream(billingShuttleModels);
            NavetteFacturationViewModel[] viewModels = viewModelStream.toArray(NavetteFacturationViewModel[]::new);
            this.source = viewModels;
            return source;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erreur lors de la conversion des modèles : {0}", e.getMessage());
            return new NavetteFacturationViewModel[0]; // Retourne un tableau vide en cas d'erreur
        }
    }

    /**
     * Crée un flux de NavetteFacturationViewModel à partir d'un tableau de BillingShuttleModel.
     *
     * @param source Tableau de BillingShuttleModel
     * @return Un flux de NavetteFacturationViewModel
     */
    private static Stream<NavetteFacturationViewModel> getNavetteFacturationViewModelStream(BillingShuttleModel[] source) {
        logger.log(Level.FINE, "Création d'un flux de NavetteFacturationViewModel à partir de {0} éléments", source.length);
        return Arrays.stream(source).map(NavetteFacturationViewModel::new);
    }

    /**
     * Récupère les navettes à partir des entités Pv du modèle source.
     *
     * @return Un tableau de BillingShuttleModel
     */
    private BillingShuttleModel[] getNavettes() {
        logger.log(Level.FINE, "Récupération des navettes à partir de BillingProcessModel");
        try {
            if (sourceFacturation == null || sourceFacturation.getPvEntities() == null) {
                logger.log(Level.WARNING, "sourceFacturation ou ses entités Pv sont null");
                return new BillingShuttleModel[0];
            }
            return new PvToNavetteService().Convertir(sourceFacturation.getPvEntities().toArray(new PvEntityPvModel[0]));
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erreur lors de la récupération des navettes : {0}", e.getMessage());
            return new BillingShuttleModel[0]; // Retourne un tableau vide en cas d'erreur
        }
    }

    /**
     * Ouvre un fichier avec l'application par défaut du système.
     *
     * @param file Le fichier à ouvrir
     */
    private void openFile(File file) {
        logger.log(Level.FINE, "Tentative d'ouverture du fichier : {0}", file.getAbsolutePath());
        try {
            if (file.exists()) {
                Desktop.getDesktop().open(file);
                logger.log(Level.FINE, "Fichier ouvert avec succès");
            } else {
                logger.log(Level.WARNING, "Le fichier n'existe pas : {0}", file.getAbsolutePath());
                resultLabel.setText("Le fichier n'existe pas.");
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Erreur lors de l'ouverture du fichier : {0}", e.getMessage());
            resultLabel.setText("Erreur lors de l'ouverture du fichier : " + e.getMessage());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erreur inattendue lors de l'ouverture du fichier : {0}", e.getMessage());
            resultLabel.setText("Erreur inattendue : " + e.getMessage());
        }
    }

    /**
     * Ouvre un dossier avec l'explorateur de fichiers du système.
     *
     * @param folder Le dossier à ouvrir
     */
    private void openFolder(File folder) {
        logger.log(Level.FINE, "Tentative d'ouverture du dossier : {0}", folder.getAbsolutePath());
        try {
            if (folder.exists()) {
                Desktop.getDesktop().open(folder);
                logger.log(Level.FINE, "Dossier ouvert avec succès");
            } else {
                logger.log(Level.WARNING, "Le dossier n'existe pas : {0}", folder.getAbsolutePath());
                resultLabel.setText("Le dossier n'existe pas.");
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Erreur lors de l'ouverture du dossier : {0}", e.getMessage());
            resultLabel.setText("Erreur lors de l'ouverture du dossier : " + e.getMessage());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erreur inattendue lors de l'ouverture du dossier : {0}", e.getMessage());
            resultLabel.setText("Erreur inattendue : " + e.getMessage());
        }
    }

    /**
     * Point d'entrée principal pour lancer l'application avec un exemple de données.
     *
     * @param args Arguments de la ligne de commande
     */
    public static void main(String[] args) {
        logger.log(Level.INFO, "Lancement de l'application NavettesFacturationView");
        BillingShuttleModel[] source = {
                new BillingShuttleModel("EU005", "Projet1", "Activité1", 1, "Note1", 10.0, "UNT", 100.0, 1000.0, 1000.0, LocalDate.now(), LocalDate.now(), "Item1", "Facture1"),
                new BillingShuttleModel("EU005", "Projet2", "Activité2", 2, "Note2", 20.0, "UNT", 200.0, 2000.0, 2000.0, LocalDate.now(), LocalDate.now(), "Item2", "Facture2")
        };
        String fichierSortie = "chemin/vers/fichierSortie.xlsx";
        NavettesFacturationView app = new NavettesFacturationView(source, fichierSortie);
        app.start(new Stage());
    }
}