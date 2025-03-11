package com.invoicegenerator.viewModels;

import com.invoicegenerator.modeles.BillingShuttleModel;
import com.invoicegenerator.services.MoisService;
import com.invoicegenerator.utils.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * ViewModel pour la gestion des données de navette de facturation dans l'interface utilisateur.
 * Cette classe encapsule les données du modèle BillingShuttleModel et fournit des méthodes pour y accéder formatées.
 */
public class NavetteFacturationViewModel {
    private static final Logger logger = LoggerFactory.getLogger(NavetteFacturationViewModel.class.getName());
    private MoisService moisService = new MoisService();

    private BillingShuttleModel modele;
    private static final DateTimeFormatter JOUR_FORMAT = DateTimeFormatter.ofPattern("dd");
    private static final DateTimeFormatter ANNEE_FORMAT = DateTimeFormatter.ofPattern("yyyy");
    private static final DateTimeFormatter MOIS_TEXTE_FORMAT = DateTimeFormatter.ofPattern("MMMM", Locale.FRANCE);

    /**
     * Constructeur par défaut. Initialise le modèle à null.
     */
    public NavetteFacturationViewModel() {
        logger.log(Level.INFO, "Initialisation de NavetteFacturationViewModel sans modèle");
        this.modele = null;
    }

    /**
     * Constructeur avec un modèle. Initialise le modèle avec la valeur fournie ou un modèle par défaut si null.
     *
     * @param modele Le modèle BillingShuttleModel à utiliser
     */
    public NavetteFacturationViewModel(BillingShuttleModel modele) {
        logger.log(Level.INFO, "Initialisation de NavetteFacturationViewModel avec modèle");
        this.modele = (modele != null) ? modele : new BillingShuttleModel();
        if (modele == null) {
            logger.log(Level.WARNING, "Le modèle fourni était null, utilisation d'une instance par défaut");
        }
    }

    /**
     * Formate une date selon un formateur donné.
     *
     * @param date La date à formater
     * @param formatter Le formateur à utiliser
     * @return La date formatée ou une chaîne vide si la date est null
     */
    private String formatDate(LocalDate date, DateTimeFormatter formatter) {
        if (date != null) {
            String formattedDate = date.format(formatter);
            logger.log(Level.FINE, "Date formatée : {0} avec le formatteur {1}", new Object[]{formattedDate, formatter.toString()});
            return formattedDate;
        }
        logger.log(Level.FINE, "Date null, retour d'une chaîne vide");
        return "";
    }

    /**
     * Retourne le PcBu du modèle.
     *
     * @return Le PcBu ou une valeur par défaut si non disponible
     */
    public String getPcBu() {
        String pcBu = (modele != null && modele.getPcBu() != null) ? modele.getPcBu() : "Default PcBu";
        logger.log(Level.FINE, "Récupération de PcBu : {0}", pcBu);
        return pcBu;
    }

    /**
     * Retourne le projet du modèle.
     *
     * @return Le projet ou une valeur par défaut si non disponible
     */
    public String getProjet() {
        String projet = (modele != null && modele.getProject() != null) ? modele.getProject() : "Default Projet";
        logger.log(Level.FINE, "Récupération de Projet : {0}", projet);
        return projet;
    }

    /**
     * Retourne l'activité du modèle.
     *
     * @return L'activité ou une valeur par défaut si non disponible
     */
    public String getActivite() {
        String activite = (modele != null && modele.getActivity() != null) ? modele.getActivity() : "Default Activite";
        logger.log(Level.FINE, "Récupération de Activite : {0}", activite);
        return activite;
    }

    /**
     * Retourne le nombre de factures du modèle.
     *
     * @return Le nombre de factures ou 0 si non disponible
     */
    public int getNombreFactures() {
        int nombre = (modele != null) ? modele.getBillNumber() : 0;
        logger.log(Level.FINE, "Récupération de NombreFactures : {0}", nombre);
        return nombre;
    }

    /**
     * Retourne la note d'événement du modèle.
     *
     * @return La note d'événement ou une valeur par défaut si non disponible
     */
    public String getNoteEvenement() {
        String note = (modele != null && modele.getEventNote() != null) ? modele.getEventNote() : "Default Note";
        logger.log(Level.FINE, "Récupération de NoteEvenement : {0}", note);
        return note;
    }

    /**
     * Retourne la quantité du modèle.
     *
     * @return La quantité ou 0.0 si non disponible
     */
    public double getQuantite() {
        double quantite = (modele != null) ? modele.getQuantity() : 0.0;
        logger.log(Level.FINE, "Récupération de Quantite : {0}", quantite);
        return quantite;
    }

    /**
     * Retourne la quantité du modèle en tant qu'entier.
     *
     * @return La quantité ou 0 si non disponible
     */
    public int getQuantiteAsInt() {
        int quantite = (modele != null) ? (int) modele.getQuantity() : 0;
        logger.log(Level.FINE, "Récupération de Quantite : {0}", quantite);
        return quantite;
    }

    public double getQuantiteAsDouble(){
        return modele.getQuantity();
    }

    /**
     * Retourne l'unité de mesure du modèle.
     *
     * @return L'unité de mesure ou une valeur par défaut si non disponible
     */
    public String getUniteMesure() {
        String unite = (modele != null && modele.getMeasureUnit() != null) ? modele.getMeasureUnit() : "Default Unite";
        logger.log(Level.FINE, "Récupération de UniteMesure : {0}", unite);
        return unite;
    }

    /**
     * Retourne le prix unitaire arrondi à deux décimales.
     *
     * @return Le prix unitaire arrondi ou 0.0 si non disponible
     */
    public double getPrixUnitaireRound() {
        double prix = (modele != null) ? Math.round(modele.getUnitPrice() * 100.0) / 100.0 : 0.0;
        logger.log(Level.FINE, "Récupération de PrixUnitaireRound : {0}", prix);
        return prix;
    }

    /**
     * Retourne le montant de facturation arrondi à deux décimales.
     *
     * @return Le montant de facturation arrondi ou 0.0 si non disponible
     */
    public double getMontantFacturationRound() {
        double montant = (modele != null) ? Math.round(modele.getBillAmount() * 100.0) / 100.0 : 0.0;
        logger.log(Level.FINE, "Récupération de MontantFacturationRound : {0}", montant);
        return montant;
    }

    /**
     * Retourne le montant calculé de l'événement arrondi à deux décimales.
     *
     * @return Le montant calculé arrondi ou 0.0 si non disponible
     */
    public double getMontantEvenementCalculeRound() {
        double montant = (modele != null) ? Math.round(modele.getCalculatedEventAmount() * 100.0) / 100.0 : 0.0;
        logger.log(Level.FINE, "Récupération de MontantEvenementCalculeRound : {0}", montant);
        return montant;
    }

    /**
     * Retourne le prix unitaire brut.
     *
     * @return Le prix unitaire ou 0.0 si non disponible
     */
    public double getPrixUnitaire() {
        double prix = (modele != null) ? modele.getUnitPrice() : 0.0;
        logger.log(Level.FINE, "Récupération de PrixUnitaire : {0}", prix);
        return prix;
    }

    /**
     * Retourne le montant de facturation brut.
     *
     * @return Le montant de facturation ou 0.0 si non disponible
     */
    public double getMontantFacturation() {
        double montant = (modele != null) ? modele.getBillAmount() : 0.0;
        logger.log(Level.FINE, "Récupération de MontantFacturation : {0}", montant);
        return montant;
    }

    /**
     * Retourne le montant calculé de l'événement brut.
     *
     * @return Le montant calculé ou 0.0 si non disponible
     */
    public double getMontantEvenementCalcule() {
        double montant = (modele != null) ? modele.getCalculatedEventAmount() : 0.0;
        logger.log(Level.FINE, "Récupération de MontantEvenementCalcule : {0}", montant);
        return montant;
    }

    /**
     * Retourne le jour de début de la période de facturation.
     *
     * @return Le jour ou une chaîne vide si non disponible
     */
    public String getPeriodeFacturationDu() {
        String jour = (modele != null) ? formatDate(modele.getBillPeriodFrom(), JOUR_FORMAT) : "";
        logger.log(Level.FINE, "Récupération de PeriodeFacturationDu : {0}", jour);
        return jour;
    }

    /**
     * Retourne le jour de fin de la période de facturation.
     *
     * @return Le jour ou une chaîne vide si non disponible
     */
    public String getPeriodeFacturationAu() {
        String jour = (modele != null) ? formatDate(modele.getBillPeriodTo(), JOUR_FORMAT) : "";
        logger.log(Level.FINE, "Récupération de PeriodeFacturationAu : {0}", jour);
        return jour;
    }

    /**
     * Retourne l'identifiant de l'élément.
     *
     * @return L'identifiant ou une valeur par défaut si non disponible
     */
    public String getItemId() {
        String itemId = (modele != null && modele.getItemId() != null) ? modele.getItemId() : "Default ItemId";
        logger.log(Level.FINE, "Récupération de ItemId : {0}", itemId);
        return itemId;
    }

    /**
     * Retourne la facture initiale.
     *
     * @return La facture initiale ou une valeur par défaut si non disponible
     */
    public String getFactureInitiale() {
        String facture = (modele != null && modele.getInitialBill() != null) ? modele.getInitialBill() : "Default Facture";
        logger.log(Level.FINE, "Récupération de FactureInitiale : {0}", facture);
        return facture;
    }

    /**
     * Retourne le modèle sous-jacent.
     *
     * @return Le modèle BillingShuttleModel
     */
    public BillingShuttleModel getModel() {
        logger.log(Level.FINE, "Récupération du modèle");
        return modele;
    }

    /**
     * Retourne le jour de début de la période de facturation.
     *
     * @return Le jour ou une chaîne vide si non disponible
     */
    public String getJourPeriodeFacturationDu() {
        String jour = (modele != null) ? formatDate(modele.getBillPeriodFrom(), JOUR_FORMAT) : "";
        logger.log(Level.FINE, "Récupération de JourPeriodeFacturationDu : {0}", jour);
        return jour;
    }

    /**
     * Retourne l'année de début de la période de facturation.
     *
     * @return L'année ou une chaîne vide si non disponible
     */
    public String getAnneePeriodeFacturationDu() {
        String annee = (modele != null) ? formatDate(modele.getBillPeriodFrom(), ANNEE_FORMAT) : "";
        logger.log(Level.FINE, "Récupération de AnneePeriodeFacturationDu : {0}", annee);
        return annee;
    }

    /**
     * Retourne le mois en texte de début de la période de facturation.
     *
     * @return Le mois en texte ou une chaîne vide si non disponible
     */
    public String getMoisTextePeriodeFacturationDu() {
        String mois;
        mois = extraireMois(modele.getBillPeriodFrom());
        return mois;
    }

    private String extraireMois(LocalDate date) {
        String mois = (modele != null) ? formatDate(date, MOIS_TEXTE_FORMAT) : "";
        mois = moisService.extraireMois(date, Locale.FRANCE);
        logger.log(Level.FINE, "Récupération du mois : {0} sur la date {1}", new Object[]{mois, date.toString()});
        return mois;
    }

    /**
     * Capitalize the first letter of the given string.
     *
     * @param mois The string to capitalize.
     * @return The string with the first letter capitalized.
     */
    private String capitalizeFirstLetter(String mois) {
        if (mois == null || mois.isEmpty()) {
            return mois;
        }
        return mois.substring(0, 1).toUpperCase() + mois.substring(1).toLowerCase();
    }

    /**
     * Retourne le jour de fin de la période de facturation.
     *
     * @return Le jour ou une chaîne vide si non disponible
     */
    public String getJourPeriodeFacturationAu() {
        String jour = (modele != null) ? formatDate(modele.getBillPeriodTo(), JOUR_FORMAT) : "";
        logger.log(Level.FINE, "Récupération de JourPeriodeFacturationAu : {0}", jour);
        return jour;
    }

    /**
     * Retourne l'année de fin de la période de facturation.
     *
     * @return L'année ou une chaîne vide si non disponible
     */
    public String getAnneePeriodeFacturationAu() {
        String annee = (modele != null) ? formatDate(modele.getBillPeriodTo(), ANNEE_FORMAT) : "";
        logger.log(Level.FINE, "Récupération de AnneePeriodeFacturationAu : {0}", annee);
        return annee;
    }

    /**
     * Retourne le mois en texte de fin de la période de facturation.
     *
     * @return Le mois en texte ou une chaîne vide si non disponible
     */
    public String getMoisTextePeriodeFacturationAu() {
        String mois;
        mois = extraireMois(modele.getBillPeriodTo());
        return mois;
    }
}