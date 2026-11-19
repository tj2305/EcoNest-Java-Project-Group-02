package econest.members.sa26610146.task5;

import econest.common.AppStore;
import econest.common.Client;
import econest.common.CustomerOrder;
import econest.common.OrderLine;
import econest.common.Product;
import econest.ui.Refreshable;
import econest.ui.UiSupport;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;

/** Member SA26610146 - Task 5: create orders, add products, and calculate totals. */
public class OrderManagementPanel extends JPanel implements Refreshable {
    private final AppStore store = AppStore.getInstance();
    private final JComboBox<Client> clientBox = new JComboBox<Client>();
    private final JComboBox<Product> productBox = new JComboBox<Product>();
    private final JSpinner quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 999, 1));
    private final DefaultTableModel draftModel = UiSupport.tableModel("Product ID", "Product", "Quantity", "Unit price (LKR)", "Line total (LKR)");
    private final DefaultTableModel ordersModel = UiSupport.tableModel("Order ID", "Client", "Order date", "Items", "Total (LKR)", "Status");
    private final JTable draftTable = new JTable(draftModel);
    private final JTable ordersTable = new JTable(ordersModel);
    private final List<OrderLine> draftItems = new ArrayList<OrderLine>();
    private final JLabel totalLabel = new JLabel("Draft total: LKR 0.00");

    public OrderManagementPanel() {
        setLayout(new BorderLayout(8, 8));
        JPanel form = new JPanel(new GridLayout(1, 3, 8, 5));
        form.add(UiSupport.labeled("Client", clientBox));
        form.add(UiSupport.labeled("Product", productBox));
        form.add(UiSupport.labeled("Quantity", quantitySpinner));
        JButton addItemButton = new JButton("Add product to draft");
        JPanel top = new JPanel(new BorderLayout(6, 6)); top.add(form, BorderLayout.CENTER); top.add(addItemButton, BorderLayout.EAST);
        add(top, BorderLayout.NORTH);

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                titledTable("Current order draft", draftTable), titledTable("Saved orders", ordersTable));
        split.setResizeWeight(0.52); add(split, BorderLayout.CENTER);
        JButton saveButton = new JButton("Save order"); JButton clearButton = new JButton("Clear draft");
        JPanel bottom = new JPanel(new BorderLayout()); bottom.add(totalLabel, BorderLayout.WEST);
        bottom.add(UiSupport.buttonRow(saveButton, clearButton), BorderLayout.EAST); add(bottom, BorderLayout.SOUTH);
        addItemButton.addActionListener(event -> addDraftItem());
        saveButton.addActionListener(event -> saveOrder()); clearButton.addActionListener(event -> clearDraft());
        refresh();
    }

    private JPanel titledTable(String title, JTable table) {
        JPanel panel = new JPanel(new BorderLayout(4, 4)); panel.add(new JLabel(title), BorderLayout.NORTH);
        panel.add(UiSupport.tablePanel(table), BorderLayout.CENTER); return panel;
    }

    private void addDraftItem() {
        Product product = (Product) productBox.getSelectedItem();
        if (product == null) { UiSupport.showError(this, "Add a product first."); return; }
        int quantity = ((Number) quantitySpinner.getValue()).intValue();
        int alreadyAdded = 0;
        for (OrderLine line : draftItems) if (line.getProductId().equals(product.getId())) alreadyAdded += line.getQuantity();
        if (alreadyAdded + quantity > product.getQuantityOnHand()) {
            UiSupport.showError(this, "The draft quantity is higher than the available stock."); return;
        }
        draftItems.add(new OrderLine(product, quantity)); refreshDraft();
    }

    private void saveOrder() {
        Client client = (Client) clientBox.getSelectedItem();
        if (client == null) { UiSupport.showError(this, "Add a client first."); return; }
        if (draftItems.isEmpty()) { UiSupport.showError(this, "Add at least one product to the draft."); return; }
        Map<String, Integer> quantities = new HashMap<String, Integer>();
        for (OrderLine line : draftItems) {
            Integer current = quantities.get(line.getProductId());
            quantities.put(line.getProductId(), (current == null ? 0 : current) + line.getQuantity());
        }
        for (Map.Entry<String, Integer> entry : quantities.entrySet()) {
            Product product = store.findProduct(entry.getKey());
            if (product == null || product.getQuantityOnHand() < entry.getValue()) {
                UiSupport.showError(this, "Stock changed while this draft was open. Review the product quantities."); return;
            }
        }
        CustomerOrder order = new CustomerOrder(store.nextOrderId(), client);
        for (OrderLine line : draftItems) order.addItem(line);
        for (Map.Entry<String, Integer> entry : quantities.entrySet()) store.findProduct(entry.getKey()).setQuantityOnHand(
                store.findProduct(entry.getKey()).getQuantityOnHand() - entry.getValue());
        store.addOrder(order); clearDraft(); refresh();
    }

    private void refreshDraft() {
        draftModel.setRowCount(0); double total = 0.0;
        for (OrderLine line : draftItems) {
            draftModel.addRow(new Object[] {line.getProductId(), line.getProductName(), line.getQuantity(),
                String.format("%.2f", line.getUnitPrice()), String.format("%.2f", line.getLineTotal())});
            total += line.getLineTotal();
        }
        totalLabel.setText("Draft total: " + UiSupport.money(total));
    }

    private void clearDraft() { draftItems.clear(); refreshDraft(); }

    @Override
    public void refresh() {
        String clientId = clientBox.getSelectedItem() instanceof Client ? ((Client) clientBox.getSelectedItem()).getId() : null;
        String productId = productBox.getSelectedItem() instanceof Product ? ((Product) productBox.getSelectedItem()).getId() : null;
        clientBox.removeAllItems(); for (Client client : store.getClients()) clientBox.addItem(client);
        productBox.removeAllItems(); for (Product product : store.getProducts()) productBox.addItem(product);
        if (clientId != null) for (int i = 0; i < clientBox.getItemCount(); i++) if (clientId.equals(clientBox.getItemAt(i).getId())) clientBox.setSelectedIndex(i);
        if (productId != null) for (int i = 0; i < productBox.getItemCount(); i++) if (productId.equals(productBox.getItemAt(i).getId())) productBox.setSelectedIndex(i);
        ordersModel.setRowCount(0);
        for (CustomerOrder order : store.getOrders()) {
            StringBuilder names = new StringBuilder();
            for (OrderLine line : order.getItems()) { if (names.length() > 0) names.append(", "); names.append(line.getProductName()).append(" x").append(line.getQuantity()); }
            ordersModel.addRow(new Object[] {order.getId(), order.getClient().getName(), order.getOrderDate(), names.toString(),
                String.format("%.2f", order.getTotal()), order.getDeliveryStatus()});
        }
        refreshDraft();
    }
}
