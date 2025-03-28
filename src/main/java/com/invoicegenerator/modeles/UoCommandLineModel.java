package com.invoicegenerator.modeles;

/**
 * Représente le modèle de ligne de commande d'unité de commande qui contient des détails sur l'unité de commande,
 * le prix unitaire, la quantité et divers montants.
 */
public class UoCommandLineModel {
    private String commandLabel;
    private String uoType;
    private double unitPrice;
    private int uoNumber;
    private final UoAmountModel uoTotal = new UoAmountModel();
    private final UoAmountModel uoCost = new UoAmountModel();
    private final UoAmountModel uoToSpend = new UoAmountModel();

    /**
     * Obtient l'étiquette.
     *
     * @return L'étiquette.
     */
    public String getCommandLabel() {
        return commandLabel;
    }

    /**
     * Définit l'étiquette.
     *
     * @param commandLabel L'étiquette à définir.
     */
    public void setCommandLabel(String commandLabel) {
        this.commandLabel = commandLabel;
    }

    /**
     * Obtient le type d'unité de commande.
     *
     * @return Le type d'unité de commande.
     */
    public String getUoType() {
        return uoType;
    }

    /**
     * Définit le type d'unité de commande.
     *
     * @param uoType Le type d'unité de commande à définir.
     */
    public void setUoType(String uoType) {
        this.uoType = uoType;
    }

    /**
     * Obtient le prix unitaire.
     *
     * @return Le prix unitaire.
     */
    public double getUnitPrice() {
        return unitPrice;
    }

    /**
     * Définit le prix unitaire et met à jour le prix unitaire dans les modèles de montant associés.
     *
     * @param unitPrice Le prix unitaire à définir.
     */
    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
        this.uoCost.setUnitPrice(this.unitPrice);
        this.uoTotal.setUnitPrice(this.unitPrice);
        this.uoToSpend.setUnitPrice(this.unitPrice);
    }

    /**
     * Obtient la quantité d'unités de commande.
     *
     * @return La quantité d'unités de commande.
     */
    public int getUoNumber() {
        return uoNumber;
    }

    /**
     * Définit la quantité d'unités de commande.
     *
     * @param uoNumber La quantité d'unités de commande à définir.
     */
    public void setUoNumber(int uoNumber) {
        this.uoNumber = uoNumber;
    }

    /**
     * Obtient le modèle de montant total.
     *
     * @return Le modèle de montant total.
     */
    public UoAmountModel getUoTotal() {
        return uoTotal;
    }

    /**
     * Obtient le modèle de montant.
     *
     * @return Le modèle de montant.
     */
    public UoAmountModel getUoCost() {
        return uoCost;
    }

    /**
     * Obtient le modèle de montant restant à dépenser.
     *
     * @return Le modèle de montant restant à dépenser.
     */
    public UoAmountModel getUoToSpend() {
        return uoToSpend;
    }

    /**
     * Définit le taux de taxe et met à jour le taux de taxe dans les modèles de montant associés.
     *
     * @param tva Le taux de taxe à définir.
     */
    public void setTVA(double tva) {
        this.uoCost.setTva(tva);
        this.uoTotal.setTva(tva);
        this.uoToSpend.setTva(tva);
    }
}