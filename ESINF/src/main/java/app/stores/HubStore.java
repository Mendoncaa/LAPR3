package app.stores;

import app.domain.model.ClientsProducers;

import java.util.ArrayList;

public class HubStore {

    private ArrayList<ClientsProducers> hubs;

    public HubStore() {
        this.hubs = new ArrayList<>();
    }

    public ArrayList<ClientsProducers> getHubs() {
        return this.hubs;
    }

    public void addHub(ClientsProducers hub) {
        this.hubs.add(hub);
    }

    public void removeHub(ClientsProducers hub) {
        this.hubs.remove(hub);
    }

    public void clearHubs() {
        this.hubs.clear();
    }

    public boolean containsHub(ClientsProducers hub) {
        return this.hubs.contains(hub);
    }

    public int size() {
        return this.hubs.size();
    }

    public ClientsProducers getHub(int index) {
        return this.hubs.get(index);
    }

    public void setHub(int index, ClientsProducers hub) {
        this.hubs.set(index, hub);
    }

    public void removeHub(int index) {
        this.hubs.remove(index);
    }

    public void addHub(int index, ClientsProducers hub) {
        this.hubs.add(index, hub);
    }

    public int indexOfHub(ClientsProducers hub) {
        return this.hubs.indexOf(hub);
    }

    public boolean isEmpty() {
        return this.hubs.isEmpty();
    }

    public void addAllHubs(ArrayList<ClientsProducers> hubs) {
        this.hubs.addAll(hubs);
    }
}
