package com.invoicegenerator.modeles.navettedtos;

/**
 * Modèle pour les détails des événements de facturation.
 */
public class EventDetailsModel {
    private String eventNote;
    private double calculatedEventAmount;

    /**
     * Constructeur vide pour EventDetailsModel.
     */
    public EventDetailsModel() {
    }

    /**
     * Constructeur paramétré pour EventDetailsModel.
     *
     * @param eventNote La note de l'événement.
     * @param calculatedEventAmount Le montant calculé de l'événement.
     */
    public EventDetailsModel(String eventNote, double calculatedEventAmount) {
        this.eventNote = eventNote;
        this.calculatedEventAmount = calculatedEventAmount;
    }

    /**
     * Obtient la note de l'événement.
     *
     * @return La note de l'événement.
     */
    public String getEventNote() {
        return eventNote;
    }

    /**
     * Définit la note de l'événement.
     *
     * @param eventNote La note de l'événement.
     * @return L'instance actuelle de EventDetailsModel.
     */
    public EventDetailsModel setEventNote(String eventNote) {
        this.eventNote = eventNote;
        return this;
    }

    /**
     * Obtient le montant calculé de l'événement.
     *
     * @return Le montant calculé de l'événement.
     */
    public double getCalculatedEventAmount() {
        return calculatedEventAmount;
    }

    /**
     * Définit le montant calculé de l'événement.
     *
     * @param calculatedEventAmount Le montant calculé de l'événement.
     * @return L'instance actuelle de EventDetailsModel.
     */
    public EventDetailsModel setCalculatedEventAmount(double calculatedEventAmount) {
        this.calculatedEventAmount = calculatedEventAmount;
        return this;
    }
}