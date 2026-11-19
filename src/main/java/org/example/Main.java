
package org.example;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        OrderDAO dao = new OrderDAO();

        while (true) {

            System.out.println("\n===== ORDER MANAGEMENT SYSTEM =====");
            System.out.println("1. Insert Three Sample Orders");
            System.out.println("2. Insert New Order Manually");
            System.out.println("3. Display Orders By Status");
            System.out.println("4. Increase Order Quantity");
            System.out.println("5. Update Order Status");
            System.out.println("6. Display Total Order Value");
            System.out.println("7. Delete Cancelled Order");
            System.out.println("8. Exit");

            System.out.print("Enter your choice: ");

            int choice = Integer.parseInt(sc.nextLine());

            try {

                switch (choice) {

                    // QUESTION (a): Insert three sample orders
                    case 1:
                        dao.insertOrder(new Order(
                                101, "Lokesh", "Laptop", 3, 55000, "Pending"));

                        dao.insertOrder(new Order(
                                102, "Rahul", "Mouse", 5, 800, "Shipped"));

                        dao.insertOrder(new Order(
                                103, "Aman", "Keyboard", 4, 1500, "Cancelled"));
                        break;

                    // Insert an order entered by the user
                    case 2:
                        System.out.print("Enter Order ID: ");
                        int id = Integer.parseInt(sc.nextLine());

                        System.out.print("Enter Customer Name: ");
                        String customer = sc.nextLine();

                        System.out.print("Enter Product Name: ");
                        String product = sc.nextLine();

                        System.out.print("Enter Quantity: ");
                        int quantity = Integer.parseInt(sc.nextLine());

                        System.out.print("Enter Unit Price: ");
                        double price = Double.parseDouble(sc.nextLine());

                        System.out.print("Enter Status (Pending/Shipped/Delivered/Cancelled): ");
                        String status = sc.nextLine();

                        dao.insertOrder(new Order(
                                id, customer, product, quantity, price, status));
                        break;

                    // QUESTION (b): Display orders by status
                    case 3:
                        System.out.print("Enter Order Status: ");
                        String searchStatus = sc.nextLine();

                        List<Order> orders =
                                dao.getOrdersByStatus(searchStatus);

                        System.out.println("\n===== FILTERED ORDERS =====");

                        if (orders.isEmpty()) {
                            System.out.println("No Orders Found!");
                        } else {
                            for (Order order : orders) {
                                System.out.println(order);
                            }
                        }
                        break;

                    // QUESTION (c): Increase order quantity
                    case 4:
                        System.out.print("Enter Order ID: ");
                        int updateId = Integer.parseInt(sc.nextLine());

                        Order existingOrder = dao.getOrderById(updateId);

                        if (existingOrder == null) {
                            System.out.println("Order does not exist!");
                            break;
                        }

                        System.out.print("Enter Additional Quantity: ");
                        int additional = Integer.parseInt(sc.nextLine());

                        if (additional <= 0) {
                            System.out.println("Quantity must be greater than zero!");
                            break;
                        }

                        dao.increaseQuantity(updateId, additional);

                        System.out.println("Quantity Updated Successfully!");
                        System.out.println(dao.getOrderById(updateId));
                        break;

                    // QUESTION (d): Update order status
                    case 5:
                        System.out.print("Enter Order ID: ");
                        int statusId = Integer.parseInt(sc.nextLine());

                        Order orderToUpdate = dao.getOrderById(statusId);

                        if (orderToUpdate == null) {
                            System.out.println("Order does not exist!");
                            break;
                        }

                        System.out.println("Current Order Details:");
                        System.out.println(orderToUpdate);

                        System.out.print("Enter New Status (Pending/Shipped/Delivered): ");
                        String newStatus = sc.nextLine();

                        if (!(newStatus.equalsIgnoreCase("Pending")
                                || newStatus.equalsIgnoreCase("Shipped")
                                || newStatus.equalsIgnoreCase("Delivered"))) {

                            System.out.println("Invalid Status!");
                            break;
                        }

                        // Normalize status capitalization
                        newStatus = newStatus.substring(0, 1).toUpperCase()
                                + newStatus.substring(1).toLowerCase();

                        dao.updateStatus(statusId, newStatus);

                        System.out.println("\nStatus Updated Successfully!");
                        System.out.println("Updated Order Details:");
                        System.out.println(dao.getOrderById(statusId));
                        break;

                    // QUESTION (e): Calculate total order value
                    case 6:
                        double total = dao.getTotalOrderValue();

                        System.out.println("\nTotal Order Value: " + total);
                        break;

                    // QUESTION (e): Delete only Cancelled orders
                    case 7:
                        System.out.print("Enter Order ID to Delete: ");
                        int deleteId = Integer.parseInt(sc.nextLine());

                        Order orderToDelete = dao.getOrderById(deleteId);

                        if (orderToDelete == null) {
                            System.out.println("Order does not exist!");
                        } else if (!orderToDelete.getStatus()
                                .equalsIgnoreCase("Cancelled")) {

                            System.out.println(
                                    "Order cannot be deleted! Its status is not Cancelled.");

                        } else {
                            dao.deleteCancelledOrder(deleteId);
                            System.out.println("Cancelled Order Deleted Successfully!");
                        }
                        break;

                    case 8:
                        System.out.println("Exiting Order Management System...");
                        sc.close();
                        return;

                    default:
                        System.out.println("Invalid Choice! Try Again.");
                }

            } catch (SQLException e) {
                System.out.println("Database Error: " + e.getMessage());

            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number!");
            }
        }
    }
}
