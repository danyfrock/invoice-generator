package com.invoicegenerator.services;

import com.invoicegenerator.modeles.ActionResult;
import com.invoicegenerator.modeles.CommandModel;
import com.invoicegenerator.modeles.PvEntityPvModel;
import com.invoicegenerator.utils.ExcelPvReaderUtil;

import java.io.File;
import java.util.logging.Logger;

/**
 * Service pour gérer les entités Pv.
 */
public class EntitePvService {
    private static final Logger logger = Logger.getLogger(EntitePvService.class.getName());

    /**
     * Obtient un modèle PvEntityPv à partir d'un chemin de fichier.
     * @param path Le chemin du fichier.
     * @return Un modèle PvEntityPv rempli.
     */
    public PvEntityPvModel GetPvFrom(String path) {
        logger.info("Début de GetPvFrom avec le chemin : " + path);
        PvEntityPvModel pv = new PvEntityPvModel();
        PvEntityPvModel result = FillPvFrom(path, pv);
        logger.info("Fin de GetPvFrom avec succès");
        return result;
    }

    /**
     * Remplit un modèle PvEntityPv à partir d'un chemin de fichier.
     * @param path Le chemin du fichier.
     * @param pv Le modèle PvEntityPv à remplir.
     * @return Le modèle PvEntityPv rempli.
     */
    public PvEntityPvModel FillPvFrom(String path, PvEntityPvModel pv) {
        logger.info("Début de FillPvFrom avec le chemin : " + path);
        CommandModel commande = pv.getCommand();
        pv.setFilePath(path);
        pv.setFileName(new File(path).getName());
        ActionResult ar = ExcelPvReaderUtil.lireFichierExcel(path, commande);

        if (!ar.success()) {
            logger.warning("Échec de la lecture du fichier Excel : " + ar.message());
            pv.setFilePath(ar.message());
        } else {
            logger.info("Lecture du fichier Excel réussie");
        }

        logger.info("Fin de FillPvFrom avec succès");
        return pv;
    }
}