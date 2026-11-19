# EcoNest-Java-Project-Group-02
EcoNest - Java Swing Retail Management System

## Run

Open this folder in NetBeans or run:

```text
mvn compile
mvn exec:java
```

The unified launcher is `econest.Main` and includes Tasks 1 through 9:

1. Product catalogue
2. Client management
3. Inventory and supplier stock-in
4. Delivery-agent management
5. Order creation
6. Delivery assignment and status
7. Monthly sales and inventory reports
8. Client dispatch notifications
9. Delivery-agent notifications

## User experience

The shared workspace uses a forest-green navigation rail, responsive task pages, searchable/sortable tables, and consistent form and action styling. The task panels continue to be organized by their original member packages.

The application uses a shared in-memory `AppStore`. Data and notification logs are retained while the application is running and reset when it closes. Email notifications are recorded in a local demo log; SMTP delivery is not configured.
