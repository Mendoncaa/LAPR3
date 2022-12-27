package app.ui.console;

import app.controller.App;
import app.controller.ExpeditionListNoRestController;
import app.domain.model.BasketElement;
import app.domain.model.ExpeditionList;
import app.domain.shared.SurplusCalculator;
import app.ui.console.utils.Utils;

import java.util.ArrayList;
import java.util.Scanner;


public class ExpeditionListNoRestUI implements Runnable {

    private ExpeditionListNoRestController ctrl = new ExpeditionListNoRestController();
    public static int counter = 0;

    @Override
    public void run() {
        boolean go = false;
        String answer;

        if (counter != 0) {
            System.out.println("\nFor a more precise expedition list you should restart the app.\nType Y if you want it anyway");
            answer = Utils.readLineFromConsole("Type here: ");
            if (answer != null && answer.equalsIgnoreCase("y")) {
                go = true;
            }
        } else {
            go = true;
        }

        if ((!App.getInstance().getCompany().getOrders().getOrders().isEmpty() || App.getInstance().getCompany().getStock().getStock().isEmpty()) && go) {
            System.out.println("\n###        Expedition list with no restrictions        ###\n");

            boolean move = false;

            int day = getDay();
            if (day > 0 && day <= App.getInstance().getCompany().getOrders().getOrders().size()) move = true;


            if (move) {
                SurplusCalculator.CalculateSurplus(day);
                ArrayList<ExpeditionList> expedition_list = ctrl.getExpeditionListNoRestrictions(day);
                ArrayList<String> clients_with_nothing_to_deliver = new ArrayList<>();


                if (!expedition_list.isEmpty()) {
                    System.out.println("\n----  Expedition list with no restrictions was successfully generated  ----\n");
                    for (ExpeditionList expeditionList : expedition_list) {
                        int i = 0;
                        if (!expeditionList.getBasketElements().isEmpty()) {
                            System.out.println(expeditionList.getReceiver().getCode() + ":");
                            ArrayList<BasketElement> basket = expeditionList.getBasketElements();
                            for (BasketElement basketElement : basket) {
                                System.out.printf("%-10s  quantity ordered: %-3.1f     quantity delivered: %-3.1f     producer:  %-4s\n", basketElement.getProduct().getName(), expeditionList.getBasketOrderedElements().get(i).getProduct().getQuantity(), basketElement.getProduct().getQuantity(), basketElement.getProducer().getCode());
                                i++;
                            }
                            System.out.println();
                        } else {
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
                App.getInstance().getCompany().getStock().getStockClone().clear();
                counter++;
            } else {
                System.out.println("\nThere are no orders to generate an expedition list for this day.");
            }
        }

    }

    private int getDay() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Insert day: ");
        return sc.nextInt();
    }
}
