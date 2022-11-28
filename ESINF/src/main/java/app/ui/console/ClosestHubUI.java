package app.ui.console;

import app.controller.App;
import app.controller.ClosestHubController;
import app.domain.model.ClientsProducers;
import app.graph.Graph;
import app.ui.console.utils.Utils;

import java.util.ArrayList;
import java.util.Scanner;

public class ClosestHubUI implements Runnable {

    private ClosestHubController ctrl = new ClosestHubController();

    @Override
    public void run() {
        System.out.println("\nClients available : \n");

        Graph<ClientsProducers, Integer> graph = App.getInstance().getCompany().getClientsProducersGraph();
        ArrayList<ClientsProducers> clients = new ArrayList<>();

        for (int i = 0; i < graph.vertices().size(); i++) {
            if (graph.vertices().get(i).getType(graph.vertices().get(i).getCode()).equalsIgnoreCase("Cliente")) {
                clients.add(graph.vertices().get(i));
            }
        }

        int option = 0;

        try {

            do {

                for (int i = 0; i < clients.size(); i++) {
                    System.out.println(i + 1 + ". " + clients.get(i).getCode());
                }
                System.out.println();
                option = Utils.readIntegerFromConsole("Which client you want?");
            } while (option < 0 || option > clients.size());

        } catch (NumberFormatException e) {
            System.out.println("\nInput not correct!\n");
        }

        System.out.println("\nClient : \n" + clients.get(option - 1));
        System.out.println("\nClosest Hub : \n" + ctrl.getClosestHub(clients.get(option - 1)));

    }
}
