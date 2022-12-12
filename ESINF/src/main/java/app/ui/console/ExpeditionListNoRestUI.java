package app.ui.console;

import app.controller.App;
import app.controller.ExpeditionListNoRestController;

public class ExpeditionListNoRestUI implements Runnable {

    private ExpeditionListNoRestController ctrl = new ExpeditionListNoRestController();

    @Override
    public void run() {

        if (!App.getInstance().getCompany().getOrders().getOrders().isEmpty() || App.getInstance().getCompany().getStock().getStock().isEmpty()) {
            System.out.println("\n###        Expedition list with no restrictions        ###");

            if (ctrl.getExpeditionListNoRestrictions()) {
                System.out.println("Expedition list with no restrictions was successfully generated.");
            } else {
                System.out.println("Expedition list with no restrictions was not generated.");
            }
        }else {
            System.out.println("There are no orders or stock to generate an expedition list.");
        }
    }
}
