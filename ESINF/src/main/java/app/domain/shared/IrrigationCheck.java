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
                if (checkIrrigationTime(hoursMinutes, device)) {
                    return null;//incompleto
                }
            }
        }

        return new IrrigationInfo(false, null, 0);
    }

    private static boolean checkIrrigationTime(HoursMinutes hm, IrrigationDevice device) {
        HoursMinutes hm1 = new HoursMinutes(hm);
        ArrayList<Irrigation> toBeWatered = irrigationsOfTheDay(device);
        HoursMinutes now = new HoursMinutes(LocalDateTime.now().getHour(), LocalDateTime.now().getMinute());

        for (Irrigation irrigation : toBeWatered) {
            hm1.addMinutes(irrigation.getDuration());
        }

        return now.isBetween(hm, hm1);

    }

    private static IrrigationInfo findIrrigationInfo(IrrigationDevice device, HoursMinutes hm) {
        HoursMinutes aux = new HoursMinutes(hm);
        HoursMinutes aux2 = new HoursMinutes(hm);
        Map<HoursMinutes, IrrigationInfo> irrigationInfoMap = new TreeMap<>();
        ArrayList<Irrigation> irrigations = device.getIrrigations();
        HoursMinutes now = new HoursMinutes(LocalDateTime.now().getHour(), LocalDateTime.now().getMinute());

        for (int i = 0; i < irrigations.size(); i++) {
            aux2.addMinutes(irrigations.get(i).getDuration());
            aux2.subtractHoursMinutes(now);
            irrigationInfoMap.put(aux, new IrrigationInfo(true, irrigations.get(i).getSector(), aux2.getMinutes()));
            aux.addMinutes(irrigations.get(i).getDuration());
        }
        return null;
    }


    //criar classe para descobrir que parcela está a regar


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
