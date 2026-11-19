package econest;

import econest.members.sa26610145.task6.DeliveryAssignmentPanel;
import econest.ui.MemberProjectFrame;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MemberProjectFrame("SA26610145", "Task 6",
                new String[] {"Task 6 - Delivery Scheduling"}, new JComponent[] {new DeliveryAssignmentPanel()}).setVisible(true));
    }
}
