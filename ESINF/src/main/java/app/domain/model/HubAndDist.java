package app.domain.model;

public class HubAndDist {
    private ClientsProducers hub;
    private int dist;

    public HubAndDist(ClientsProducers hub, int dist) {
        this.hub = hub;
        this.dist = dist;
    }

    public ClientsProducers getHub() {
        return hub;
    }

    public int getDist() {
        return dist;
    }
}
