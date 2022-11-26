package app.domain.shared;

import app.controller.App;
import app.domain.model.ClientsProducers;
import app.domain.model.HoursMinutes;
import app.domain.model.Irrigation;
import app.domain.model.IrrigationDevice;
import app.graph.Edge;
import app.graph.map.MapGraph;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FilesReaderApp {

    public static boolean readIrrigationDeviceFile(File path)  {
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
    public static MapGraph<ClientsProducers, Edge<ClientsProducers, Double>> readProducerCSV(File fileVertexes, File fileEdges)  {

        System.out.println("running reading graph");

        MapGraph<ClientsProducers, Edge<ClientsProducers, Double>> clientProducersMap = new MapGraph<>(true);
        String buffer;
        int counter;

        try {

            Scanner scanner = new Scanner(fileVertexes);
            scanner.nextLine(); //skip first line of file
            counter = 2;

            while (scanner.hasNextLine()) {

                buffer = scanner.nextLine();
                String[] arrBuffer = buffer.split(",");

                if (arrBuffer.length != 4) {
                    System.out.printf("V - Line number: %d isn't valid.\n", counter );
                    continue;
                }

                ClientsProducers clp = new ClientsProducers(arrBuffer[0], Float.parseFloat(arrBuffer[1]), Float.parseFloat(arrBuffer[2]), arrBuffer[3]);
                clientProducersMap.addVertex(clp);
                //System.out.println(clp);
                counter++;
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
                String[] arrBuffer = buffer.split(",");

                if (arrBuffer.length != 3) {
                    System.out.printf("E - Line number: %d isn't valid.\n", counter);
                    continue;
                }

                Iterable<ClientsProducers> iterateCP = clientProducersMap.vertices();

                ClientsProducers cpCode1 = null;
                ClientsProducers cpCode2 = null;

                for (ClientsProducers cp : iterateCP) {

                    if (cp.getCode().equals(arrBuffer[0])) {
                        cpCode1 = cp;
                        continue;
                    }

                    if (cp.getCode().equals(arrBuffer[1])) {
                        cpCode2 = cp;
                    }
                }

                if (cpCode1 != null && cpCode2 != null && !cpCode1.equals(cpCode2)) {

                   //clientProducersMap.addEdge(cpCode1, cpCode2, Integer.parseInt(arrBuffer[2]));
                }

                counter++;
            }

        } catch (FileNotFoundException e) {
            System.out.println("Graph Edges file not found");
        }

        System.out.println(clientProducersMap);
        return clientProducersMap;
    }

}
