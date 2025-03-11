package com.invoicegenerator.services;

import com.invoicegenerator.modeles.Langue;

import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

// Service pour gérer les mois
public class MoisService {
    private static final Logger logger = Logger.getLogger(MoisService.class.getName());

    // Tableaux des mois dans les deux langues (index 0 = janvier/January, etc.)
    private static final String[] MOIS_FRANCAIS = {
            "Janvier", "Février", "Mars", "Avril", "Mai", "Juin",
            "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre"
    };

    private static final String[] MOIS_ANGLAIS = {
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
    };

    /**
     * Extrait le mois d'une LocalDate et le retourne dans la langue spécifiée
     * @param date La LocalDate contenant le mois
     * @param langue La langue souhaitée (FRANCAIS ou ANGLAIS)
     * @return Le nom du mois dans la langue demandée
     */
    public String extraireMois(LocalDate date, Langue langue) {
        if (date == null) {
            logger.log(Level.WARNING, "Date fournie est null");
            return "";
        }

        // getMonthValue() retourne un int de 1 à 12, on soustrait 1 pour l'index du tableau (0-11)
        int moisIndex = date.getMonthValue() - 1;

        String mois = switch (langue) {
            case FRANCAIS -> MOIS_FRANCAIS[moisIndex];
            case ANGLAIS -> MOIS_ANGLAIS[moisIndex];
            default -> {
                logger.log(Level.WARNING, "Langue non supportée : {0}", langue);
                yield "";
            }
        };

        logger.log(Level.FINE, "Récupération du mois : {0} pour la date {1} en langue {2}",
                new Object[]{mois, date.toString(), langue.toString()});

        return mois;
    }

    // Méthode utilitaire pour capitaliser la première lettre si nécessaire
    private String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    // Exemple d'utilisation
    public static void main(String[] args) {
        MoisService service = new MoisService();
        LocalDate date = LocalDate.of(2025, 3, 11); // 11 mars 2025

        System.out.println(service.extraireMois(date, Langue.FRANCAIS)); // Affiche "Mars"
        System.out.println(service.extraireMois(date, Langue.ANGLAIS));  // Affiche "March"
    }
}