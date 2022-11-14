package org.example.domain.model;


import org.example.controller.App;
import org.example.stores.UsersStore;
import pt.isep.lei.esoft.auth.AuthFacade;
import org.apache.commons.lang3.StringUtils;


public class Company {

    private String designation;
    private AuthFacade authFacade;

    private UsersStore usersStore;


    public Company(String designation) {
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
        return this.usersStore;
    }

    public void saveUser(AppUser appUser) {
        if (!this.usersStore.validateUser(appUser)) { //if the employee is not validated
            return;
        }
        this.authFacade.addUserWithRole(appUser.getName(), appUser.getEmail(), appUser.getPassword(), appUser.getRole()); // adding the user to the system
        System.out.println(appUser.getPassword());
        App.getInstance().getEmailsPasswords().add(appUser.getEmail(), appUser.getPassword());
        this.usersStore.saveUser(appUser);
    }
}