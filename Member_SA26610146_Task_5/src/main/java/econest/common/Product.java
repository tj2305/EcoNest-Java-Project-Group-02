package econest.common;

public class Product {
    private final String id;
    private String name;
    private String category;
    private double unitPrice;
    private int ecoRating;
    private int quantityOnHand;
    private int reorderLevel;

    public Product(String id, String name, String category, double unitPrice,
                   int ecoRating, int quantityOnHand, int reorderLevel) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.unitPrice = unitPrice;
        this.ecoRating = ecoRating;
        this.quantityOnHand = quantityOnHand;
        this.reorderLevel = reorderLevel;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public double getUnitPrice() { return unitPrice; }
    public void setUnitPrice(double unitPrice) { this.unitPrice = unitPrice; }
    public int getEcoRating() { return ecoRating; }
    public void setEcoRating(int ecoRating) { this.ecoRating = ecoRating; }
    public int getQuantityOnHand() { return quantityOnHand; }
    public void setQuantityOnHand(int quantityOnHand) { this.quantityOnHand = quantityOnHand; }
    public int getReorderLevel() { return reorderLevel; }
    public void setReorderLevel(int reorderLevel) { this.reorderLevel = reorderLevel; }
    public void addStock(int amount) { quantityOnHand += amount; }

    @Override
    public String toString() {
        return id + " - " + name;
    }
}
