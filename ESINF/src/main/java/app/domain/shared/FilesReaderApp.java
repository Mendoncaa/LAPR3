package app.domain.shared;

import app.controller.App;
import app.domain.model.HoursMinutes;
import app.domain.model.Irrigation;
import app.domain.model.IrrigationDevice;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FilesReaderApp {
    public static boolean readIrrigationDeviceFile(File path) {
        try {
            Scanner scanner = new Scanner(path);
            boolean valid = true;

            String[] hours = scanner.nextLine().split(",");

            IrrigationDevice device = new IrrigationDevice();
            for (String hour : hours) {
                String[] time = hour.trim().split(":");
                device.addCicle(new HoursMinutes(Integer.parseInt(time[0]), Integer.parseInt(time[1])));
            }

            while (scanner.hasNextLine()) {
                try {
                    String[] items = scanner.nextLine().split(",");
                    if (items.length == 3) {
                        if (items[2].trim().equalsIgnoreCase("all") || items[2].trim().equalsIgnoreCase("even") || items[2].trim().equalsIgnoreCase("odd")) {
                            int t = Integer.parseInt(items[1].trim());
                           if (valid) {
                               device.addIrrigation(new Irrigation(items[0], t, items[2]));
                           }
                        }
                    }
                } catch (NumberFormatException e) {
                    valid = false;
                }
            }

            App.getInstance().getCompany().getIrrigationDeviceStore().add(device);


        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            return false;
        }

        return true;
    }
}
