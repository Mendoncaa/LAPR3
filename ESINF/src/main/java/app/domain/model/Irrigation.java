package app.domain.model;

import java.util.Objects;

public class Irrigation {
    private String sector;
    private int duration;
    private String frequency;

    public Irrigation(String sector, int duration, String frequency) {
        this.sector = sector;
        this.duration = duration;
        this.frequency = frequency;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Irrigation that = (Irrigation) o;
        return duration == that.duration && Objects.equals(sector, that.sector) && Objects.equals(frequency, that.frequency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sector, duration, frequency);
    }

    @Override
    public String toString() {
        return "sector: " + sector + ", duration: " + duration + ", days: " + frequency;
    }
}
