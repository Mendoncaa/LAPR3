package org.example.ui.console;


import org.example.controller.RegisterUserController;
import org.example.domain.shared.Constants;
import org.example.ui.console.utils.Utils;

import java.util.InputMismatchException;
import java.util.NoSuchElementException;

public class ClientRegisterUI implements Runnable{
    private String name;
    private String email;
    private String role = Constants.ROLE_CLIENT;
    private RegisterUserController ctrl;

    public ClientRegisterUI() {
        this.ctrl = new RegisterUserController();
    }

    @Override
    public void run() {

        boolean confirmationBoolean = false;

        do {

            try {

                ShowTextUI show = new ShowTextUI("###        Client        ###");   //show this title
                show.run();

                name = Utils.readLineFromConsole("Name : "); //read the name
                email = Utils.readLineFromConsole("Email : ");//read the email

            } catch (
                    InputMismatchException exception) { //if the program notice that the input is not right show the message below

                ShowTextUI show = new ShowTextUI("The input is not correct.");
                show.run();

            }

            try {

                String response = Utils.readLineFromConsole("Do you confirm this data?  (Y/N)"); // asks for confirmation

                confirmationBoolean = response.equalsIgnoreCase("Y");

            } catch (
                    NoSuchElementException e) { //if the program notice that the input is not right show the message below

                ShowTextUI error = new ShowTextUI("The input is not correct. Try again!");
                error.run();
            }

        } while (!confirmationBoolean);

        boolean newEmployee = this.ctrl.createNewUser(name, role, email); //creation object confirmation

        if (newEmployee) { // if the program has the confirmation that was created an employee
            this.ctrl.saveUser(); // saves it
            ShowTextUI success = new ShowTextUI("New client successfully added!"); // and show this message
            success.run();
        } else {
            ShowTextUI failure = new ShowTextUI("The register was not possible!");
            failure.run();
        }

    }
}
