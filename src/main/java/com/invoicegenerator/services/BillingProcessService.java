package com.invoicegenerator.services;

import com.google.gson.*;
import com.invoicegenerator.modeles.BillingProcessModel;
import com.invoicegenerator.utils.backend.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

/**
 * Service pour gérer la sauvegarde et le chargement des modèles BillingProcessModel.
 */
public class BillingProcessService {
    private static final Logger logger = LoggerFactory.getLogger(BillingProcessService.class.getName());
    private String billingFileName;

    /**
     * Constructeur de la classe BillingProcessService.
     * @param billingFileName Le nom du fichier où sauvegarder/charger le modèle.
     */
    public BillingProcessService(String billingFileName) {
        this.billingFileName = billingFileName;
        logger.info("BillingProcessService initialisé avec le fichier : " + billingFileName);
    }

    /**
     * Enregistre un modèle BillingProcessModel dans un fichier JSON.
     * @param model Le modèle à enregistrer.
     */
    public void enregistrerBillingProcess(BillingProcessModel model) {
        // Configuration de Gson avec un adaptateur pour LocalDate
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();

        try {
            // Afficher le JSON dans les logs pour vérification
            String jsonString = gson.toJson(model);
            logger.info("Contenu JSON avant enregistrement : \n" + jsonString);

            // Écrire dans le fichier
            try (FileWriter writer = new FileWriter(billingFileName)) {
                gson.toJson(model, writer);
                logger.info("Modèle BillingProcessModel enregistré avec succès dans le fichier : " + billingFileName);
            }
        } catch (IOException e) {
            logger.severe("Erreur lors de l'enregistrement du modèle : " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Charge un modèle BillingProcessModel depuis un fichier JSON.
     * @return Le modèle chargé, ou un nouveau modèle vide si le fichier n'existe pas ou en cas d'erreur.
     */
    public BillingProcessModel chargerBillingProcess() {
        // Configuration de Gson avec un adaptateur pour LocalDate
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();

        File file = new File(billingFileName);

        if (!file.exists()) {
            logger.warning("Le fichier du modèle n'existe pas : " + billingFileName);
            return new BillingProcessModel();
        }

        try (FileReader reader = new FileReader(file)) {
            BillingProcessModel model = gson.fromJson(reader, BillingProcessModel.class);
            logger.info("Modèle BillingProcessModel chargé avec succès depuis le fichier : " + billingFileName);
            return model;
        } catch (IOException e) {
            logger.severe("Erreur lors du chargement du modèle : " + e.getMessage());
            e.printStackTrace();
            return new BillingProcessModel();
        }
    }

    /**
     * Adaptateur Gson personnalisé pour sérialiser/désérialiser LocalDate.
     */
    private static class LocalDateAdapter implements JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {
        private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

        @Override
        public JsonElement serialize(LocalDate src, java.lang.reflect.Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.format(FORMATTER));
        }

        @Override
        public LocalDate deserialize(JsonElement json, java.lang.reflect.Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            return LocalDate.parse(json.getAsString(), FORMATTER);
        }
    }
}