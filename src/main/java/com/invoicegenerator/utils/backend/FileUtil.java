package com.invoicegenerator.utils.backend;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utilitaire pour les opérations sur les fichiers.
 */
public class FileUtil {
    private static final Logger logger = LoggerFactory.getLogger(FileUtil.class.getName());

    /**
     * Ajoute un suffixe au nom de fichier avant l'extension.
     * @param filePath Le chemin du fichier.
     * @param suffix Le suffixe à ajouter.
     * @return Le chemin complet du fichier avec le suffixe ajouté.
     */
    public static String addSuffixToFileName(String filePath, String suffix) {
        logger.info(String.format("Ajout du suffixe '%s' au fichier : %s", suffix, filePath));
        File file = new File(filePath);
        String name = file.getName();
        int lastDot = name.lastIndexOf('.');

        if (lastDot == -1) {
            String newPath = new File(file.getParent(), name + suffix).getAbsolutePath();
            logger.info("Nouveau chemin de fichier : " + newPath);
            return newPath;
        }

        String baseName = name.substring(0, lastDot);
        String extension = name.substring(lastDot);
        String newPath = new File(file.getParent(), baseName + suffix + extension).getAbsolutePath();
        logger.log(Level.INFO, "Nouveau chemin de fichier : {0}", newPath);
        return newPath;
    }

    /**
     * Concatène un chemin et un nom de fichier.
     * @param path Le chemin du répertoire.
     * @param fileName Le nom du fichier.
     * @return Le chemin complet du fichier.
     */
    public static String concat(String path, String fileName) {
        logger.info(String.format("Concaténation du chemin : %s avec le nom de fichier : %s", path, fileName));
        Path fullPath = Paths.get(path, fileName);
        String result = fullPath.toString();
        logger.log(Level.INFO, "Chemin complet du fichier : {0}", result);
        return result;
    }
}