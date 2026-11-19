package econest.members.sa26610139.task4;

import econest.common.AppStore;
import econest.common.CustomerOrder;
import econest.common.DeliveryAgent;
import econest.ui.Refreshable;
import econest.ui.UiSupport;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/** Member SA26610139 - Task 4: manage delivery agents and their vehicles. */
public class DeliveryAgentPanel extends JPanel implements Refreshable {
    private final AppStore store = AppStore.getInstance();
    private final DefaultTableModel model = UiSupport.tableModel("ID", "Name", "Email", "Phone", "Vehicle details");
    private final JTable table = new JTable(model);
    private final JTextField nameField = new JTextField();
    private final JTextField emailField = new JTextField();
    private final JTextField phoneField = new JTextField();
    private final JTextField vehicleField = new JTextField();

    public DeliveryAgentPanel() {
        setLayout(new BorderLayout(8, 8));
        JPanel form = new JPanel(new GridLayout(2, 2, 8, 5));
        form.add(UiSupport.labeled("Name", nameField)); form.add(UiSupport.labeled("Email", emailField));
        form.add(UiSupport.labeled("Phone", phoneField)); form.add(UiSupport.labeled("Vehicle details", vehicleField));
        add(form, BorderLayout.NORTH); add(UiSupport.tablePanel(table), BorderLayout.CENTER);
        JButton addButton = new JButton("Add agent"); JButton updateButton = new JButton("Update selected");
        JButton removeButton = new JButton("Remove selected"); JButton clearButton = new JButton("Clear fields");
        add(UiSupport.buttonRow(addButton, updateButton, removeButton, clearButton), BorderLayout.SOUTH);
        addButton.addActionListener(event -> addAgent()); updateButton.addActionListener(event -> updateAgent());
        removeButton.addActionListener(event -> removeAgent()); clearButton.addActionListener(event -> clearFields());
        table.getSelectionModel().addListSelectionListener(event -> loadSelected()); refresh();
    }

    private DeliveryAgent readAgent(String id) {
        String name = nameField.getText().trim(), email = emailField.getText().trim();
        String phone = phoneField.getText().trim(), vehicle = vehicleField.getText().trim();
        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || vehicle.isEmpty())
            throw new IllegalArgumentException("Enter the agent's name, email, phone, and vehicle details.");
        if (!email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$"))
            throw new IllegalArgumentException("Enter a valid email address.");
        if (!phone.matches("^\\+?[0-9][0-9 ()-]{6,19}$"))
            throw new IllegalArgumentException("Enter a valid phone number.");
        for (DeliveryAgent agent : store.getAgents()) {
            if (agent.getId().equals(id)) continue;
            if (agent.getEmail().equalsIgnoreCase(email))
                throw new IllegalArgumentException("Another delivery agent already uses this email address.");
            if (normalizePhone(agent.getPhone()).equals(normalizePhone(phone)))
                throw new IllegalArgumentException("Another delivery agent already uses this phone number.");
        }
        return new DeliveryAgent(id, name, email, phone, vehicle);
    }

    private String normalizePhone(String phone) {
        return phone.replaceAll("[^0-9]", "");
    }

    private void addAgent() {
        try { store.addAgent(readAgent(store.nextAgentId())); refresh(); clearFields(); }
        catch (IllegalArgumentException ex) { UiSupport.showError(this, ex.getMessage()); }
    }
    private void updateAgent() {
        int row = table.getSelectedRow(); if (row < 0) { UiSupport.showError(this, "Select an agent first."); return; }
        try {
            String id = String.valueOf(model.getValueAt(table.convertRowIndexToModel(row), 0));
            DeliveryAgent current = store.findAgent(id), values = readAgent(id);
            current.setName(values.getName()); current.setEmail(values.getEmail());
            current.setPhone(values.getPhone()); current.setVehicleDetails(values.getVehicleDetails()); refresh();
        } catch (IllegalArgumentException ex) { UiSupport.showError(this, ex.getMessage()); }
    }
    private void removeAgent() {
        int row = table.getSelectedRow(); if (row < 0) { UiSupport.showError(this, "Select an agent first."); return; }
        String id = String.valueOf(model.getValueAt(table.convertRowIndexToModel(row), 0));
        for (CustomerOrder order : store.getOrders()) {
            DeliveryAgent assignedAgent = order.getDeliveryAgent();
            if (assignedAgent != null && assignedAgent.getId().equals(id)) {
                UiSupport.showError(this, "This agent is assigned to order " + order.getId()
                        + ". Reassign the order before removing the agent.");
                return;
            }
        }
        store.getAgents().remove(store.findAgent(id)); refresh(); clearFields();
    }
    private void loadSelected() {
        int row = table.getSelectedRow(); if (row < 0) return; row = table.convertRowIndexToModel(row);
        DeliveryAgent agent = store.findAgent(String.valueOf(model.getValueAt(row, 0))); if (agent == null) return;
        nameField.setText(agent.getName()); emailField.setText(agent.getEmail());
        phoneField.setText(agent.getPhone()); vehicleField.setText(agent.getVehicleDetails());
    }
    private void clearFields() { nameField.setText(""); emailField.setText(""); phoneField.setText(""); vehicleField.setText(""); table.clearSelection(); }

    @Override public void refresh() {
        model.setRowCount(0);
        for (DeliveryAgent agent : store.getAgents()) model.addRow(new Object[] {
            agent.getId(), agent.getName(), agent.getEmail(), agent.getPhone(), agent.getVehicleDetails()});
    }
}
