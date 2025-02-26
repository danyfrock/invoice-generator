package com.invoicegenerator.modeles;

/**
 * Represents the PV entity model which contains file details and a command model.
 */
public class PvEntityPvModel {
    private String fileName = "";
    private String filePath = "";
    private final CommandModel command = new CommandModel();

    /**
     * Gets the file name.
     *
     * @return The file name.
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Sets the file name.
     *
     * @param fileName The file name to set.
     * @return The current instance of PvEntityPvModel.
     */
    public PvEntityPvModel setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    /**
     * Gets the file path.
     *
     * @return The file path.
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * Sets the file path.
     *
     * @param filePath The file path to set.
     * @return The current instance of PvEntityPvModel.
     */
    public PvEntityPvModel setFilePath(String filePath) {
        this.filePath = filePath;
        return this;
    }

    /**
     * Gets the command model.
     *
     * @return The command model.
     */
    public CommandModel getCommand() {
        return command;
    }
}