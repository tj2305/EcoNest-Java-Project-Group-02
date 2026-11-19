package econest;

import econest.members.sa26610085.task3.InventoryPanel;
import econest.members.sa26610086.task2.ClientManagementPanel;
import econest.members.sa26610086.task9.AgentNotificationPanel;
import econest.members.sa26610139.task4.DeliveryAgentPanel;
import econest.members.sa26610144.task7.ReportsPanel;
import econest.members.sa26610145.task6.DeliveryAssignmentPanel;
import econest.members.sa26610146.task5.OrderManagementPanel;
import econest.members.sa26610153.task1.ProductCataloguePanel;
import econest.members.sa26610153.task8.ClientNotificationPanel;
import econest.ui.MemberProjectFrame;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;

/** Unified EcoNest launcher containing all nine collaborator tasks. */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MemberProjectFrame(
                "Group 02",
                "Tasks 1-9",
                new String[] {
                    "Task 1 - Products",
                    "Task 2 - Clients",
                    "Task 3 - Inventory",
                    "Task 4 - Delivery Agents",
                    "Task 5 - Orders",
                    "Task 6 - Delivery Scheduling",
                    "Task 7 - Reports",
                    "Task 8 - Client Email",
                    "Task 9 - Agent Email"
                },
                new JComponent[] {
                    new ProductCataloguePanel(),
                    new ClientManagementPanel(),
                    new InventoryPanel(),
                    new DeliveryAgentPanel(),
                    new OrderManagementPanel(),
                    new DeliveryAssignmentPanel(),
                    new ReportsPanel(),
                    new ClientNotificationPanel(),
                    new AgentNotificationPanel()
                }
        ).setVisible(true));
    }
}
