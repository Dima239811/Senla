package bookStore.repo.dao;

import bookStore.dependesies.annotation.Inject;
import bookStore.model.Order;
import bookStore.enums.OrderStatus;
import bookStore.repo.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO implements GenericDAO<Order> {
    @Inject
    private BookDAO bookDAO;

    @Inject
    private CustomerDAO customerDAO;

    @Override
    public void create(Order object) throws SQLException {
        String sqlCreateOrder = "INSERT INTO orders (customerid, bookid, dateorder, price, status) VALUES (?, ?, ?, ?, ?)";
        Connection connection = DBConnection.getInstance().getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sqlCreateOrder)) {

            statement.setInt(1, object.getCustomer().getCustomerID());
            statement.setInt(2, object.getBook().getBookId());
            statement.setDate(3, new java.sql.Date(object.getOrderDate().getTime()));
            statement.setDouble(4, object.getFinalPrice());
            statement.setString(5, object.getStatus().getValue());

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating order failed, no rows affected.");
            }

        } catch (SQLException exception) {
            throw new SQLException("Fail create order " + exception);
        }
    }

    @Override
    public Order findById(int id) throws SQLException {
        String sqlFindByIdOrder = "select * from orders where id = ?";
        Connection connection = DBConnection.getInstance().getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sqlFindByIdOrder)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                return new Order(
                        bookDAO.findById(rs.getInt("bookid")),
                        customerDAO.findById(rs.getInt("customerid")),
                        rs.getDate("dateorder"),
                        rs.getDouble("price"),
                        OrderStatus.fromValue(rs.getString("status")),
                        rs.getInt("id")
                );
            }

            else {
                return null;
            }

        } catch (SQLException exception) {
            throw new SQLException("Fail find order with id: " + id + exception);
        }
    }

    @Override
    public void update(Order object) throws SQLException {
        String sqlUpdateBook = "UPDATE orders SET customerid = ?, bookid = ?, dateorder = ?, price = ?, " +
                "status = ? WHERE id = ?";
        Connection connection = DBConnection.getInstance().getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sqlUpdateBook)) {


            statement.setInt(1, object.getCustomer().getCustomerID());
            statement.setInt(2, object.getBook().getBookId());
            statement.setDate(3, new java.sql.Date(object.getOrderDate().getTime()));
            statement.setDouble(4, object.getFinalPrice());
            statement.setObject(5, object.getStatus().getValue(), java.sql.Types.OTHER);

            statement.setInt(6, object.getOrderId());

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Update failed, no rows affected.");
            }

        } catch (SQLException exception) {
            throw new SQLException("Fail find order with id: " + object.getOrderId() + exception);
        }
    }

    // возможно надо потом подумать чтоб менять статусы у книг при удалении заказа
    @Override
    public void delete(int id) throws SQLException {
        String sqlDeleteOrder = "delete from orders where id = ?";
        Connection connection = DBConnection.getInstance().getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sqlDeleteOrder)) {

            statement.setInt(1, id);

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Update failed, no rows affected.");
            }

        } catch (SQLException exception) {
            throw new SQLException("Fail delete order with id: " + id + exception);
        }
    }

    @Override
    public List<Order> getAll() throws SQLException {
        String sqlFindAllOrder = "select * from orders";
        List<Order> orders = new ArrayList<>();

        Connection connection = DBConnection.getInstance().getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sqlFindAllOrder)) {

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                Order order = new Order(
                        bookDAO.findById(rs.getInt("bookid")),
                        customerDAO.findById(rs.getInt("customerid")),
                        rs.getDate("dateorder"),
                        rs.getDouble("price"),
                        OrderStatus.fromValue(rs.getString("status")),
                        rs.getInt("id")
                );
                orders.add(order);
            }

        } catch (SQLException e) {
            throw new SQLException("Failed to fetch all orders: " + e.getMessage(), e);
        }
        return orders;
    }
}
