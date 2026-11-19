package econest.members.sa26610153.task8;

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

/** Member SA26610153 - Task 8: client dispatch notifications. */
public class ClientNotificationPanel extends JPanel implements Refreshable {
    private final AppStore store = AppStore.getInstance();
    private final JComboBox<CustomerOrder> orderBox = new JComboBox<CustomerOrder>();
    private final JTextArea logArea = new JTextArea(12, 70);

    public ClientNotificationPanel() {
        setLayout(new BorderLayout(8, 8));
        JPanel top = new JPanel();
        top.add(new JLabel("Dispatched order"));
        top.add(orderBox);
        JButton sendButton = new JButton("Send / resend client notification");
        top.add(sendButton);
        add(top, BorderLayout.NORTH);
        logArea.setEditable(false);
        add(new JScrollPane(logArea), BorderLayout.CENTER);
        add(new JLabel("Demo mode: messages are recorded locally; no email is sent until SMTP is configured."), BorderLayout.SOUTH);
        sendButton.addActionListener(event -> sendNotification());
        refresh();
    }

    private void sendNotification() {
        CustomerOrder order = (CustomerOrder) orderBox.getSelectedItem();
        if (order == null) { JOptionPane.showMessageDialog(this, "Create an order first."); return; }
        if (!"DISPATCHED".equals(order.getDeliveryStatus())) {
            JOptionPane.showMessageDialog(this, "The order must have DISPATCHED status before notifying the client.");
            return;
        }
        if (order.getClient().getEmail().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "This client does not have an email address.");
            return;
        }
        EmailNotificationService.notifyClientDispatched(order);
        refresh();
        JOptionPane.showMessageDialog(this, "Notification added to the local demo log.");
    }

    @Override
    public void refresh() {
        String selectedId = orderBox.getSelectedItem() instanceof CustomerOrder
                ? ((CustomerOrder) orderBox.getSelectedItem()).getId() : null;
        orderBox.removeAllItems();
        for (CustomerOrder order : store.getOrders()) orderBox.addItem(order);
        if (selectedId != null) {
            for (int i = 0; i < orderBox.getItemCount(); i++) {
                if (selectedId.equals(orderBox.getItemAt(i).getId())) orderBox.setSelectedIndex(i);
            }
        }
        StringBuilder text = new StringBuilder();
        for (String entry : EmailNotificationService.getLog()) {
            if (entry.contains("Subject: EcoNest order ")) text.append(entry).append('\n');
        }
        logArea.setText(text.toString());
    }
}
