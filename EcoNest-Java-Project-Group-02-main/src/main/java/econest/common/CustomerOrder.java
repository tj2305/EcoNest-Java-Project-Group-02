package econest.common;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CustomerOrder {
    private final String id;
    private final Client client;
    private final LocalDate orderDate;
    private final List<OrderLine> items = new ArrayList<OrderLine>();
    private String deliveryStatus = "PENDING";
    private DeliveryAgent deliveryAgent;
    private LocalDate scheduledDate;

    public CustomerOrder(String id, Client client) {
        this.id = id;
        this.client = client;
        this.orderDate = LocalDate.now();
    }

    public String getId() { return id; }
    public Client getClient() { return client; }
    public LocalDate getOrderDate() { return orderDate; }
    public List<OrderLine> getItems() { return Collections.unmodifiableList(items); }
    public void addItem(Product product, int quantity) { items.add(new OrderLine(product, quantity)); }
    public void addItem(OrderLine item) { items.add(item); }
    public double getTotal() {
        double total = 0.0;
        for (OrderLine item : items) total += item.getLineTotal();
        return total;
    }
    public String getDeliveryStatus() { return deliveryStatus; }
    public void setDeliveryStatus(String deliveryStatus) { this.deliveryStatus = deliveryStatus; }
    public DeliveryAgent getDeliveryAgent() { return deliveryAgent; }
    public void setDeliveryAgent(DeliveryAgent deliveryAgent) { this.deliveryAgent = deliveryAgent; }
    public LocalDate getScheduledDate() { return scheduledDate; }
    public void setScheduledDate(LocalDate scheduledDate) { this.scheduledDate = scheduledDate; }

    @Override
    public String toString() {
        return id + " - " + client.getName() + " (LKR " + String.format("%.2f", getTotal()) + ")";
    }
}
