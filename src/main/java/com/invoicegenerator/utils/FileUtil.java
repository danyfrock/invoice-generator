package com.invoicegenerator.utils;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtil {
    public static String addSuffixToFileName(String filePath, String suffix) {
        File file = new File(filePath);
        String name = file.getName();
        int lastDot = name.lastIndexOf('.');

        if (lastDot == -1) {
            return new File(file.getParent(), name + suffix).getAbsolutePath();
        }

        String baseName = name.substring(0, lastDot);
        String extension = name.substring(lastDot);
        return new File(file.getParent(), baseName + suffix + extension).getAbsolutePath();
    }

    public static String concat(String path, String fileName) {
        Path fullPath = Paths.get(path, fileName);
        return fullPath.toString();
    }
}
