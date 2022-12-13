package app.ui.console;

import app.controller.App;
import app.controller.ExpeditionListNoRestController;
import app.domain.model.BasketElement;
import app.domain.model.ExpeditionList;

import java.util.ArrayList;
import java.util.Scanner;


public class ExpeditionListNoRestUI implements Runnable {

    private ExpeditionListNoRestController ctrl = new ExpeditionListNoRestController();

    @Override
    public void run() {

        if (!App.getInstance().getCompany().getOrders().getOrders().isEmpty() || App.getInstance().getCompany().getStock().getStock().isEmpty()) {
            System.out.println("\n###        Expedition list with no restrictions        ###\n");

            Scanner sc = new Scanner(System.in);
            System.out.println("Insert day: ");

            ArrayList<ExpeditionList> expedition_list = ctrl.getExpeditionListNoRestrictions(sc.nextInt());
            ArrayList<String> clients_with_nothing_to_deliver = new ArrayList<>();


            if (!expedition_list.isEmpty()) {
                System.out.println("\n----  Expedition list with no restrictions was successfully generated  ----\n");
                for (ExpeditionList expeditionList : expedition_list) {
                    int i = 0;
                    if (!expeditionList.getBasketElements().isEmpty()) {
                        System.out.println(expeditionList.getReceiver().getCode() + ":");
                        ArrayList<BasketElement> basket = expeditionList.getBasketElements();
                        for (BasketElement basketElement : basket) {
                            System.out.printf("%-10s  quantity ordered: %-3.1f     quantity delivered: %-3.1f     producer:  %-4s\n", basketElement.getProduct().getName(), expeditionList.getBasketOrderedElements().get(i).getProduct().getQuantity() ,basketElement.getProduct().getQuantity(), basketElement.getProducer().getCode());
                            i++;
                        }
                        System.out.println();
                    }else {
                        clients_with_nothing_to_deliver.add(expeditionList.getReceiver().getCode());
                    }
                }

                if (!clients_with_nothing_to_deliver.isEmpty()) {
                    System.out.println("\n\nClients with nothing delivered:\n");
                    for (String client : clients_with_nothing_to_deliver) {
                        System.out.println(client);
                    }
                }

            } else {
                System.out.println("\nExpedition list with no restrictions was not generated.");
            }
        } else {
            System.out.println("There are no orders or stock to generate an expedition list.");
        }
    }
}
