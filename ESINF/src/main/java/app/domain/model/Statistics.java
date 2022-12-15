package app.domain.model;

import java.util.HashMap;
import java.util.Map;

public class Statistics {

    Map<Integer, StatisticBasket> statisticsForBasket;

    public Statistics() {
        this.statisticsForBasket = new HashMap<>();
    }

    public Map<Integer, StatisticBasket> getStatisticsForBasket() {
        return statisticsForBasket;
    }

    public void addStatisticBasket(int day, StatisticBasket statisticBasket) {
        this.statisticsForBasket.put(day, statisticBasket);
    }
}
