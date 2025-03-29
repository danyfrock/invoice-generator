package com.invoicegenerator.utils.backend;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermissions;
import java.text.MessageFormat;
import java.util.Set;
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

    /**
     * Crée un fichier temporaire sécurisé.
     * <p>
     * Sur Unix/Linux, il restreint les permissions à l'utilisateur (rwx------).
     * Sur Windows, il utilise un dossier sécurisé.
     * </p>
     *
     * @param prefix Préfixe du fichier temporaire.
     * @param suffix Extension du fichier (ex: ".txt", ".html").
     * @return Un fichier temporaire sécurisé.
     */
    public static File createTempFile(String prefix, String suffix) {
        Path tempFile = null;
        if (isUnix()) {
            // Permissions strictes pour éviter tout accès externe
            FileAttribute<Set<java.nio.file.attribute.PosixFilePermission>> attr =
                    PosixFilePermissions.asFileAttribute(PosixFilePermissions.fromString("rwx------"));
            try {
                tempFile = Files.createTempFile(prefix, suffix, attr);
            } catch (IOException e) {
                logger.severe("Erreur lors de la création du fichier temporaire sur Unix/Linux : " + e.getMessage());
            }
        } else {
            // Windows : créer un fichier temporaire dans un dossier privé
            File secureDir = new File(System.getProperty("user.home"), "mySecureTemp");
            if (!secureDir.exists()) {
                if (!secureDir.mkdirs()) {
                    logger.severe("Impossible de créer le dossier sécurisé : " + secureDir.getAbsolutePath());
                }
            }

            if (!secureDir.setReadable(true, true)) {
                logger.severe("Impossible de définir la permission en lecture pour le dossier : " + secureDir.getAbsolutePath());
            }

            if (!secureDir.setWritable(true, true)) {
                logger.severe("Impossible de définir la permission en écriture pour le dossier : " + secureDir.getAbsolutePath());
            }

            if (!secureDir.setExecutable(false, true)) {
                logger.severe("Impossible de définir la permission d'exécution pour le dossier : " + secureDir.getAbsolutePath());
            }

            try {
                tempFile = Files.createTempFile(secureDir.toPath(), prefix, suffix);
            } catch (IOException e) {
                logger.severe("Erreur lors de la création du fichier temporaire dans le dossier sécurisé : " + e.getMessage());
            }
        }

        // Si la création du fichier a échoué, loguer une erreur.
        if (tempFile == null) {
            logger.severe("La création du fichier temporaire a échoué.");
        }

        return tempFile == null ? new File("") : tempFile.toFile();
    }

    /**
     * Vérifie si le système d'exploitation est Unix/Linux.
     *
     * @return true si Unix/Linux, false sinon.
     */
    private static boolean isUnix() {
        String os = System.getProperty("os.name").toLowerCase();
        return os.contains("nix") || os.contains("nux") || os.contains("mac");
    }
}