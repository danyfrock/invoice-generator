package com.invoicegenerator.modeles;

public class PvEntityPvModel {
    private String nomFichier = "";
    private String pathFichier = "";
    private CommandModel commande = new CommandModel();

    // Getters et setters
    public String getNomFichier() {
        return nomFichier;
    }

    public PvEntityPvModel setNomFichier(String nomFichier) {
        this.nomFichier = nomFichier;
        return this;
    }

    public String getPathFichier() {
        return pathFichier;
    }

    public PvEntityPvModel setPathFichier(String pathFichier) {
        this.pathFichier = pathFichier;
        return this;
    }

    public CommandModel getCommande() {
        return commande;
    }

}