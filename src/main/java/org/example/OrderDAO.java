
package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    // A. INSERT ORDER
    public boolean insertOrder(Order order) throws SQLException {

        String sql = "INSERT INTO `Orders` " +
                "(order_id, customer_name, product_name, quantity, unit_price, status) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, order.getOrderId());
            ps.setString(2, order.getCustomerName());
            ps.setString(3, order.getProductName());
            ps.setInt(4, order.getQuantity());
            ps.setDouble(5, order.getUnitPrice());
            ps.setString(6, order.getStatus());

            ps.executeUpdate();

            System.out.println("Order Inserted Successfully!");
            return true;

        } catch (SQLIntegrityConstraintViolationException e) {

            System.out.println(
                    "Duplicate Order ID! This ID already exists."
            );

            return false;
        }
    }


    // B. DISPLAY ORDERS BY STATUS AND QUANTITY > 2
    public List<Order> getOrdersByStatus(String status)
            throws SQLException {

        List<Order> list = new ArrayList<>();

        String sql = "SELECT * FROM `Orders` " +
                "WHERE status = ? AND quantity > 2 " +
                "ORDER BY unit_price DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, status);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    Order order = new Order(
                            rs.getInt("order_id"),
                            rs.getString("customer_name"),
                            rs.getString("product_name"),
                            rs.getInt("quantity"),
                            rs.getDouble("unit_price"),
                            rs.getString("status")
                    );

                    list.add(order);
                }
            }
        }

        return list;
    }


    // C. INCREASE ORDER QUANTITY
    public boolean increaseQuantity(int orderId, int additionalQuantity)
            throws SQLException {

        String sql = "UPDATE `Orders` " +
                "SET quantity = quantity + ? " +
                "WHERE order_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, additionalQuantity);
            ps.setInt(2, orderId);

            int rows = ps.executeUpdate();

            return rows > 0;
        }
    }


    // GET ORDER BY ID
    public Order getOrderById(int orderId) throws SQLException {

        String sql = "SELECT * FROM `Orders` WHERE order_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, orderId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    return new Order(
                            rs.getInt("order_id"),
                            rs.getString("customer_name"),
                            rs.getString("product_name"),
                            rs.getInt("quantity"),
                            rs.getDouble("unit_price"),
                            rs.getString("status")
                    );
                }
            }
        }

        return null;
    }


    // D. UPDATE ORDER STATUS
    public boolean updateStatus(int orderId, String newStatus)
            throws SQLException {

        String sql = "UPDATE `Orders` " +
                "SET status = ? WHERE order_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, newStatus);
            ps.setInt(2, orderId);

            return ps.executeUpdate() > 0;
        }
    }


    // E1. CALCULATE TOTAL ORDER VALUE
    public double getTotalOrderValue() throws SQLException {

        String sql = "SELECT SUM(quantity * unit_price) " +
                "AS total FROM `Orders`";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getDouble("total");
            }
        }

        return 0.0;
    }


    // E2. DELETE ORDER ONLY IF STATUS IS CANCELLED
    public boolean deleteCancelledOrder(int orderId)
            throws SQLException {

        String sql = "DELETE FROM `Orders` " +
                "WHERE order_id = ? AND status = 'Cancelled'";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, orderId);

            return ps.executeUpdate() > 0;
        }
    }
}
