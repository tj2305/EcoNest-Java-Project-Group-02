package econest.common;

import java.time.LocalDateTime;

public class StockInRecord {
    private final String productId;
    private final String productName;
    private final int quantity;
    private final String supplier;
    private final LocalDateTime receivedAt;

    public StockInRecord(Product product, int quantity, String supplier) {
        this.productId = product.getId();
        this.productName = product.getName();
        this.quantity = quantity;
        this.supplier = supplier;
        this.receivedAt = LocalDateTime.now();
    }

    public String getProductId() { return productId; }
    public String getProductName() { return productName; }
    public int getQuantity() { return quantity; }
    public String getSupplier() { return supplier; }
    public LocalDateTime getReceivedAt() { return receivedAt; }
}
