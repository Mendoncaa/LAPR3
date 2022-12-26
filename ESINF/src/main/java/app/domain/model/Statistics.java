package app.domain.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Statistics {

    Map<Integer, ArrayList<ExpeditionList>> statisticsForBasket;
    Map<Integer, Map<ClientsProducers,ExpeditionList>> statisticsForClient;

    Map<Integer, ExpeditionList> statisticsForBasketDelivered;

    public Statistics() {
        this.statisticsForBasket = new HashMap<>();
    }

    public Map<Integer, ArrayList<ExpeditionList>> getStatisticsForBasket() {
        return statisticsForBasket;
    }

    public Map<Integer, Map<ClientsProducers, ExpeditionList>> getStatisticsForClient() {
        return statisticsForClient;
    }

}
