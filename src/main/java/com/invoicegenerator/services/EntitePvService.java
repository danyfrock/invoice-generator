package com.invoicegenerator.services;

import com.invoicegenerator.modeles.ActionResult;
import com.invoicegenerator.modeles.CommandModel;
import com.invoicegenerator.modeles.PvEntityPvModel;
import com.invoicegenerator.utils.ExcelPvReaderUtil;

import java.io.File;

public class EntitePvService {
    public PvEntityPvModel GetPvFrom(String path) {
        PvEntityPvModel pv = new PvEntityPvModel();

        return FillPvFrom(path, pv);
    }

    public PvEntityPvModel FillPvFrom(String path, PvEntityPvModel pv) {
        CommandModel commande = pv.getCommand();
        pv.setFilePath(path);
        pv.setFileName(new File(path).getName());
        ActionResult ar = ExcelPvReaderUtil.lireFichierExcel(path, commande);

        if(!ar.success()){
            pv.setFilePath(ar.message());
        }

        return pv;
    }
}
