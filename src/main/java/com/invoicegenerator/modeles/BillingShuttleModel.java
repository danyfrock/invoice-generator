package com.invoicegenerator.modeles;

import com.invoicegenerator.utils.backend.LoggerFactory;

import java.time.LocalDate;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Représente le modèle de navette de facturation qui contient diverses informations sur le processus de facturation.
 */
public class BillingShuttleModel {
    private static final Logger logger = LoggerFactory.getLogger(BillingShuttleModel.class.getName());

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
     * Constructeur par défaut pour BillingShuttleModel.
     */
    public BillingShuttleModel() {
        logger.log(Level.INFO, "Création d'une nouvelle instance de BillingShuttleModel avec valeurs par défaut");
    }

    /**
     * Constructeur paramétré pour BillingShuttleModel.
     *
     * @param pcBu Le PC BU.
     * @param project Le nom du projet.
     * @param activity Le nom de l'activité.
     * @param billNumber Le nombre de factures.
     * @param eventNote La note de l'événement.
     * @param quantity La quantité.
     * @param measureUnit L'unité de mesure.
     * @param unitPrice Le prix unitaire.
     * @param billAmount Le montant de la facturation.
     * @param calculatedEventAmount Le montant calculé de l'événement.
     * @param billPeriodFrom La date de début de la période de facturation.
     * @param billPeriodTo La date de fin de la période de facturation.
     * @param itemId L'identifiant de l'élément.
     * @param initialBill La facture initiale.
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
        logger.log(Level.INFO, "Création d'une instance de BillingShuttleModel avec paramètres personnalisés");
    }

    /**
     * Récupère le PC BU.
     *
     * @return Le PC BU.
     */
    public String getPcBu() {
        logger.log(Level.FINE, "Récupération du PC BU : {0}", pcBu);
        return pcBu;
    }

    /**
     * Définit le PC BU.
     *
     * @param pcBu Le PC BU à définir.
     */
    public void setPcBu(String pcBu) {
        this.pcBu = pcBu;
        logger.log(Level.FINE, "PC BU défini : {0}", pcBu);
    }

    /**
     * Récupère le nom du projet.
     *
     * @return Le nom du projet.
     */
    public String getProject() {
        logger.log(Level.FINE, "Récupération du nom du projet : {0}", project);
        return project;
    }

    /**
     * Définit le nom du projet.
     *
     * @param project Le nom du projet à définir.
     */
    public void setProject(String project) {
        this.project = project;
        logger.log(Level.FINE, "Nom du projet défini : {0}", project);
    }

    /**
     * Récupère le nom de l'activité.
     *
     * @return Le nom de l'activité.
     */
    public String getActivity() {
        logger.log(Level.FINE, "Récupération du nom de l'activité : {0}", activity);
        return activity;
    }

    /**
     * Définit le nom de l'activité.
     *
     * @param activity Le nom de l'activité à définir.
     */
    public void setActivity(String activity) {
        this.activity = activity;
        logger.log(Level.FINE, "Nom de l'activité défini : {0}", activity);
    }

    /**
     * Récupère le nombre de factures.
     *
     * @return Le nombre de factures.
     */
    public int getBillNumber() {
        logger.log(Level.FINE, "Récupération du nombre de factures : {0}", billNumber);
        return billNumber;
    }

    /**
     * Définit le nombre de factures.
     *
     * @param billNumber Le nombre de factures à définir.
     */
    public void setBillNumber(int billNumber) {
        this.billNumber = billNumber;
        logger.log(Level.FINE, "Nombre de factures défini : {0}", billNumber);
    }

    /**
     * Récupère la note de l'événement.
     *
     * @return La note de l'événement.
     */
    public String getEventNote() {
        logger.log(Level.FINE, "Récupération de la note de l'événement : {0}", eventNote);
        return eventNote;
    }

    /**
     * Définit la note de l'événement.
     *
     * @param eventNote La note de l'événement à définir.
     */
    public void setEventNote(String eventNote) {
        this.eventNote = eventNote;
        logger.log(Level.FINE, "Note de l'événement définie : {0}", eventNote);
    }

    /**
     * Récupère la quantité.
     *
     * @return La quantité.
     */
    public double getQuantity() {
        logger.log(Level.FINE, "Récupération de la quantité : {0}", quantity);
        return quantity;
    }

    /**
     * Définit la quantité.
     *
     * @param quantity La quantité à définir.
     */
    public void setQuantity(double quantity) {
        this.quantity = quantity;
        logger.log(Level.FINE, "Quantité définie : {0}", quantity);
    }

    /**
     * Récupère l'unité de mesure.
     *
     * @return L'unité de mesure.
     */
    public String getMeasureUnit() {
        logger.log(Level.FINE, "Récupération de l'unité de mesure : {0}", measureUnit);
        return measureUnit;
    }

    /**
     * Définit l'unité de mesure.
     *
     * @param measureUnit L'unité de mesure à définir.
     */
    public void setMeasureUnit(String measureUnit) {
        this.measureUnit = measureUnit;
        logger.log(Level.FINE, "Unité de mesure définie : {0}", measureUnit);
    }

    /**
     * Récupère le prix unitaire.
     *
     * @return Le prix unitaire.
     */
    public double getUnitPrice() {
        logger.log(Level.FINE, "Récupération du prix unitaire : {0}", unitPrice);
        return unitPrice;
    }

    /**
     * Définit le prix unitaire.
     *
     * @param unitPrice Le prix unitaire à définir.
     */
    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
        logger.log(Level.FINE, "Prix unitaire défini : {0}", unitPrice);
    }

    /**
     * Récupère le montant de la facturation.
     *
     * @return Le montant de la facturation.
     */
    public double getBillAmount() {
        logger.log(Level.FINE, "Récupération du montant de la facturation : {0}", billAmount);
        return billAmount;
    }

    /**
     * Définit le montant de la facturation.
     *
     * @param billAmount Le montant de la facturation à définir.
     */
    public void setBillAmount(double billAmount) {
        this.billAmount = billAmount;
        logger.log(Level.FINE, "Montant de la facturation défini : {0}", billAmount);
    }

    /**
     * Récupère le montant calculé de l'événement.
     *
     * @return Le montant calculé de l'événement.
     */
    public double getCalculatedEventAmount() {
        logger.log(Level.FINE, "Récupération du montant calculé de l'événement : {0}", calculatedEventAmount);
        return calculatedEventAmount;
    }

    /**
     * Définit le montant calculé de l'événement.
     *
     * @param calculatedEventAmount Le montant calculé de l'événement à définir.
     */
    public void setCalculatedEventAmount(double calculatedEventAmount) {
        this.calculatedEventAmount = calculatedEventAmount;
        logger.log(Level.FINE, "Montant calculé de l'événement défini : {0}", calculatedEventAmount);
    }

    /**
     * Récupère la date de début de la période de facturation.
     *
     * @return La date de début de la période de facturation.
     */
    public LocalDate getBillPeriodFrom() {
        logger.log(Level.FINE, "Récupération de la date de début de la période de facturation : {0}", billPeriodFrom);
        return billPeriodFrom;
    }

    /**
     * Définit la date de début de la période de facturation.
     *
     * @param billPeriodFrom La date de début de la période de facturation à définir.
     */
    public void setBillPeriodFrom(LocalDate billPeriodFrom) {
        this.billPeriodFrom = billPeriodFrom;
        logger.log(Level.FINE, "Date de début de la période de facturation définie : {0}", billPeriodFrom);
    }

    /**
     * Récupère la date de fin de la période de facturation.
     *
     * @return La date de fin de la période de facturation.
     */
    public LocalDate getBillPeriodTo() {
        logger.log(Level.FINE, "Récupération de la date de fin de la période de facturation : {0}", billPeriodTo);
        return billPeriodTo;
    }

    /**
     * Définit la date de fin de la période de facturation.
     *
     * @param billPeriodTo La date de fin de la période de facturation à définir.
     */
    public void setBillPeriodTo(LocalDate billPeriodTo) {
        this.billPeriodTo = billPeriodTo;
        logger.log(Level.FINE, "Date de fin de la période de facturation définie : {0}", billPeriodTo);
    }

    /**
     * Récupère l'identifiant de l'élément.
     *
     * @return L'identifiant de l'élément.
     */
    public String getItemId() {
        logger.log(Level.FINE, "Récupération de l'identifiant de l'élément : {0}", itemId);
        return itemId;
    }

    /**
     * Définit l'identifiant de l'élément.
     *
     * @param itemId L'identifiant de l'élément à définir.
     */
    public void setItemId(String itemId) {
        this.itemId = itemId;
        logger.log(Level.FINE, "Identifiant de l'élément défini : {0}", itemId);
    }

    /**
     * Récupère la facture initiale.
     *
     * @return La facture initiale.
     */
    public String getInitialBill() {
        logger.log(Level.FINE, "Récupération de la facture initiale : {0}", initialBill);
        return initialBill;
    }

    /**
     * Définit la facture initiale.
     *
     * @param initialBill La facture initiale à définir.
     */
    public void setInitialBill(String initialBill) {
        this.initialBill = initialBill;
        logger.log(Level.FINE, "Facture initiale définie : {0}", initialBill);
    }

    /**
     * Retourne une représentation sous forme de chaîne du BillingShuttleModel.
     *
     * @return Une représentation sous forme de chaîne du BillingShuttleModel.
     */
    @Override
    public String toString() {
        String result = "NavetteFacturation{" +
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
        logger.log(Level.FINE, "Génération de la représentation textuelle : {0}", result);
        return result;
    }
}