package com.invoicegenerator.viewModels;

import com.invoicegenerator.modeles.BillingProcessModel;
import com.invoicegenerator.modeles.PvEntityPvModel;
import com.invoicegenerator.services.BillingProcessService;
import com.invoicegenerator.services.EntitePvService;
import com.invoicegenerator.services.ParametresService;
import com.invoicegenerator.utils.backend.FileUtil;
import com.invoicegenerator.utils.backend.LoggerFactory;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * ViewModel pour gérer les commandes dans le processus de facturation.
 */
public class CommandesViewModel {
    private static final Logger logger = LoggerFactory.getLogger(CommandesViewModel.class.getName());

    private final EntitePvService pvService = new EntitePvService();
    private final ParametresService parametresService;
    private final BillingProcessModel source;
    private final ObservableList<CommandeViewModel> commandes = FXCollections.observableArrayList();
    private final SimpleStringProperty complement = new SimpleStringProperty("");
    private final SimpleStringProperty outputFileName = new SimpleStringProperty("");
    private final SimpleBooleanProperty allVerified = new SimpleBooleanProperty(true);

    /**
     * Constructeur pour initialiser le ViewModel avec le modèle de processus de facturation.
     * @param source Le modèle de processus de facturation.
     */
    public CommandesViewModel(BillingProcessModel source) {
        logger.log(Level.INFO, "Initialisation de CommandesViewModel");
        this.source = source;
        this.parametresService = new ParametresService(source.getParameters().getParametersFileName());
        initializeCommandes();
        initializeProperties();
        logger.log(Level.INFO, "CommandesViewModel initialisé avec succès");
    }

    /**
     * Initialise la liste des commandes à partir des entités PV du modèle source.
     */
    private void initializeCommandes() {
        logger.log(Level.FINE, "Initialisation de la liste des commandes");
        for (PvEntityPvModel pv : source.getPvEntities()) {
            CommandeViewModel vm = new CommandeViewModel(pv, this);
            commandes.add(vm);
            vm.estVerifieProperty().addListener((obs, oldVal, newVal) -> updateAllVerified());
        }
        if (!commandes.isEmpty()) {
            commandes.getFirst().estLeaderProperty().set(true);
        }
        updateAllVerified(); // Calcul initial
    }

    /**
     * Initialise les propriétés observables du ViewModel.
     */
    private void initializeProperties() {
        logger.log(Level.FINE, "Initialisation des propriétés observables");
        this.complement.set(source.getComplement());
        this.complement.addListener((obs, oldVal, newVal) -> {
            source.setComplement(newVal);
            updateOutputFileName();
        });

        if (!commandes.isEmpty()) {
            Optional<CommandeViewModel> leaderStream = this.commandes.stream().filter(CommandeViewModel::isEstLeader).findFirst();
            if (leaderStream.isPresent()) {
                CommandeViewModel leader = leaderStream.get();
                leader.codeContratProperty().addListener((obs, oldVal, newVal) -> {
                    updateAllContractCodes(newVal);
                    updateOutputFileName();
                });
            }
        }

        this.outputFileName.set(source.getOutputFileName());
        updateOutputFileName(); // Calcul initial
    }

    /**
     * Met à jour le nom du fichier de sortie en fonction des propriétés actuelles.
     */
    private void updateOutputFileName() {
        logger.log(Level.FINE, "Mise à jour du nom du fichier de sortie");

        // Récupère les données
        String codeContrat = commandes.isEmpty() ? "" : commandes.getFirst().getSource().getCommand().getContractCode();
        codeContrat = codeContrat == null ? "" : codeContrat;
        String comp = complement.get() == null ? "" : complement.get();

        // Définit le nom du fichier de sortie
        String basePath = FileUtil.concat(source.getParameters().getOutputFolder(), source.getParameters().getOutputFileDefaultName());
        String sortie = FileUtil.addSuffixToFileName(basePath, codeContrat);
        sortie = FileUtil.addSuffixToFileName(sortie, comp);
        this.outputFileName.set(sortie);
        source.setOutputFileName(sortie);
    }

    /**
     * Met à jour la propriété allVerified en fonction de l'état de vérification de toutes les commandes.
     */
    private void updateAllVerified() {
        boolean allVerifiedNow = commandes.stream().allMatch(vm -> vm.estVerifieProperty().get());
        this.allVerified.set(allVerifiedNow);
        logger.log(Level.FINE, "Mise à jour de allVerified : {0}", allVerifiedNow);
    }

    /**
     * Met à jour le code contrat pour toutes les commandes.
     * @param newCode Le nouveau code contrat.
     */
    public void updateAllContractCodes(String newCode) {
        logger.log(Level.FINE, "Mise à jour du code contrat pour toutes les commandes : {0}", newCode);
        for (CommandeViewModel commande : commandes) {
            if (!Objects.equals(commande.getSource().getCommand().getContractCode(), newCode)) {
                commande.codeContratProperty().set(newCode);
            }
        }
        updateOutputFileName();
    }

    /**
     * Sauvegarde la progression actuelle dans un fichier.
     * @param file Le fichier dans lequel sauvegarder la progression.
     */
    public void sauvegarderProgression(File file) {
        logger.log(Level.INFO, "Sauvegarde de la progression dans : {0}", file.getAbsolutePath());
        source.getParameters().setDernierEmplacementConnuProgression(file.getParentFile().getAbsolutePath());
        parametresService.enregistrerParametres(source.getParameters());
        new BillingProcessService(file.getAbsolutePath()).enregistrerBillingProcess(this.source);
    }

    /**
     * @return Le modèle de processus de facturation source.
     */
    public BillingProcessModel getSource() {
        return source;
    }

    /**
     * @return La liste observable des commandes.
     */
    public ObservableList<CommandeViewModel> getCommandes() {
        return commandes;
    }

    /**
     * @return La propriété complement.
     */
    public SimpleStringProperty complementProperty() {
        return complement;
    }

    /**
     * @return Le complément actuel.
     */
    public String getComplement() {
        return complement.get();
    }

    /**
     * Définit le complément.
     * @param complement Le nouveau complément.
     */
    public void setComplement(String complement) {
        this.complement.set(complement);
    }

    /**
     * @return La propriété outputFileName.
     */
    public SimpleStringProperty outputFileNameProperty() {
        return outputFileName;
    }

    /**
     * @return Le nom du fichier de sortie actuel.
     */
    public String getOutputFileName() {
        return outputFileName.get();
    }

    /**
     * @return La propriété allVerified.
     */
    public SimpleBooleanProperty allVerifiedProperty() {
        return allVerified;
    }

    /**
     * @return L'état de vérification de toutes les commandes.
     */
    public boolean isAllVerified() {
        return allVerified.get();
    }

    /**
     * @return Le service EntitePv.
     */
    public EntitePvService getPvService() {
        return pvService;
    }

}