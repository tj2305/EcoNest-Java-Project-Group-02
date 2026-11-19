package econest.common;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

/** Shared in-memory data for the group project starter. */
public final class AppStore {
    private static final AppStore INSTANCE = new AppStore();
    private final List<Product> products = new ArrayList<Product>();
    private final List<Client> clients = new ArrayList<Client>();
    private final List<DeliveryAgent> agents = new ArrayList<DeliveryAgent>();
    private final List<CustomerOrder> orders = new ArrayList<CustomerOrder>();
    private final List<StockInRecord> stockInRecords = new ArrayList<StockInRecord>();
    private int productSequence = 1;
    private int clientSequence = 1;
    private int agentSequence = 1;
    private int orderSequence = 1;

    private AppStore() {
        addProduct(new Product("P001", "Peace Lily", "Indoor Plant", 1850.00, 5, 12, 4));
        addProduct(new Product("P002", "Recycled Clay Planter", "Planter", 1200.00, 4, 8, 3));
        addProduct(new Product("P003", "Balcony Herb Kit", "Gardening Kit", 3200.00, 5, 5, 2));
        addClient(new Client("C001", "Household", "Sample Household", "home@example.com", "0770000000", "Colombo"));
        addClient(new Client("C002", "Office", "Sample Office", "office@example.com", "0110000000", "Kandy"));
        addAgent(new DeliveryAgent("A001", "Sample Agent", "agent@example.com", "0710000000", "Motorbike - WP ABC 1234"));
        CustomerOrder pending = new CustomerOrder("O001", clients.get(0));
        pending.addItem(products.get(0), 1);
        addOrder(pending);
        CustomerOrder dispatched = new CustomerOrder("O002", clients.get(1));
        dispatched.addItem(products.get(1), 2);
        dispatched.setDeliveryAgent(agents.get(0));
        dispatched.setScheduledDate(LocalDate.now());
        dispatched.setDeliveryStatus("DISPATCHED");
        addOrder(dispatched);
        productSequence = 4;
        clientSequence = 3;
        agentSequence = 2;
        orderSequence = 3;
    }

    public static AppStore getInstance() { return INSTANCE; }
    public List<Product> getProducts() { return products; }
    public List<Client> getClients() { return clients; }
    public List<DeliveryAgent> getAgents() { return agents; }
    public List<CustomerOrder> getOrders() { return orders; }
    public List<StockInRecord> getStockInRecords() { return stockInRecords; }

    public String nextProductId() { return String.format("P%03d", productSequence++); }
    public String nextClientId() { return String.format("C%03d", clientSequence++); }
    public String nextAgentId() { return String.format("A%03d", agentSequence++); }
    public String nextOrderId() { return String.format("O%03d", orderSequence++); }
    public void addProduct(Product product) { products.add(product); }
    public void addClient(Client client) { clients.add(client); }
    public void addAgent(DeliveryAgent agent) { agents.add(agent); }
    public void addOrder(CustomerOrder order) { orders.add(order); }
    public void recordStockIn(Product product, int quantity, String supplier) {
        stockInRecords.add(new StockInRecord(product, quantity, supplier));
    }

    public Product findProduct(String id) {
        for (Product product : products) if (product.getId().equals(id)) return product;
        return null;
    }
    public Client findClient(String id) {
        for (Client client : clients) if (client.getId().equals(id)) return client;
        return null;
    }
    public DeliveryAgent findAgent(String id) {
        for (DeliveryAgent agent : agents) if (agent.getId().equals(id)) return agent;
        return null;
    }
    public CustomerOrder findOrder(String id) {
        for (CustomerOrder order : orders) if (order.getId().equals(id)) return order;
        return null;
    }
}
