package econest;

import econest.members.sa26610144.task7.ReportsPanel;
import econest.ui.MemberProjectFrame;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MemberProjectFrame("SA26610144", "Task 7",
                new String[] {"Task 7 - Reports"}, new JComponent[] {new ReportsPanel()}).setVisible(true));
    }
}
