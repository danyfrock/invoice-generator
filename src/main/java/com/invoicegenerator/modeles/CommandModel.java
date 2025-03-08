package com.invoicegenerator.modeles;

import com.invoicegenerator.utils.LoggerFactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Représente le modèle de commande qui contient les détails sur la période de commande,
 * le code du contrat, le code d'activité et une liste de lignes de commande.
 */
public class CommandModel {
    private static final Logger logger = LoggerFactory.getLogger(CommandModel.class.getName());

    private LocalDate dateDebut;
    private LocalDate dateFin;
    private String contractCode;
    private String activityCode;
    private List<UoCommandLineModel> commandLines = new ArrayList<>();
    private static final String bonDeCommandePrefix = "BDC PO";
    private String bonDeCommandeCellB9;
    private String objetDeLaPrestationCellB11;

    /**
     * Constructeur par défaut pour CommandModel.
     */
    public CommandModel() {
        logger.log(Level.INFO, "Création d'une nouvelle instance de CommandModel avec valeurs par défaut");
    }

    /**
     * Récupère la date de début de la commande.
     *
     * @return La date de début de la commande.
     */
    public LocalDate getDateDebut() {
        logger.log(Level.FINE, "Récupération de la date de début : {0}", dateDebut);
        return dateDebut;
    }

    /**
     * Définit la date de début de la commande.
     *
     * @param dateDebut La date de début à définir.
     */
    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
        logger.log(Level.FINE, "Date de début définie : {0}", dateDebut);
    }

    /**
     * Récupère la date de fin de la commande.
     *
     * @return La date de fin de la commande.
     */
    public LocalDate getDateFin() {
        logger.log(Level.FINE, "Récupération de la date de fin : {0}", dateFin);
        return dateFin;
    }

    /**
     * Définit la date de fin de la commande.
     *
     * @param dateFin La date de fin à définir.
     */
    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
        logger.log(Level.FINE, "Date de fin définie : {0}", dateFin);
    }

    /**
     * Récupère le code du contrat.
     *
     * @return Le code du contrat.
     */
    public String getContractCode() {
        logger.log(Level.FINE, "Récupération du code du contrat : {0}", contractCode);
        return contractCode;
    }

    /**
     * Définit le code du contrat.
     *
     * @param contractCode Le code du contrat à définir.
     */
    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
        logger.log(Level.FINE, "Code du contrat défini : {0}", contractCode);
    }

    /**
     * Récupère le code de l'activité.
     *
     * @return Le code de l'activité.
     */
    public String getActivityCode() {
        logger.log(Level.FINE, "Récupération du code de l'activité : {0}", activityCode);
        return activityCode;
    }

    /**
     * Définit le code de l'activité.
     *
     * @param activityCode Le code de l'activité à définir.
     */
    public void setActivityCode(String activityCode) {
        this.activityCode = activityCode;
        logger.log(Level.FINE, "Code de l'activité défini : {0}", activityCode);
    }

    /**
     * Récupère la liste des lignes de commande.
     *
     * @return Une liste de UoCommandLineModel.
     */
    public List<UoCommandLineModel> getCommandLines() {
        logger.log(Level.FINE, "Récupération de la liste des lignes de commande, taille : {0}", commandLines.size());
        return commandLines;
    }

    /**
     * Définit la liste des lignes de commande.
     *
     * @param commandLines La liste des lignes de commande à définir.
     */
    public void setCommandLines(List<UoCommandLineModel> commandLines) {
        if (commandLines == null) {
            logger.log(Level.WARNING, "Tentative de définition d'une liste de lignes de commande null, ignorée");
            return;
        }
        this.commandLines = commandLines;
        logger.log(Level.FINE, "Liste des lignes de commande définie, taille : {0}", commandLines.size());
    }

    /**
     * Récupère le bon de commande.
     *
     * @return Le bon de commande.
     */
    public String getBonDeCommandeCellB9() {
        logger.log(Level.FINE, "Récupération du bon de commande : {0}", bonDeCommandeCellB9);
        return bonDeCommandeCellB9;
    }


    /**
     * Récupère le préfix du bon de commande.
     *
     * @return Le préfix du bon de commande.
     */
    public String getBonDeCommandePrefix() {
        logger.log(Level.FINE, "Récupération du bon de commande prefix : {0}", bonDeCommandePrefix);
        return bonDeCommandePrefix;
    }

    /**
     * Définit le bon de commande.
     *
     * @param orderForm Le bon de commande à définir.
     */
    public void setBonDeCommandeCellB9(String orderForm) {
        this.bonDeCommandeCellB9 = orderForm;
        logger.log(Level.FINE, "Bon de commande défini : {0}", orderForm);
    }

    /**
     * Récupère l'objet de la prestation.
     *
     * @return L'objet de la prestation.
     */
    public String getBenefitPurposeCellB11() {
        logger.log(Level.FINE, "Récupération de l'objet de la prestation : {0}", objetDeLaPrestationCellB11);
        return objetDeLaPrestationCellB11;
    }

    /**
     * Définit l'objet de la prestation.
     *
     * @param benefitPurpose défini l'objet de la prestation.
     */
    public void setBenefitPurposeCellB11(String benefitPurpose) {
        this.objetDeLaPrestationCellB11 = benefitPurpose;
        logger.log(Level.FINE, "  : {0}", benefitPurpose);
    }
}