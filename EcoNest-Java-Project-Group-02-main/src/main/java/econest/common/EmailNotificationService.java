package econest.common;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/** Demo notification logger. Replace this with an SMTP-backed service for real email. */
public final class EmailNotificationService {
    private static final List<String> LOG = new ArrayList<String>();

    private EmailNotificationService() { }

    public static String notifyClientDispatched(CustomerOrder order) {
        String message = "To: " + order.getClient().getEmail()
                + " | Subject: EcoNest order " + order.getId() + " dispatched"
                + " | Message: Your order has been dispatched. Delivery status: " + order.getDeliveryStatus();
        return record(message);
    }

    public static String notifyAgentAssigned(CustomerOrder order) {
        DeliveryAgent agent = order.getDeliveryAgent();
        String message = "To: " + agent.getEmail()
                + " | Subject: New EcoNest delivery " + order.getId()
                + " | Message: A delivery has been assigned to you for client " + order.getClient().getName()
                + (order.getScheduledDate() == null ? "." : " on " + order.getScheduledDate() + ".");
        return record(message);
    }

    private static String record(String message) {
        String entry = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                + " | DEMO ONLY (not emailed) | " + message;
        LOG.add(entry);
        System.out.println(entry);
        return entry;
    }

    public static List<String> getLog() { return new ArrayList<String>(LOG); }
}
