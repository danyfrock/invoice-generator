package com.invoicegenerator.viewModels;

import com.invoicegenerator.modeles.BillingShuttleModel;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class NavetteFacturationViewModel {
    private BillingShuttleModel modele;
    private static final DateTimeFormatter JOUR_FORMAT = DateTimeFormatter.ofPattern("dd");
    private static final DateTimeFormatter ANNEE_FORMAT = DateTimeFormatter.ofPattern("yyyy");
    private static final DateTimeFormatter MOIS_TEXTE_FORMAT = DateTimeFormatter.ofPattern("MMMM", Locale.FRANCE);

    public NavetteFacturationViewModel() {
        this.modele = null; // Default value
    }

    public NavetteFacturationViewModel(BillingShuttleModel modele) {
        this.modele = (modele != null) ? modele : new BillingShuttleModel(); // Default value if null
    }

    private String formatDate(LocalDate date, DateTimeFormatter formatter) {
        return (date != null) ? date.format(formatter) : "";
    }

    public String getPcBu() {
        return (modele != null && modele.getPcBu() != null) ? modele.getPcBu() : "Default PcBu";
    }

    public String getProjet() {
        return (modele != null && modele.getProject() != null) ? modele.getProject() : "Default Projet";
    }

    public String getActivite() {
        return (modele != null && modele.getActivity() != null) ? modele.getActivity() : "Default Activite";
    }

    public int getNombreFactures() {
        return (modele != null) ? modele.getBillNumber() : 0;
    }

    public String getNoteEvenement() {
        return (modele != null && modele.getEventNote() != null) ? modele.getEventNote() : "Default Note";
    }

    public double getQuantite() {
        return (modele != null) ? modele.getQuantity() : 0.0;
    }

    public String getUniteMesure() {
        return (modele != null && modele.getMeasureUnit() != null) ? modele.getMeasureUnit() : "Default Unite";
    }

    public double getPrixUnitaireRound() {
        return (modele != null) ? Math.round(modele.getUnitPrice() * 100.0) / 100.0 : 0.0;
    }

    public double getMontantFacturationRound() {
        return (modele != null) ? Math.round(modele.getBillAmount() * 100.0) / 100.0 : 0.0;
    }

    public double getMontantEvenementCalculeRound() {
        return (modele != null) ? Math.round(modele.getCalculatedEventAmount() * 100.0) / 100.0 : 0.0;
    }


    public double getPrixUnitaire() {
        return (modele != null) ? modele.getUnitPrice() : 0.0;
    }

    public double getMontantFacturation() {
        return (modele != null) ? modele.getBillAmount() : 0.0;
    }

    public double getMontantEvenementCalcule() {
        return (modele != null) ? modele.getCalculatedEventAmount() : 0.0;
    }


    public String getPeriodeFacturationDu() {
        return (modele != null) ? formatDate(modele.getBillPeriodFrom(), JOUR_FORMAT) : "";
    }

    public String getPeriodeFacturationAu() {
        return (modele != null) ? formatDate(modele.getBillPeriodTo(), JOUR_FORMAT) : "";
    }

    public String getItemId() {
        return (modele != null && modele.getItemId() != null) ? modele.getItemId() : "Default ItemId";
    }

    public String getFactureInitiale() {
        return (modele != null && modele.getInitialBill() != null) ? modele.getInitialBill() : "Default Facture";
    }

    public BillingShuttleModel getModel() {
        return modele;
    }

    public String getJourPeriodeFacturationDu() {
        return (modele != null) ? formatDate(modele.getBillPeriodFrom(), JOUR_FORMAT) : "";
    }

    public String getAnneePeriodeFacturationDu() {
        return (modele != null) ? formatDate(modele.getBillPeriodFrom(), ANNEE_FORMAT) : "";
    }

    public String getMoisTextePeriodeFacturationDu() {
        return (modele != null) ? formatDate(modele.getBillPeriodFrom(), MOIS_TEXTE_FORMAT) : "";
    }

    public String getJourPeriodeFacturationAu() {
        return (modele != null) ? formatDate(modele.getBillPeriodTo(), JOUR_FORMAT) : "";
    }

    public String getAnneePeriodeFacturationAu() {
        return (modele != null) ? formatDate(modele.getBillPeriodTo(), ANNEE_FORMAT) : "";
    }

    public String getMoisTextePeriodeFacturationAu() {
        return (modele != null) ? formatDate(modele.getBillPeriodTo(), MOIS_TEXTE_FORMAT) : "";
    }
}