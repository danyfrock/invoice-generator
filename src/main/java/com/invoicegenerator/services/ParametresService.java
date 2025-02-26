package com.invoicegenerator.services;

import com.google.gson.Gson;
import com.invoicegenerator.modeles.ParametersModel;

import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;

public class ParametresService {
    private String parametresFileName;

    public ParametresService(String parametresFileName) {
        this.parametresFileName = parametresFileName;
    }

    public void enregistrerParametres(ParametersModel parametres) {
        Gson gson = new Gson();
        try (FileWriter writer = new FileWriter(parametresFileName)) {
            gson.toJson(parametres, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ParametersModel chargerParametres() {
        Gson gson = new Gson();
        File file = new File(parametresFileName);

        if (!file.exists()) {
            return new ParametersModel(); // Retourne un objet vide si le fichier n'existe pas
        }

        try (FileReader reader = new FileReader(file)) {
            return gson.fromJson(reader, ParametersModel.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new ParametersModel();
        }
    }
}