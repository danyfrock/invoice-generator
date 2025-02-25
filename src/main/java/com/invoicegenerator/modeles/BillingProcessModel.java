package com.invoicegenerator.modeles;

import java.util.ArrayList;
import java.util.List;

public class BillingProcessModel {
    private ParametersModel parametres = new ParametersModel();
    private List<PvEntityPvModel> pvEntityPvModels = new ArrayList<>();
    private String nomFichierSortie;
    private String complementNom;



    public List<PvEntityPvModel> getEntitePvModeles() {
        return pvEntityPvModels;
    }

    public ParametersModel getParametres() {
        return parametres;
    }

    public void setParametres(ParametersModel parametres) {
        this.parametres = parametres;
    }

    public String getNomFichierSortie() {
        return nomFichierSortie;
    }

    public void setNomFichierSortie(String nomFichierSortie) {
        this.nomFichierSortie = nomFichierSortie;
    }

    public String getComplementNom() {
        return complementNom;
    }

    public void setComplementNom(String complementNom) {
        this.complementNom = complementNom;
    }
}
