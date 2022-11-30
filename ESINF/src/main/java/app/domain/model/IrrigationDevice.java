package app.domain.model;

import app.domain.shared.HoursComparator;

import java.time.LocalDateTime;
import java.util.*;

public class IrrigationDevice {

    private Set<HoursMinutes> cicles;
    private ArrayList<Irrigation> irrigations;

    private LocalDateTime lastIrrigationDay;

    private static int idCounter = 1;
    private int id;

    public IrrigationDevice() {
        this.cicles = new TreeSet<>(new HoursComparator());
        this.irrigations = new ArrayList<>();
        this.id = idCounter;
        this.lastIrrigationDay = LocalDateTime.now().plusDays(30);
        idCounter++;
    }

    public void addCicle(HoursMinutes cicle) {
        this.cicles.add(cicle);
    }

    public void addIrrigation(Irrigation irrigation) {
        this.irrigations.add(irrigation);
    }

    public Set<HoursMinutes> getCicles() {
        return cicles;
    }

    public ArrayList<Irrigation> getIrrigations() {
        return irrigations;
    }

    public LocalDateTime getLastIrrigationDay() {
        return lastIrrigationDay;
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IrrigationDevice that = (IrrigationDevice) o;
        return id == that.id && Objects.equals(cicles, that.cicles) && Objects.equals(irrigations, that.irrigations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cicles, irrigations, id);
    }
}
