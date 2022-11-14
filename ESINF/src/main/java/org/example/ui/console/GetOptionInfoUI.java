package org.example.ui.console;

import org.example.controller.GetInfoController;
import org.example.domain.model.AppUser;
import org.example.domain.shared.Constants;

import java.util.Set;

public class GetOptionInfoUI implements Runnable {
    private GetInfoController ctrl;
    private int option;

    public GetOptionInfoUI(int option) {
        ctrl = new GetInfoController();
        this.option = option;
    }


    @Override
    public void run() {

        String roleToList = roleToFind(this.option);


        if (this.ctrl.existUsers()) {

            Set<AppUser> appUserList = this.ctrl.userList(roleToList);

            if (!appUserList.isEmpty()) {
                printList(appUserList);
            } else {
                ShowTextUI text = new ShowTextUI("There are no users registered with this role");
                text.run();
            }

        } else {
            ShowTextUI text = new ShowTextUI("There's no users");
            text.run();
        }


    }


    public void printList(Set<AppUser> list) {
        System.out.printf("%n%n%n");
        for (AppUser appUser : list) {
            System.out.println(appUser);
            System.out.println();
        }
    }


    private String roleToFind(int option) {
        String role = null;

        switch (option) {
            case 1:
                role = Constants.ROLE_AGRICULTURAL_MANAGER;
                break;
            case 2:
                role = Constants.ROLE_DISTRIBUTION_MANAGER;
                break;
            case 3:
                role = Constants.ROLE_CLIENT;
                break;
            case 4:
                role = Constants.ROLE_DRIVER;
                break;
            case 5:
                role = "all";

        }
        return role;
    }
}
