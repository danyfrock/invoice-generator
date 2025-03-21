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

public class CommandesViewModel {
    private static final Logger logger = LoggerFactory.getLogger(CommandesViewModel.class.getName());

    private final EntitePvService pvService = new EntitePvService();
    private final ParametresService parametresService;
    private final BillingProcessModel source;
    private final ObservableList<CommandeViewModel> commandes = FXCollections.observableArrayList();
    private final SimpleStringProperty complement = new SimpleStringProperty("");
    private final SimpleStringProperty outputFileName = new SimpleStringProperty("");
    private final SimpleBooleanProperty allVerified = new SimpleBooleanProperty(true);

    public CommandesViewModel(BillingProcessModel source) {
        logger.log(Level.INFO, "Initialisation de CommandesViewModel");
        this.source = source;
        this.parametresService = new ParametresService(source.getParameters().getParametersFileName());
        initializeCommandes();
        initializeProperties();
        logger.log(Level.INFO, "CommandesViewModel initialisé avec succès");
    }

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

    private void updateOutputFileName() {
        logger.log(Level.FINE, "Mise à jour du nom du fichier de sortie");
        String codeContrat = commandes.isEmpty() ? "" : commandes.getFirst().getSource().getCommand().getContractCode();
        String basePath = FileUtil.concat(source.getParameters().getOutputFolder(), source.getParameters().getOutputFileDefaultName());
        String sortie = FileUtil.addSuffixToFileName(basePath, codeContrat);
        sortie = FileUtil.addSuffixToFileName(sortie, complement.get());
        this.outputFileName.set(sortie);
        source.setOutputFileName(sortie);
    }

    private void updateAllVerified() {
        boolean allVerifiedNow = commandes.stream().allMatch(vm -> vm.estVerifieProperty().get());
        this.allVerified.set(allVerifiedNow);
        logger.log(Level.FINE, "Mise à jour de allVerified : {0}", allVerifiedNow);
    }

    public void updateAllContractCodes(String newCode) {
        logger.log(Level.FINE, "Mise à jour du code contrat pour toutes les commandes : {0}", newCode);
        for (CommandeViewModel commande : commandes) {
            if (!Objects.equals(commande.getSource().getCommand().getContractCode(), newCode)) {
                commande.codeContratProperty().set(newCode);
            }
        }
        updateOutputFileName();
    }

    public void sauvegarderProgression(File file) {
        logger.log(Level.INFO, "Sauvegarde de la progression dans : {0}", file.getAbsolutePath());
        source.getParameters().setDernierEmplacementConnuProgression(file.getParentFile().getAbsolutePath());
        parametresService.enregistrerParametres(source.getParameters());
        new BillingProcessService(file.getAbsolutePath()).enregistrerBillingProcess(this.source);
    }

    public BillingProcessModel getSource() {
        return source;
    }

    public ObservableList<CommandeViewModel> getCommandes() {
        return commandes;
    }

    public SimpleStringProperty complementProperty() {
        return complement;
    }

    public String getComplement() {
        return complement.get();
    }

    public void setComplement(String complement) {
        this.complement.set(complement);
    }

    public SimpleStringProperty outputFileNameProperty() {
        return outputFileName;
    }

    public String getOutputFileName() {
        return outputFileName.get();
    }

    public SimpleBooleanProperty allVerifiedProperty() {
        return allVerified;
    }

    public boolean isAllVerified() {
        return allVerified.get();
    }

    public EntitePvService getPvService() {
        return pvService;
    }

}