package app.domain.model;

import java.util.Objects;

public class HoursMinutes {

    public int hours;
    public int minutes;

    public HoursMinutes(int hours, int minutes) {
        this.hours = hours;
        this.minutes = minutes;
    }

    public HoursMinutes() {
        this.hours = 0;
        this.minutes = 0;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public boolean compare(HoursMinutes hm) {
        if (this.hours > hm.hours) {
            return true;
        }
        if (this.hours == hm.hours) {
            if (this.minutes > hm.minutes) {
                return true;
            }
        }
        return false;
    }


    public boolean isBetween(HoursMinutes start, HoursMinutes end) {
        if (this.isAfter(start) && this.isBefore(end)) {
            return true;
        }
        return false;
    }

    public boolean isAfter(HoursMinutes hm) {
        if (this.hours > hm.hours) {
            return true;
        }
        if (this.hours == hm.hours) {
            if (this.minutes > hm.minutes) {
                return true;
            }
        }
        return false;
    }

    public boolean isBefore(HoursMinutes hm) {
        if (this.hours < hm.hours) {
            return true;
        }
        if (this.hours == hm.hours) {
            if (this.minutes < hm.minutes) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HoursMinutes that = (HoursMinutes) o;
        return hours == that.hours && minutes == that.minutes;
    }

    @Override
    public int hashCode() {
        return Objects.hash(hours, minutes);
    }

    @Override
    public String toString() {
        return "Hours : " + this.hours + " Minutes : " + this.minutes;
    }
}
