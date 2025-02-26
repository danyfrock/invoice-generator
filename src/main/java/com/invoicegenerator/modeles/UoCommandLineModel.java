package com.invoicegenerator.modeles;

/**
 * Represents the unit of order command line model which contains details about the unit of order,
 * unit price, quantity, and various amounts.
 */
public class UoCommandLineModel {
    private String commandLabel;
    private String uoType;
    private double unitPrice;
    private int uoNumber;
    private final UoAmountModel uoTotal = new UoAmountModel();
    private final UoAmountModel uoCost = new UoAmountModel();
    private UoAmountModel uoToSpend = new UoAmountModel();

    /**
     * Gets the label.
     *
     * @return The label.
     */
    public String getCommandLabel() {
        return commandLabel;
    }

    /**
     * Sets the label.
     *
     * @param commandLabel The label to set.
     */
    public void setCommandLabel(String commandLabel) {
        this.commandLabel = commandLabel;
    }

    /**
     * Gets the type of unit of order.
     *
     * @return The type of unit of order.
     */
    public String getUoType() {
        return uoType;
    }

    /**
     * Sets the type of unit of order.
     *
     * @param uoType The type of unit of order to set.
     */
    public void setUoType(String uoType) {
        this.uoType = uoType;
    }

    /**
     * Gets the unit price.
     *
     * @return The unit price.
     */
    public double getUnitPrice() {
        return unitPrice;
    }

    /**
     * Sets the unit price and updates the unit price in related amount models.
     *
     * @param unitPrice The unit price to set.
     */
    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
        this.uoCost.setUnitPrice(this.unitPrice);
        this.uoTotal.setUnitPrice(this.unitPrice);
        this.uoToSpend.setUnitPrice(this.unitPrice);
    }

    /**
     * Gets the quantity of units of order.
     *
     * @return The quantity of units of order.
     */
    public int getUoNumber() {
        return uoNumber;
    }

    /**
     * Sets the quantity of units of order.
     *
     * @param uoNumber The quantity of units of order to set.
     */
    public void setUoNumber(int uoNumber) {
        this.uoNumber = uoNumber;
    }

    /**
     * Gets the total amount model.
     *
     * @return The total amount model.
     */
    public UoAmountModel getUoTotal() {
        return uoTotal;
    }

    /**
     * Gets the amount model.
     *
     * @return The amount model.
     */
    public UoAmountModel getUoCost() {
        return uoCost;
    }

    /**
     * Gets the remaining amount to be spent model.
     *
     * @return The remaining amount to be spent model.
     */
    public UoAmountModel getUoToSpend() {
        return uoToSpend;
    }

    /**
     * Sets the tax rate and updates the tax rate in related amount models.
     *
     * @param tva The tax rate to set.
     */
    public void setTVA(double tva) {
        this.uoCost.setTva(tva);
        this.uoTotal.setTva(tva);
        this.uoToSpend.setTva(tva);
    }
}