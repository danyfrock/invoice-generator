package com.invoicegenerator.modeles;

import com.invoicegenerator.utils.backend.LoggerFactory;

import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Représente le modèle d'unité de montant qui contient les détails sur le montant total,
 * le prix unitaire, la quantité et la taxe.
 */
public class UoAmountModel {
    private static final Logger logger = LoggerFactory.getLogger(UoAmountModel.class.getName());

    private double totalHT;
    private double totalTTC;
    private int number;
    private double unitPrice;
    private double tva;

    /**
     * Constructeur par défaut pour UoAmountModel.
     * Initialise les valeurs à zéro par défaut.
     */
    public UoAmountModel() {
        logger.log(Level.INFO, "Création d'une nouvelle instance de UoAmountModel avec valeurs par défaut");
    }

    /**
     * Récupère la quantité.
     *
     * @return La quantité.
     */
    public int getNumber() {
        logger.log(Level.FINE, "Récupération de la quantité : {0}", number);
        return number;
    }

    /**
     * Récupère le montant total hors taxes.
     *
     * @return Le montant total hors taxes.
     */
    public double getTotalHT() {
        logger.log(Level.FINE, "Récupération du montant total HT : {0}", totalHT);
        return totalHT;
    }

    /**
     * Récupère le montant total toutes taxes comprises.
     *
     * @return Le montant total TTC.
     */
    public double getTotalTTC() {
        logger.log(Level.FINE, "Récupération du montant total TTC : {0}", totalTTC);
        return totalTTC;
    }

    /**
     * Définit la quantité et recalcule le montant total toutes taxes comprises.
     *
     * @param number La quantité à définir.
     */
    public void setNumber(int number) {
        this.number = number;
        logger.log(Level.FINE, "Quantité définie : {0}", number);
        calculateTTC();
    }

    /**
     * Définit le prix unitaire et recalcule le montant total toutes taxes comprises.
     *
     * @param unitPrice Le prix unitaire à définir.
     */
    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
        logger.log(Level.FINE, "Prix unitaire défini : {0}", unitPrice);
        calculateTTC();
    }

    /**
     * Définit le taux de taxe et recalcule le montant total toutes taxes comprises.
     *
     * @param tva Le taux de taxe à définir.
     */
    public void setTva(double tva) {
        this.tva = tva;
        logger.log(Level.FINE, "Taux de TVA défini : {0}", tva);
        calculateTTC();
    }

    /**
     * Recalcule le montant total toutes taxes comprises en fonction de la quantité,
     * du prix unitaire et du taux de taxe actuels.
     */
    private void calculateTTC() {
        this.totalHT = this.number * this.unitPrice;
        this.totalTTC = this.totalHT * (1 + this.tva);
        logger.log(Level.FINE, "Recalcul des montants - Total HT : {0}, Total TTC : {1}", new Object[]{totalHT, totalTTC});
    }
}