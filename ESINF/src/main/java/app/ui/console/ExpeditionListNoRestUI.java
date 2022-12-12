package app.ui.console;

import app.controller.App;
import app.controller.ExpeditionListNoRestController;
import app.domain.model.ExpeditionList;

import java.util.Scanner;
import java.util.Set;

public class ExpeditionListNoRestUI implements Runnable {

    private ExpeditionListNoRestController ctrl = new ExpeditionListNoRestController();

    @Override
    public void run() {

        if (!App.getInstance().getCompany().getOrders().getOrders().isEmpty() || App.getInstance().getCompany().getStock().getStock().isEmpty()) {
            System.out.println("\n###        Expedition list with no restrictions        ###\n");

            Scanner sc = new Scanner(System.in);
            System.out.println("Insert day: ");

            Set<ExpeditionList> expedition_list = ctrl.getExpeditionListNoRestrictions(sc.nextInt());

            if (!expedition_list.isEmpty()) {
                System.out.println("Expedition list with no restrictions was successfully generated.");
            } else {
                System.out.println("Expedition list with no restrictions was not generated.");
            }
        }else {
            System.out.println("There are no orders or stock to generate an expedition list.");
        }
    }
}
