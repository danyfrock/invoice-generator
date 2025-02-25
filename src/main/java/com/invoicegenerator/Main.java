package com.invoicegenerator;


import com.invoicegenerator.views.FileSelectorView;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        // Créez une instance de FileSelector et rendez-la visible
        FileSelectorView fileSelectorView = new FileSelectorView();
        fileSelectorView.start(primaryStage);
    }

    public Main() {
        // Constructeur sans argument
    }

    public static void main(String[] args) {
        launch(args);
    }
}