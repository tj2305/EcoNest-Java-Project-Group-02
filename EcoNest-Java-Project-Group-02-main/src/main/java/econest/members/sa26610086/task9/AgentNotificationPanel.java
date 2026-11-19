package econest.members.sa26610086.task9;

import econest.common.AppStore;
import econest.common.CustomerOrder;
import econest.common.EmailNotificationService;
import econest.ui.Refreshable;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/** Member SA26610086 - Task 9: notify an agent about a newly assigned delivery. */
public class AgentNotificationPanel extends JPanel implements Refreshable {
    private final AppStore store = AppStore.getInstance();
    private final JComboBox<CustomerOrder> orderBox = new JComboBox<CustomerOrder>();
    private final JTextArea logArea = new JTextArea(12, 70);

    public AgentNotificationPanel() {
        setLayout(new BorderLayout(8, 8));
        JPanel top = new JPanel(); top.add(new JLabel("Assigned order")); top.add(orderBox);
        JButton sendButton = new JButton("Send / resend agent notification"); top.add(sendButton);
        add(top, BorderLayout.NORTH);
        logArea.setEditable(false); add(new JScrollPane(logArea), BorderLayout.CENTER);
        add(new JLabel("Demo mode: messages are recorded locally; no email is sent until SMTP is configured."), BorderLayout.SOUTH);
        sendButton.addActionListener(event -> sendNotification()); refresh();
    }

    private void sendNotification() {
        CustomerOrder order = (CustomerOrder) orderBox.getSelectedItem();
        if (order == null) { JOptionPane.showMessageDialog(this, "Create an order first."); return; }
        if (order.getDeliveryAgent() == null) { JOptionPane.showMessageDialog(this, "Assign a delivery agent first."); return; }
        if (order.getDeliveryAgent().getEmail().trim().isEmpty()) { JOptionPane.showMessageDialog(this, "The agent does not have an email address."); return; }
        EmailNotificationService.notifyAgentAssigned(order); refresh();
        JOptionPane.showMessageDialog(this, "Notification added to the local demo log.");
    }

    @Override
    public void refresh() {
        String selectedId = orderBox.getSelectedItem() instanceof CustomerOrder
                ? ((CustomerOrder) orderBox.getSelectedItem()).getId() : null;
        orderBox.removeAllItems();
        for (CustomerOrder order : store.getOrders()) orderBox.addItem(order);
        if (selectedId != null) for (int i = 0; i < orderBox.getItemCount(); i++)
            if (selectedId.equals(orderBox.getItemAt(i).getId())) orderBox.setSelectedIndex(i);
        StringBuilder text = new StringBuilder();
        for (String entry : EmailNotificationService.getLog()) {
            if (entry.contains("Subject: New EcoNest delivery ")) text.append(entry).append('\n');
        }
        logArea.setText(text.toString());
    }
}
