package org.example.controller;


import org.example.domain.model.AppUser;
import org.example.domain.model.Company;
import org.example.domain.shared.UserAttributes;

public class RegisterUserController {

    private Company company;
    private AppUser appUser;

    public RegisterUserController() {
        this(App.getInstance().getCompany());
    }

    public RegisterUserController(Company company) {
        this.company = company;
        this.appUser = null;
    }


    public boolean createNewUser(String name, String role, String email) {
        if (UserAttributes.validateUser(email)){
            return false;
        }
        this.appUser = this.company.getUserStore().createNewUser(name, role, email);
        return this.company.getUserStore().validateUser(appUser);
    }


    public void saveUser() {
        this.company.saveUser(appUser);
    }

}

