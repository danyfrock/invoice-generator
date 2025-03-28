package com.invoicegenerator.services;

import com.invoicegenerator.modeles.ActionResult;
import com.invoicegenerator.modeles.CommandModel;
import com.invoicegenerator.modeles.PvEntityPvModel;
import com.invoicegenerator.utils.backend.ExcelPvReaderUtil;
import com.invoicegenerator.utils.backend.LoggerFactory;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Service pour gérer les entités Pv.
 */
public class EntitePvService {
    private static final Logger logger = LoggerFactory.getLogger(EntitePvService.class.getName());

    /**
     * Remplit un modèle PvEntityPv à partir d'un chemin de fichier.
     * @param path Le chemin du fichier.
     * @param pv Le modèle PvEntityPv à remplir.
     * @return Le modèle PvEntityPv rempli.
     */
    public PvEntityPvModel FillPvFrom(String path, PvEntityPvModel pv) {
        logger.log(Level.INFO, "Début de FillPvFrom avec le chemin : {0}", path);
        CommandModel commande = pv.getCommand();
        pv.setFilePath(path);
        pv.setFileName(new File(path).getName());
        ActionResult ar = ExcelPvReaderUtil.lireFichierExcel(path, commande);

        if (!ar.success()) {
            logger.log(Level.WARNING,"Échec de la lecture du fichier Excel : {0}", ar.message());
            pv.setFilePath(ar.message());
        } else {
            logger.info("Lecture du fichier Excel réussie");
        }

        logger.info("Fin de FillPvFrom avec succès");
        return pv;
    }
}