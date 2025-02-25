package com.invoicegenerator.utils;

import com.invoicegenerator.modeles.ActionResult;
import com.invoicegenerator.viewModels.NavetteFacturationViewModel;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class ExcelNavetteWritterUtil {

    private static final int HEADER_ROW_INDEX = 3; // Ligne 4 en Excel (index 3 en Java)

    public static ActionResult writeNewTemplate(String sortie){
        ActionResult result = new ActionResult(true, "");
        InputStream inputStream = ExcelNavetteWritterUtil.class.getClassLoader().getResourceAsStream("WST-CO_.xlsm");
        try {
            if (inputStream != null) {
                Files.copy(inputStream, Paths.get(sortie), StandardCopyOption.REPLACE_EXISTING);
            }
            else {
                throw new FileNotFoundException("fichier ressource non trouvé");
            }
        } catch (IOException e) {
            result = new ActionResult(false, "Erreur lors de l'écriture du fichier Excel : " + e.getMessage());
        }

        return result;
    }

    public static ActionResult writeNavette(List<NavetteFacturationViewModel> liste, String pathExcel, String sheetName) {
        return writeNewTemplate(pathExcel).plus(writeAllNavettes(liste, pathExcel,sheetName));
    }

    private static ActionResult writeAllNavettes(List<NavetteFacturationViewModel> liste, String pathExcel, String sheetName) {
        try (FileInputStream fis = new FileInputStream(pathExcel);
             Workbook workbook = new XSSFWorkbook(fis)) {
            
            Sheet sheet = workbook.getSheet(sheetName); // Utiliser le nom de la feuille spécifié
            if (sheet == null) {
                return new ActionResult(false, "La feuille " + sheetName + " n'existe pas dans le fichier Excel.");
            }
            
            // Trouver la première ligne vide
            int rowNum = findFirstEmptyRow(sheet);

            // Écriture des données
            for (NavetteFacturationViewModel navette : liste) {
                Row row = sheet.getRow(rowNum);
                if (row == null) {
                    row = sheet.createRow(rowNum);
                }
                rowNum++;
                updateCell(row, 0, navette.getPcBu());
                updateCell(row, 1, navette.getProjet());
                updateCell(row, 2, navette.getActivite());
                updateCell(row, 3, String.valueOf(navette.getNombreFactures()));
                updateCell(row, 4, navette.getNoteEvenement());
                updateCell(row, 5, navette.getQuantite());
                updateCell(row, 6, navette.getUniteMesure());
                updateCell(row, 7, navette.getPrixUnitaire());
                updateCell(row, 8, navette.getMontantFacturation());
                updateCell(row, 9, navette.getMontantEvenementCalcule());
                updateCell(row, 10, navette.getJourPeriodeFacturationDu());
                updateCell(row, 11, navette.getMoisTextePeriodeFacturationDu());
                updateCell(row, 12, navette.getAnneePeriodeFacturationDu());
                updateCell(row, 13, navette.getJourPeriodeFacturationAu());
                updateCell(row, 14, navette.getMoisTextePeriodeFacturationAu());
                updateCell(row, 15, navette.getAnneePeriodeFacturationAu());
                updateCell(row, 16, navette.getItemId());
                updateCell(row, 17, navette.getFactureInitiale());
            }

            // Sauvegarde du fichier
            try (FileOutputStream fileOut = new FileOutputStream(pathExcel)) {
                workbook.write(fileOut);
            }

            return new ActionResult(true, "Données ajoutées avec succès à : " + pathExcel);

        } catch (IOException e) {
            return new ActionResult(false, "Erreur lors de l'écriture du fichier Excel : " + e.getMessage());
        }
    }

    private static void updateCell(Row row, int cellIndex, double value) {
        Cell cell = row.getCell(cellIndex);
        if (cell == null) {
            cell = row.createCell(cellIndex);
        }
        cell.setCellValue(value);
    }

    private static void updateCell(Row row, int cellIndex, String value) {
        Cell cell = row.getCell(cellIndex);
        if (cell == null) {
            cell = row.createCell(cellIndex);
        }
        cell.setCellValue(value);
    }

    private static int findFirstEmptyRow(Sheet sheet) {
        int rowNum = HEADER_ROW_INDEX + 1; // Commencer après les en-têtes
        while (sheet.getRow(rowNum) != null && sheet.getRow(rowNum).getCell(0) != null
               && !sheet.getRow(rowNum).getCell(0).getStringCellValue().isEmpty()) {
            rowNum++;
        }
        return rowNum;
    }
}