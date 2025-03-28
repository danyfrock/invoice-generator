package com.invoicegenerator.services;

import java.time.LocalDate;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Service dédié à l'extraction du mois à partir d'un objet LocalDate.
 * Ce service utilise des fichiers de ressources pour retourner le nom du mois
 * dans la langue définie par une Locale (par exemple, "mars" en français ou "March" en anglais).
 */
public class MoisService {
    private static final Logger logger = Logger.getLogger(MoisService.class.getName());

    /**
     * Extrait le mois d'une LocalDate et le retourne dans la langue spécifiée
     * @param date La LocalDate contenant le mois
     * @param locale La locale souhaitée (Locale.FRANCE, Locale.ENGLISH, etc.)
     * @return Le nom du mois dans la langue demandée
     */
    public String extraireMois(LocalDate date, Locale locale) {
        if (date == null) {
            logger.log(Level.WARNING, "Date fournie est null");
            return "";
        }

        ResourceBundle bundle = ResourceBundle.getBundle("mois", locale);
        String monthKey = date.getMonth().name().toLowerCase(); // "january", "february", etc.

        return bundle.getString(monthKey);
    }

    public static void main(String[] args) {
        MoisService service = new MoisService();
        LocalDate date = LocalDate.of(2025, 3, 11); // 11 mars 2025

        logger.fine(service.extraireMois(date, Locale.FRANCE));
        logger.fine(service.extraireMois(date, Locale.ENGLISH));
    }
}
