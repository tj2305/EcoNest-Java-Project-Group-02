package econest.members.sa26610086.task2;

import econest.common.AppStore;
import econest.common.Client;
import econest.ui.Refreshable;
import econest.ui.UiSupport;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/** Member SA26610086 - Task 2: manage household and office clients. */
public class ClientManagementPanel extends JPanel implements Refreshable {
    private final AppStore store = AppStore.getInstance();
    private final DefaultTableModel model = UiSupport.tableModel("ID", "Type", "Name", "Email", "Phone", "Address");
    private final JTable table = new JTable(model);
    private final JComboBox<String> typeBox = new JComboBox<String>(new String[] {"Household", "Office"});
    private final JTextField nameField = new JTextField();
    private final JTextField emailField = new JTextField();
    private final JTextField phoneField = new JTextField();
    private final JTextField addressField = new JTextField();

    public ClientManagementPanel() {
        setLayout(new BorderLayout(8, 8));
        JPanel form = new JPanel(new GridLayout(2, 3, 8, 5));
        form.add(UiSupport.labeled("Client type", typeBox));
        form.add(UiSupport.labeled("Name", nameField));
        form.add(UiSupport.labeled("Email", emailField));
        form.add(UiSupport.labeled("Phone", phoneField));
        form.add(UiSupport.labeled("Address", addressField));
        add(form, BorderLayout.NORTH);
        add(UiSupport.tablePanel(table), BorderLayout.CENTER);
        JButton addButton = new JButton("Add client");
        JButton updateButton = new JButton("Update selected");
        JButton removeButton = new JButton("Remove selected");
        JButton clearButton = new JButton("Clear fields");
        add(UiSupport.buttonRow(addButton, updateButton, removeButton, clearButton), BorderLayout.SOUTH);
        addButton.addActionListener(event -> addClient());
        updateButton.addActionListener(event -> updateClient());
        removeButton.addActionListener(event -> removeClient());
        clearButton.addActionListener(event -> clearFields());
        table.getSelectionModel().addListSelectionListener(event -> loadSelected());
        refresh();
    }

    private Client readClient(String id) {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String address = addressField.getText().trim();
        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || address.isEmpty()) {
            throw new IllegalArgumentException("Enter the client's name, email, phone, and address.");
        }
        return new Client(id, (String) typeBox.getSelectedItem(), name, email, phone, address);
    }

    private void addClient() {
        try {
            store.addClient(readClient(store.nextClientId())); refresh(); clearFields();
        } catch (IllegalArgumentException ex) { UiSupport.showError(this, ex.getMessage()); }
    }

    private void updateClient() {
        int row = table.getSelectedRow();
        if (row < 0) { UiSupport.showError(this, "Select a client first."); return; }
        try {
            String id = String.valueOf(model.getValueAt(table.convertRowIndexToModel(row), 0));
            Client current = store.findClient(id); Client values = readClient(id);
            current.setType(values.getType()); current.setName(values.getName());
            current.setEmail(values.getEmail()); current.setPhone(values.getPhone()); current.setAddress(values.getAddress());
            refresh();
        } catch (IllegalArgumentException ex) { UiSupport.showError(this, ex.getMessage()); }
    }

    private void removeClient() {
        int row = table.getSelectedRow();
        if (row < 0) { UiSupport.showError(this, "Select a client first."); return; }
        String id = String.valueOf(model.getValueAt(table.convertRowIndexToModel(row), 0));
        store.getClients().remove(store.findClient(id)); refresh(); clearFields();
    }

    private void loadSelected() {
        int row = table.getSelectedRow(); if (row < 0) return;
        row = table.convertRowIndexToModel(row);
        Client client = store.findClient(String.valueOf(model.getValueAt(row, 0))); if (client == null) return;
        typeBox.setSelectedItem(client.getType()); nameField.setText(client.getName());
        emailField.setText(client.getEmail()); phoneField.setText(client.getPhone()); addressField.setText(client.getAddress());
    }

    private void clearFields() {
        nameField.setText(""); emailField.setText(""); phoneField.setText(""); addressField.setText(""); table.clearSelection();
    }

    @Override
    public void refresh() {
        model.setRowCount(0);
        for (Client client : store.getClients()) model.addRow(new Object[] {
            client.getId(), client.getType(), client.getName(), client.getEmail(), client.getPhone(), client.getAddress()});
    }
}
