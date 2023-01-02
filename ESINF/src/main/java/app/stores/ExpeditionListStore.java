package app.stores;

import app.domain.model.ExpeditionList;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class ExpeditionListStore {

    private Map<Integer, ArrayList<ExpeditionList>> expeditionListNoRest;

    public ExpeditionListStore() {
        this.expeditionListNoRest = new TreeMap<>();
    }

    public Map<Integer, ArrayList<ExpeditionList>> getExpeditionListNoRest() {
        return expeditionListNoRest;
    }

    public void addExpeditionListNoRest(int day, ArrayList<ExpeditionList> expeditionList) {
        this.expeditionListNoRest.put(day, expeditionList);
    }

    public boolean existDay(int day) {
        return this.expeditionListNoRest.containsKey(day);
    }

    public ArrayList<ExpeditionList> getExpeditionListNoRestByDay(int day) {
        return this.expeditionListNoRest.get(day);
    }


}
