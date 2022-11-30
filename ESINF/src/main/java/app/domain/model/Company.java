package app.domain.model;

import app.domain.shared.FilesReaderApp;
import app.graph.Edge;
import app.graph.Graph;
import app.graph.map.MapGraph;
import app.stores.IrrigationDeviceStore;
import app.stores.UsersStore;
import pt.isep.lei.esoft.auth.AuthFacade;
import org.apache.commons.lang3.StringUtils;

import java.io.File;


public class Company {

    private String designation;
    private AuthFacade authFacade;
    private UsersStore usersStore;
    private IrrigationDeviceStore irrigationDeviceStore;
    private MapGraph<ClientsProducers, Integer> clientsProducersGraph;

    public Company(String designation) {
        if (StringUtils.isBlank(designation))
            throw new IllegalArgumentException("Designation cannot be blank.");

        this.designation = designation;
        this.authFacade = new AuthFacade();
        this.usersStore = new UsersStore();
        this.irrigationDeviceStore = new IrrigationDeviceStore();
        this.clientsProducersGraph = new MapGraph<>(false);
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

    public void saveUser(AppUser appUser) {
        this.authFacade.addUserWithRole(appUser.getName(), appUser.getEmail(), appUser.getPassword(), appUser.getRole());
        System.out.println(appUser.getPassword());
    }
}
