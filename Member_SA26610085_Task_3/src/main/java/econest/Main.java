package econest;

import econest.members.sa26610085.task3.InventoryPanel;
import econest.ui.MemberProjectFrame;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MemberProjectFrame("SA26610085", "Task 3",
                new String[] {"Task 3 - Inventory"}, new JComponent[] {new InventoryPanel()}).setVisible(true));
    }
}
