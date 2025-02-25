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

public class ExcelPvReaderUtil {

    public static int getStartRow() {
        return 3;
    }

    public static ActionResult lireFichierExcel(String filePath, CommandModel commande) {
        ActionResult retour;
        try{
            retour = lireGeneral(filePath, commande);
            retour = retour.plus(lireDetails(filePath, getStartRow(), commande));
        } catch (Exception e) {
            retour = new ActionResult(false, filePath + " KO : " + e.getMessage());
            e.printStackTrace();
        }
        return retour;
    }

    private static ActionResult lireGeneral(String filePath, CommandModel commande) {
        ActionResult retour = new ActionResult(true, "");

            try (FileInputStream fis = new FileInputStream(filePath);
                 Workbook workbook = new XSSFWorkbook(fis)) {

                Sheet sheet = workbook.getSheet("PV");
                Row row11 = sheet.getRow(10);
                Cell cellB11 = row11.getCell(1);
                commande.setObjetDeLaPrestationSuffixe(cellB11.getStringCellValue());

                Row row9 = sheet.getRow(8);
                Cell cellB9 = row11.getCell(1);
                commande.setBonDeCommande(cellB9.getStringCellValue());
            } catch (IOException e) {
                retour = new ActionResult(false, e.getMessage());
                e.printStackTrace();
            }

            return retour;
        }

    private static ActionResult lireDetails(String filePath, int startRow, CommandModel commande) {
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
                if(headerRow1.getCell(cell.getColumnIndex()) != null) {
                    if(!headerRow1.getCell(cell.getColumnIndex()).getStringCellValue().isEmpty()) {
                        header1 = headerRow1.getCell(cell.getColumnIndex()).getStringCellValue();
                    }
                }
                String header = header1 + " - " + cell.getStringCellValue();
                headerMap.put(header, cell.getColumnIndex());
            }

            List<UoCommandLineModel> listeLigneCommande = new ArrayList<>();

            // Lire les données à partir de startRow
            for (int i = startRow; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    Cell firstCell = row.getCell(0);
                    if (firstCell == null || firstCell.getCellType() == CellType.BLANK) {
                        break; // Exit the loop if the first cell is empty
                    }

                    UoCommandLineModel ligneCommande = new UoCommandLineModel();
                    for (Map.Entry<String, Integer> entry : headerMap.entrySet()) {
                        Cell cell = row.getCell(entry.getValue());
                        if (cell != null) {
                            switch (entry.getKey()) {
                                case "Détails de la Commande - Libellé de la ligne de commande":
                                    ligneCommande.setLibelle(cell.getStringCellValue());
                                    break;
                                case "Détails de la Commande - Type d’UO":
                                    ligneCommande.setTypeUO(cell.getStringCellValue());
                                    break;
                                case "Détails de la Commande - Prix Unitaire UO HT":
                                    ligneCommande.setPrixUnitaire(cell.getNumericCellValue());
                                    break;
                                case "Détails de la Commande - Nombre d’UO":
                                    ligneCommande.setNombreUO((int) cell.getNumericCellValue());
                                    break;
                                case "Détails de la Commande - Taux de TVA":
                                    ligneCommande.setTVA(cell.getNumericCellValue());
                                    break;
                                case "Total des PV signés (supposés facturés) - Nombre d’UO":
                                    ligneCommande.getTotalPV().setNombre((int) cell.getNumericCellValue());
                                    break;
                                case "Montant du PV (au jalon considéré) - Nombre d’UO":
                                    ligneCommande.getMontantPV().setNombre((int) cell.getNumericCellValue());
                                    break;
                                case "Reste à dépenser (après ce PV) - Nombre d’UO":
                                    ligneCommande.getResteADepenserPV().setNombre((int) cell.getNumericCellValue());
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                    listeLigneCommande.add(ligneCommande);
                }
            }

            commande.setListeLigneCommande(listeLigneCommande);

        } catch (IOException e) {
            retour = new ActionResult(false, e.getMessage());
            e.printStackTrace();
        }

        return retour;
    }
}