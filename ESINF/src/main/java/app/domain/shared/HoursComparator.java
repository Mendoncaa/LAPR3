package app.domain.shared;

import app.domain.model.HoursMinutes;

import java.util.Comparator;

public class HoursComparator implements Comparator<HoursMinutes> {


    @Override
    public int compare(HoursMinutes o1, HoursMinutes o2) {
        if (o1.getHours() == o2.getHours()) {
            return Integer.compare(o1.getMinutes(), o2.getMinutes());
        }
            return Integer.compare(o1.getHours(), o2.getHours());
    }
}

