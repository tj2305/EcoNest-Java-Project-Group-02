package econest.members.sa26610085.task3;

import econest.common.AppStore;
import econest.common.Product;
import econest.common.StockInRecord;
import econest.ui.Refreshable;
import econest.ui.UiSupport;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.time.format.DateTimeFormatter;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/** Member SA26610085 - Task 3: stock, reorder levels, and supplier stock-in. */
public class InventoryPanel extends JPanel implements Refreshable {
    private final AppStore store = AppStore.getInstance();
    private final DefaultTableModel stockModel = UiSupport.tableModel("ID", "Product", "Quantity on hand", "Reorder level", "Stock status");
    private final DefaultTableModel movementModel = UiSupport.tableModel("Received", "Product", "Quantity", "Supplier");
    private final JTable stockTable = new JTable(stockModel);
    private final JTable movementTable = new JTable(movementModel);
    private final JComboBox<Product> productBox = new JComboBox<Product>();
    private final JTextField quantityField = new JTextField();
    private final JTextField supplierField = new JTextField();
    private final JTextField reorderField = new JTextField();

    public InventoryPanel() {
        setLayout(new BorderLayout(8, 8));
        JPanel form = new JPanel(new GridLayout(1, 4, 8, 5));
        form.add(UiSupport.labeled("Product", productBox));
        form.add(UiSupport.labeled("Stock-in quantity", quantityField));
        form.add(UiSupport.labeled("Supplier", supplierField));
        form.add(UiSupport.labeled("New reorder level", reorderField));
        add(form, BorderLayout.NORTH);

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                titledTable("Current inventory", stockTable), titledTable("Stock-in history", movementTable));
        split.setResizeWeight(0.62);
        add(split, BorderLayout.CENTER);
        JButton stockInButton = new JButton("Receive stock from supplier");
        JButton reorderButton = new JButton("Update reorder level");
        add(UiSupport.buttonRow(stockInButton, reorderButton), BorderLayout.SOUTH);
        stockInButton.addActionListener(event -> stockIn());
        reorderButton.addActionListener(event -> updateReorderLevel());
        stockTable.getSelectionModel().addListSelectionListener(event -> loadSelected());
        refresh();
    }

    private JPanel titledTable(String title, JTable table) {
        JPanel panel = new JPanel(new BorderLayout(4, 4));
        panel.add(new JLabel(title), BorderLayout.NORTH);
        panel.add(UiSupport.tablePanel(table), BorderLayout.CENTER);
        return panel;
    }

    private void stockIn() {
        Product product = (Product) productBox.getSelectedItem();
        if (product == null) { UiSupport.showError(this, "Add a product first."); return; }
        try {
            int quantity = Integer.parseInt(quantityField.getText().trim());
            String supplier = supplierField.getText().trim();
            if (quantity <= 0 || supplier.isEmpty()) throw new IllegalArgumentException("Enter a positive quantity and the supplier name.");
            product.addStock(quantity);
            store.recordStockIn(product, quantity, supplier);
            quantityField.setText(""); supplierField.setText(""); refresh();
        } catch (NumberFormatException ex) { UiSupport.showError(this, "Stock-in quantity must be a whole number."); }
        catch (IllegalArgumentException ex) { UiSupport.showError(this, ex.getMessage()); }
    }

    private void updateReorderLevel() {
        Product product = (Product) productBox.getSelectedItem();
        if (product == null) { UiSupport.showError(this, "Select a product first."); return; }
        try {
            int level = Integer.parseInt(reorderField.getText().trim());
            if (level < 0) throw new IllegalArgumentException("Reorder level cannot be negative.");
            product.setReorderLevel(level); refresh();
        } catch (NumberFormatException ex) { UiSupport.showError(this, "Reorder level must be a whole number."); }
        catch (IllegalArgumentException ex) { UiSupport.showError(this, ex.getMessage()); }
    }

    private void loadSelected() {
        int row = stockTable.getSelectedRow(); if (row < 0) return;
        row = stockTable.convertRowIndexToModel(row);
        Product product = store.findProduct(String.valueOf(stockModel.getValueAt(row, 0)));
        if (product != null) { productBox.setSelectedItem(product); reorderField.setText(String.valueOf(product.getReorderLevel())); }
    }

    @Override
    public void refresh() {
        String selectedId = productBox.getSelectedItem() instanceof Product ? ((Product) productBox.getSelectedItem()).getId() : null;
        productBox.removeAllItems(); stockModel.setRowCount(0);
        for (Product product : store.getProducts()) {
            productBox.addItem(product);
            String status = product.getQuantityOnHand() <= product.getReorderLevel() ? "LOW STOCK" : "OK";
            stockModel.addRow(new Object[] {product.getId(), product.getName(), product.getQuantityOnHand(), product.getReorderLevel(), status});
        }
        if (selectedId != null) for (int i = 0; i < productBox.getItemCount(); i++)
            if (selectedId.equals(productBox.getItemAt(i).getId())) productBox.setSelectedIndex(i);
        movementModel.setRowCount(0);
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        for (int i = store.getStockInRecords().size() - 1; i >= 0; i--) {
            StockInRecord record = store.getStockInRecords().get(i);
            movementModel.addRow(new Object[] {record.getReceivedAt().format(format), record.getProductName(), record.getQuantity(), record.getSupplier()});
        }
    }
}
