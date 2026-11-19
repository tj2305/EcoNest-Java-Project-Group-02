package econest.members.sa26610153.task1;

import econest.common.AppStore;
import econest.common.Product;
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

/** Member SA26610153 - Task 1: manage the product catalogue. */
public class ProductCataloguePanel extends JPanel implements Refreshable {
    private final AppStore store = AppStore.getInstance();
    private final DefaultTableModel model = UiSupport.tableModel("ID", "Product", "Category", "Price (LKR)", "Eco rating", "Quantity", "Reorder level");
    private final JTable table = new JTable(model);
    private final JTextField nameField = new JTextField();
    private final JComboBox<String> categoryBox = new JComboBox<String>(new String[] {"Indoor Plant", "Planter", "Gardening Kit"});
    private final JTextField priceField = new JTextField();
    private final JTextField ratingField = new JTextField();
    private final JTextField quantityField = new JTextField();
    private final JTextField reorderField = new JTextField();

    public ProductCataloguePanel() {
        setLayout(new BorderLayout(8, 8));
        JPanel form = new JPanel(new GridLayout(2, 3, 8, 5));
        form.add(UiSupport.labeled("Product name", nameField));
        form.add(UiSupport.labeled("Category", categoryBox));
        form.add(UiSupport.labeled("Unit price (LKR)", priceField));
        form.add(UiSupport.labeled("Eco rating (1-5)", ratingField));
        form.add(UiSupport.labeled("Quantity on hand", quantityField));
        form.add(UiSupport.labeled("Reorder level", reorderField));
        add(form, BorderLayout.NORTH);
        add(UiSupport.tablePanel(table), BorderLayout.CENTER);

        JButton addButton = new JButton("Add product");
        JButton updateButton = new JButton("Update selected");
        JButton removeButton = new JButton("Remove selected");
        JButton clearButton = new JButton("Clear fields");
        add(UiSupport.buttonRow(addButton, updateButton, removeButton, clearButton), BorderLayout.SOUTH);
        addButton.addActionListener(event -> addProduct());
        updateButton.addActionListener(event -> updateProduct());
        removeButton.addActionListener(event -> removeProduct());
        clearButton.addActionListener(event -> clearFields());
        table.getSelectionModel().addListSelectionListener(event -> loadSelected());
        refresh();
    }

    private Product readProduct(String id) {
        String name = nameField.getText().trim();
        double price = Double.parseDouble(priceField.getText().trim());
        int rating = Integer.parseInt(ratingField.getText().trim());
        int quantity = Integer.parseInt(quantityField.getText().trim());
        int reorder = Integer.parseInt(reorderField.getText().trim());
        if (name.isEmpty() || price <= 0 || rating < 1 || rating > 5 || quantity < 0 || reorder < 0) {
            throw new IllegalArgumentException("Enter a name, a positive price, an eco rating from 1 to 5, and non-negative stock values.");
        }
        return new Product(id, name, (String) categoryBox.getSelectedItem(), price, rating, quantity, reorder);
    }

    private void addProduct() {
        try {
            store.addProduct(readProduct(store.nextProductId()));
            refresh();
            clearFields();
        } catch (IllegalArgumentException ex) {
            UiSupport.showError(this, ex.getMessage() == null ? "Check the numeric fields." : ex.getMessage());
        }
    }

    private void updateProduct() {
        int row = table.getSelectedRow();
        if (row < 0) { UiSupport.showError(this, "Select a product first."); return; }
        try {
            String id = String.valueOf(model.getValueAt(table.convertRowIndexToModel(row), 0));
            Product current = store.findProduct(id);
            Product values = readProduct(id);
            current.setName(values.getName());
            current.setCategory(values.getCategory());
            current.setUnitPrice(values.getUnitPrice());
            current.setEcoRating(values.getEcoRating());
            current.setQuantityOnHand(values.getQuantityOnHand());
            current.setReorderLevel(values.getReorderLevel());
            refresh();
        } catch (IllegalArgumentException ex) {
            UiSupport.showError(this, ex.getMessage() == null ? "Check the numeric fields." : ex.getMessage());
        }
    }

    private void removeProduct() {
        int row = table.getSelectedRow();
        if (row < 0) { UiSupport.showError(this, "Select a product first."); return; }
        String id = String.valueOf(model.getValueAt(table.convertRowIndexToModel(row), 0));
        Product product = store.findProduct(id);
        store.getProducts().remove(product);
        refresh();
        clearFields();
    }

    private void loadSelected() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        row = table.convertRowIndexToModel(row);
        Product product = store.findProduct(String.valueOf(model.getValueAt(row, 0)));
        if (product == null) return;
        nameField.setText(product.getName());
        categoryBox.setSelectedItem(product.getCategory());
        priceField.setText(String.valueOf(product.getUnitPrice()));
        ratingField.setText(String.valueOf(product.getEcoRating()));
        quantityField.setText(String.valueOf(product.getQuantityOnHand()));
        reorderField.setText(String.valueOf(product.getReorderLevel()));
    }

    private void clearFields() {
        nameField.setText(""); priceField.setText(""); ratingField.setText("");
        quantityField.setText(""); reorderField.setText("");
        table.clearSelection();
    }

    @Override
    public void refresh() {
        model.setRowCount(0);
        for (Product product : store.getProducts()) {
            model.addRow(new Object[] {product.getId(), product.getName(), product.getCategory(),
                String.format("%.2f", product.getUnitPrice()), product.getEcoRating(),
                product.getQuantityOnHand(), product.getReorderLevel()});
        }
    }
}
