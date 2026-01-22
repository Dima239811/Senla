package bookstore.service.entityService;

import bookstore.comporator.book.AvailiableComporator;
import bookstore.comporator.book.LetterComporator;
import bookstore.comporator.book.PriceComporator;
import bookstore.comporator.book.YearComporator;
import bookstore.dependesies.annotation.Inject;
import bookstore.dependesies.annotation.PostConstruct;
import bookstore.enums.StatusBook;
import bookstore.model.Book;
import bookstore.repo.dao.BookDAO;

import java.sql.SQLException;
import java.util.List;

public class BookService implements IService<Book> {

    @Inject
    private BookDAO bookDAO;


    @PostConstruct
    public void postConstruct() {
        System.out.println("BookService has been inizialized");
    }

    @Override
    public List<Book> getAll() {
        try {
            List<Book> books = bookDAO.getAll();
            return books;
        } catch (SQLException e) {
            throw new RuntimeException("Fail to get all book in warehouseSevice in getAll()" + e.getMessage());
        }
    }

    @Override
    public Book getById(int id) {
        try {
            Book book = bookDAO.findById(id);
            return book;
        } catch (SQLException e) {
            throw new RuntimeException("Fail to get book by id " + id + " in warehouseSevice in getById()" + e.getMessage());
        }
    }

    @Override
    public void add(Book item) {
        try {
            Book book = bookDAO.findById(item.getBookId());
            if (book != null) {
                System.out.println("Книга с таким id же существует, ее данные будут обновлены");
                bookDAO.update(item);
            } else {
                bookDAO.create(item);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Fail to add book " + item.getBookId() + " in warehouseSevice in add()" + e.getMessage());
        }
    }

    @Override
    public void update(Book item) {
        try {
            bookDAO.update(item);
        } catch (SQLException e) {
            throw new RuntimeException("Fail to update book " + item.getBookId() + " in warehouseSevice in update()" + e.getMessage());
        }
    }

    public List<Book> sortBooks(String criteria) {
        try {
            List<Book> books = bookDAO.getAll();
            switch (criteria.toLowerCase()) {
                case "по алфавиту":
                    books.sort(new LetterComporator());
                    return books;
                case "по цене":
                    books.sort(new PriceComporator());
                    return books;
                case "по году издания":
                    books.sort(new YearComporator());
                    return books;
                case "по наличию на складе":
                    books.sort(new AvailiableComporator());
                    return books;
                default:
                    System.out.println("Ошибка: неопознанный критерий сортировки.");
                    return books;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Fail to get all book in warehouseSevice in sortBooks" + e.getMessage());
        }
    }

    public void writeOffBook(int bookId) {
        List<Book> books = getAll();
        for (Book b: books) {
            if (b.getBookId() == bookId & b.getStatus().equals(StatusBook.IN_STOCK)) {
                b.setStatus(StatusBook.OUT_OF_STOCK);
                update(b);
                System.out.println("Статус книги изменен на - отсутсвует");
                return;
            }
        }
        System.out.println("Книга с id " + bookId + "  не найдена");
    }
}
