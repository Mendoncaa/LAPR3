package app.domain.model;

public class BasketElement {
    private Product product;
    private ClientsProducers producer;

    public BasketElement(Product product, ClientsProducers producer) {
        this.product = product;
        this.producer = producer;
    }

    public Product getProduct() {
        return product;
    }

    public ClientsProducers getProducer() {
        return producer;
    }
}
