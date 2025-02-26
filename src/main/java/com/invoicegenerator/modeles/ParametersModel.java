package com.invoicegenerator.modeles;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * Represents the parameters model which contains various configuration settings for the invoice generator.
 */
public class ParametersModel {
    private String outputFolder = Paths.get(System.getProperty("user.home"), "Desktop").toString();
    private List<String> activityCodes = Arrays.asList("30001", "30003", "30005", "30007", "30009");
    private int minYear = 2024;
    private int maxYear = 2029;
    private String outputFileDefaultName = "WST-CO_.xlsm";
    private String parametersFileName = Paths.get(System.getProperty("user.home"), "") + "\\parametres.json";

    /**
     * Default constructor for ParametersModel.
     */
    public ParametersModel() {
    }

    /**
     * Gets the output folder location.
     *
     * @return The output folder location.
     */
    public String getOutputFolder() {
        return outputFolder;
    }

    /**
     * Sets the output folder location.
     *
     * @param outputFolder The output folder location to set.
     */
    public void setOutputFolder(String outputFolder) {
        this.outputFolder = outputFolder;
    }

    /**
     * Gets the list of activity codes.
     *
     * @return A list of activity codes.
     */
    public List<String> getActivityCodes() {
        return activityCodes;
    }

    /**
     * Gets the minimum year.
     *
     * @return The minimum year.
     */
    public int getMinYear() {
        return minYear;
    }

    /**
     * Sets the minimum year.
     *
     * @param minYear The minimum year to set.
     */
    public void setMinYear(int minYear) {
        this.minYear = minYear;
    }

    /**
     * Gets the maximum year.
     *
     * @return The maximum year.
     */
    public int getMaxYear() {
        return maxYear;
    }

    /**
     * Sets the maximum year.
     *
     * @param maxYear The maximum year to set.
     */
    public void setMaxYear(int maxYear) {
        this.maxYear = maxYear;
    }

    /**
     * Gets the default output file name.
     *
     * @return The default output file name.
     */
    public String getOutputFileDefaultName() {
        return outputFileDefaultName;
    }

    /**
     * Gets the parameters file name.
     *
     * @return The parameters file name.
     */
    public String getParametersFileName() {
        return parametersFileName;
    }

    /**
     * Returns a string representation of the ParametersModel.
     *
     * @return A string representation of the ParametersModel.
     */
    @Override
    public String toString() {
        return "ParametresModele{" +
                "emplacementDossierSortie='" + outputFolder + '\'' +
                ", codesActivite=" + activityCodes +
                ", anneeMin=" + minYear +
                ", anneeMax=" + maxYear +
                '}';
    }
}