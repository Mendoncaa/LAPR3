package app.stores;

import app.domain.model.IrrigationDevice;

import java.util.ArrayList;

public class IrrigationDeviceStore {

    private ArrayList<IrrigationDevice> irrigationDeviceList;

    public IrrigationDeviceStore() {
        this.irrigationDeviceList = new ArrayList<>();
    }

    public boolean add(IrrigationDevice irrigationDevice) {
        return this.irrigationDeviceList.add(irrigationDevice);
    }

    public boolean remove(IrrigationDevice irrigationDevice) {
        return this.irrigationDeviceList.remove(irrigationDevice);
    }

    public boolean contains(IrrigationDevice irrigationDevice) {
        return this.irrigationDeviceList.contains(irrigationDevice);
    }

    public int size() {
        return this.irrigationDeviceList.size();
    }

    public IrrigationDevice getByID(int i) {
        for (IrrigationDevice irrigationDevice : this.irrigationDeviceList) {
            if (irrigationDevice.getId() == i) {
                return irrigationDevice;
            }
        }
        return null;
    }

    public ArrayList<IrrigationDevice> getIrrigationDeviceList() {
        return irrigationDeviceList;
    }

    public void removeAll() {
        this.irrigationDeviceList = new ArrayList<>();
    }


}
