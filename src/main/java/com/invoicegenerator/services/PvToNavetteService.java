package com.invoicegenerator.services;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.invoicegenerator.modeles.PvEntityPvModel;
import com.invoicegenerator.modeles.UoCommandLineModel;
import com.invoicegenerator.modeles.BillingShuttleModel;

/**
 * Service pour convertir les entités Pv en modèles de navette de facturation.
 */
public class PvToNavetteService {
	private static final Logger logger = Logger.getLogger(PvToNavetteService.class.getName());

	/**
	 * Convertit un tableau d'entités PvEntityPvModel en un tableau de BillingShuttleModel.
	 * @param entites Les entités PvEntityPvModel à convertir.
	 * @return Un tableau de BillingShuttleModel.
	 */
	public BillingShuttleModel[] Convertir(PvEntityPvModel[] entites) {
		logger.info("Début de la conversion des entités Pv en modèles de navette de facturation");
		List<BillingShuttleModel> navettesList = new ArrayList<>();
		int i = 0;
		for (PvEntityPvModel entite : entites) {
			i++;
			logger.info("Traitement de l'entité Pv numéro : " + i);
			for (UoCommandLineModel ligne : entite.getCommand().getCommandLines()) {
				BillingShuttleModel navette = new BillingShuttleModel();

				navette.setPcBu("EU005");
				navette.setMeasureUnit("UNT = Units");
				navette.setActivity(entite.getCommand().getActivityCode());
				navette.setProject(entite.getCommand().getContractCode());

				/*
					o	<Bon de commande>+<Objet de la prestation>+ <Référence UO> + <Description UO>
					o	Ces informations sont issues des PVs :
					A:	<Bon de commande>  Texte « BDC PO» + Onglet « PV » cellule B9  Exemple « BDC PO1000376967 »
					B:	<Objet de la prestation>  Onglet « PV » cellule B11  Exemple « Accompagnement Architecture applicative »
					C:	<Référence UO>  Onglet « Détails des prestations du PV » colonne « Type d’UO »  Exemple : « L5 – P15.1.re.a »
					D:	<Description UO>  Onglet « Détails des prestations du PV » colonne « Libellé de la ligne de commande»  Exemple : « Architecture Applicative Simple»
				 */
				navette.setEventNote(
						entite.getCommand().getBonDeCommandePrefix() + entite.getCommand().getBonDeCommandeCellB9() + // A
								"-" + entite.getCommand().getBenefitPurposeCellB11() + // B
								"-" + ligne.getUoType() + // C
								"- " + ligne.getCommandLabel()); // D

				navette.setBillAmount(ligne.getUoTotal().getTotalTTC());
				navette.setUnitPrice(ligne.getUnitPrice());
				navette.setQuantity(ligne.getUoTotal().getNumber());
				navette.setCalculatedEventAmount(ligne.getUoTotal().getTotalTTC());
				navette.setBillPeriodFrom(entite.getCommand().getDateDebut());
				navette.setBillPeriodTo(entite.getCommand().getDateFin());
				navette.setBillNumber(i);
				navette.setItemId(ligne.getCommandLabel() + "-" + i);
				navette.setBillNumber(i);

				logger.info("Ajout de la navette pour la ligne de commande : " + ligne.getCommandLabel());
				navettesList.add(navette);
			}
		}
		BillingShuttleModel[] navettesArray = new BillingShuttleModel[navettesList.size()];
		logger.info("Conversion terminée avec succès");
		return navettesList.toArray(navettesArray);
	}
}