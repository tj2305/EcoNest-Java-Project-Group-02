package econest;

import econest.members.sa26610139.task4.DeliveryAgentPanel;
import econest.ui.MemberProjectFrame;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MemberProjectFrame("SA26610139", "Task 4",
                new String[] {"Task 4 - Delivery Agents"}, new JComponent[] {new DeliveryAgentPanel()}).setVisible(true));
    }
}
