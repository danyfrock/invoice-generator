package com.invoicegenerator.services;


import com.invoicegenerator.modeles.ParametersModel;

import java.util.List;

public class ActivityCodeService {
    public List<String> getActivityCodes() {
        return new ParametresService(new ParametersModel().getParametresFileName()).chargerParametres().getCodesActivite();
    }
}
