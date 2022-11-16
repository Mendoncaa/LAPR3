package app.domain.model;

import app.stores.UsersStore;
import pt.isep.lei.esoft.auth.AuthFacade;
import org.apache.commons.lang3.StringUtils;


public class Company {

    private String designation;
    private AuthFacade authFacade;
    private UsersStore usersStore;

    public Company(String designation)
    {
        if (StringUtils.isBlank(designation))
            throw new IllegalArgumentException("Designation cannot be blank.");

        this.designation = designation;
        this.authFacade = new AuthFacade();
        this.usersStore = new UsersStore();
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
