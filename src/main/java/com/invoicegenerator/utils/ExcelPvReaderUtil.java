package com.invoicegenerator.utils;

import com.invoicegenerator.modeles.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Utilitaire pour lire les données des fichiers Excel pour les entités Pv.
 */
public class ExcelPvReaderUtil {
    private static final Logger logger = Logger.getLogger(ExcelPvReaderUtil.class.getName());

    /**
     * Obtient l'index de la ligne de départ pour la lecture des données.
     * @return L'index de la ligne de départ.
     */
    public static int getStartRow() {
        return 3;
    }

    /**
     * Lit les données d'un fichier Excel et les charge dans un modèle de commande.
     * @param filePath Le chemin du fichier Excel.
     * @param commande Le modèle de commande à remplir.
     * @return Un résultat d'action indiquant le succès ou l'échec de l'opération.
     */
    public static ActionResult lireFichierExcel(String filePath, CommandModel commande) {
        logger.log(Level.INFO, "Début de la lecture du fichier Excel : {0}", filePath);
        ActionResult retour;
        try {
            retour = lireGeneral(filePath, commande);
            retour = retour.plus(lireDetails(filePath, getStartRow(), commande));
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erreur lors de la lecture du fichier Excel : {0}", e.getMessage());
            retour = new ActionResult(false, filePath + " KO : " + e.getMessage());
            e.printStackTrace();
        }
        return retour;
    }

    /**
     * Lit les informations générales d'un fichier Excel et les charge dans un modèle de commande.
     * @param filePath Le chemin du fichier Excel.
     * @param commande Le modèle de commande à remplir.
     * @return Un résultat d'action indiquant le succès ou l'échec de l'opération.
     */
    private static ActionResult lireGeneral(String filePath, CommandModel commande) {
        logger.log(Level.INFO, "Lecture des informations générales du fichier Excel : {0}", filePath);
        ActionResult retour = new ActionResult(true, "");

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet("PV");
            Row row11 = sheet.getRow(10);
            Cell cellB11 = row11.getCell(1);
            commande.setBenefitPurposeCellB11(getStringCellValue(cellB11));

            Row row9 = sheet.getRow(8);
            Cell cellB9 = row9.getCell(1);
            commande.setBonDeCommandeCellB9(String.valueOf(getNumericCellValue(cellB9)));

            logger.log(Level.INFO, "Informations générales lues avec succès");

        } catch (IOException e) {
            logger.log(Level.SEVERE, "Erreur lors de la lecture des informations générales : {0}", e.getMessage());
            retour = new ActionResult(false, e.getMessage());
            e.printStackTrace();
        }

        return retour;
    }

    /**
     * Lit les détails des prestations d'un fichier Excel et les charge dans un modèle de commande.
     * @param filePath Le chemin du fichier Excel.
     * @param startRow L'index de la ligne de départ pour la lecture des détails.
     * @param commande Le modèle de commande à remplir.
     * @return Un résultat d'action indiquant le succès ou l'échec de l'opération.
     */
    private static ActionResult lireDetails(String filePath, int startRow, CommandModel commande) {
        logger.log(Level.INFO, "Lecture des détails des prestations du fichier Excel : {0}", filePath);
        ActionResult retour = new ActionResult(true, "");

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet("Détails des prestations du PV");

            // Lire les en-têtes
            Row headerRow1 = sheet.getRow(startRow - 2);
            Row headerRow2 = sheet.getRow(startRow - 1);
            Map<String, Integer> headerMap = new HashMap<>();
            String header1 = "";
            for (Cell cell : headerRow2) {
                if (headerRow1.getCell(cell.getColumnIndex()) != null) {
                    if (!getStringCellValue(headerRow1.getCell(cell.getColumnIndex())).isEmpty()) {
                        header1 = getStringCellValue(headerRow1.getCell(cell.getColumnIndex()));
                    }
                }
                String header = header1 + " - " + getStringCellValue(cell);
                headerMap.put(header, cell.getColumnIndex());
            }

            List<UoCommandLineModel> listeLigneCommande = new ArrayList<>();

            // Lire les données à partir de startRow
            for (int i = startRow; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    Cell firstCell = row.getCell(0);
                    if (firstCell == null || firstCell.getCellType() == CellType.BLANK) {
                        break; // Sortir de la boucle si la première cellule est vide
                    }

                    UoCommandLineModel ligneCommande = new UoCommandLineModel();
                    for (Map.Entry<String, Integer> entry : headerMap.entrySet()) {
                        Cell cell = row.getCell(entry.getValue());
                        if (cell != null) {
                            switch (entry.getKey()) {
                                case "Détails de la Commande - Libellé de la ligne de commande":
                                    ligneCommande.setCommandLabel(getStringCellValue(cell));
                                    break;
                                case "Détails de la Commande - Type d’UO":
                                    ligneCommande.setUoType(getStringCellValue(cell));
                                    break;
                                case "Détails de la Commande - Prix Unitaire UO HT":
                                    ligneCommande.setUnitPrice(getNumericCellValue(cell));
                                    break;
                                case "Détails de la Commande - Nombre d’UO":
                                    ligneCommande.setUoNumber((int) getNumericCellValue(cell));
                                    break;
                                case "Détails de la Commande - Taux de TVA":
                                    ligneCommande.setTVA(getNumericCellValue(cell));
                                    break;
                                case "Total des PV signés (supposés facturés) - Nombre d’UO":
                                    ligneCommande.getUoTotal().setNumber((int) getNumericCellValue(cell));
                                    break;
                                case "Montant du PV (au jalon considéré) - Nombre d’UO":
                                    ligneCommande.getUoCost().setNumber((int) getNumericCellValue(cell));
                                    break;
                                case "Reste à dépenser (après ce PV) - Nombre d’UO":
                                    ligneCommande.getUoToSpend().setNumber((int) getNumericCellValue(cell));
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                    listeLigneCommande.add(ligneCommande);
                }
            }

            commande.setCommandLines(listeLigneCommande);
            logger.log(Level.INFO, "Détails des prestations lus avec succès");

        } catch (IOException e) {
            logger.log(Level.SEVERE, "Erreur lors de la lecture des détails des prestations : {0}", e.getMessage());
            retour = new ActionResult(false, e.getMessage());
            e.printStackTrace();
        }

        return retour;
    }

    /**
     * Récupère la valeur numérique d'une cellule, avec gestion des types et des erreurs.
     * @param cell La cellule à lire
     * @return La valeur numérique sous forme de double, ou 0.0 si la cellule est vide ou invalide
     */
    private static double getNumericCellValue(Cell cell) {
        if (cell == null || cell.getCellType() == CellType.BLANK) {
            logger.log(Level.FINE, "Cellule vide ou null, retour de 0.0");
            return 0.0;
        }

        try {
            switch (cell.getCellType()) {
                case NUMERIC:
                    return cell.getNumericCellValue();
                case STRING:
                    String value = cell.getStringCellValue().trim();
                    if (value.isEmpty()) {
                        logger.log(Level.FINE, "Chaîne vide dans la cellule, retour de 0.0");
                        return 0.0;
                    }
                    try {
                        return Double.parseDouble(value);
                    } catch (NumberFormatException e) {
                        logger.log(Level.WARNING, "Impossible de convertir la chaîne '{0}' en nombre, retour de 0.0", value);
                        return 0.0;
                    }
                case BOOLEAN:
                    return cell.getBooleanCellValue() ? 1.0 : 0.0;
                case FORMULA:
                    // Évalue la formule si elle donne un résultat numérique
                    Workbook workbook = cell.getSheet().getWorkbook();
                    FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
                    CellValue cellValue = evaluator.evaluate(cell);
                    if (cellValue.getCellType() == CellType.NUMERIC) {
                        return cellValue.getNumberValue();
                    } else {
                        logger.log(Level.WARNING, "Formule non numérique dans la cellule, retour de 0.0");
                        return 0.0;
                    }
                default:
                    logger.log(Level.WARNING, "Type de cellule non géré : {0}, retour de 0.0", cell.getCellType());
                    return 0.0;
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erreur lors de la lecture de la valeur numérique de la cellule : {0}", e.getMessage());
            return 0.0;
        }
    }

    /**
     * Récupère la valeur sous forme de chaîne d'une cellule, avec gestion des types et des erreurs.
     * @param cell La cellule à lire
     * @return La valeur sous forme de chaîne, ou "" si la cellule est vide ou invalide
     */
    private static String getStringCellValue(Cell cell) {
        if (cell == null || cell.getCellType() == CellType.BLANK) {
            logger.log(Level.FINE, "Cellule vide ou null, retour de chaîne vide");
            return "";
        }

        try {
            switch (cell.getCellType()) {
                case STRING:
                    return cell.getStringCellValue().trim();
                case NUMERIC:
                    double numValue = cell.getNumericCellValue();
                    // Éviter les décimales inutiles pour les entiers
                    if (numValue == (int) numValue) {
                        return String.valueOf((int) numValue);
                    } else {
                        return String.valueOf(numValue);
                    }
                case BOOLEAN:
                    return String.valueOf(cell.getBooleanCellValue());
                case FORMULA:
                    Workbook workbook = cell.getSheet().getWorkbook();
                    FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
                    CellValue cellValue = evaluator.evaluate(cell);
                    switch (cellValue.getCellType()) {
                        case STRING:
                            return cellValue.getStringValue().trim();
                        case NUMERIC:
                            double formulaNum = cellValue.getNumberValue();
                            if (formulaNum == (int) formulaNum) {
                                return String.valueOf((int) formulaNum);
                            } else {
                                return String.valueOf(formulaNum);
                            }
                        case BOOLEAN:
                            return String.valueOf(cellValue.getBooleanValue());
                        default:
                            logger.log(Level.WARNING, "Résultat de formule non géré : {0}, retour de chaîne vide", cellValue.getCellType());
                            return "";
                    }
                default:
                    logger.log(Level.WARNING, "Type de cellule non géré : {0}, retour de chaîne vide", cell.getCellType());
                    return "";
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erreur lors de la lecture de la valeur textuelle de la cellule : {0}", e.getMessage());
            return "";
        }
    }
}