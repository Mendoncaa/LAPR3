package app.controller;

import app.domain.model.Company;
import app.domain.model.IrrigationDevice;
import app.domain.model.IrrigationInfo;
import app.domain.shared.IrrigationCheck;


public class IrrigationCheckController {

    private Company company;

    public IrrigationCheckController() {
        this.company = App.getInstance().getCompany();
    }

    public IrrigationCheckController(Company company) {
        this.company = company;
    }


    public IrrigationInfo isWorking(IrrigationDevice device) {
        return IrrigationCheck.isWorking(device);
    }

}

