package econest.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public final class UiSupport {
    private UiSupport() { }

    public static DefaultTableModel tableModel(String... columns) {
        return new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
    }

    public static JPanel tablePanel(JTable table) {
        JPanel panel = new JPanel(new BorderLayout(6, 6));
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    public static JPanel buttonRow(JButton... buttons) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        for (JButton button : buttons) panel.add(button);
        return panel;
    }

    public static JPanel labeled(String label, java.awt.Component component) {
        JPanel panel = new JPanel(new BorderLayout(4, 2));
        panel.add(new JLabel(label), BorderLayout.NORTH);
        panel.add(component, BorderLayout.CENTER);
        return panel;
    }

    public static String money(double amount) { return String.format("LKR %.2f", amount); }

    public static void showError(java.awt.Component parent, String message) {
        javax.swing.JOptionPane.showMessageDialog(parent, message, "Check the entered details",
                javax.swing.JOptionPane.ERROR_MESSAGE);
    }
}
