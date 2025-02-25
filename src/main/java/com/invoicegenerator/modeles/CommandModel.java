package com.invoicegenerator.modeles;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CommandModel {
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private String codeContrat;
    private String codeActivite;
    private List<UoCommandLineModel> listeLigneCommande = new ArrayList<>();
    private static String bonDeCommandePrefix = "BDC PO";
    private String bonDeCommande;
    private String ObjetDeLaPrestationSuffixe;

    // Getters et setters
    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public String getCodeContrat() {
        return codeContrat;
    }

    public void setCodeContrat(String codeContrat) {
        this.codeContrat = codeContrat;
    }

    public String getCodeActivite() {
        return codeActivite;
    }

    public void setCodeActivite(String codeActivite) {
        this.codeActivite = codeActivite;
    }

    public List<UoCommandLineModel> getListeLigneCommande() {
        return listeLigneCommande;
    }

    public void setListeLigneCommande(List<UoCommandLineModel> listeLigneCommande) {
        this.listeLigneCommande = listeLigneCommande;
    }

    public String getBonDeCommande() {
        return bonDeCommande;
    }

    public void setBonDeCommande(String bonDeCommande) {
        this.bonDeCommande = bonDeCommande;
    }

    public String getObjetDeLaPrestation() {
        return bonDeCommandePrefix + ObjetDeLaPrestationSuffixe;
    }

    public void setObjetDeLaPrestationSuffixe(String objetDeLaPrestationSuffixe) {
        ObjetDeLaPrestationSuffixe = objetDeLaPrestationSuffixe;
    }
}