package bookstore.repo.dao;

import bookstore.model.Customer;
import bookstore.repo.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO implements GenericDAO<Customer> {
    @Override
    public void create(Customer object) throws SQLException {
        String sqlCreateCustomer = "INSERT INTO customer (fullname, phonenumber, age, email, address) VALUES (?, ?, ?, ?, ?)";
        Connection connection = DBConnection.getInstance().getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sqlCreateCustomer, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, object.getFullName());
            statement.setString(2, object.getPhoneNumber());
            statement.setInt(3, object.getAge());
            statement.setString(4, object.getEmail());
            statement.setString(5, object.getAddress());

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating customer failed, no rows affected.");
            }

            // Получаем сгенерированный ID
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
                    object.setCustomerID(generatedId);
                    //System.out.println("установлен объекту айди " + generatedId);
                } else {
                    throw new SQLException("Creating customer failed, no ID obtained.");
                }
            }
        } catch (SQLException exception) {
            throw new SQLException("Fail create Customer " + exception);
        }
    }

    @Override
    public Customer findById(int id) throws SQLException {
        String sqlFindByIdCustomer = "select * from customer where id = ?";
        Connection connection = DBConnection.getInstance().getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sqlFindByIdCustomer)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                return new Customer(
                        rs.getString("fullname"),
                        rs.getInt("age"),
                        rs.getString("phonenumber"),
                        rs.getString("email"),
                        rs.getString("address"),
                        rs.getInt("id")
                );
            } else {
                return null;
            }
        } catch (SQLException exception) {
            throw new SQLException("Fail find Customer with id: " + id + exception);
        }
    }

    @Override
    public void update(Customer object) throws SQLException {
        String sqlUpdateCustomer = "UPDATE customer SET fullname = ?, phonenumber = ?, age = ?, email = ?, " +
                "address = ? WHERE id = ?";
        Connection connection = DBConnection.getInstance().getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sqlUpdateCustomer)) {

            statement.setString(1, object.getFullName());
            statement.setString(2, object.getPhoneNumber());
            statement.setInt(3, object.getAge());
            statement.setString(4, object.getEmail());
            statement.setString(5, object.getAddress());

            statement.setInt(6, object.getCustomerID());

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Update failed, no rows affected.");
            }
        } catch (SQLException exception) {
            throw new SQLException("Fail find Customer with id: " + object.getCustomerID() + exception);
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sqlDeleteCustomer = "delete from customer where id = ?";
        Connection connection = DBConnection.getInstance().getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sqlDeleteCustomer)) {

            statement.setInt(1, id);

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Update failed, no rows affected.");
            }
        } catch (SQLException exception) {
            throw new SQLException("Fail delete Customer with id: " + id + exception);
        }
    }

    @Override
    public List<Customer> getAll() throws SQLException {
        String sqlFindAllCustomer = "select * from customer";
        List<Customer> customers = new ArrayList<>();

        Connection connection = DBConnection.getInstance().getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sqlFindAllCustomer)) {

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                Customer customer = new Customer(
                        rs.getString("fullname"),
                        rs.getInt("age"),
                        rs.getString("phonenumber"),
                        rs.getString("email"),
                        rs.getString("address"),
                        rs.getInt("id")
                );
                customers.add(customer);
            }
        } catch (SQLException e) {
            throw new SQLException("Failed to fetch all customers: " + e.getMessage(), e);
        }
        return customers;
    }
}
