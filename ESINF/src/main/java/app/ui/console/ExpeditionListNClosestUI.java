package app.ui.console;

import app.controller.App;
import app.controller.ExpeditionListNClosestController;

import app.domain.model.BasketElement;
import app.domain.model.ExpeditionList;
import app.domain.shared.SurplusCalculator;
import app.ui.console.utils.Utils;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class ExpeditionListNClosestUI implements Runnable {

    private ExpeditionListNClosestController ctrl = new ExpeditionListNClosestController();
    public static int counter = 0;

    @Override
    public void run() {

        if(NCloserPointsUI.defined) {
            
            if ((!App.getInstance().getCompany().getOrders().getOrders().isEmpty() || App.getInstance().getCompany().getStock().getStock().isEmpty())) {
                System.out.println("\n###        Expedition list N closest producers        ###\n");

                boolean move = false;

                int day = getDay();
                if (day > 0 && day <= App.getInstance().getCompany().getOrders().getOrders().size()) move = true;

                int n = Integer.parseInt(Objects.requireNonNull(Utils.readLineFromConsole("\nNumber of closest producers: ")));


                if (move) {
                    SurplusCalculator.CalculateSurplus(day);
                    ArrayList<ExpeditionList> expedition_list = ctrl.ExpeditionListNClosest(day, n);
                    ArrayList<String> clients_with_nothing_to_deliver = new ArrayList<>();

                    if (!expedition_list.isEmpty()) {
                        System.out.println("\n----  Expedition list N closed producers was successfully generated  ----\n");
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
                        System.out.println("\nExpedition list n closest was not generated.");
                    }
                    App.getInstance().getCompany().getStock().getStockNoRest().clear();
                    counter++;
                } else {
                    System.out.println("\nThere are no orders to generate an expedition list for this day.");
                }
            }
        }else {
            System.out.println("Hubs not yet defined!\n");
        }

    }

    private int getDay() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Insert day: ");
        return sc.nextInt();
    }

}

