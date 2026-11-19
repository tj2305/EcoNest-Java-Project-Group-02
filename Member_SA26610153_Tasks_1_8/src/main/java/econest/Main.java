package econest;

import econest.members.sa26610153.task1.ProductCataloguePanel;
import econest.members.sa26610153.task8.ClientNotificationPanel;
import econest.ui.MemberProjectFrame;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MemberProjectFrame("SA26610153", "Tasks 1 and 8",
                new String[] {"Task 1 - Products", "Task 8 - Client Email"},
                new JComponent[] {new ProductCataloguePanel(), new ClientNotificationPanel()}).setVisible(true));
    }
}
