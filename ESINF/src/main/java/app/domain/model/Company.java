package app.domain.model;

import app.domain.shared.FilesReaderApp;
import app.graph.Edge;
import app.graph.Graph;
import app.graph.map.MapGraph;
import app.stores.*;
import pt.isep.lei.esoft.auth.AuthFacade;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;


public class Company {

    private String designation;
    private AuthFacade authFacade;
    private UsersStore usersStore;
    private IrrigationDeviceStore irrigationDeviceStore;

    private HubStore hubStore;
    private MapGraph<ClientsProducers, Integer> clientsProducersGraph;

    private Orders orders;
    private Stock stock;
    private Statistics statistics;


    public Company(String designation) {
        if (StringUtils.isBlank(designation))
            throw new IllegalArgumentException("Designation cannot be blank.");

        this.designation = designation;
        this.authFacade = new AuthFacade();
        this.usersStore = new UsersStore();
        this.irrigationDeviceStore = new IrrigationDeviceStore();
        this.clientsProducersGraph = new MapGraph<>(false);
        this.hubStore = new HubStore();
        this.orders = new Orders();
        this.stock = new Stock();
        this.statistics = new Statistics();
    }

    public Orders getOrders() {
        return orders;
    }

    public Stock getStock() {
        return stock;
    }

    public void updateOrders(Map<Integer, ArrayList<ClientBasket>> orders) {
        this.orders.updateOrders(orders);
    }

    public void updateStock(Map<Integer, ArrayList<ClientBasket>> stock) {
        this.stock.updateStock(stock);
    }

    public Statistics getStatistics() {
        return statistics;
    }

    public void updateStatistics(int day, StatisticBasket statisticBasket) {
        this.statistics.addStatisticBasket(day, statisticBasket);
    }

    public MapGraph<ClientsProducers, Integer> getClientsProducersGraph() {
        return clientsProducersGraph;
    }

    public IrrigationDeviceStore getIrrigationDeviceStore() {
        return irrigationDeviceStore;
    }

    public String getDesignation() {
        return designation;
    }

    public AuthFacade getAuthFacade() {
        return authFacade;
    }

    public UsersStore getUserStore() {
        return usersStore;
    }

    public HubStore getHubStore() {
        return hubStore;
    }


    public void saveUser(AppUser appUser) {
        this.authFacade.addUserWithRole(appUser.getName(), appUser.getEmail(), appUser.getPassword(), appUser.getRole());
        System.out.println(appUser.getPassword());
    }
}
