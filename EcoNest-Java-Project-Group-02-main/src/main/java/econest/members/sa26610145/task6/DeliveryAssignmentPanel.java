package econest.members.sa26610145.task6;

import econest.common.AppStore;
import econest.common.CustomerOrder;
import econest.common.DeliveryAgent;
import econest.common.EmailNotificationService;
import econest.ui.Refreshable;
import econest.ui.UiSupport;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/** Member SA26610145 - Task 6: assign agents and track order delivery status. */
public class DeliveryAssignmentPanel extends JPanel implements Refreshable {
    private final AppStore store = AppStore.getInstance();
    private final DefaultTableModel model = UiSupport.tableModel("Order ID", "Client", "Total (LKR)", "Delivery agent", "Scheduled date", "Status");
    private final JTable table = new JTable(model);
    private final JComboBox<DeliveryAgent> agentBox = new JComboBox<DeliveryAgent>();
    private final JTextField dateField = new JTextField();
    private final JComboBox<String> statusBox = new JComboBox<String>(new String[] {"PENDING", "SCHEDULED", "DISPATCHED", "DELIVERED"});

    public DeliveryAssignmentPanel() {
        setLayout(new BorderLayout(8, 8));
        JPanel fields = new JPanel(new GridLayout(1, 3, 8, 5));
        fields.add(UiSupport.labeled("Delivery agent", agentBox));
        fields.add(UiSupport.labeled("Scheduled date (YYYY-MM-DD)", dateField));
        fields.add(UiSupport.labeled("Delivery status", statusBox));
        add(fields, BorderLayout.NORTH); add(UiSupport.tablePanel(table), BorderLayout.CENTER);
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.add(new JLabel("Select an order in the table, then update its agent, date, and status."), BorderLayout.CENTER);
        javax.swing.JButton saveButton = new javax.swing.JButton("Save delivery details");
        bottom.add(saveButton, BorderLayout.EAST); add(bottom, BorderLayout.SOUTH);
        table.getSelectionModel().addListSelectionListener(event -> loadSelected());
        saveButton.addActionListener(event -> saveAssignment()); refresh();
    }

    private void loadSelected() {
        int row = table.getSelectedRow(); if (row < 0) return; row = table.convertRowIndexToModel(row);
        CustomerOrder order = store.findOrder(String.valueOf(model.getValueAt(row, 0))); if (order == null) return;
        agentBox.setSelectedItem(order.getDeliveryAgent());
        dateField.setText(order.getScheduledDate() == null ? "" : order.getScheduledDate().toString());
        statusBox.setSelectedItem(order.getDeliveryStatus());
    }

    private void saveAssignment() {
        int row = table.getSelectedRow();
        if (row < 0) { UiSupport.showError(this, "Select an order first."); return; }
        row = table.convertRowIndexToModel(row);
        CustomerOrder order = store.findOrder(String.valueOf(model.getValueAt(row, 0)));
        DeliveryAgent newAgent = (DeliveryAgent) agentBox.getSelectedItem();
        String newStatus = (String) statusBox.getSelectedItem();
        String dateText = dateField.getText().trim();
        LocalDate scheduledDate = null;
        try {
            if (!dateText.isEmpty()) scheduledDate = LocalDate.parse(dateText);
        } catch (DateTimeParseException ex) { UiSupport.showError(this, "Use the date format YYYY-MM-DD."); return; }
        if (newStatus.equals("SCHEDULED") && (newAgent == null || scheduledDate == null)) {
            UiSupport.showError(this, "A scheduled delivery needs an agent and a scheduled date."); return;
        }
        if ((newStatus.equals("DISPATCHED") || newStatus.equals("DELIVERED")) && newAgent == null) {
            UiSupport.showError(this, "Assign a delivery agent before dispatching or completing the order."); return;
        }

        DeliveryAgent oldAgent = order.getDeliveryAgent();
        String oldStatus = order.getDeliveryStatus();
        order.setDeliveryAgent(newAgent); order.setScheduledDate(scheduledDate); order.setDeliveryStatus(newStatus);
        if (newAgent != null && (oldAgent == null || !oldAgent.getId().equals(newAgent.getId()))
                && !newAgent.getEmail().trim().isEmpty()) EmailNotificationService.notifyAgentAssigned(order);
        if (!"DISPATCHED".equals(oldStatus) && "DISPATCHED".equals(newStatus)
                && !order.getClient().getEmail().trim().isEmpty()) EmailNotificationService.notifyClientDispatched(order);
        refresh();
    }

    @Override
    public void refresh() {
        String agentId = agentBox.getSelectedItem() instanceof DeliveryAgent ? ((DeliveryAgent) agentBox.getSelectedItem()).getId() : null;
        agentBox.removeAllItems(); for (DeliveryAgent agent : store.getAgents()) agentBox.addItem(agent);
        if (agentId != null) for (int i = 0; i < agentBox.getItemCount(); i++) if (agentId.equals(agentBox.getItemAt(i).getId())) agentBox.setSelectedIndex(i);
        model.setRowCount(0);
        for (CustomerOrder order : store.getOrders()) model.addRow(new Object[] {
            order.getId(), order.getClient().getName(), String.format("%.2f", order.getTotal()),
            order.getDeliveryAgent() == null ? "Unassigned" : order.getDeliveryAgent().getName(),
            order.getScheduledDate() == null ? "-" : order.getScheduledDate(), order.getDeliveryStatus()});
    }
}
