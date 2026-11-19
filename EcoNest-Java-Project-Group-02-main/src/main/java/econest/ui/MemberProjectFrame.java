package econest.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

/** Shared EcoNest workspace shell for the group project and member demos. */
public class MemberProjectFrame extends JFrame {
    private final String[] tabNames;
    private final JComponent[] taskPanels;
    private final JButton[] navigationButtons;
    private final JLabel pageTitle = new JLabel();
    private final JLabel pageDescription = new JLabel();
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cardPanel = new JPanel(cardLayout);
    private int selectedTask = -1;

    public MemberProjectFrame(String memberId, String taskSummary, String[] tabNames, JComponent[] taskPanels) {
        super("EcoNest | " + taskSummary);
        if (tabNames.length != taskPanels.length || tabNames.length == 0) {
            throw new IllegalArgumentException("Every task needs a name and a panel.");
        }

        this.tabNames = tabNames.clone();
        this.taskPanels = taskPanels.clone();
        this.navigationButtons = new JButton[taskPanels.length];

        UiSupport.installTheme();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(980, 640));
        setSize(1280, 820);
        setLocationRelativeTo(null);

        JPanel workspace = new JPanel(new BorderLayout());
        workspace.setBackground(UiSupport.CANVAS);
        workspace.add(createSidebar(memberId, taskSummary), BorderLayout.WEST);
        workspace.add(createMainContent(memberId, taskSummary), BorderLayout.CENTER);
        setContentPane(workspace);
        setLocationRelativeTo(null);
        selectTask(0);
    }

    private JPanel createSidebar(String memberId, String taskSummary) {
        JPanel sidebar = new JPanel(new BorderLayout(0, 24));
        sidebar.setBackground(UiSupport.SIDEBAR);
        sidebar.setBorder(BorderFactory.createEmptyBorder(24, 16, 18, 16));
        sidebar.setPreferredSize(new Dimension(252, 0));

        JPanel brand = new JPanel(new BorderLayout(11, 0));
        brand.setOpaque(false);
        JLabel mark = new JLabel("E", SwingConstants.CENTER);
        mark.setOpaque(true);
        mark.setBackground(UiSupport.BRAND_LIGHT);
        mark.setForeground(Color.WHITE);
        mark.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 22));
        mark.setBorder(BorderFactory.createEmptyBorder(5, 12, 5, 12));
        JLabel name = new JLabel("<html><b>EcoNest</b><br><span style='font-size:10px'>SUSTAINABLE RETAIL</span></html>");
        name.setForeground(Color.WHITE);
        name.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18));
        brand.add(mark, BorderLayout.WEST);
        brand.add(name, BorderLayout.CENTER);

        JPanel navigation = new JPanel();
        navigation.setOpaque(false);
        navigation.setLayout(new BoxLayout(navigation, BoxLayout.Y_AXIS));
        JLabel navigationLabel = new JLabel("WORKSPACE");
        navigationLabel.setForeground(UiSupport.SIDEBAR_MUTED);
        navigationLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 11));
        navigationLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 0));
        navigationLabel.setAlignmentX(LEFT_ALIGNMENT);
        navigation.add(navigationLabel);

        for (int i = 0; i < tabNames.length; i++) {
            final int taskIndex = i;
            JButton button = new JButton(taskLabel(i, tabNames[i]));
            button.setHorizontalAlignment(SwingConstants.LEFT);
            button.setAlignmentX(LEFT_ALIGNMENT);
            button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
            button.setPreferredSize(new Dimension(216, 46));
            button.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 13));
            button.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 10));
            button.setFocusPainted(false);
            button.setContentAreaFilled(true);
            button.setOpaque(true);
            button.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
            button.setToolTipText("Open " + tabNames[i]);
            button.getAccessibleContext().setAccessibleName(tabNames[i]);
            button.addActionListener(event -> selectTask(taskIndex));
            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent event) {
                    if (selectedTask != taskIndex) button.setBackground(UiSupport.SIDEBAR_HOVER);
                }

                @Override
                public void mouseExited(MouseEvent event) {
                    if (selectedTask != taskIndex) button.setBackground(UiSupport.SIDEBAR);
                }
            });
            navigationButtons[i] = button;
            navigation.add(button);
            navigation.add(Box.createVerticalStrut(4));
        }

        JScrollPane navigationScroll = new JScrollPane(navigation);
        navigationScroll.setBorder(null);
        navigationScroll.setOpaque(false);
        navigationScroll.getViewport().setOpaque(false);
        navigationScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        navigationScroll.getVerticalScrollBar().setUnitIncrement(16);

        JPanel footer = new JPanel();
        footer.setOpaque(false);
        footer.setLayout(new BoxLayout(footer, BoxLayout.Y_AXIS));
        JLabel demo = new JLabel("LOCAL DEMO  |  SESSION DATA");
        demo.setForeground(UiSupport.SIDEBAR_MUTED);
        demo.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 10));
        JLabel context = new JLabel(memberId + "  |  " + taskSummary);
        context.setForeground(Color.WHITE);
        context.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 11));
        footer.add(demo);
        footer.add(Box.createVerticalStrut(7));
        footer.add(context);

        sidebar.add(brand, BorderLayout.NORTH);
        sidebar.add(navigationScroll, BorderLayout.CENTER);
        sidebar.add(footer, BorderLayout.SOUTH);
        return sidebar;
    }

    private JPanel createMainContent(String memberId, String taskSummary) {
        JPanel main = new JPanel(new BorderLayout(0, 18));
        main.setBackground(UiSupport.CANVAS);
        main.setBorder(BorderFactory.createEmptyBorder(26, 28, 26, 28));

        JPanel header = new JPanel(new BorderLayout(12, 0));
        header.setOpaque(false);
        JPanel titles = new JPanel();
        titles.setOpaque(false);
        titles.setLayout(new BoxLayout(titles, BoxLayout.Y_AXIS));
        pageTitle.setForeground(UiSupport.TEXT);
        pageTitle.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 26));
        pageDescription.setForeground(UiSupport.MUTED);
        pageDescription.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 13));
        titles.add(pageTitle);
        titles.add(Box.createVerticalStrut(5));
        titles.add(pageDescription);

        JPanel status = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        status.setOpaque(false);
        JLabel workspaceLabel = new JLabel("GROUP 02");
        workspaceLabel.setForeground(UiSupport.BRAND);
        workspaceLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 11));
        workspaceLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UiSupport.BORDER),
                BorderFactory.createEmptyBorder(8, 11, 8, 11)));
        JLabel demoLabel = new JLabel("DEMO MODE");
        demoLabel.setForeground(UiSupport.BRAND_DARK);
        demoLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 11));
        demoLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UiSupport.SOFT_GREEN),
                BorderFactory.createEmptyBorder(8, 11, 8, 11)));
        status.add(workspaceLabel);
        status.add(demoLabel);
        header.add(titles, BorderLayout.CENTER);
        header.add(status, BorderLayout.EAST);

        cardPanel.setOpaque(false);
        for (int i = 0; i < taskPanels.length; i++) {
            JComponent taskPanel = taskPanels[i];
            SwingUtilities.updateComponentTreeUI(taskPanel);
            UiSupport.prepareTaskPanel(taskPanel);
            JPanel card = new JPanel(new BorderLayout());
            card.setBackground(UiSupport.SURFACE);
            card.setBorder(BorderFactory.createLineBorder(UiSupport.BORDER));
            card.add(taskPanel, BorderLayout.CENTER);
            cardPanel.add(card, "task-" + i);
        }

        main.add(header, BorderLayout.NORTH);
        main.add(cardPanel, BorderLayout.CENTER);
        return main;
    }

    private void selectTask(int index) {
        if (index < 0 || index >= taskPanels.length) {
            return;
        }

        selectedTask = index;
        cardLayout.show(cardPanel, "task-" + index);
        pageTitle.setText(displayName(tabNames[index]));
        pageDescription.setText(description(tabNames[index]));
        for (int i = 0; i < navigationButtons.length; i++) {
            boolean selected = i == index;
            JButton button = navigationButtons[i];
            button.setBackground(selected ? UiSupport.SIDEBAR_SELECTED : UiSupport.SIDEBAR);
            button.setForeground(selected ? Color.WHITE : UiSupport.SIDEBAR_TEXT);
            button.setFont(new Font(Font.SANS_SERIF, selected ? Font.BOLD : Font.PLAIN, 13));
        }

        if (taskPanels[index] instanceof Refreshable) {
            ((Refreshable) taskPanels[index]).refresh();
        }
    }

    private String taskLabel(int index, String title) {
        return String.format("%02d    %s", index + 1, displayName(title));
    }

    private String displayName(String title) {
        int separator = title.indexOf(" - ");
        return separator >= 0 ? title.substring(separator + 3) : title;
    }

    private String description(String title) {
        String lower = title.toLowerCase();
        if (lower.contains("product")) return "Manage sustainable products, pricing and eco ratings.";
        if (lower.contains("client")) return "Keep customer profiles and delivery details up to date.";
        if (lower.contains("inventory")) return "Monitor stock levels, reorder points and supplier receipts.";
        if (lower.contains("agent")) return "Manage delivery partners, vehicles and assignment messages.";
        if (lower.contains("order")) return "Build customer orders, check availability and review totals.";
        if (lower.contains("delivery")) return "Schedule deliveries and follow each order through fulfilment.";
        if (lower.contains("report")) return "Review monthly sales performance and low-stock alerts.";
        if (lower.contains("email")) return "Review dispatch and assignment notification activity.";
        return "Sustainable retail operations at a glance.";
    }
}
