package com.invoicegenerator.modeles;

import java.time.LocalDate;

/**
 * Represents the billing shuttle model which contains various details about the billing process.
 */
public class BillingShuttleModel {
    private String pcBu;
    private String project;
    private String activity;
    private int billNumber;
    private String eventNote;
    private double quantity;
    private String measureUnit;
    private double unitPrice;
    private double billAmount;
    private double calculatedEventAmount;
    private LocalDate billPeriodFrom;
    private LocalDate billPeriodTo;
    private String itemId;
    private String initialBill;

    /**
     * Default constructor for BillingShuttleModel.
     */
    public BillingShuttleModel() {}

    /**
     * Parameterized constructor for BillingShuttleModel.
     *
     * @param pcBu The PC BU.
     * @param project The project name.
     * @param activity The activity name.
     * @param billNumber The number of invoices.
     * @param eventNote The event note.
     * @param quantity The quantity.
     * @param measureUnit The unit of measure.
     * @param unitPrice The unit price.
     * @param billAmount The billing amount.
     * @param calculatedEventAmount The calculated event amount.
     * @param billPeriodFrom The billing period start date.
     * @param billPeriodTo The billing period end date.
     * @param itemId The item ID.
     * @param initialBill The initial invoice.
     */
    public BillingShuttleModel(String pcBu, String project, String activity, int billNumber, String eventNote,
                               double quantity, String measureUnit, double unitPrice, double billAmount,
                               double calculatedEventAmount, LocalDate billPeriodFrom, LocalDate billPeriodTo,
                               String itemId, String initialBill) {
        this.pcBu = pcBu;
        this.project = project;
        this.activity = activity;
        this.billNumber = billNumber;
        this.eventNote = eventNote;
        this.quantity = quantity;
        this.measureUnit = measureUnit;
        this.unitPrice = unitPrice;
        this.billAmount = billAmount;
        this.calculatedEventAmount = calculatedEventAmount;
        this.billPeriodFrom = billPeriodFrom;
        this.billPeriodTo = billPeriodTo;
        this.itemId = itemId;
        this.initialBill = initialBill;
    }

    // Getters and Setters

    /**
     * Gets the PC BU.
     *
     * @return The PC BU.
     */
    public String getPcBu() { return pcBu; }

    /**
     * Sets the PC BU.
     *
     * @param pcBu The PC BU to set.
     */
    public void setPcBu(String pcBu) { this.pcBu = pcBu; }

    /**
     * Gets the project name.
     *
     * @return The project name.
     */
    public String getProject() { return project; }

    /**
     * Sets the project name.
     *
     * @param project The project name to set.
     */
    public void setProject(String project) { this.project = project; }

    /**
     * Gets the activity name.
     *
     * @return The activity name.
     */
    public String getActivity() { return activity; }

    /**
     * Sets the activity name.
     *
     * @param activity The activity name to set.
     */
    public void setActivity(String activity) { this.activity = activity; }

    /**
     * Gets the number of invoices.
     *
     * @return The number of invoices.
     */
    public int getBillNumber() { return billNumber; }

    /**
     * Sets the number of invoices.
     *
     * @param billNumber The number of invoices to set.
     */
    public void setBillNumber(int billNumber) { this.billNumber = billNumber; }

    /**
     * Gets the event note.
     *
     * @return The event note.
     */
    public String getEventNote() { return eventNote; }

    /**
     * Sets the event note.
     *
     * @param eventNote The event note to set.
     */
    public void setEventNote(String eventNote) { this.eventNote = eventNote; }

    /**
     * Gets the quantity.
     *
     * @return The quantity.
     */
    public double getQuantity() { return quantity; }

    /**
     * Sets the quantity.
     *
     * @param quantity The quantity to set.
     */
    public void setQuantity(double quantity) { this.quantity = quantity; }

    /**
     * Gets the unit of measure.
     *
     * @return The unit of measure.
     */
    public String getMeasureUnit() { return measureUnit; }

    /**
     * Sets the unit of measure.
     *
     * @param measureUnit The unit of measure to set.
     */
    public void setMeasureUnit(String measureUnit) { this.measureUnit = measureUnit; }

    /**
     * Gets the unit price.
     *
     * @return The unit price.
     */
    public double getUnitPrice() { return unitPrice; }

    /**
     * Sets the unit price.
     *
     * @param unitPrice The unit price to set.
     */
    public void setUnitPrice(double unitPrice) { this.unitPrice = unitPrice; }

    /**
     * Gets the billing amount.
     *
     * @return The billing amount.
     */
    public double getBillAmount() { return billAmount; }

    /**
     * Sets the billing amount.
     *
     * @param billAmount The billing amount to set.
     */
    public void setBillAmount(double billAmount) { this.billAmount = billAmount; }

    /**
     * Gets the calculated event amount.
     *
     * @return The calculated event amount.
     */
    public double getCalculatedEventAmount() { return calculatedEventAmount; }

    /**
     * Sets the calculated event amount.
     *
     * @param calculatedEventAmount The calculated event amount to set.
     */
    public void setCalculatedEventAmount(double calculatedEventAmount) { this.calculatedEventAmount = calculatedEventAmount; }

    /**
     * Gets the billing period start date.
     *
     * @return The billing period start date.
     */
    public LocalDate getBillPeriodFrom() { return billPeriodFrom; }

    /**
     * Sets the billing period start date.
     *
     * @param billPeriodFrom The billing period start date to set.
     */
    public void setBillPeriodFrom(LocalDate billPeriodFrom) { this.billPeriodFrom = billPeriodFrom; }

    /**
     * Gets the billing period end date.
     *
     * @return The billing period end date.
     */
    public LocalDate getBillPeriodTo() { return billPeriodTo; }

    /**
     * Sets the billing period end date.
     *
     * @param billPeriodTo The billing period end date to set.
     */
    public void setBillPeriodTo(LocalDate billPeriodTo) { this.billPeriodTo = billPeriodTo; }

    /**
     * Gets the item ID.
     *
     * @return The item ID.
     */
    public String getItemId() { return itemId; }

    /**
     * Sets the item ID.
     *
     * @param itemId The item ID to set.
     */
    public void setItemId(String itemId) { this.itemId = itemId; }

    /**
     * Gets the initial invoice.
     *
     * @return The initial invoice.
     */
    public String getInitialBill() { return initialBill; }

    /**
     * Sets the initial invoice.
     *
     * @param initialBill The initial invoice to set.
     */
    public void setInitialBill(String initialBill) { this.initialBill = initialBill; }

    /**
     * Returns a string representation of the BillingShuttleModel.
     *
     * @return A string representation of the BillingShuttleModel.
     */
    @Override
    public String toString() {
        return "NavetteFacturation{" +
                "pcBu='" + pcBu + '\'' +
                ", projet='" + project + '\'' +
                ", activite='" + activity + '\'' +
                ", nombreFactures=" + billNumber +
                ", noteEvenement='" + eventNote + '\'' +
                ", quantite=" + quantity +
                ", uniteMesure='" + measureUnit + '\'' +
                ", prixUnitaire=" + unitPrice +
                ", montantFacturation=" + billAmount +
                ", montantEvenementCalcule=" + calculatedEventAmount +
                ", periodeFacturationDu='" + billPeriodFrom + '\'' +
                ", periodeFacturationAu='" + billPeriodTo + '\'' +
                ", itemId='" + itemId + '\'' +
                ", factureInitiale='" + initialBill + '\'' +
                '}';
    }
}