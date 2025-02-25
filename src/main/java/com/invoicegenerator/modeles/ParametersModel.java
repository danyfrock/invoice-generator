package com.invoicegenerator.modeles;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class ParametersModel {
    private String emplacementDossierSortie = Paths.get(System.getProperty("user.home"), "Desktop").toString();
    private List<String> codesActivite  = Arrays.asList("30001", "30003", "30005", "30007", "30009");;
    private int anneeMin = 2024;
    private int anneeMax = 2029;
    private final String nomDefautFichierSortie = "WST-CO_.xlsm";
    private String parametresFileName = Paths.get(System.getProperty("user.home"),"") + "\\parametres.json";

    public ParametersModel() {
    }

    public String getEmplacementDossierSortie() {
        return emplacementDossierSortie;
    }

    public void setEmplacementDossierSortie(String emplacementDossierSortie) {
        this.emplacementDossierSortie = emplacementDossierSortie;
    }

    public List<String> getCodesActivite() {
        return codesActivite;
    }

    public void ajouterCodeActivite(String code) {
        if (code != null && !code.trim().isEmpty() && !this.codesActivite.contains(code)) {
            this.codesActivite.add(code);
        }
    }

    public void supprimerCodeActivite(String code) {
        this.codesActivite.remove(code);
    }

    public int getAnneeMin() {
        return anneeMin;
    }

    public void setAnneeMin(int anneeMin) {
        this.anneeMin = anneeMin;
    }

    public int getAnneeMax() {
        return anneeMax;
    }

    public void setAnneeMax(int anneeMax) {
        this.anneeMax = anneeMax;
    }

    @Override
    public String toString() {
        return "ParametresModele{" +
                "emplacementDossierSortie='" + emplacementDossierSortie + '\'' +
                ", codesActivite=" + codesActivite +
                ", anneeMin=" + anneeMin +
                ", anneeMax=" + anneeMax +
                '}';
    }

    public String getNomDefautFichierSortie() {
        return nomDefautFichierSortie;
    }

    public String getParametresFileName() {
        return parametresFileName;
    }
}
