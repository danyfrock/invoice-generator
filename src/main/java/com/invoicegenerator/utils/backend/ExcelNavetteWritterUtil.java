package com.invoicegenerator.utils.backend;

import com.invoicegenerator.modeles.ActionResult;
import com.invoicegenerator.viewmodels.NavetteFacturationViewModel;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utilitaire pour écrire des données de navette de facturation dans un fichier Excel.
 */
public class ExcelNavetteWritterUtil {
    private static final Logger logger = LoggerFactory.getLogger(ExcelNavetteWritterUtil.class.getName());
    private static final int HEADER_ROW_INDEX = 3; // Ligne 4 en Excel (index 3 en Java)

    private  ExcelNavetteWritterUtil() { throw new IllegalStateException("Utility class");}

    /**
     * Écrit un nouveau modèle de fichier Excel à l'emplacement spécifié.
     * @param sortie Le chemin de sortie du fichier Excel.
     * @return Un résultat d'action indiquant le succès ou l'échec de l'opération.
     */
    public static ActionResult writeNewTemplate(String sortie) {
        logger.log(Level.INFO, "Début de l''écriture d''un nouveau modèle de fichier Excel à : {0}", sortie);

        ActionResult result = new ActionResult(true, "");
        InputStream inputStream =
                ExcelNavetteWritterUtil.class.getClassLoader().getResourceAsStream("WST-CO_.xlsm");

        try {
            if (inputStream != null) {
                Files.copy(inputStream, Paths.get(sortie), StandardCopyOption.REPLACE_EXISTING);
                logger.log(Level.INFO, "Modèle de fichier Excel écrit avec succès à : {0}", sortie);
            } else {
                throw new FileNotFoundException("Fichier ressource non trouvé");
            }
        } catch (IOException e) {
            String errorText = "Erreur lors de l''écriture du template fichier Excel : ";
            logger.severe(errorText + e.getMessage());
            result = new ActionResult(false, errorText + e.getMessage());
        }

        return result;
    }

    /**
     * Écrit les données de navette dans un fichier Excel.
     * @param liste La liste des données de navette à écrire.
     * @param pathExcel Le chemin du fichier Excel.
     * @param sheetName Le nom de la feuille Excel où écrire les données.
     * @return Un résultat d'action indiquant le succès ou l'échec de l'opération.
     */
    public static ActionResult writeNavette(List<NavetteFacturationViewModel> liste, String pathExcel, String sheetName) {
        logger.log(Level.INFO, "Début de l''écriture des navettes dans le fichier Excel : {0}", pathExcel);
        return writeNewTemplate(pathExcel).plus(writeAllNavettes(liste, pathExcel, sheetName));
    }

    /**
     * Écrit toutes les navettes dans la feuille Excel spécifiée.
     * @param liste La liste des données de navette à écrire.
     * @param pathExcel Le chemin du fichier Excel.
     * @param sheetName Le nom de la feuille Excel où écrire les données.
     * @return Un résultat d'action indiquant le succès ou l'échec de l'opération.
     */
    private static ActionResult writeAllNavettes(List<NavetteFacturationViewModel> liste, String pathExcel, String sheetName) {
        try (FileInputStream fis = new FileInputStream(pathExcel);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName); // Utiliser le nom de la feuille spécifié
            if (sheet == null) {
                String message = String.format("La feuille %s n'existe pas dans le fichier Excel.", sheetName);
                logger.warning(message);
                return new ActionResult(false, message);
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
                updateCell(row, 5, navette.getQuantiteAsDouble());
                updateCell(row, 6, navette.getUniteMesure());
                updateCell(row, 7, navette.getPrixUnitaire());
                row.getCell(8).setCellFormula("H" + rowNum + "*F" + rowNum);
                updateCell(row, 9, navette.getMontantEvenementCalcule()); // contient déjà une formule
                updateCell(row, 10, navette.getMoisTextePeriodeFacturationDu());
                updateCell(row, 11, navette.getJourPeriodeFacturationDu());
                updateCell(row, 12, navette.getAnneePeriodeFacturationDu());
                updateCell(row, 13, navette.getMoisTextePeriodeFacturationAu());
                updateCell(row, 14, navette.getJourPeriodeFacturationAu());
                updateCell(row, 15, navette.getAnneePeriodeFacturationAu());
                updateCell(row, 16, navette.getItemId());
                updateCell(row, 17, navette.getFactureInitiale());
            }

            // Sauvegarde du fichier
            String message = String.format("Données ajoutées avec succès à : %s", pathExcel);
            try (FileOutputStream fileOut = new FileOutputStream(pathExcel)) {
                workbook.write(fileOut);
                logger.info(message);
            }

            return new ActionResult(true, message);

        } catch (IOException e) {
            String errorText = String.format("Erreur lors de l''écriture du fichier Excel : %s", e.getMessage());
            logger.severe(errorText);
            return new ActionResult(false, errorText);
        }
    }

    /**
     * Met à jour la valeur d'une cellule avec un double.
     * @param row La ligne contenant la cellule.
     * @param cellIndex L'index de la cellule.
     * @param value La valeur à écrire dans la cellule.
     */
    private static void updateCell(Row row, int cellIndex, double value) {
        Cell cell = row.getCell(cellIndex);
        if (cell == null) {
            cell = row.createCell(cellIndex);
        }
        cell.setCellValue(value);
    }

    /**
     * Met à jour la valeur d'une cellule avec une chaîne de caractères.
     * @param row La ligne contenant la cellule.
     * @param cellIndex L'index de la cellule.
     * @param value La valeur à écrire dans la cellule.
     */
    private static void updateCell(Row row, int cellIndex, String value) {
        Cell cell = row.getCell(cellIndex);
        if (cell == null) {
            cell = row.createCell(cellIndex);
        }
        cell.setCellValue(value);
    }

    /**
     * Trouve la première ligne vide dans une feuille Excel.
     * @param sheet La feuille Excel.
     * @return L'index de la première ligne vide.
     */
    private static int findFirstEmptyRow(Sheet sheet) {
        int rowNum = HEADER_ROW_INDEX + 1; // Commencer après les en-têtes
        while (sheet.getRow(rowNum) != null && sheet.getRow(rowNum).getCell(0) != null
                && !sheet.getRow(rowNum).getCell(0).getStringCellValue().isEmpty()) {
            rowNum++;
        }
        return rowNum;
    }
}