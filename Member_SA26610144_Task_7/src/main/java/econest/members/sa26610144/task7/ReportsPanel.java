package econest.members.sa26610144.task7;

import econest.common.AppStore;
import econest.common.CustomerOrder;
import econest.common.Product;
import econest.ui.Refreshable;
import econest.ui.UiSupport;
import java.awt.BorderLayout;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/** Member SA26610144 - Task 7: monthly sales and inventory reports. */
public class ReportsPanel extends JPanel implements Refreshable {
    private final AppStore store = AppStore.getInstance();
    private final JComboBox<YearMonth> monthBox = new JComboBox<YearMonth>();
    private final JTextArea reportArea = new JTextArea();

    public ReportsPanel() {
        setLayout(new BorderLayout(8, 8));
        JPanel top = new JPanel(); top.add(new javax.swing.JLabel("Report month")); top.add(monthBox);
        JButton generateButton = new JButton("Generate report"); top.add(generateButton); add(top, BorderLayout.NORTH);
        reportArea.setEditable(false); reportArea.setFont(new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 13));
        add(new JScrollPane(reportArea), BorderLayout.CENTER);
        generateButton.addActionListener(event -> generateReport()); refresh();
    }

    private void generateReport() {
        YearMonth month = (YearMonth) monthBox.getSelectedItem();
        if (month == null) { reportArea.setText("No report month selected."); return; }
        int orderCount = 0; double revenue = 0.0;
        StringBuilder orderLines = new StringBuilder();
        for (CustomerOrder order : store.getOrders()) {
            if (YearMonth.from(order.getOrderDate()).equals(month)) {
                orderCount++; revenue += order.getTotal();
                orderLines.append("  ").append(order.getId()).append(" | ").append(order.getClient().getName())
                        .append(" | ").append(order.getOrderDate()).append(" | ").append(UiSupport.money(order.getTotal())).append('\n');
            }
        }
        StringBuilder lowStock = new StringBuilder(); int lowCount = 0;
        for (Product product : store.getProducts()) {
            if (product.getQuantityOnHand() <= product.getReorderLevel()) {
                lowCount++;
                lowStock.append("  ").append(product.getId()).append(" | ").append(product.getName())
                        .append(" | On hand: ").append(product.getQuantityOnHand())
                        .append(" | Reorder at: ").append(product.getReorderLevel()).append('\n');
            }
        }
        StringBuilder report = new StringBuilder();
        report.append("ECONEST MONTHLY SALES AND INVENTORY REPORT\n");
        report.append("Month: ").append(month.format(DateTimeFormatter.ofPattern("MMMM yyyy"))).append("\n\n");
        report.append("SALES SUMMARY\n").append("Orders: ").append(orderCount).append('\n')
                .append("Revenue: ").append(UiSupport.money(revenue)).append("\n\n");
        report.append("ORDERS\n").append(orderLines.length() == 0 ? "  No orders for this month.\n" : orderLines);
        report.append("\nLOW-STOCK ALERTS (current inventory)\n").append("Items: ").append(lowCount).append('\n')
                .append(lowStock.length() == 0 ? "  No low-stock products.\n" : lowStock);
        reportArea.setText(report.toString()); reportArea.setCaretPosition(0);
    }

    @Override
    public void refresh() {
        YearMonth selected = (YearMonth) monthBox.getSelectedItem();
        monthBox.removeAllItems();
        YearMonth current = YearMonth.now();
        for (int i = 11; i >= 0; i--) monthBox.addItem(current.minusMonths(i));
        monthBox.setSelectedItem(selected == null ? current : selected);
        generateReport();
    }
}
