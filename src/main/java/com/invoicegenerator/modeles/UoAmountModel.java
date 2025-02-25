package com.invoicegenerator.modeles;

public class UoAmountModel {
    private double totalHT;
    private double totalTTC;
    private int nombre;
    private double prixUnitaire;
    private double tva;

    public int getNombre() {
        return nombre;
    }

    public double getTotalHT() {
        return totalHT;
    }

    public double getTotalTTC() {
        return totalTTC;
    }

    public void setNombre(int nombre) {
        this.nombre = nombre;
        calculerTotalTTC();
    }

    public void setPrixUnitaire(double prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
        calculerTotalTTC();
    }

    public void setTva(double tva) {
        this.tva = tva;
        calculerTotalTTC();
    }

    private void calculerTotalTTC() {
        this.totalHT = this.nombre * this.prixUnitaire;
        this.totalTTC = this.totalHT * (1 + this.tva);
    }
}