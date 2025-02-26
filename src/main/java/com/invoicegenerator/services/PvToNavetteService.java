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
	        for (UoCommandLineModel ligne : entite.getCommand().getCommandLines()) {
	            BillingShuttleModel navette = new BillingShuttleModel();

				navette.setPcBu("EU005");
				navette.setMeasureUnit("UNT = Units");
	            navette.setActivity(entite.getCommand().getActivityCode());
				navette.setProject(entite.getCommand().getContractCode());
				navette.setEventNote(
						entite.getCommand().getOrderForm()+"-"+entite.getCommand().getBenefitPurpose() +
						"-" + ligne.getUoType() + "- " + ligne.getCommandLabel());
				navette.setBillAmount(ligne.getUoTotal().getTotalTTC());
				navette.setUnitPrice(ligne.getUnitPrice());
				navette.setQuantity(ligne.getUoTotal().getNumber());
				navette.setCalculatedEventAmount(ligne.getUoTotal().getTotalTTC());
				navette.setBillPeriodFrom(entite.getCommand().getDateDebut());
				navette.setBillPeriodTo(entite.getCommand().getDateFin());
				navette.setBillNumber(i);
				navette.setItemId(ligne.getCommandLabel() + "-" + i);
				navette.setBillNumber(i);

	            navettesList.add(navette);
	        }
	    }
	    BillingShuttleModel[] navettesArray = new BillingShuttleModel[navettesList.size()];
	    return navettesList.toArray(navettesArray);
	}
}
