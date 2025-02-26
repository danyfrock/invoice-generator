package com.invoicegenerator.modeles;

/**
 * Represents the unit of amount model which contains details about the total amount,
 * unit price, quantity, and tax.
 */
public class UoAmountModel {
    private double totalHT;
    private double totalTTC;
    private int number;
    private double unitPrice;
    private double tva;

    /**
     * Gets the quantity.
     *
     * @return The quantity.
     */
    public int getNumber() {
        return number;
    }

    /**
     * Gets the total amount excluding tax.
     *
     * @return The total amount excluding tax.
     */
    public double getTotalHT() {
        return totalHT;
    }

    /**
     * Gets the total amount including tax.
     *
     * @return The total amount including tax.
     */
    public double getTotalTTC() {
        return totalTTC;
    }

    /**
     * Sets the quantity and recalculates the total amount including tax.
     *
     * @param number The quantity to set.
     */
    public void setNumber(int number) {
        this.number = number;
        calculateTTC();
    }

    /**
     * Sets the unit price and recalculates the total amount including tax.
     *
     * @param unitPrice The unit price to set.
     */
    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
        calculateTTC();
    }

    /**
     * Sets the tax rate and recalculates the total amount including tax.
     *
     * @param tva The tax rate to set.
     */
    public void setTva(double tva) {
        this.tva = tva;
        calculateTTC();
    }

    /**
     * Recalculates the total amount including tax based on the current quantity, unit price, and tax rate.
     */
    private void calculateTTC() {
        this.totalHT = this.number * this.unitPrice;
        this.totalTTC = this.totalHT * (1 + this.tva);
    }
}