package econest.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public final class UiSupport {
    public static final Color CANVAS = new Color(245, 248, 245);
    public static final Color SURFACE = Color.WHITE;
    public static final Color TEXT = new Color(32, 48, 39);
    public static final Color MUTED = new Color(101, 117, 106);
    public static final Color BRAND = new Color(42, 116, 77);
    public static final Color BRAND_DARK = new Color(29, 82, 57);
    public static final Color BRAND_LIGHT = new Color(72, 148, 101);
    public static final Color SOFT_GREEN = new Color(225, 240, 228);
    public static final Color BORDER = new Color(220, 229, 221);
    public static final Color SIDEBAR = new Color(24, 58, 47);
    public static final Color SIDEBAR_HOVER = new Color(35, 75, 61);
    public static final Color SIDEBAR_SELECTED = new Color(48, 103, 72);
    public static final Color SIDEBAR_TEXT = new Color(220, 233, 223);
    public static final Color SIDEBAR_MUTED = new Color(160, 186, 168);
    private static final Color DANGER = new Color(157, 56, 61);
    private static final Color DANGER_SOFT = new Color(250, 235, 235);

    private UiSupport() { }

    public static void installTheme() {
        Font base = UIManager.getFont("Label.font");
        if (base == null) base = new Font(Font.SANS_SERIF, Font.PLAIN, 13);
        Font body = base.deriveFont(Font.PLAIN, 13f);
        Font action = base.deriveFont(Font.BOLD, 13f);

        UIManager.put("Panel.background", CANVAS);
        UIManager.put("Label.foreground", TEXT);
        UIManager.put("Label.font", body);
        UIManager.put("Button.font", action);
        UIManager.put("Button.background", BRAND);
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("Button.focus", BORDER);
        UIManager.put("TextField.font", body);
        UIManager.put("TextField.foreground", TEXT);
        UIManager.put("TextField.background", SURFACE);
        UIManager.put("ComboBox.font", body);
        UIManager.put("ComboBox.foreground", TEXT);
        UIManager.put("ComboBox.background", SURFACE);
        UIManager.put("Spinner.font", body);
        UIManager.put("Table.font", body);
        UIManager.put("Table.foreground", TEXT);
        UIManager.put("Table.background", SURFACE);
        UIManager.put("Table.selectionBackground", SOFT_GREEN);
        UIManager.put("Table.selectionForeground", TEXT);
        UIManager.put("Table.gridColor", BORDER);
        UIManager.put("TableHeader.font", action);
        UIManager.put("TableHeader.background", BRAND_DARK);
        UIManager.put("TableHeader.foreground", Color.WHITE);
        UIManager.put("ScrollPane.background", SURFACE);
        UIManager.put("ToolTip.background", new Color(31, 49, 39));
        UIManager.put("ToolTip.foreground", Color.WHITE);
    }

    public static void prepareTaskPanel(JComponent panel) {
        panel.setOpaque(true);
        panel.setBackground(CANVAS);
        panel.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
        styleChildren(panel);
    }

    public static DefaultTableModel tableModel(String... columns) {
        return new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
    }

    public static JPanel tablePanel(JTable table) {
        JPanel panel = new JPanel(new BorderLayout(6, 6));
        panel.setBackground(SURFACE);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    public static JPanel buttonRow(JButton... buttons) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 2));
        panel.setBackground(SURFACE);
        for (JButton button : buttons) panel.add(button);
        return panel;
    }

    public static JPanel labeled(String label, Component component) {
        JPanel panel = new JPanel(new BorderLayout(4, 5));
        panel.setBackground(SURFACE);
        JLabel fieldLabel = new JLabel(label);
        fieldLabel.setForeground(MUTED);
        fieldLabel.setFont(fieldLabel.getFont().deriveFont(Font.BOLD, 11f));
        fieldLabel.setLabelFor(component);
        panel.add(fieldLabel, BorderLayout.NORTH);
        panel.add(component, BorderLayout.CENTER);
        return panel;
    }

    public static String money(double amount) { return String.format("LKR %.2f", amount); }

    public static void showError(Component parent, String message) {
        javax.swing.JOptionPane.showMessageDialog(parent, message, "Check the entered details",
                javax.swing.JOptionPane.ERROR_MESSAGE);
    }

    private static void styleChildren(Container parent) {
        for (Component component : parent.getComponents()) {
            if (component instanceof JPanel) {
                JPanel panel = (JPanel) component;
                panel.setOpaque(true);
                panel.setBackground(SURFACE);
                panel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
            } else if (component instanceof JTable) {
                styleTable((JTable) component);
            } else if (component instanceof JButton) {
                styleButton((JButton) component);
            } else if (component instanceof JTextField) {
                JTextField field = (JTextField) component;
                field.setForeground(TEXT);
                field.setBackground(SURFACE);
                field.setPreferredSize(new Dimension(120, 34));
                field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER),
                        BorderFactory.createEmptyBorder(5, 9, 5, 9)));
            } else if (component instanceof JComboBox) {
                JComboBox<?> combo = (JComboBox<?>) component;
                combo.setForeground(TEXT);
                combo.setBackground(SURFACE);
                combo.setBorder(BorderFactory.createLineBorder(BORDER));
                combo.setPreferredSize(new Dimension(120, 34));
            } else if (component instanceof JSpinner) {
                JSpinner spinner = (JSpinner) component;
                spinner.setBorder(BorderFactory.createLineBorder(BORDER));
                spinner.setPreferredSize(new Dimension(90, 34));
            } else if (component instanceof JScrollPane) {
                JScrollPane scroll = (JScrollPane) component;
                scroll.setBorder(BorderFactory.createLineBorder(BORDER));
                scroll.getViewport().setBackground(SURFACE);
            } else if (component instanceof JTextArea) {
                JTextArea area = (JTextArea) component;
                area.setForeground(TEXT);
                area.setBackground(SURFACE);
                if (Font.MONOSPACED.equals(area.getFont().getFamily())) {
                    area.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
                }
                area.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
            }

            if (component instanceof Container) {
                styleChildren((Container) component);
            }
        }
    }

    private static void styleButton(JButton button) {
        String text = button.getText() == null ? "" : button.getText().toLowerCase();
        boolean destructive = text.contains("remove");
        boolean secondary = text.contains("clear");
        button.setFont(button.getFont().deriveFont(Font.BOLD, 12f));
        button.setForeground(destructive ? DANGER : (secondary ? TEXT : Color.WHITE));
        button.setBackground(destructive ? DANGER_SOFT : (secondary ? CANVAS : BRAND));
        button.setBorder(BorderFactory.createEmptyBorder(9, 14, 9, 14));
        button.setFocusPainted(false);
        button.setContentAreaFilled(true);
        button.setOpaque(true);
        button.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
    }

    private static void styleTable(JTable table) {
        table.setRowHeight(34);
        table.setAutoCreateRowSorter(true);
        table.setFillsViewportHeight(true);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(SOFT_GREEN);
        table.setSelectionForeground(TEXT);
        table.setForeground(TEXT);
        table.setBackground(SURFACE);
        table.setDefaultRenderer(Object.class, new StripedCellRenderer());
        table.setDefaultRenderer(Number.class, new StripedCellRenderer());

        JTableHeader header = table.getTableHeader();
        header.setReorderingAllowed(false);
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 40));
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable source, Object value,
                    boolean selected, boolean focused, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(
                        source, value, selected, focused, row, column);
                label.setOpaque(true);
                label.setBackground(BRAND_DARK);
                label.setForeground(Color.WHITE);
                label.setFont(label.getFont().deriveFont(Font.BOLD, 11f));
                label.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                return label;
            }
        });
    }

    private static final class StripedCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean selected, boolean focused, int row, int column) {
            Component cell = super.getTableCellRendererComponent(
                    table, value, selected, focused, row, column);
            if (selected) {
                cell.setBackground(SOFT_GREEN);
                cell.setForeground(TEXT);
            } else {
                cell.setBackground(row % 2 == 0 ? SURFACE : CANVAS);
                cell.setForeground(TEXT);
            }
            setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
            return cell;
        }
    }
}
