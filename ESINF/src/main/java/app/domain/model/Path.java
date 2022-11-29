package app.domain.model;

import java.util.ArrayList;
import java.util.LinkedList;

public class Path {

    private ClientsProducers entity;
    private ArrayList<LinkedList<ClientsProducers>> paths;
    private ArrayList<Integer> dists;

    private int averageDist;


    public Path(ClientsProducers entity, ArrayList<LinkedList<ClientsProducers>> paths, ArrayList<Integer> dists) {
        this.entity = entity;
        this.paths = paths;
        this.dists = dists;
        this.averageDist = getAverageCompanyDist();
    }

    public ClientsProducers getEntity() {
        return entity;
    }

    public ArrayList<LinkedList<ClientsProducers>> getPaths() {
        return paths;
    }

    public ArrayList<Integer> getDists() {
        return dists;
    }

    public int getAverageDist() {
        return averageDist;
    }

    private int getAverageCompanyDist() {
        if (paths.isEmpty()) {
            return 0;
        }

        ArrayList<Integer> distsAux = new ArrayList<>();

        for (int i = 0; i < this.paths.size(); i++) {
            if (this.paths.get(i).getLast().getType().equalsIgnoreCase("Cliente")
                    || this.paths.get(i).getLast().getType().equalsIgnoreCase("Produtor")) {
                distsAux.add(this.dists.get(i));
            }
        }

        int soma = 0;

        for (Integer aux : distsAux) {
            soma += aux;
        }

        return soma / distsAux.size();
    }
}
