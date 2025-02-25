package com.invoicegenerator.modeles;

import java.time.LocalDate;

public class BillingShuttleModel {
    private String pcBu;
    private String projet;
    private String activite;
    private int nombreFactures;
    private String noteEvenement;
    private double quantite;
    private String uniteMesure;
    private double prixUnitaire;
    private double montantFacturation;
    private double montantEvenementCalcule;
    private LocalDate periodeFacturationDu;
    private LocalDate periodeFacturationAu;
    private String itemId;
    private String factureInitiale;

    public BillingShuttleModel() {}

    public BillingShuttleModel(String pcBu, String projet, String activite, int nombreFactures, String noteEvenement,
                               double quantite, String uniteMesure, double prixUnitaire, double montantFacturation,
                               double montantEvenementCalcule, LocalDate periodeFacturationDu, LocalDate periodeFacturationAu,
                               String itemId, String factureInitiale) {
        this.pcBu = pcBu;
        this.projet = projet;
        this.activite = activite;
        this.nombreFactures = nombreFactures;
        this.noteEvenement = noteEvenement;
        this.quantite = quantite;
        this.uniteMesure = uniteMesure;
        this.prixUnitaire = prixUnitaire;
        this.montantFacturation = montantFacturation;
        this.montantEvenementCalcule = montantEvenementCalcule;
        this.periodeFacturationDu = periodeFacturationDu;
        this.periodeFacturationAu = periodeFacturationAu;
        this.itemId = itemId;
        this.factureInitiale = factureInitiale;
    }

    // Getters et Setters
    public String getPcBu() { return pcBu; }
    public void setPcBu(String pcBu) { this.pcBu = pcBu; }
    public String getProjet() { return projet; }
    public void setProjet(String projet) { this.projet = projet; }
    public String getActivite() { return activite; }
    public void setActivite(String activite) { this.activite = activite; }
    public int getNombreFactures() { return nombreFactures; }
    public void setNombreFactures(int nombreFactures) { this.nombreFactures = nombreFactures; }
    public String getNoteEvenement() { return noteEvenement; }
    public void setNoteEvenement(String noteEvenement) { this.noteEvenement = noteEvenement; }
    public double getQuantite() { return quantite; }
    public void setQuantite(double quantite) { this.quantite = quantite; }
    public String getUniteMesure() { return uniteMesure; }
    public void setUniteMesure(String uniteMesure) { this.uniteMesure = uniteMesure; }
    public double getPrixUnitaire() { return prixUnitaire; }
    public void setPrixUnitaire(double prixUnitaire) { this.prixUnitaire = prixUnitaire; }
    public double getMontantFacturation() { return montantFacturation; }
    public void setMontantFacturation(double montantFacturation) { this.montantFacturation = montantFacturation; }
    public double getMontantEvenementCalcule() { return montantEvenementCalcule; }
    public void setMontantEvenementCalcule(double montantEvenementCalcule) { this.montantEvenementCalcule = montantEvenementCalcule; }
    public LocalDate getPeriodeFacturationDu() { return periodeFacturationDu; }
    public void setPeriodeFacturationDu(LocalDate periodeFacturationDu) { this.periodeFacturationDu = periodeFacturationDu; }
    public LocalDate getPeriodeFacturationAu() { return periodeFacturationAu; }
    public void setPeriodeFacturationAu(LocalDate periodeFacturationAu) { this.periodeFacturationAu = periodeFacturationAu; }
    public String getItemId() { return itemId; }
    public void setItemId(String itemId) { this.itemId = itemId; }
    public String getFactureInitiale() { return factureInitiale; }
    public void setFactureInitiale(String factureInitiale) { this.factureInitiale = factureInitiale; }

    @Override
    public String toString() {
        return "NavetteFacturation{" +
                "pcBu='" + pcBu + '\'' +
                ", projet='" + projet + '\'' +
                ", activite='" + activite + '\'' +
                ", nombreFactures=" + nombreFactures +
                ", noteEvenement='" + noteEvenement + '\'' +
                ", quantite=" + quantite +
                ", uniteMesure='" + uniteMesure + '\'' +
                ", prixUnitaire=" + prixUnitaire +
                ", montantFacturation=" + montantFacturation +
                ", montantEvenementCalcule=" + montantEvenementCalcule +
                ", periodeFacturationDu='" + periodeFacturationDu + '\'' +
                ", periodeFacturationAu='" + periodeFacturationAu + '\'' +
                ", itemId='" + itemId + '\'' +
                ", factureInitiale='" + factureInitiale + '\'' +
                '}';
    }
}
