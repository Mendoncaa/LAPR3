package app.domain.shared;

import app.controller.App;
import app.domain.model.*;
import app.domain.shared.GraphDiameter;
import app.graph.Algorithms;
import app.graph.map.MapGraph;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;


public class FilesReaderApp {

    public static boolean readIrrigationDeviceFile(File path) {
        try {
            Scanner scanner = new Scanner(path);
            boolean valid = true;

            String[] hours = scanner.nextLine().split(",");

            if (hours.length > 0 && hours[0].contains(":")) {
                IrrigationDevice device = new IrrigationDevice();
                for (String hour : hours) {
                    String[] time = hour.trim().split(":");
                    device.addCicle(new HoursMinutes(Integer.parseInt(time[0]), Integer.parseInt(time[1])));
                }

                while (scanner.hasNextLine()) {
                    try {
                        String[] items = scanner.nextLine().split(",");
                        if (items.length == 3) {
                            if (items[2].trim().equalsIgnoreCase("t") || items[2].trim().equalsIgnoreCase("p") || items[2].trim().equalsIgnoreCase("i")) {
                                int t = Integer.parseInt(items[1].trim());
                                if (valid) {
                                    device.addIrrigation(new Irrigation(items[0], t, items[2]));
                                }
                            }
                        }
                    } catch (NumberFormatException e) {
                        valid = false;
                    }
                }

                App.getInstance().getCompany().getIrrigationDeviceStore().add(device);
            } else {
                return false;
            }

        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            return false;
        }
        return true;
    }

    /*
     * Reads the files containing all the clients and producers information
     *
     * @return clientProducersMap graph containing all the countries and their borders
     */
    public static void readProducerCSV(File fileVertexes, File fileEdges) {

        System.out.println("Creating graph");
        String buffer;
        int counter;

        try {

            Scanner scanner = new Scanner(fileVertexes);
            scanner.nextLine(); //skip first line of file
            counter = 2;

            while (scanner.hasNextLine()) {

                buffer = scanner.nextLine();
                String[] arrBuffer = buffer.split(",");

                if (arrBuffer.length == 4) {

                    ClientsProducers clp = new ClientsProducers(arrBuffer[0], Float.parseFloat(arrBuffer[1]), Float.parseFloat(arrBuffer[2]), arrBuffer[3]);
                    App.getInstance().getCompany().getClientsProducersGraph().addVertex(clp);
                    //System.out.println(clp);
                    counter++;
                } else {
                    System.out.printf("V - Line number: %d isn't valid.\n", counter);
                    counter++;
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Graph Vertexes file not found");
        }

        try {

            Scanner scanner2 = new Scanner(fileEdges);
            scanner2.nextLine(); //skip first line of file
            counter = 2;

            while (scanner2.hasNextLine()) {

                buffer = scanner2.nextLine();
                String[] arrBuffer = buffer.split(",");         //Loc id 1, Loc id 2, length (m)

                if (arrBuffer.length == 3) {

                    Iterator<ClientsProducers> cp = App.getInstance().getCompany().getClientsProducersGraph().vertices().iterator();
                    boolean exist = false;
                    boolean exist2 = false;
                    ClientsProducers aux = null;
                    ClientsProducers aux2 = null;

                    while (cp.hasNext()) {
                        ClientsProducers c = cp.next();
                        if (c.getLocationID().equals(arrBuffer[0].trim())) {
                            exist = true;
                            aux = new ClientsProducers(c);
                        }
                        if (c.getLocationID().equals(arrBuffer[1].trim())) {
                            exist2 = true;
                            aux2 = new ClientsProducers(c);
                        }
                    }

                    if (exist && exist2) {
                        App.getInstance().getCompany().getClientsProducersGraph().addEdge(aux, aux2, Integer.parseInt(arrBuffer[2].trim()));
                    } else {
                        System.out.println("\nOne of the locations didn't exist\n");
                    }

                    counter++;
                } else {
                    System.out.printf("E - Line number: %d isn't valid.\n", counter);
                    counter++;
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println("Graph Edges file not found");
        }

        MapGraph<ClientsProducers, Integer> cpgraph = App.getInstance().getCompany().getClientsProducersGraph();
        int cpVert = App.getInstance().getCompany().getClientsProducersGraph().numVertices();
        ArrayList<ClientsProducers> cp = cpgraph.vertices();
        ClientsProducers cpOrig = cp.get(0);

        boolean connected = isConnected(cpgraph, cpOrig, cp);
        System.out.printf("Connected graph: %b \n", connected);

        int diameter = GraphDiameter.getDiameter(cpgraph);
        System.out.printf("Graph diameter: %d \n", diameter);
        //System.out.println(graph);
    }

    public static boolean isConnected(MapGraph<ClientsProducers, Integer> cpgraph, ClientsProducers cpOrig, ArrayList<ClientsProducers> cp) {

        LinkedList<ClientsProducers> dfsResults = Algorithms.DepthFirstSearch(cpgraph, cpOrig);
        boolean connected = true;

        int i;
        for (i = 0; i < cp.size(); i++) {
            if (!dfsResults.contains(cp.get(i))) {
                //System.out.println("not connected");
                connected = false;
            }
            //System.out.println("seeing if its connected");
        }

        return connected;
    }

    public static Pair<Integer, Integer> importBasketList(File file) {

        int clients = 0;
        int producers = 0;

        try {
            Scanner scanner = new Scanner(file);
            String[] line = scanner.nextLine().split(",");

            ArrayList<String> productsName = new ArrayList<>();
            int len = line.length;

            for (int i = 2; i < len; i++) {
                line[i] = takeCommasOut(line[i]);
                productsName.add(line[i]);
            }

            int lineCounter = 1;

            while (scanner.hasNextLine()) {
                line = scanner.nextLine().split(",");
                if (line.length == len) {
                    for (int i = 0; i < line.length; i++) {
                        line[i] = takeCommasOut(line[i]);
                    }
                    ClientsProducers cp = App.getInstance().getClientProducerByCode(line[0]);
                    if (cp != null) {
                        int day = Integer.parseInt(line[1]);
                        ArrayList<Product> products = new ArrayList<>();

                        for (int i = 2; i < len; i++) {
                            products.add(new Product(productsName.get(i - 2), Float.parseFloat(line[i])));
                        }

                        ClientBasket basket = new ClientBasket(cp, products);

                        if (isThisHub(cp, 0)) {
                            App.getInstance().getCompany().getOrders().addHubOrder(day, basket);
                        }

                        if (cp.getType().equalsIgnoreCase(Constants.PRODUTOR)) {
                            App.getInstance().getCompany().getStock().addStock(day, basket);
                            producers++;
                        } else {
                            App.getInstance().getCompany().getOrders().addOrder(day, basket);
                            clients++;
                        }
                    } else {
                        System.out.println("Client Producer in line " + lineCounter + " not found");
                    }
                }
                lineCounter++;
            }

        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            return null;
        }
        return new ImmutablePair<>(clients, producers);
    }

    private static String takeCommasOut(String element) {
        if (element.contains("\"")) {
            element = element.substring(1, element.length() - 1);
        }
        return element;
    }

    private static boolean isThisHub(ClientsProducers entity, int idx) {
        if (idx >= App.getInstance().getCompany().getHubStore().getHubs().size()) return false;

        if (App.getInstance().getCompany().getHubStore().getHub(idx).equals(entity)) {
            return true;
        }
        return isThisHub(entity, idx + 1);
    }

}
