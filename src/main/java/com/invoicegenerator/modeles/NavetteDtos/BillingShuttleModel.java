package com.invoicegenerator.modeles.NavetteDtos;

import com.invoicegenerator.utils.backend.LoggerFactory;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Représente le modèle de navette de facturation qui contient diverses informations sur le processus de facturation.
 */
public class BillingShuttleModel {
    private static final Logger logger = LoggerFactory.getLogger(BillingShuttleModel.class.getName());

    private BillingDetailsModel billingDetails = new BillingDetailsModel();
    private EventDetailsModel eventDetails = new EventDetailsModel();
    private ItemDetailsModel itemDetails = new ItemDetailsModel();

    /**
     * Constructeur vide pour BillingShuttleModel.
     */
    public BillingShuttleModel() {
        logger.log(Level.INFO, "Création d''une instance vide de BillingShuttleModel");
    }

    /**
     * Constructeur paramétré pour BillingShuttleModel.
     *
     * @param billingDetails Les détails de la facturation.
     * @param eventDetails Les détails de l'événement.
     * @param itemDetails Les détails de l'élément.
     */
    public BillingShuttleModel(BillingDetailsModel billingDetails, EventDetailsModel eventDetails, ItemDetailsModel itemDetails) {
        this.billingDetails = billingDetails;
        this.eventDetails = eventDetails;
        this.itemDetails = itemDetails;
        logger.log(Level.INFO, "Création d''une instance de BillingShuttleModel avec paramètres personnalisés");
    }

    /**
     * Obtient les détails de la facturation.
     *
     * @return Les détails de la facturation.
     */
    public BillingDetailsModel getBillingDetails() {
        return billingDetails;
    }

    /**
     * Définit les détails de la facturation.
     *
     * @param billingDetails Les détails de la facturation.
     * @return L'instance actuelle de BillingShuttleModel.
     */
    public BillingShuttleModel setBillingDetails(BillingDetailsModel billingDetails) {
        this.billingDetails = billingDetails;
        return this;
    }

    /**
     * Obtient les détails de l'événement.
     *
     * @return Les détails de l'événement.
     */
    public EventDetailsModel getEventDetails() {
        return eventDetails;
    }

    /**
     * Définit les détails de l'événement.
     *
     * @param eventDetails Les détails de l'événement.
     * @return L'instance actuelle de BillingShuttleModel.
     */
    public BillingShuttleModel setEventDetails(EventDetailsModel eventDetails) {
        this.eventDetails = eventDetails;
        return this;
    }

    /**
     * Obtient les détails de l'élément.
     *
     * @return Les détails de l'élément.
     */
    public ItemDetailsModel getItemDetails() {
        return itemDetails;
    }

    /**
     * Définit les détails de l'élément.
     *
     * @param itemDetails Les détails de l'élément.
     * @return L'instance actuelle de BillingShuttleModel.
     */
    public BillingShuttleModel setItemDetails(ItemDetailsModel itemDetails) {
        this.itemDetails = itemDetails;
        return this;
    }
}