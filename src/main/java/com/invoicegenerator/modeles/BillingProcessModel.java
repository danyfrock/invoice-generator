package com.invoicegenerator.modeles;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the billing process model which contains parameters, a list of PV entity models,
 * and file name details.
 */
public class BillingProcessModel {
    private ParametersModel parameters = new ParametersModel();
    private final List<PvEntityPvModel> pvEntityPvModels = new ArrayList<>();
    private String outputFileName;
    private String complement;

    /**
     * Gets the list of PV entity models.
     *
     * @return A list of PvEntityPvModel.
     */
    public List<PvEntityPvModel> getPvEntities() {
        return pvEntityPvModels;
    }

    /**
     * Gets the parameters model.
     *
     * @return The ParametersModel.
     */
    public ParametersModel getParameters() {
        return parameters;
    }

    /**
     * Sets the parameters model.
     *
     * @param parameters The ParametersModel to set.
     */
    public void setParameters(ParametersModel parameters) {
        this.parameters = parameters;
    }

    /**
     * Gets the output file name.
     *
     * @return The output file name.
     */
    public String getOutputFileName() {
        return outputFileName;
    }

    /**
     * Sets the output file name.
     *
     * @param outputFileName The output file name to set.
     */
    public void setOutputFileName(String outputFileName) {
        this.outputFileName = outputFileName;
    }

    /**
     * Gets the name complement.
     *
     * @return The name complement.
     */
    public String getComplement() {
        return complement;
    }

    /**
     * Sets the name complement.
     *
     * @param complement The name complement to set.
     */
    public void setComplement(String complement) {
        this.complement = complement;
    }
}