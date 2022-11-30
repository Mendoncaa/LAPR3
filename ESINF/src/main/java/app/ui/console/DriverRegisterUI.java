package app.ui.console;


import app.controller.RegisterUserController;
import app.domain.shared.Constants;
import app.ui.console.utils.Utils;

import java.util.InputMismatchException;
import java.util.NoSuchElementException;

public class DriverRegisterUI implements Runnable{

    private RegisterUserController ctrl;

    public DriverRegisterUI() {
        this.ctrl = new RegisterUserController();
    }

    @Override
    public void run() {
        String name = null, email = null;

        boolean confirmationBoolean = false;

        do {

            try {

                ShowTextUI show = new ShowTextUI("###        Driver        ###");   //show this title
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

                if (response != null) {
                    confirmationBoolean = response.equalsIgnoreCase("Y");
                }

            } catch (
                    NoSuchElementException e) { //if the program notice that the input is not right show the message below

                ShowTextUI error = new ShowTextUI("The input is not correct. Try again!");
                error.run();
            }


        } while (!confirmationBoolean);

        boolean newEmployee = this.ctrl.createNewUser(name, email, Constants.ROLE_DRIVER); //creation object confirmation

        if (newEmployee) { // if the program has the confirmation that was created an employee
            this.ctrl.saveUser(); // saves it
            ShowTextUI success = new ShowTextUI("New driver successfully added!"); // and show this message
            success.run();
        } else {
            ShowTextUI failure = new ShowTextUI("The register was not possible!");
            failure.run();
        }
    }
}
