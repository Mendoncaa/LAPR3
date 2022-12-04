package app.domain.shared;

import app.controller.App;
import app.domain.model.ClientsProducers;
import app.domain.model.HoursMinutes;
import app.domain.model.Irrigation;
import app.domain.model.IrrigationDevice;
import app.graph.Algorithms;
import app.graph.Edge;
import app.graph.map.MapGraph;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;

import static app.graph.Algorithms.DepthFirstSearch;

public class FilesReaderApp {

    public static void bootstrap() {


        File graphVertexFile = new File("ESINF/src/files/Big/clientes-produtores_big.csv");
        File graphEdgeFile = new File("ESINF/src/files/Big/distancias_big.csv");
        FilesReaderApp.readProducerCSV(graphVertexFile, graphEdgeFile);

    }

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
        System.out.printf("Connected graph: %b", connected);
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

}
