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
        if (hub != null && !this.hubs.contains(hub)) {
            this.hubs.add(hub);
        }
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

    public boolean isEmpty() {
        return this.hubs.isEmpty();
    }

}
