package app.domain.shared;

import app.domain.model.HoursMinutes;
import app.domain.model.Irrigation;
import app.domain.model.IrrigationDevice;
import app.domain.model.IrrigationInfo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;

public class IrrigationCheck {

    public static IrrigationInfo isWorking(IrrigationDevice device) {
        ArrayList<Irrigation> toBeWatered = irrigationsOfTheDay(device);

        if (toBeWatered.size() == 0) {
            System.out.println("There is no irrigations programmed today");
            return null;
        }

        return isWorking(device); //incompleto
    }

    private static boolean findRightCicle(IrrigationDevice device) {
        LocalDateTime now = LocalDateTime.now();
        HoursMinutes nowHM = new HoursMinutes(now.getHour(), now.getMinute());

        Iterator<HoursMinutes> itr = device.getCicles().iterator();
        while (itr.hasNext()) {
            if (nowHM.isAfter(itr.next())) {
                return true;
            }
        }
        return false;//incompleto
    }

    private static boolean checkIrrigationTime(HoursMinutes hm){
    return false;//incompleto
    }


    private static ArrayList<Irrigation> irrigationsOfTheDay(IrrigationDevice device) {
        ArrayList<Irrigation> irrigations = new ArrayList<>();
        int day = LocalDateTime.now().getDayOfMonth();
        int rest = day % 2;

        if (device.getIrrigations().size() != 0) {
            if (rest != 0) {
                for (int i = 0; i < device.getIrrigations().size(); i++) {
                    if (device.getIrrigations().get(i).getFrequency().equalsIgnoreCase("odd") || device.getIrrigations().get(i).getFrequency().equalsIgnoreCase("all")) {
                        irrigations.add(device.getIrrigations().get(i));
                    }
                }
            } else {
                for (int i = 0; i < device.getIrrigations().size(); i++) {
                    if (device.getIrrigations().get(i).getFrequency().equalsIgnoreCase("even") || device.getIrrigations().get(i).getFrequency().equalsIgnoreCase("all")) {
                        irrigations.add(device.getIrrigations().get(i));
                    }
                }
            }
        }
        return irrigations;
    }

}
