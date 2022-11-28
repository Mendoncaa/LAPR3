package app.controller;

import app.domain.model.Company;
import app.domain.shared.Constants;
import app.stores.EmailsPasswords;
import pt.isep.lei.esoft.auth.AuthFacade;
import pt.isep.lei.esoft.auth.UserSession;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class App {

    private Company company;
    private AuthFacade authFacade;

    private EmailsPasswords emailsPasswords;

    private App() {
        Properties props = getProperties();
        this.company = new Company(props.getProperty(Constants.PARAMS_COMPANY_DESIGNATION));
        this.authFacade = this.company.getAuthFacade();
        this.emailsPasswords = new EmailsPasswords();
        bootstrap();
    }

    public Company getCompany() {
        return this.company;
    }


    public UserSession getCurrentUserSession() {
        return this.authFacade.getCurrentUserSession();
    }

    public boolean doLogin(String email, String pwd) {
        return this.authFacade.doLogin(email, pwd).isLoggedIn();
    }

    public void doLogout() {
        this.authFacade.doLogout();
    }

    private Properties getProperties() {
        Properties props = new Properties();

        // Add default properties and values
        props.setProperty(Constants.PARAMS_COMPANY_DESIGNATION, "DGS/SNS");


        // Read configured values
        try {
            InputStream in = new FileInputStream(Constants.PARAMS_FILENAME);
            props.load(in);
            in.close();
        } catch (IOException ex) {

        }
        return props;
    }


    private void bootstrap() {
        this.authFacade.addUserRole(Constants.ROLE_ADMIN, Constants.ROLE_ADMIN);
        this.authFacade.addUserRole(Constants.ROLE_CLIENT, Constants.ROLE_CLIENT);
        this.authFacade.addUserRole(Constants.ROLE_DISTRIBUTION_MANAGER, Constants.ROLE_DISTRIBUTION_MANAGER);
        this.authFacade.addUserRole(Constants.ROLE_AGRICULTURAL_MANAGER, Constants.ROLE_AGRICULTURAL_MANAGER);
        this.authFacade.addUserRole(Constants.ROLE_DRIVER, Constants.ROLE_DRIVER);
        this.authFacade.addUserWithRole("Main Administrator", "admin@lei.sem3.pt", "123456", Constants.ROLE_ADMIN);
        this.authFacade.addUserWithRole("Main AgricManager", "agrman@lei.sem3.pt", "123456", Constants.ROLE_AGRICULTURAL_MANAGER);
        this.authFacade.addUserWithRole("Main DistManager", "disman@lei.sem3.pt", "123456", Constants.ROLE_DISTRIBUTION_MANAGER);
    }

    // Extracted from https://www.javaworld.com/article/2073352/core-java/core-java-simply-singleton.html?page=2
    private static App singleton = null;

    public static App getInstance() {
        if (singleton == null) {
            synchronized (App.class) {
                singleton = new App();
            }
        }
        return singleton;
    }

    public EmailsPasswords getEmailsPasswords() {
        return emailsPasswords;
    }
}
