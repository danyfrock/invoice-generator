package com.invoicegenerator.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.invoicegenerator.modeles.ParametersModel;
import com.invoicegenerator.utils.backend.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Service pour gérer les paramètres de l'application.
 */
public class ParametresService {
    private static final Logger logger = LoggerFactory.getLogger(ParametresService.class.getName());
    private final String parametresFileName;

    /**
     * Constructeur de la classe ParametresService.
     * @param parametresFileName Le nom du fichier de paramètres.
     */
    public ParametresService(String parametresFileName) {
        this.parametresFileName = parametresFileName;
        logger.log(Level.INFO, "ParametresService initialisé avec le fichier : {0}", parametresFileName);
    }

    /**
     * Enregistre les paramètres dans un fichier JSON avec un formatage lisible.
     * @param parametres Les paramètres à enregistrer.
     */
    public void enregistrerParametres(ParametersModel parametres) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        try (FileWriter writer = new FileWriter(parametresFileName)) {
            gson.toJson(parametres, writer);
            logger.log(Level.INFO, "Paramètres enregistrés avec succès dans le fichier : {0}", parametresFileName);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Erreur lors de l''enregistrement des paramètres : {0}", e.getMessage());
        }
    }

    /**
     * Charge les paramètres depuis un fichier JSON.
     * @return Les paramètres chargés.
     */
    public ParametersModel chargerParametres() {
        Gson gson = new Gson();
        File file = new File(parametresFileName);

        if (!file.exists()) {
            logger.log(Level.WARNING, "Le fichier de paramètres n''existe pas : {0}", parametresFileName);
            return new ParametersModel(); // Retourne un objet vide si le fichier n'existe pas
        }

        try (FileReader reader = new FileReader(file)) {
            ParametersModel parametres = gson.fromJson(reader, ParametersModel.class);
            logger.log(Level.INFO, "Paramètres chargés avec succès depuis le fichier : {0}", parametresFileName);
            return parametres;
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Erreur lors du chargement des paramètres : {0}", e.getMessage());
            return new ParametersModel();
        }
    }
}