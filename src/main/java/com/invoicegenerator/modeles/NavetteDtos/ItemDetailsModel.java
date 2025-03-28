package com.invoicegenerator.modeles.NavetteDtos;

/**
 * Modèle pour les détails des éléments de facturation.
 */
public class ItemDetailsModel {
    private String pcBu;
    private String project;
    private String activity;
    private double quantity;
    private String measureUnit;
    private double unitPrice;
    private String itemId;

    /**
     * Constructeur vide pour ItemDetailsModel.
     */
    public ItemDetailsModel() {
    }

    /**
     * Constructeur paramétré pour ItemDetailsModel.
     *
     * @param pcBu Le PC BU.
     * @param project Le nom du projet.
     * @param activity Le nom de l'activité.
     * @param quantity La quantité.
     * @param measureUnit L'unité de mesure.
     * @param unitPrice Le prix unitaire.
     * @param itemId L'identifiant de l'élément.
     */
    public ItemDetailsModel(String pcBu, String project, String activity, double quantity, String measureUnit, double unitPrice, String itemId) {
        this.pcBu = pcBu;
        this.project = project;
        this.activity = activity;
        this.quantity = quantity;
        this.measureUnit = measureUnit;
        this.unitPrice = unitPrice;
        this.itemId = itemId;
    }

    /**
     * Obtient le PC BU.
     *
     * @return Le PC BU.
     */
    public String getPcBu() {
        return pcBu;
    }

    /**
     * Définit le PC BU.
     *
     * @param pcBu Le PC BU.
     * @return L'instance actuelle de ItemDetailsModel.
     */
    public ItemDetailsModel setPcBu(String pcBu) {
        this.pcBu = pcBu;
        return this;
    }

    /**
     * Obtient le nom du projet.
     *
     * @return Le nom du projet.
     */
    public String getProject() {
        return project;
    }

    /**
     * Définit le nom du projet.
     *
     * @param project Le nom du projet.
     * @return L'instance actuelle de ItemDetailsModel.
     */
    public ItemDetailsModel setProject(String project) {
        this.project = project;
        return this;
    }

    /**
     * Obtient le nom de l'activité.
     *
     * @return Le nom de l'activité.
     */
    public String getActivity() {
        return activity;
    }

    /**
     * Définit le nom de l'activité.
     *
     * @param activity Le nom de l'activité.
     * @return L'instance actuelle de ItemDetailsModel.
     */
    public ItemDetailsModel setActivity(String activity) {
        this.activity = activity;
        return this;
    }

    /**
     * Obtient la quantité.
     *
     * @return La quantité.
     */
    public double getQuantity() {
        return quantity;
    }

    /**
     * Définit la quantité.
     *
     * @param quantity La quantité.
     * @return L'instance actuelle de ItemDetailsModel.
     */
    public ItemDetailsModel setQuantity(double quantity) {
        this.quantity = quantity;
        return this;
    }

    /**
     * Obtient l'unité de mesure.
     *
     * @return L'unité de mesure.
     */
    public String getMeasureUnit() {
        return measureUnit;
    }

    /**
     * Définit l'unité de mesure.
     *
     * @param measureUnit L'unité de mesure.
     * @return L'instance actuelle de ItemDetailsModel.
     */
    public ItemDetailsModel setMeasureUnit(String measureUnit) {
        this.measureUnit = measureUnit;
        return this;
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
     * Définit le prix unitaire.
     *
     * @param unitPrice Le prix unitaire.
     * @return L'instance actuelle de ItemDetailsModel.
     */
    public ItemDetailsModel setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
        return this;
    }

    /**
     * Obtient l'identifiant de l'élément.
     *
     * @return L'identifiant de l'élément.
     */
    public String getItemId() {
        return itemId;
    }

    /**
     * Définit l'identifiant de l'élément.
     *
     * @param itemId L'identifiant de l'élément.
     * @return L'instance actuelle de ItemDetailsModel.
     */
    public ItemDetailsModel setItemId(String itemId) {
        this.itemId = itemId;
        return this;
    }
}