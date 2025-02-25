package com.invoicegenerator.services;

import com.google.gson.Gson;
import com.invoicegenerator.modeles.ParametersModel;

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
        try (FileReader reader = new FileReader(parametresFileName)) {
            return gson.fromJson(reader, ParametersModel.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new ParametersModel();
        }
    }
}