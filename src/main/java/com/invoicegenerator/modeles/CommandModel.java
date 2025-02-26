package com.invoicegenerator.modeles;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the command model which contains details about the command period, contract code,
 * activity code, and a list of command lines.
 */
public class CommandModel {
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private String contractCode;
    private String activityCode;
    private List<UoCommandLineModel> commandLines = new ArrayList<>();
    private static String orderFormPrefix = "BDC PO";
    private String orderForm;
    private String benefitPurposeSuffix;

    /**
     * Gets the start date of the command.
     *
     * @return The start date of the command.
     */
    public LocalDate getDateDebut() {
        return dateDebut;
    }

    /**
     * Sets the start date of the command.
     *
     * @param dateDebut The start date to set.
     */
    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    /**
     * Gets the end date of the command.
     *
     * @return The end date of the command.
     */
    public LocalDate getDateFin() {
        return dateFin;
    }

    /**
     * Sets the end date of the command.
     *
     * @param dateFin The end date to set.
     */
    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    /**
     * Gets the contract code.
     *
     * @return The contract code.
     */
    public String getContractCode() {
        return contractCode;
    }

    /**
     * Sets the contract code.
     *
     * @param contractCode The contract code to set.
     */
    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    /**
     * Gets the activity code.
     *
     * @return The activity code.
     */
    public String getActivityCode() {
        return activityCode;
    }

    /**
     * Sets the activity code.
     *
     * @param activityCode The activity code to set.
     */
    public void setActivityCode(String activityCode) {
        this.activityCode = activityCode;
    }

    /**
     * Gets the list of command lines.
     *
     * @return A list of UoCommandLineModel.
     */
    public List<UoCommandLineModel> getCommandLines() {
        return commandLines;
    }

    /**
     * Sets the list of command lines.
     *
     * @param commandLines The list of command lines to set.
     */
    public void setCommandLines(List<UoCommandLineModel> commandLines) {
        this.commandLines = commandLines;
    }

    /**
     * Gets the order form.
     *
     * @return The order form.
     */
    public String getOrderForm() {
        return orderForm;
    }

    /**
     * Sets the order form.
     *
     * @param orderForm The order form to set.
     */
    public void setOrderForm(String orderForm) {
        this.orderForm = orderForm;
    }

    /**
     * Gets the object of the service.
     *
     * @return The object of the service.
     */
    public String getBenefitPurpose() {
        return orderFormPrefix + benefitPurposeSuffix;
    }

    /**
     * Sets the suffix for the object of the service.
     *
     * @param benefitPurposeSuffix The suffix to set.
     */
    public void setBenefitPurposeSuffix(String benefitPurposeSuffix) {
        this.benefitPurposeSuffix = benefitPurposeSuffix;
    }
}