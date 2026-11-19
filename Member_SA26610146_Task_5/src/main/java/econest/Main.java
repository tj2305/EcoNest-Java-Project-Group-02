package econest;

import econest.members.sa26610146.task5.OrderManagementPanel;
import econest.ui.MemberProjectFrame;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MemberProjectFrame("SA26610146", "Task 5",
                new String[] {"Task 5 - Orders"}, new JComponent[] {new OrderManagementPanel()}).setVisible(true));
    }
}
