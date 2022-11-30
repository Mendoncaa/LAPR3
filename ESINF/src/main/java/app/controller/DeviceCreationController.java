package app.controller;

import app.domain.model.Company;
import app.domain.shared.FilesReaderApp;

import java.io.File;

public class DeviceCreationController {

    private Company company;

    public DeviceCreationController() {
        this.company = App.getInstance().getCompany();
    }

    public DeviceCreationController(Company company) {
        this.company = company;
    }


    public boolean readFile(String path) {
        return FilesReaderApp.readIrrigationDeviceFile(new File(path));
    }


}
