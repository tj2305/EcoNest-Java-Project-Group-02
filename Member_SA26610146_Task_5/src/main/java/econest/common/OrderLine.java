package econest.common;

public class OrderLine {
    private final String productId;
    private final String productName;
    private final double unitPrice;
    private final int quantity;

    public OrderLine(Product product, int quantity) {
        this.productId = product.getId();
        this.productName = product.getName();
        this.unitPrice = product.getUnitPrice();
        this.quantity = quantity;
    }

    public String getProductId() { return productId; }
    public String getProductName() { return productName; }
    public double getUnitPrice() { return unitPrice; }
    public int getQuantity() { return quantity; }
    public double getLineTotal() { return unitPrice * quantity; }
}
