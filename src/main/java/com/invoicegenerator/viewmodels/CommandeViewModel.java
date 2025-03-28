package com.invoicegenerator.viewmodels;

import com.invoicegenerator.utils.backend.LoggerFactory;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import com.invoicegenerator.modeles.*;

import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * ViewModel pour la gestion des commandes dans l'interface utilisateur.
 * Cette classe encapsule les contrôles UI et la logique pour afficher et modifier les données d'une commande.
 */
public class CommandeViewModel {
    private static final Logger logger = LoggerFactory.getLogger(CommandeViewModel.class.getName());

    private PvEntityPvModel source;
    private CommandesViewModel backReference;

    private final SimpleBooleanProperty estVerifie = new SimpleBooleanProperty(false);
    private final SimpleBooleanProperty estLeader = new SimpleBooleanProperty(false);
    private final SimpleStringProperty doitRemplir = new SimpleStringProperty("");
    private final SimpleStringProperty nomFichier = new SimpleStringProperty("");
    private final SimpleStringProperty pathFichier = new SimpleStringProperty("");
    private final SimpleStringProperty codeContrat = new SimpleStringProperty("");
    private final SimpleStringProperty codeActivite = new SimpleStringProperty("");
    private Property<LocalDate> dateDebutProperty = new SimpleObjectProperty<>(null);
    private Property<LocalDate> dateFinProrperty = new SimpleObjectProperty<>(null);

    /**
     * Constructeur par défaut. Initialise l'interface utilisateur et charge les paramètres.
     */
    public CommandeViewModel(PvEntityPvModel pv, CommandesViewModel backReference) {
        logger.log(Level.INFO, "Initialisation de CommandeViewModel");

        this.source = pv;
        this.backReference = backReference;
        this.autoFill();

        // Listeners pour synchroniser le modèle
        this.dateDebutProperty.addListener((obs, oldVal, newVal) -> setDateDebut(newVal));
        this.dateFinProrperty.addListener((obs, oldVal, newVal) -> setDateFin(newVal));
        this.codeContrat.addListener((obs, oldVal, newVal) -> setCodeContrat(newVal));
        this.codeActivite.addListener((obs, oldVal, newVal) -> setCodeActivite(newVal));

        this.updateFromModel();
        logger.log(Level.INFO, "Interface utilisateur initialisée avec succès");
    }

    /**
     * Retourne la source de données actuelle.
     *
     * @return L'entité PvEntityPvModel source
     */
    public PvEntityPvModel getSource() {
        return this.source;
    }

    /**
     * Retourne un message indiquant ce qui doit être rempli ou corrigé.
     *
     * @return Une chaîne de caractères décrivant les erreurs ou une chaîne vide si tout est correct
     */
    public String getDoitRemplir() {
        CommandModel commande = this.getSource().getCommand();
        ParametersModel params = this.backReference.getSource().getParameters();

        // dates remplies
        if (commande.getDateDebut() == null) {
            return "Remplir le contrôle Date Début";
        }
        if (commande.getDateFin() == null) {
            return "Remplir le contrôle Date Fin";
        }

        // code activité
        String activityCode = commande.getActivityCode();
        List<String> activityCodes = this.backReference.getSource().getParameters().getActivityCodes();
        if (activityCode == null || activityCode.isEmpty()) {
            return "Remplir le contrôle Code Activité";
        }
        if (!activityCodes.contains(activityCode)) {
            return "La valeur du Code Activité n'est pas valide.";
        }

        //contrat
        if (commande.getContractCode() == null || commande.getContractCode().isEmpty()) {
            return "Remplir le contrôle Code Contrat";
        }

        // dates cohérentes
        if (commande.getDateDebut().isEqual(commande.getDateFin())) {
            return "La date de début doit être différente de la date de fin";
        }
        if (commande.getDateDebut().isAfter(commande.getDateFin())) {
            return "La date de début doit être avant la date de fin";
        }

        int yearMin = params.getMinYear();
        if (commande.getDateDebut().getYear() < yearMin) {
            return "La date de début doit être après " + yearMin;
        }
        if (commande.getDateFin().getYear() < yearMin) {
            return "La date de fin doit être après " + yearMin;
        }

        int yearMax = params.getMaxYear();
        if (commande.getDateDebut().getYear() > yearMax) {
            return "La date de début doit être avant " + yearMax;
        }
        if (commande.getDateFin().getYear() > yearMax) {
            return "La date de fin doit être avant " + yearMax;
        }

        return "";
    }

    /**
     * Retourne la propriété indiquant si les données sont vérifiées.
     *
     * @return SimpleBooleanProperty pour estVerifie
     */
    public SimpleBooleanProperty estVerifieProperty() {
        return estVerifie;
    }

    /**
     * Retourne la propriété contenant le message de ce qui doit être rempli.
     *
     * @return SimpleStringProperty pour doitRemplir
     */
    public SimpleStringProperty doitRemplirProperty() {
        return doitRemplir;
    }

    /**
     * Retourne la propriété contenant le nom du fichier.
     *
     * @return SimpleStringProperty pour nomFichier
     */
    public SimpleStringProperty nomFichierProperty() {
        return nomFichier;
    }

    /**
     * Retourne la propriété contenant le chemin du fichier.
     *
     * @return SimpleStringProperty pour pathFichier
     */
    public SimpleStringProperty pathFichierProperty() {
        return pathFichier;
    }

    /**
     * Retourne la propriété contenant le code du contrat.
     *
     * @return SimpleStringProperty pour codeContrat
     */
    public SimpleStringProperty codeContratProperty() {
        return codeContrat;
    }

    /**
     * Retourne la propriété contenant le code d'activité.
     *
     * @return SimpleStringProperty pour codeActivite
     */
    public SimpleStringProperty codeActiviteProperty() {
        return codeActivite;
    }

    /**
     * Retourne la propriété contenant la date de début.
     * @return La propriété de type LocalDate représentant la date de début.
     */
    public Property<LocalDate> getDateDebutProperty() {
        return dateDebutProperty;
    }

    /**
     * Retourne la propriété contenant la date de fin.
     * @return La propriété de type LocalDate représentant la date de fin.
     */
    public Property<LocalDate> getDateFinProrperty() {
        return dateFinProrperty;
    }

    /**
     * Définit la date de début et met à jour les propriétés de validation.
     *
     * @param dateDebut La nouvelle date de début
     */
    private void setDateDebut(LocalDate dateDebut) {
        logger.log(Level.FINE, "Mise à jour de dateDebut : {0}", dateDebut);
        source.getCommand().setDateDebut(dateDebut);
        this.updateValidationProperties();
    }

    /**
     * Définit la date de fin et met à jour les propriétés de validation.
     *
     * @param dateFin La nouvelle date de fin
     */
    private void setDateFin(LocalDate dateFin) {
        logger.log(Level.FINE, "Mise à jour de dateFin : {0}", dateFin);
        source.getCommand().setDateFin(dateFin);
        this.updateValidationProperties();
    }

    /**
     * Définit le code du contrat et met à jour les propriétés de validation.
     *
     * @param codeContrat Le nouveau code du contrat
     */
    private void setCodeContrat(String codeContrat) {
        logger.log(Level.FINE, "Mise à jour de codeContrat : {0}", codeContrat);
        source.getCommand().setContractCode(codeContrat);
        this.updateValidationProperties();
    }

    /**
     * Définit le code de l'activité et met à jour les propriétés de validation.
     *
     * @param codeActivite Le nouveau code de l'activité
     */
    private void setCodeActivite(String codeActivite) {
        logger.log(Level.FINE, "Mise à jour de codeActivite : {0}", codeActivite);
        source.getCommand().setActivityCode(codeActivite);
        this.updateValidationProperties();
    }

    /**
    * Obtient le ViewModel parent.
    * */
    public CommandesViewModel getBackReference() {
        return backReference;
    }

    /**
     * Indique si cette entité a un rôle décisionnaire sur certaines informations
     * au sein de {@link CommandeViewModel}.
     */
    public boolean isEstLeader() {
        return estLeader.get();
    }

    /**
     * Retourne la propriété observable associée à l'état de leader de l'entité.
     * Permet de lier cet état à une interface graphique ou un autre composant réactif.
     *
     * @return la propriété SimpleBooleanProperty représentant l'état de leader.
     */
    public SimpleBooleanProperty estLeaderProperty() {
        return estLeader;
    }

    private void autoFill() {
        this.backReference.getPvService().fillPvFrom(this.source.getFilePath(), this.source);
        logger.log(Level.FINE, "Conversion du fichier {0} en PvEntityPvModel", this.source.getFilePath());
    }

    /**
     * Met à jour les propriétés du viewmodel à partir du modèle source (PvEntityPvModel).
     * Cette méthode doit être appelée après autoFill() pour synchroniser les données du modèle
     * avec les propriétés observables utilisées par l'interface utilisateur.
     */
    public void updateFromModel() {
        logger.log(Level.INFO, "Mise à jour des propriétés du viewmodel à partir de PvEntityPvModel");

        CommandModel commande = this.source.getCommand();

        // Mise à jour des propriétés à partir du modèle
        this.nomFichier.set(this.source.getFileName());
        this.pathFichier.set(this.source.getFilePath());
        this.codeContrat.set(commande.getContractCode());
        this.codeActivite.set(commande.getActivityCode());
        this.dateDebutProperty.setValue(commande.getDateDebut());
        this.dateFinProrperty.setValue(commande.getDateFin());

        logger.log(Level.FINE, "Propriétés du viewmodel mises à jour avec succès pour le fichier {0}", this.source.getFilePath());
    }

    /**
     * Met à jour les propriétés dérivées doitRemplir et estVerifie en fonction de l'état actuel des données.
     */
    private void updateValidationProperties() {
        this.doitRemplir.set(this.getDoitRemplir());
        this.estVerifie.set(this.getDoitRemplir().isEmpty());
    }
}