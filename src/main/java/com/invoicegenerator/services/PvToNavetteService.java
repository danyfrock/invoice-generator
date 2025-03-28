package com.invoicegenerator.services;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.invoicegenerator.modeles.navetteDtos.BillingDetailsModel;
import com.invoicegenerator.modeles.navetteDtos.EventDetailsModel;
import com.invoicegenerator.modeles.navetteDtos.ItemDetailsModel;
import com.invoicegenerator.modeles.PvEntityPvModel;
import com.invoicegenerator.modeles.UoCommandLineModel;
import com.invoicegenerator.modeles.navetteDtos.BillingShuttleModel;
import com.invoicegenerator.utils.backend.LoggerFactory;

/**
 * Service pour convertir les entités Pv en modèles de navette de facturation.
 */
public class PvToNavetteService {
	private static final Logger logger = LoggerFactory.getLogger(PvToNavetteService.class.getName());

	/**
	 * Convertit un tableau d'entités PvEntityPvModel en un tableau de BillingShuttleModel.
	 * @param entites Les entités PvEntityPvModel à convertir.
	 * @return Un tableau de BillingShuttleModel.
	 */
	public BillingShuttleModel[] convertir(PvEntityPvModel[] entites) {
		logger.info("Début de la conversion des entités Pv en modèles de navette de facturation");
		List<BillingShuttleModel> navettesList = new ArrayList<>();
		int i = 0;
		for (PvEntityPvModel entite : entites) {
			i++;
			logger.log(Level.INFO, "Traitement de l'entité Pv numéro : {0}", i);
			for (UoCommandLineModel ligne : entite.getCommand().getCommandLines()) {
				BillingShuttleModel navette = new BillingShuttleModel()
						.setItemDetails(new ItemDetailsModel()
								.setPcBu("EU005")
								.setMeasureUnit("UNT = Units")
								.setActivity(entite.getCommand().getActivityCode())
								.setProject(entite.getCommand().getContractCode())
								.setUnitPrice(ligne.getUnitPrice())
								.setQuantity(ligne.getUoTotal().getNumber())
								.setItemId(ligne.getCommandLabel() + "-" + i)
						)
						.setEventDetails(new EventDetailsModel()
								.setEventNote(
										entite.getCommand().getBonDeCommandePrefix() +
												entite.getCommand().getBonDeCommandeCellB9() + // A
										"-" + entite.getCommand().getBenefitPurposeCellB11() + // B
										"-" + ligne.getUoType() + // C
										"- " + ligne.getCommandLabel())
								.setCalculatedEventAmount(ligne.getUoTotal().getTotalHT())
						)
						.setBillingDetails(new BillingDetailsModel()
								.setBillAmount(ligne.getUoTotal().getTotalHT())
								.setBillPeriodFrom(entite.getCommand().getDateDebut())
								.setBillPeriodTo(entite.getCommand().getDateFin())
								.setBillNumber(i)
						);

				logger.info("Ajout de la navette pour la ligne de commande : " + ligne.getCommandLabel());
				navettesList.add(navette);
			}
		}
		BillingShuttleModel[] navettesArray = new BillingShuttleModel[navettesList.size()];
		logger.info("Conversion terminée avec succès");
		return navettesList.toArray(navettesArray);
	}
}