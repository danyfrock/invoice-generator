package com.invoicegenerator.modeles.navetteDtos;

import java.time.LocalDate;

/**
 * Modèle pour les détails de facturation.
 */
public class BillingDetailsModel {
    private int billNumber;
    private double billAmount;
    private LocalDate billPeriodFrom;
    private LocalDate billPeriodTo;
    private String initialBill;

    /**
     * Constructeur vide pour BillingDetailsModel.
     */
    public BillingDetailsModel() {
    }


    /**
     * Constructeur paramétré pour BillingDetailsModel.
     *
     * @param billNumber Le numéro de la facture.
     * @param billAmount Le montant de la facture.
     * @param billPeriodFrom La date de début de la période de facturation.
     * @param billPeriodTo La date de fin de la période de facturation.
     * @param initialBill La facture initiale.
     */
    public BillingDetailsModel(int billNumber, double billAmount, LocalDate billPeriodFrom, LocalDate billPeriodTo, String initialBill) {
        this.billNumber = billNumber;
        this.billAmount = billAmount;
        this.billPeriodFrom = billPeriodFrom;
        this.billPeriodTo = billPeriodTo;
        this.initialBill = initialBill;
    }

    /**
     * Obtient le numéro de la facture.
     *
     * @return Le numéro de la facture.
     */
    public int getBillNumber() {
        return billNumber;
    }

    /**
     * Définit le numéro de la facture.
     *
     * @param billNumber Le numéro de la facture.
     * @return L'instance actuelle de BillingDetailsModel.
     */
    public BillingDetailsModel setBillNumber(int billNumber) {
        this.billNumber = billNumber;
        return this;
    }

    /**
     * Obtient le montant de la facture.
     *
     * @return Le montant de la facture.
     */
    public double getBillAmount() {
        return billAmount;
    }

    /**
     * Définit le montant de la facture.
     *
     * @param billAmount Le montant de la facture.
     * @return L'instance actuelle de BillingDetailsModel.
     */
    public BillingDetailsModel setBillAmount(double billAmount) {
        this.billAmount = billAmount;
        return this;
    }

    /**
     * Obtient la date de début de la période de facturation.
     *
     * @return La date de début de la période de facturation.
     */
    public LocalDate getBillPeriodFrom() {
        return billPeriodFrom;
    }

    /**
     * Définit la date de début de la période de facturation.
     *
     * @param billPeriodFrom La date de début de la période de facturation.
     * @return L'instance actuelle de BillingDetailsModel.
     */
    public BillingDetailsModel setBillPeriodFrom(LocalDate billPeriodFrom) {
        this.billPeriodFrom = billPeriodFrom;
        return this;
    }

    /**
     * Obtient la date de fin de la période de facturation.
     *
     * @return La date de fin de la période de facturation.
     */
    public LocalDate getBillPeriodTo() {
        return billPeriodTo;
    }

    /**
     * Définit la date de fin de la période de facturation.
     *
     * @param billPeriodTo La date de fin de la période de facturation.
     * @return L'instance actuelle de BillingDetailsModel.
     */
    public BillingDetailsModel setBillPeriodTo(LocalDate billPeriodTo) {
        this.billPeriodTo = billPeriodTo;
        return this;
    }

    /**
     * Obtient la facture initiale.
     *
     * @return La facture initiale.
     */
    public String getInitialBill() {
        return initialBill;
    }

    /**
     * Définit la facture initiale.
     *
     * @param initialBill La facture initiale.
     * @return L'instance actuelle de BillingDetailsModel.
     */
    public BillingDetailsModel setInitialBill(String initialBill) {
        this.initialBill = initialBill;
        return this;
    }
}