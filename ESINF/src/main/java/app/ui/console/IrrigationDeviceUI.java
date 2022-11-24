package app.ui.console;

import app.controller.App;
import app.controller.IrrigationCheckController;
import app.domain.model.IrrigationDevice;
import app.domain.model.IrrigationInfo;
import app.ui.console.utils.Utils;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class IrrigationDeviceUI implements Runnable {

    public IrrigationCheckController ctrl;

    public IrrigationDeviceUI() {
        this.ctrl = new IrrigationCheckController();
    }

    @Override
    public void run() {
        int option = 0;
        ArrayList<IrrigationDevice> devices = App.getInstance().getCompany().getIrrigationDeviceStore().getIrrigationDeviceList();

        System.out.println("\nYou have " + devices.size() + " irrigation devices registered in the system.\n");

        if (devices.size() > 0) {

            while (option > devices.size() || option < 1) {
                option = Utils.readIntegerFromConsole("\nWhich device do you want to check by its ID?");
                if (option > devices.size() || option < 1) {
                    System.out.println("Invalid option!");
                    return;
                }
            }

            if (LocalDateTime.now().isBefore(devices.get(option - 1).getLastIrrigationDay())) {
                IrrigationInfo print = this.ctrl.isWorking(devices.get(option - 1));
                if (print != null) {

                    if (print.isIrrigating()) {
                        System.out.println("\nIrrigation device " + option + " is working.");
                        System.out.println("Sector: " + print.getSectorName());
                        System.out.println("Irrigation minutes left: " + print.getIrrigationMinutesLeft());
                    } else {
                        System.out.println("\nIrrigation device " + option + " is not working at this moment.");
                    }
                } else {
                    System.out.println("There is no irrigations programmed today");
                }
            } else {
                System.out.println("\nIrrigation device " + option + " plan is expired.");
            }
        }
    }
}

