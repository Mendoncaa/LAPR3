package app.domain.model;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

public class ExpeditionList {

    private ArrayList<BasketElement> basketElements;
    private ArrayList<BasketElement> basketOrderedElements;
    private ClientsProducers receiver;

    public ExpeditionList(ArrayList<BasketElement> basketElements,ArrayList<BasketElement> basketOrderedElements ,ClientsProducers receiver) {
        this.basketElements = basketElements;
        this.receiver = receiver;
        this.basketOrderedElements = basketOrderedElements;
    }

    public ExpeditionList(ClientsProducers receiver) {
        this.receiver = receiver;
        this.basketElements = new ArrayList<>();
        this.basketOrderedElements = new ArrayList<>();
    }

    public ArrayList<BasketElement> getBasketElements() {
        return basketElements;
    }

    public ArrayList<BasketElement> getBasketOrderedElements() {
        return basketOrderedElements;
    }

    public ClientsProducers getReceiver() {
        return receiver;
    }

}
