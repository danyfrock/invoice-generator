package com.invoicegenerator.services;

import java.util.ArrayList;
import java.util.List;

import com.invoicegenerator.modeles.PvEntityPvModel;
import com.invoicegenerator.modeles.UoCommandLineModel;
import com.invoicegenerator.modeles.BillingShuttleModel;

public class PvToNavetteService {
	public BillingShuttleModel[] Convertir(PvEntityPvModel[] entites) {
	    List<BillingShuttleModel> navettesList = new ArrayList<>();
		int i = 0;
	    for (PvEntityPvModel entite : entites) {
			i++;
	        for (UoCommandLineModel ligne : entite.getCommande().getListeLigneCommande()) {
	            BillingShuttleModel navette = new BillingShuttleModel();

				navette.setPcBu("EU005");
				navette.setUniteMesure("UNT = Units");
	            navette.setActivite(entite.getCommande().getCodeActivite());
				navette.setProjet(entite.getCommande().getCodeContrat());
				navette.setNoteEvenement(
						entite.getCommande().getBonDeCommande()+"-"+entite.getCommande().getObjetDeLaPrestation() +
						"-" + ligne.getTypeUO() + "- " + ligne.getLibelle());
				navette.setMontantFacturation(ligne.getTotalPV().getTotalTTC());
				navette.setPrixUnitaire(ligne.getPrixUnitaire());
				navette.setQuantite(ligne.getTotalPV().getNombre());
				navette.setMontantEvenementCalcule(ligne.getTotalPV().getTotalTTC());
				navette.setPeriodeFacturationDu(entite.getCommande().getDateDebut());
				navette.setPeriodeFacturationAu(entite.getCommande().getDateFin());
				navette.setNombreFactures(i);
				navette.setItemId(ligne.getLibelle() + "-" + i);
				navette.setNombreFactures(i);

	            navettesList.add(navette);
	        }
	    }
	    BillingShuttleModel[] navettesArray = new BillingShuttleModel[navettesList.size()];
	    return navettesList.toArray(navettesArray);
	}
}
