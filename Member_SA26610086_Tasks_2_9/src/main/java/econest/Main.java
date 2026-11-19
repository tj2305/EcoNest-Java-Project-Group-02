package econest;

import econest.members.sa26610086.task2.ClientManagementPanel;
import econest.members.sa26610086.task9.AgentNotificationPanel;
import econest.ui.MemberProjectFrame;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MemberProjectFrame("SA26610086", "Tasks 2 and 9",
                new String[] {"Task 2 - Clients", "Task 9 - Agent Email"},
                new JComponent[] {new ClientManagementPanel(), new AgentNotificationPanel()}).setVisible(true));
    }
}
