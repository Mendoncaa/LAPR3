package app.ui.console;

import app.controller.DeviceCreationController;
import app.ui.console.utils.Utils;

public class IrrigationDeviceCreationUI implements Runnable {

    public DeviceCreationController ctrl;

    public IrrigationDeviceCreationUI() {
        this.ctrl = new DeviceCreationController();
    }

    @Override
    public void run() {
        System.out.println("###        Irrigation device        ###");
        System.out.println();

        String path = Utils.readLineFromConsole("File path to read: ");
        if (this.ctrl.readFile(path)) {
            System.out.println("New irrigation device created!");
        } else {
            System.out.println("Error reading file!");
        }
    }
}
