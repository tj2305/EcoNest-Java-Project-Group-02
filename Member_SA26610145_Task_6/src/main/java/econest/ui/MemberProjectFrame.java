package econest.ui;

import java.awt.BorderLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/** A small launcher window that displays only one member's assigned tasks. */
public class MemberProjectFrame extends JFrame {
    public MemberProjectFrame(String memberId, String taskSummary, String[] tabNames, JComponent[] taskPanels) {
        super("EcoNest - " + memberId + " - " + taskSummary);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1050, 700);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        for (int i = 0; i < taskPanels.length; i++) tabs.addTab(tabNames[i], taskPanels[i]);
        tabs.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent event) {
                java.awt.Component selected = tabs.getSelectedComponent();
                if (selected instanceof Refreshable) ((Refreshable) selected).refresh();
            }
        });

        JLabel banner = new JLabel(memberId + "  |  Assigned work: " + taskSummary, SwingConstants.CENTER);
        add(banner, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
    }
}
