package bookStore.repo.dao;

import bookStore.dependesies.annotation.Inject;
import bookStore.model.RequestBook;
import bookStore.enums.RequestStatus;
import bookStore.repo.util.DBConnection;
import lombok.Getter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RequestBookDAO implements GenericDAO<RequestBook> {
    @Getter
    @Inject
    private BookDAO bookDAO;

    @Getter
    @Inject
    private CustomerDAO customerDAO;

    @Override
    public void create(RequestBook object) throws SQLException {
        String sqlCreateOrder = "INSERT INTO request (customerid, bookid, status) VALUES (?, ?, ?)";
        Connection connection = DBConnection.getInstance().getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sqlCreateOrder)) {

            statement.setInt(1, object.getCustomer().getCustomerID());
            statement.setInt(2, object.getBook().getBookId());
            statement.setString(3, object.getStatus().getValue());

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating request failed, no rows affected.");
            }

        } catch (SQLException exception) {
            throw new SQLException("Fail create request " + exception);
        }
    }

    @Override
    public RequestBook findById(int id) throws SQLException {
        String sqlFindByIdRequest = "select * from request where id = ?";
        Connection connection = DBConnection.getInstance().getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sqlFindByIdRequest)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                return new RequestBook (
                        customerDAO.findById(rs.getInt("customerid")),
                        bookDAO.findById(rs.getInt("bookid")),
                        RequestStatus.fromValue(rs.getString("status")),
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
    public void update(RequestBook object) throws SQLException {
        String sqlUpdateBook = "UPDATE request SET customerid = ?, bookid = ?,  status = ? WHERE id = ?";
        Connection connection = DBConnection.getInstance().getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sqlUpdateBook)) {


            statement.setInt(1, object.getCustomer().getCustomerID());
            statement.setInt(2, object.getBook().getBookId());
            statement.setObject(3, object.getStatus().getValue(), java.sql.Types.OTHER);

            statement.setInt(4, object.getRequestId());

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Update failed, no rows affected.");
            }

        } catch (SQLException exception) {
            throw new SQLException("Fail find request with id: " + object.getRequestId() + exception);
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sqlDeleteRequest = "delete from request where id = ?";
        Connection connection = DBConnection.getInstance().getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sqlDeleteRequest)) {

            statement.setInt(1, id);

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Update failed, no rows affected.");
            }

        } catch (SQLException exception) {
            throw new SQLException("Fail delete request with id: " + id + exception);
        }
    }

    @Override
    public List<RequestBook> getAll() throws SQLException {
        String sqlFindAllRequest = "select * from request";
        List<RequestBook> requestBooks = new ArrayList<>();

        Connection connection = DBConnection.getInstance().getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sqlFindAllRequest)) {

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                RequestBook requestBook = new RequestBook(
                        customerDAO.findById(rs.getInt("customerid")),
                        bookDAO.findById(rs.getInt("bookid")),
                        RequestStatus.fromValue(rs.getString("status")),
                        rs.getInt("id")
                );
                requestBooks.add(requestBook);
            }

        } catch (SQLException e) {
            throw new SQLException("Failed to fetch all requests: " + e.getMessage(), e);
        }
        return requestBooks;
    }

    public RequestBook findByBookId(int id) throws SQLException {
        String sqlFindByIdRequest = "select * from request where bookid = ?";
        Connection connection = DBConnection.getInstance().getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sqlFindByIdRequest)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                return new RequestBook (
                        customerDAO.findById(rs.getInt("customerid")),
                        bookDAO.findById(rs.getInt("bookid")),
                        RequestStatus.fromValue(rs.getString("status")),
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
}

