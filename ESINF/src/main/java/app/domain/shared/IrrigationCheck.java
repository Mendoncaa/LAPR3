package app.domain.shared;

import app.domain.model.HoursMinutes;
import app.domain.model.Irrigation;
import app.domain.model.IrrigationDevice;
import app.domain.model.IrrigationInfo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;


public class IrrigationCheck {

    public static IrrigationInfo isWorking(IrrigationDevice device) {
        ArrayList<Irrigation> toBeWatered = irrigationsOfTheDay(device);

        if (toBeWatered.isEmpty()) {
            return null;
        }

        return findRightCicle(device);
    }

    private static IrrigationInfo findRightCicle(IrrigationDevice device) {
        LocalDateTime now = LocalDateTime.now();
        HoursMinutes nowHM = new HoursMinutes(now.getHour(), now.getMinute());

        for (HoursMinutes hoursMinutes : device.getCicles()) {
            if (nowHM.isAfter(hoursMinutes)) {
                if (checkIrrigationTime(device,hoursMinutes)) {
                    return findIrrigationInfo(device, hoursMinutes);
                }
            }
        }

        return new IrrigationInfo(false, null, 0);
    }

    private static boolean checkIrrigationTime(IrrigationDevice device, HoursMinutes hm) {
        HoursMinutes hm1 = new HoursMinutes(hm);
        ArrayList<Irrigation> toBeWatered = irrigationsOfTheDay(device);
        HoursMinutes now = new HoursMinutes(LocalDateTime.now().getHour(), LocalDateTime.now().getMinute());

        for (Irrigation irrigation : toBeWatered) {
            hm1.addMinutes(irrigation.getDuration());
        }

        return now.isBetween(hm, hm1);

    }

    private static IrrigationInfo findIrrigationInfo(IrrigationDevice device, HoursMinutes hm) {
        HoursMinutes aux = new HoursMinutes(hm);    //horas dos ciclos em análise
        HoursMinutes aux2 = new HoursMinutes(hm);
        HoursMinutes aux3 = new HoursMinutes();

        Map<HoursMinutes, IrrigationInfo> irrigationInfoMap = new TreeMap<>();
        ArrayList<Irrigation> irrigations = irrigationsOfTheDay(device);

        HoursMinutes now = new HoursMinutes(LocalDateTime.now().getHour(), LocalDateTime.now().getMinute());

        for (Irrigation irrigation : irrigations) {
            aux2 = new HoursMinutes(aux);
            aux2.addMinutes(irrigation.getDuration());
            aux2.subtractHoursMinutes(now);
            aux.addMinutes(irrigation.getDuration());
            irrigationInfoMap.put(new HoursMinutes(aux), new IrrigationInfo(true, irrigation.getSector(), aux2.getAllInMinutes()));
        }

        for (HoursMinutes key : irrigationInfoMap.keySet()) {
            if (key.isAfter(now)) {
                return irrigationInfoMap.get(key);
            }
        }
        return new IrrigationInfo(false, null, 0);
    }


    private static ArrayList<Irrigation> irrigationsOfTheDay(IrrigationDevice device) {
        ArrayList<Irrigation> irrigations = new ArrayList<>();
        int day = LocalDateTime.now().getDayOfMonth();
        int rest = day % 2;

        if (device.getIrrigations().size() != 0) {
            if (rest != 0) {
                for (int i = 0; i < device.getIrrigations().size(); i++) {
                    if (device.getIrrigations().get(i).getFrequency().trim().equalsIgnoreCase("odd") || device.getIrrigations().get(i).getFrequency().trim().equalsIgnoreCase("all")) {
                        irrigations.add(device.getIrrigations().get(i));
                    }
                }
            } else {
                for (int i = 0; i < device.getIrrigations().size(); i++) {
                    if (device.getIrrigations().get(i).getFrequency().trim().equalsIgnoreCase("even") || device.getIrrigations().get(i).getFrequency().trim().equalsIgnoreCase("all")) {
                        irrigations.add(device.getIrrigations().get(i));
                    }
                }
            }
        }
        return irrigations;
    }

}
