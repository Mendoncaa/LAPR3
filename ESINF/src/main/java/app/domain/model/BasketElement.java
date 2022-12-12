package app.domain.model;

public class BasketElement {
    private Product product;
    private ClientsProducers producer;

    public BasketElement(Product product, ClientsProducers quantity) {
        this.product = product;
        this.producer = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public ClientsProducers getProducer() {
        return producer;
    }
}
