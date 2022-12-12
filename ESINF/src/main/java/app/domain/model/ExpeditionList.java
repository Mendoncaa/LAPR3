package app.domain.model;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

public class ExpeditionList {

    private Set<BasketElement> basketElements;
    private ClientsProducers receiver;

    public ExpeditionList(Set<BasketElement> basketElements, ClientsProducers receiver) {
        this.basketElements = basketElements;
        this.receiver = receiver;
    }

    public ExpeditionList(ClientsProducers receiver) {
        this.receiver = receiver;
        this.basketElements = new TreeSet<>();
    }

    public Set<BasketElement> getBasketElements() {
        return basketElements;
    }

    public ClientsProducers getReceiver() {
        return receiver;
    }

}
