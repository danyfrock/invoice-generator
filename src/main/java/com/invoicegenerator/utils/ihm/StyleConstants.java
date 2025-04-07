package com.invoicegenerator.utils.ihm;

/**
 * Classe utilitaire contenant des constantes de style pour l'application de génération de factures.
 * Cette classe fournit des codes de couleur et des définitions de style pour les messages de succès et d'erreur.
 */
public class StyleConstants {

    private  StyleConstants() { throw new IllegalStateException("Utility class");}

    // Couleurs en hexadécimal
    public static final String COLOR_BLEU_DOUX = "#007B7F";
    public static  final String COLOR_WHITE = "#FFFFFF";
    public static final String COLOR_VERT_CLAIR = "#90EE90";
    public static final String COLOR_ROUGE_FONCE = "#7F0000";

    // Styles pour le succès et l'erreur
    public static final String SUCCESS_FRONT_STYLE =
            "-fx-background-color: " + COLOR_BLEU_DOUX +
                    "; -fx-padding: 5px; " +
                    "-fx-background-radius: 5px; " +
                    "-fx-text-fill: " + COLOR_VERT_CLAIR + ";";
    public static final String ERROR_FRONT_STYLE =
            "-fx-background-color: " + COLOR_ROUGE_FONCE +
                    "; -fx-padding: 5px; " +
                    "-fx-background-radius: 5px; " +
                    "-fx-text-fill: " + COLOR_WHITE + ";";
}
