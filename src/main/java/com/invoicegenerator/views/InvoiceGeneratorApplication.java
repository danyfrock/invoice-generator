
package com.invoicegenerator.views;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class InvoiceGeneratorApplication extends Application {

    private static final Logger logger = Logger.getLogger(InvoiceGeneratorApplication.class.getName());

    @Override
    public void start(Stage stage) {
        try {
            Image appIcon = new Image(Objects.requireNonNull(
                    InvoiceGeneratorApplication.class.getResourceAsStream("/icons/nav fac ico.png")));
            stage.getIcons().add(appIcon);
            logger.log(Level.INFO, "Icône chargée avec succès.");
        } catch (NullPointerException e) {
            logger.log(Level.SEVERE, "Icône non trouvée !", e);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erreur lors du chargement de l'icône", e);
        }

        setupView(stage);
        stage.show();
    }

    protected abstract void setupView(Stage stage);
}
