package com.invoicegenerator.services;

import com.google.gson.Gson;
import com.invoicegenerator.modeles.ParametersModel;
import com.invoicegenerator.utils.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Service pour gérer les paramètres de l'application.
 */
public class ParametresService {
    private static final Logger logger = LoggerFactory.getLogger(ParametresService.class.getName());
    private String parametresFileName;

    /**
     * Constructeur de la classe ParametresService.
     * @param parametresFileName Le nom du fichier de paramètres.
     */
    public ParametresService(String parametresFileName) {
        this.parametresFileName = parametresFileName;
        logger.info("ParametresService initialisé avec le fichier : " + parametresFileName);
    }

    /**
     * Enregistre les paramètres dans un fichier JSON.
     * @param parametres Les paramètres à enregistrer.
     */
    public void enregistrerParametres(ParametersModel parametres) {
        Gson gson = new Gson();
        try (FileWriter writer = new FileWriter(parametresFileName)) {
            gson.toJson(parametres, writer);
            logger.info("Paramètres enregistrés avec succès dans le fichier : " + parametresFileName);
        } catch (IOException e) {
            logger.severe("Erreur lors de l'enregistrement des paramètres : " + e.getMessage());
            e.printStackTrace();
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
            logger.warning("Le fichier de paramètres n'existe pas : " + parametresFileName);
            return new ParametersModel(); // Retourne un objet vide si le fichier n'existe pas
        }

        try (FileReader reader = new FileReader(file)) {
            ParametersModel parametres = gson.fromJson(reader, ParametersModel.class);
            logger.info("Paramètres chargés avec succès depuis le fichier : " + parametresFileName);
            return parametres;
        } catch (IOException e) {
            logger.severe("Erreur lors du chargement des paramètres : " + e.getMessage());
            e.printStackTrace();
            return new ParametersModel();
        }
    }
}