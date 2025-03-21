package com.invoicegenerator.views;

import com.invoicegenerator.modeles.BillingProcessModel;
import com.invoicegenerator.utils.backend.LoggerFactory;
import com.invoicegenerator.utils.ihm.MenuBuilder;
import com.invoicegenerator.viewModels.CommandeViewModel;
import com.invoicegenerator.viewModels.CommandesViewModel;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Vue principale pour l'affichage et la gestion des commandes dans l'application.
 */
public class CommandesView extends Application {
    private static final Logger logger = LoggerFactory.getLogger(CommandesView.class.getName());

    private TableView<CommandeViewModel> fileTable;
    private Button previewButton;
    private final Label fichierSortieLabel = new Label();
    private final TextField complementField = new TextField();
    private final CommandesViewModel source;
    private final CommandeView commandeView = new CommandeView();

    public CommandesView(BillingProcessModel source) {
        logger.log(Level.INFO, "Initialisation de CommandesView avec un modèle BillingProcessModel");
        this.source = new CommandesViewModel(source);
    }

    @Override
    public void start(Stage primaryStage) {
        logger.log(Level.INFO, "Démarrage de l'interface CommandesView");
        primaryStage.setTitle("Commandes UI");

        // TableView
        fileTable = new TableView<>();
        fileTable.setItems(source.getCommandes());

        TableColumn<CommandeViewModel, String> fileNameColumn = new TableColumn<>("File Name");
        fileNameColumn.setCellValueFactory(cellData -> cellData.getValue().nomFichierProperty());

        TableColumn<CommandeViewModel, String> filePathColumn = new TableColumn<>("File Path");
        filePathColumn.setCellValueFactory(cellData -> cellData.getValue().pathFichierProperty());

        TableColumn<CommandeViewModel, Boolean> verifiedColumn = new TableColumn<>("Vérifié");
        verifiedColumn.setCellValueFactory(cellData -> cellData.getValue().estVerifieProperty());
        verifiedColumn.setCellFactory(col -> new TableCell<>() {
            private final CheckBox checkBox = new CheckBox();
            { checkBox.setDisable(true); }
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty || item == null ? null : checkBox);
                if (!empty && item != null) checkBox.setSelected(item);
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

        // Boutons de navigation
        Button backButton = new Button("Retour au choix de fichiers");
        backButton.setOnAction(e -> goPrecedent(primaryStage));

        // Dans start(Stage primaryStage) :
        previewButton = new Button("Prévisualisation");
        previewButton.disableProperty().bind(source.allVerifiedProperty().not()); // Désactivé si allVerified est false
        previewButton.setOnAction(e -> goNext(primaryStage));

        complementField.textProperty().bindBidirectional(source.complementProperty());
        complementField.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            return newText.matches("\\w*") ? change : null;
        }));

        fichierSortieLabel.textProperty().bind(source.outputFileNameProperty());

        Button previousButton = new Button("<");
        previousButton.setOnAction(e -> selectPreviousRow());
        Button nextButton = new Button(">");
        nextButton.setOnAction(e -> selectNextRow());
        HBox navigationBox = new HBox(10, previousButton, nextButton);

        HBox buttonBox = new HBox(10, backButton, previewButton);
        Label complementLibelle = new Label("Complément");
        HBox complementBox = new HBox(10, complementLibelle, complementField);
        VBox sortieBox = new VBox(10, fichierSortieLabel, complementBox, navigationBox);

        // Panneau des détails
        VBox commandePanel = new VBox(commandeView);
        commandePanel.setMaxWidth(Double.MAX_VALUE);
        VBox.setVgrow(commandePanel, Priority.ALWAYS);

        fileTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            logger.log(Level.FINE, "Sélection changée : {0} -> {1}", new Object[]{oldVal != null ? oldVal.nomFichierProperty().get() : "null", newVal != null ? newVal.nomFichierProperty().get() : "null"});
            commandeView.setViewModel(newVal);
        });

        VBox mainContent = new VBox(fileTable, commandePanel);
        mainContent.setMaxWidth(Double.MAX_VALUE);
        VBox.setVgrow(mainContent, Priority.ALWAYS);

        // Menu
        MenuBar menuBar = new MenuBar();
        Menu menu = new MenuBuilder("Menu", primaryStage)
                .avecSauvegardeJson(
                        source.getSource().getParameters().getDernierEmplacementConnuProgression(),
                        "Sauvegarder progression",
                        this::sauvegarderProgression
                )
                .silVousPlait();
        Menu navigateMenu = new MenuBuilder("Naviguer", primaryStage)
                .avecNavigationSuivant("Prévisualisation (Ctrl+Flèche Droite)", "Ctrl+Right", e -> goNext(primaryStage))
                .avecNavigationPrecedent("Retour au choix de fichiers (Ctrl+Flèche Gauche)", "Ctrl+Left", e -> goPrecedent(primaryStage))
                .silVousPlait();
        Menu helpMenu = new MenuBuilder("Help", primaryStage).avecAide().silVousPlait();
        menuBar.getMenus().addAll(menu, navigateMenu, helpMenu);

        // Layout
        BorderPane root = new BorderPane();
        root.setTop(new VBox(menuBar, sortieBox));
        root.setCenter(mainContent);
        root.setBottom(buttonBox);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

        selectFirstRow();
        logger.log(Level.INFO, "Interface CommandesView affichée avec succès");
    }

    private void goPrecedent(Stage primaryStage) {
        logger.log(Level.INFO, "Retour à FileSelectorView");
        new FileSelectorView(source.getSource()).start(new Stage());
        primaryStage.close();
    }

    private void goNext(Stage primaryStage) {
        if (!previewButton.isDisabled()) {
            logger.log(Level.INFO, "Ouverture de NavettesFacturationView");
            new NavettesFacturationView(source.getSource()).start(new Stage());
            primaryStage.close();
        }
    }

    private void sauvegarderProgression(File file) {
        logger.log(Level.INFO, "Sauvegarde de la progression dans : {0}", file.getAbsolutePath());
        source.getSource().getParameters().setDernierEmplacementConnuProgression(file.getParentFile().getAbsolutePath());
        this.source.sauvegarderProgression(file);
    }

    private void selectPreviousRow() {
        int selectedIndex = fileTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex > 0) {
            fileTable.getSelectionModel().select(selectedIndex - 1);
            logger.log(Level.FINE, "Sélection de la ligne précédente : {0}", selectedIndex - 1);
        }
    }

    private void selectNextRow() {
        int selectedIndex = fileTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex < fileTable.getItems().size() - 1) {
            fileTable.getSelectionModel().select(selectedIndex + 1);
            logger.log(Level.FINE, "Sélection de la ligne suivante : {0}", selectedIndex + 1);
        }
    }

    private void selectFirstRow() {
        if (!fileTable.getItems().isEmpty()) {
            fileTable.getSelectionModel().select(0);
            logger.log(Level.FINE, "Sélection de la première ligne");
        }
    }
}