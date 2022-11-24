package app.domain.shared;

import app.controller.App;
import app.domain.model.ClientsProducers;
import app.domain.model.HoursMinutes;
import app.domain.model.Irrigation;
import app.domain.model.IrrigationDevice;
import app.graph.Edge;
import app.graph.Graph;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;
import java.util.function.Predicate;

public class FilesReaderApp {
    public static boolean readIrrigationDeviceFile(File path) {
        try {
            Scanner scanner = new Scanner(path);
            boolean valid = true;

            String[] hours = scanner.nextLine().split(",");

            IrrigationDevice device = new IrrigationDevice();
            for (String hour : hours) {
                String[] time = hour.trim().split(":");
                device.addCicle(new HoursMinutes(Integer.parseInt(time[0]), Integer.parseInt(time[1])));
            }

            while (scanner.hasNextLine()) {
                try {
                    String[] items = scanner.nextLine().split(",");
                    if (items.length == 3) {
                        if (items[2].trim().equalsIgnoreCase("all") || items[2].trim().equalsIgnoreCase("even") || items[2].trim().equalsIgnoreCase("odd")) {
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


        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            return false;
        }

        return true;
    }

    /*
     * Reads the files containing all the countriesand borders information
     *
     * @return countryMap adjacency map containing all the countries and their borders
     */
    public static Graph<ClientsProducers, Edge> readProducerCSV(File file1) throws FileNotFoundException {

        Graph<ClientsProducers, Edge> clientProducersMap = new Graph<ClientsProducers, Edge>() {
            @Override
            public boolean isDirected() {
                return false;
            }

            @Override
            public int numVertices() {
                return 0;
            }

            @Override
            public ArrayList<ClientsProducers> vertices() {
                return null;
            }

            @Override
            public boolean validVertex(ClientsProducers vert) {
                return false;
            }

            @Override
            public int key(ClientsProducers vert) {
                return 0;
            }

            @Override
            public ClientsProducers vertex(int key) {
                return null;
            }

            @Override
            public ClientsProducers vertex(Predicate<ClientsProducers> p) {
                return null;
            }

            @Override
            public Collection<ClientsProducers> adjVertices(ClientsProducers vert) {
                return null;
            }

            @Override
            public int numEdges() {
                return 0;
            }

            @Override
            public Collection<Edge<ClientsProducers, Edge>> edges() {
                return null;
            }

            @Override
            public Edge<ClientsProducers, Edge> edge(ClientsProducers vOrig, ClientsProducers vDest) {
                return null;
            }

            @Override
            public Edge<ClientsProducers, Edge> edge(int vOrigKey, int vDestKey) {
                return null;
            }

            @Override
            public int outDegree(ClientsProducers vert) {
                return 0;
            }

            @Override
            public int inDegree(ClientsProducers vert) {
                return 0;
            }

            @Override
            public Collection<Edge<ClientsProducers, Edge>> outgoingEdges(ClientsProducers vert) {
                return null;
            }

            @Override
            public Collection<Edge<ClientsProducers, Edge>> incomingEdges(ClientsProducers vert) {
                return null;
            }

            @Override
            public boolean addVertex(ClientsProducers vert) {
                return false;
            }

            @Override
            public boolean addEdge(ClientsProducers vOrig, ClientsProducers vDest, Edge weight) {
                return false;
            }

            @Override
            public boolean removeVertex(ClientsProducers vert) {
                return false;
            }

            @Override
            public boolean removeEdge(ClientsProducers vOrig, ClientsProducers vDest) {
                return false;
            }

            @Override
            public Graph<ClientsProducers, Edge> clone() {
                return null;
            }
        };

        Scanner scanner = new Scanner(file1);
        String buffer;

        while (scanner.hasNextLine()) {

            buffer = scanner.nextLine().replaceAll("\\s+", "");
            String[] arrBuffer = buffer.split(",");

            if (arrBuffer.length < 6) {
                continue;
            }

            //ClientsProducers clp = new ClientsProducers(arrBuffer[0], arrBuffer[1], Double.parseDouble(arrBuffer[2]), arrBuffer[3], Double.parseDouble(arrBuffer[4]), Double.parseDouble(arrBuffer[5]));
            //clientProducersMap.addVertex(clp);
        }


        return clientProducersMap;
    }

}
