package econest.common;

public class Client {
    private final String id;
    private String type;
    private String name;
    private String email;
    private String phone;
    private String address;

    public Client(String id, String type, String name, String email, String phone, String address) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

    public String getId() { return id; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    @Override
    public String toString() {
        return id + " - " + name;
    }
}
