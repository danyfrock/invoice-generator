package com.invoicegenerator.utils.backend;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utilitaire pour les opérations sur les fichiers.
 */
public class FileUtil {
    private static final Logger logger = LoggerFactory.getLogger(FileUtil.class.getName());

    private  FileUtil() { throw new IllegalStateException("Utility class");}

    /**
     * Ajoute un suffixe au nom de fichier avant l'extension.
     * @param filePath Le chemin du fichier.
     * @param suffix Le suffixe à ajouter.
     * @return Le chemin complet du fichier avec le suffixe ajouté.
     */
    public static String addSuffixToFileName(String filePath, String suffix) {
        String message = MessageFormat.format("Ajout du suffixe ''{0}'' au fichier : ''{1}''", suffix, filePath);
        logger.info(message);

        File file = new File(filePath);
        String name = file.getName();
        int lastDot = name.lastIndexOf('.');

        if (lastDot == -1) {
            String newPath = new File(file.getParent(), name + suffix).getAbsolutePath();
            logger.log(Level.INFO, "Nouveau chemin de fichier : {0}", newPath);
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
        logger.log(Level.INFO, "Concaténation du chemin : {0} avec le nom de fichier : {1}", new Object[]{path, fileName});
        Path fullPath = Paths.get(path, fileName);
        String result = fullPath.toString();
        logger.log(Level.INFO, "Chemin complet du fichier : {0}", result);
        return result;
    }
}