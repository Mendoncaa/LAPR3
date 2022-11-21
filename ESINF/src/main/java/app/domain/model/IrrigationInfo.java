package app.domain.model;

public class IrrigationInfo {

    private boolean isIrrigating;
    private String sectorName;
    private int irrigationMinutesLeft;

    public IrrigationInfo(boolean isIrrigating, String sectorName, int irrigationMinutesLeft) {
        this.isIrrigating = isIrrigating;
        this.sectorName = sectorName;
        this.irrigationMinutesLeft = irrigationMinutesLeft;
    }

    public boolean isIrrigating() {
        return isIrrigating;
    }

    public String getSectorName() {
        return sectorName;
    }

    public int getIrrigationMinutesLeft() {
        return irrigationMinutesLeft;
    }
}
