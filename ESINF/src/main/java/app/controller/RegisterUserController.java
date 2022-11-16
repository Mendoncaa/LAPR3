package app.controller;


import app.domain.model.AppUser;
import app.domain.model.Company;
import app.domain.shared.UserAttributes;


public class RegisterUserController {

    private Company company;
    private AppUser appUser;

    public RegisterUserController() {
        this.company = App.getInstance().getCompany();
        this.appUser = new AppUser();
    }

    public RegisterUserController(Company company) {
        this.company = company;
        this.appUser = null;
    }


    public boolean createNewUser(String name, String email, String role) {
        if (UserAttributes.validateUser(email)){
            return false;
        }
        this.appUser = this.company.getUserStore().createNewUser(name, email, role);
        return this.company.getUserStore().validateUser(appUser);
    }


    public void saveUser() {
        this.company.saveUser(appUser);
    }

}

