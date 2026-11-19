package econest.common;

public class DeliveryAgent {
    private final String id;
    private String name;
    private String email;
    private String phone;
    private String vehicleDetails;

    public DeliveryAgent(String id, String name, String email, String phone, String vehicleDetails) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.vehicleDetails = vehicleDetails;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getVehicleDetails() { return vehicleDetails; }
    public void setVehicleDetails(String vehicleDetails) { this.vehicleDetails = vehicleDetails; }

    @Override
    public String toString() {
        return id + " - " + name;
    }
}
