package bookStore.repo.dao;

import bookStore.model.Book;
import bookStore.enums.StatusBook;
import bookStore.repo.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookDAO implements GenericDAO<Book> {
    @Override
    public void create(Book object) throws SQLException {
        String sqlCreateBook = "INSERT INTO book (name, author, year, price, status) VALUES (?, ?, ?, ?, ?)";
        Connection connection = DBConnection.getInstance().getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sqlCreateBook)) {

            statement.setString(1, object.getName());
            statement.setString(2, object.getAuthtor());
            statement.setInt(3, object.getYear());
            statement.setDouble(4, object.getPrice());
            statement.setString(5, object.getStatus().getValue());

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating book failed, no rows affected.");
            }

        } catch (SQLException exception) {
            throw new SQLException("Fail create book " + exception);
        }
    }

    @Override
    public Book findById(int id) throws SQLException {
        String sqlFindByIdBook = "select * from book where id = ?";
        Connection connection = DBConnection.getInstance().getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sqlFindByIdBook)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                return new Book(
                        rs.getString("name"),
                        rs.getString("author"),
                        rs.getInt("year"),
                        rs.getDouble("price"),
                        StatusBook.fromValue(rs.getString("status")),
                        rs.getInt("id")
                );
            }

            else {
                return null;
            }

        } catch (SQLException exception) {
            throw new SQLException("Fail find book with id: " + id + exception);
        }
    }

    @Override
    public void update(Book object) throws SQLException {
        String sqlUpdateBook = "UPDATE book SET name = ?, author = ?, year = ?, price = ?, " +
                "status = ? WHERE id = ?";
        Connection connection = DBConnection.getInstance().getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sqlUpdateBook)) {

            statement.setString(1, object.getName());
            statement.setString(2, object.getAuthtor());
            statement.setInt(3, object.getYear());
            statement.setDouble(4, object.getPrice());
            statement.setString(5, object.getStatus().getValue());
            statement.setInt(6, object.getBookId());

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Update failed, no rows affected.");
            }

        } catch (SQLException exception) {
            throw new SQLException("Fail find book with id: " + object.getBookId() + exception);
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sqlDeleteBook = "delete from book where id = ?";
        Connection connection = DBConnection.getInstance().getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sqlDeleteBook)) {

            statement.setInt(1, id);

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Update failed, no rows affected.");
            }

        } catch (SQLException exception) {
            throw new SQLException("Fail delete book with id: " + id + exception);
        }
    }

    @Override
    public List<Book> getAll() throws SQLException {
        String sqlFindAllCustomer = "select * from book";
        List<Book> books = new ArrayList<>();

        Connection connection = DBConnection.getInstance().getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sqlFindAllCustomer)) {

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                Book book = new Book(
                        rs.getString("name"),
                        rs.getString("author"),
                        rs.getInt("year"),
                        rs.getDouble("price"),
                        StatusBook.fromValue(rs.getString("status")),
                        rs.getInt("id")
                );
                books.add(book);
            }

        } catch (SQLException e) {
            throw new SQLException("Failed to fetch all books: " + e.getMessage(), e);
        }
        return books;
    }
}
