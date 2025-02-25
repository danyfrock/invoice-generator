package com.invoicegenerator.modeles;

public class UoCommandLineModel {
    private String libelle;
    private String typeUO;
    private double prixUnitaire;
    private int nombreUO;
    private UoAmountModel totalPV = new UoAmountModel();
    private UoAmountModel montantPV = new UoAmountModel();
    private UoAmountModel resteADepenserPV = new UoAmountModel();

    // Getters et setters
    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getTypeUO() {
        return typeUO;
    }

    public void setTypeUO(String typeUO) {
        this.typeUO = typeUO;
    }

    public double getPrixUnitaire() {
        return prixUnitaire;
    }

    public void setPrixUnitaire(double prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
        this.montantPV.setPrixUnitaire(this.prixUnitaire);
        this.totalPV.setPrixUnitaire(this.prixUnitaire);
        this.resteADepenserPV.setPrixUnitaire(this.prixUnitaire);
    }

    public int getNombreUO() {
        return nombreUO;
    }

    public void setNombreUO(int nombreUO) {
        this.nombreUO = nombreUO;
    }

    public UoAmountModel getTotalPV() {
        return totalPV;
    }

    public UoAmountModel getMontantPV() {
        return montantPV;
    }

    public UoAmountModel getResteADepenserPV() {
        return resteADepenserPV;
    }

    public void setTVA(double tva) {
        this.montantPV.setTva(tva);
        this.totalPV.setTva(tva);
        this.resteADepenserPV.setTva(tva);
    }
}