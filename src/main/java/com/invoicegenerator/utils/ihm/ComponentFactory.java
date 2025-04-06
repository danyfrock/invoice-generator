package com.invoicegenerator.utils.ihm;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;

/**
 * Usine de composants JavaFX réutilisables pour l'interface graphique.
 */
public class ComponentFactory {

    private  ComponentFactory() { throw new IllegalStateException("Utility class");}

    /**
     * Crée une fenêtre de chargement modale avec un indicateur de progression et un message personnalisé.
     *
     * @param message Le message à afficher à l'utilisateur pendant le chargement.
     * @return Une instance de {@link Stage} configurée en tant que fenêtre modale de chargement.
     */
    public static Stage createLoadingStage(String message) {
        Stage loadingStage = new Stage();
        ProgressIndicator indicator = new ProgressIndicator(-1); // Chargement indéfini
        Label loadingLabel = new Label(message);
        VBox loadingLayout = new VBox(10, loadingLabel, indicator);
        loadingLayout.setAlignment(Pos.CENTER);
        Scene loadingScene = new Scene(loadingLayout, 300, 150);
        loadingStage.setScene(loadingScene);
        loadingStage.setTitle("Chargement");
        loadingStage.initModality(Modality.APPLICATION_MODAL);
        return loadingStage;
    }
}
