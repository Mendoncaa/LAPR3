package app.domain.shared;

import app.controller.App;
import app.domain.model.BasketElement;
import app.domain.model.ClientsProducers;
import app.domain.model.ExpeditionList;
import app.graph.Algorithms;
import app.graph.map.MapGraph;

import java.util.*;

public class ExpeditionListShortestPath {

    static MapGraph<ClientsProducers, Integer> clpGraph = App.getInstance().getCompany().getClientsProducersGraph();

    static  ArrayList<ClientsProducers> availableHubs = App.getInstance().getCompany().getHubStore().getHubs();
    private static ClientsProducers current;
    private static ClientsProducers cHub;
    private static ArrayList<BasketElement> beBuffer;
    private static ArrayList<ClientsProducers> proExpList = new ArrayList<>();
    private static final ArrayList<ClientsProducers> hubList = new ArrayList<>();

    private static  ArrayList<ClientsProducers> bufferPathDistances = new ArrayList<>();
    private static final LinkedList<ClientsProducers> proPath = new LinkedList<>();
    private static final LinkedList<ClientsProducers> hubPath = new LinkedList<>();
    private static final LinkedList<ClientsProducers> bridgePath = new LinkedList<>();
    private static final LinkedList<ClientsProducers> finalPath = new LinkedList<>();
    private static List<LinkedList<ClientsProducers>> pCombinations = new LinkedList<>();
    private static List<LinkedList<ClientsProducers>> hCombinations = new LinkedList<>();
    public static final Map<ClientsProducers, Integer> hubBaskets = new HashMap<>();
    public static LinkedList<String> getExpListShortestPath(ArrayList<ExpeditionList> expList) {

        double totalDistance = 0;

        //for each expedition list, get client closest hub, and get producers who deliver any product
        for (ExpeditionList e: expList) {
            current = e.getReceiver();

            cHub = ClosestPointsCheck.getClosestHub(current, clpGraph);

            if(!hubBaskets.containsKey(cHub)) {
                hubBaskets.put(cHub, 1);
            }else {
                int old = hubBaskets.get(cHub);
                hubBaskets.replace(cHub, old, old+1);
            }

            beBuffer = e.getBasketElements();

            for(BasketElement b : beBuffer) {
                if(!proExpList.contains(b.getProducer()) && b.getProduct().getQuantity() != 0) {
                    proExpList.add(b.getProducer());
                }
            }

        }

        //System.out.println(proExpList);

        ArrayList<ClientsProducers> proExpListClone = (ArrayList<ClientsProducers>) proExpList.clone();

        //create all permutations of producers to get CSP
        pCombinations = listPermutations(proExpList);

        //get shortest path between producers in exp list
        totalDistance = getShortestPathWithNodes(pCombinations, proPath);
        //System.out.println(proPath);

        //get list of hubs in previous map
        for (Map.Entry<ClientsProducers, Integer> mp : hubBaskets.entrySet()) {
                hubList.add(mp.getKey());
        }

        //shortest path between last producer and first hub
        Integer sPathResults = Algorithms.shortestPath(clpGraph, proPath.getLast(), hubList.get(0), Integer::compare, Integer::sum, 0, bridgePath);
        if(sPathResults != null){
            totalDistance += sPathResults;
        }

        //System.out.println(bridgePath);
        //System.out.println(proExpList);
        //System.out.println(hubList);

        ArrayList<ClientsProducers> hubListClone = (ArrayList<ClientsProducers>) hubList.clone();

        bridgePath.removeIf(filter -> !hubList.contains(filter) && !proExpListClone.contains(filter));

        //get pairs of hubs to get CSP
        hCombinations = listPermutations(hubListClone);

        //get shortest path between hubs in exp list
        double hubDistBuffer = getShortestPathWithNodes(hCombinations, hubPath);

        //hubPath.removeIf(filter -> !hubListClone.contains(filter) && !proExpListClone.contains(filter));

        totalDistance += hubDistBuffer;

        for (ClientsProducers clientsProducers : proPath) {
            if (!finalPath.contains(clientsProducers)) {
                finalPath.add(clientsProducers);
            }
        }

        for (ClientsProducers clientsProducers : hubPath) {
            if (!finalPath.contains(clientsProducers)) {
                finalPath.add(clientsProducers);
            }
        }

        //finalPath.addAll(proPath);
        //finalPath.addAll(bridgePath);
        //finalPath.addAll(hubPath);

        //System.out.println(generateToBePrinted(proPath));
        //System.out.println(generateToBePrinted(bridgePath));
        //System.out.println(generateToBePrinted(hubPath));

        //System.out.println(hubBaskets);
        //System.out.println(pCombinations);
        //System.out.println(hCombinations);

        return generateToBePrinted(finalPath);
    }

    private static double getShortestPathWithNodes(List<LinkedList<ClientsProducers>> combinations, LinkedList<ClientsProducers> path) {

        //System.out.println("starting shortestpathwithnodes");

        double minLength = 0;
        double currentLength = 0;
        LinkedList<ClientsProducers> workList = new LinkedList<>();
        LinkedList<ClientsProducers> shortestPathBuffer = new LinkedList<>();

        //System.out.println(combinations);

        for (LinkedList<ClientsProducers> combo : combinations) {

            workList.clear();
            currentLength = 0;

            for (int i = 0; i < combo.size() - 1; i++) {

                shortestPathBuffer.clear();
                Integer shortestPathBufferLength = Algorithms.shortestPath(clpGraph, combo.get(i), combo.get(i + 1),Integer::compare, Integer::sum, 0, shortestPathBuffer);
                //System.out.println(combo.get(i));
                //System.out.println(combo.get(i+1));
                //System.out.println(clpGraph.validVertex(combo.get(i)));
                //System.out.println(clpGraph.validVertex(combo.get(i+1)));

                // Path doesn't exist anymore. Skip this iteration.
                if (shortestPathBufferLength == 0) {
                    continue;
                }
                currentLength += shortestPathBufferLength;
                // If it's over the minimum length of another attempt, it's unnecessary to continue.
                // Also verifies if minLength has been set.
                if (currentLength > minLength && minLength != 0) {
                    continue;
                }
                // Merge current LinkedList combination with the latest shortest path.
                mergeLLists(workList, shortestPathBuffer);
            }
            if (minLength == 0 || minLength > currentLength) {
                minLength = currentLength;
                path.clear();
                path.addAll(workList);
            }
        }
        return minLength;
    }

    private static void mergeLLists(LinkedList<ClientsProducers> origin, LinkedList<ClientsProducers> addon) {

        // If the size of the original LinkedList is 0, simply add all of the elements of the other LinkedList.
        if (origin.size() == 0) {

            origin.addAll(addon);
            return;
        }
        // Verifies if the head of the original LinkedList and the tail of the other LinkedList match.
        // If it does not, it throws the below exception.
        if (!(origin.getLast() == addon.getFirst())) {
            throw new ArrayStoreException("Error merging Linked Lists: head doesnt match tail");
        }
        // Remove the first element of the other LinkedList.
        addon.removeFirst();
        origin.addAll(addon);
    }

    public static List<LinkedList<ClientsProducers>> listPermutations(ArrayList<ClientsProducers> proExpList) {

        if (proExpList.size() == 0) {
            List<LinkedList<ClientsProducers>> result = new LinkedList<>();
            result.add(new LinkedList<>());
            return result;
        }

        List<LinkedList<ClientsProducers>> returnMe = new LinkedList<>();

        ClientsProducers firstElement = proExpList.remove(0);

        List<LinkedList<ClientsProducers>> recursiveReturn = listPermutations(proExpList);
        for (LinkedList<ClientsProducers> li : recursiveReturn) {

            for (int index = 0; index <= li.size(); index++) {
                LinkedList<ClientsProducers> temp = new LinkedList<ClientsProducers>(li);
                temp.add(index, firstElement);
                returnMe.add(temp);
            }

        }
        return returnMe;
    }

    private static LinkedList<String> generateToBePrinted (LinkedList<ClientsProducers> finalPath) {

        LinkedList<String> tobePrinted = new LinkedList<>();

        for(int i = 0; i < finalPath.size(); i++) {

            String cpCodeBuffer = finalPath.get(i).getCode();

            tobePrinted.add(cpCodeBuffer);
        }

        return tobePrinted;
    }

    public static Map<String, Integer> generateToBePrintedMap () {

        Map<String, Integer> tobePrintedMap = new HashMap<>();

        for (Map.Entry<ClientsProducers, Integer> mp : hubBaskets.entrySet()) {
            tobePrintedMap.put(mp.getKey().getCode(), mp.getValue());
        }

        return tobePrintedMap;
    }
}
