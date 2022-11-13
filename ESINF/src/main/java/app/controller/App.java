package app.controller;

import app.domain.model.Storage;
import app.domain.shared.Constants;
import pt.isep.lei.esoft.auth.AuthFacade;
import pt.isep.lei.esoft.auth.UserSession;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class App {

    private Storage storage;
    private AuthFacade authFacade;

    private App()
    {
        Properties props = getProperties();
        this.storage = new Storage(props.getProperty(Constants.PARAMS_STORAGE_DESIGNATION));
        this.authFacade = this.storage.getAuthFacade();
        bootstrap();
    }

    public Storage getCompany()
    {
        return this.storage;
    }


    public UserSession getCurrentUserSession()
    {
        return this.authFacade.getCurrentUserSession();
    }

    public boolean doLogin(String email, String pwd)
    {
        return this.authFacade.doLogin(email,pwd).isLoggedIn();
    }

    public void doLogout()
    {
        this.authFacade.doLogout();
    }

    private Properties getProperties()
    {
        Properties props = new Properties();

        // Add default properties and values
        props.setProperty(Constants.PARAMS_STORAGE_DESIGNATION, "To be defined");


        // Read configured values
        try
        {
            InputStream in = new FileInputStream(Constants.PARAMS_FILENAME);
            props.load(in);
            in.close();
        }
        catch(IOException ex)
        {

        }
        return props;
    }


    private void bootstrap()
    {
        this.authFacade.addUserRole(Constants.ROLE_ADMIN,Constants.ROLE_ADMIN);
        this.authFacade.addUserRole(Constants.ROLE_CLIENT,Constants.ROLE_CLIENT);
        this.authFacade.addUserRole(Constants.ROLE_DRIVER,Constants.ROLE_DRIVER);
        this.authFacade.addUserRole(Constants.ROLE_AGRICULTURAL_MANAGER,Constants.ROLE_AGRICULTURAL_MANAGER);
        this.authFacade.addUserRole(Constants.ROLE_DISTRIBUTION_MANAGER,Constants.ROLE_DISTRIBUTION_MANAGER);

        this.authFacade.addUserWithRole("Main Administrator", "admin@lei.sem2.pt", "123456",Constants.ROLE_ADMIN);
    }

    // Extracted from https://www.javaworld.com/article/2073352/core-java/core-java-simply-singleton.html?page=2
    private static App singleton = null;
    public static App getInstance()
    {
        if(singleton == null)
        {
            synchronized(App.class)
            {
                singleton = new App();
            }
        }
        return singleton;
    }
}
