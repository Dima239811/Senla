package Task6.model;

import Task6.comporator.book.AvailiableComporator;
import Task6.comporator.book.LetterComporator;
import Task6.comporator.book.PriceComporator;
import Task6.comporator.book.YearComporator;
import Task6.enums.StatusBook;

import java.util.ArrayList;
import java.util.List;

public class BookCol {
    private List<Book> books;
    private final int maxCapacityBook;

    public BookCol() {
        this.maxCapacityBook = 100;
        this.books = new ArrayList<>();
    }

    public BookCol(int maxCapacityBook) {
        this.books = new ArrayList<>();
        this.maxCapacityBook = maxCapacityBook;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public int getMaxCapacityBook() {
        return maxCapacityBook;
    }

    // cписание книги со склада
    public void writeOffBookFromWareHouse(int bookId) {
        for (Book b: books) {
            if (b.getBookId() == bookId & b.getStatus().equals(StatusBook.IN_STOCK)) {
                b.setStatus(StatusBook.OUT_OF_STOCK);
                System.out.println("Статус книги изменен на - отсутсвует");
                return;
            }
        }
        System.out.println("Книга с id " + bookId + "  не найдена");
    }

    public boolean addBook(Book book) {
        if (this.maxCapacityBook > books.size()) {
            books.add(book);
            System.out.println("Книга успешно добавлена на склад!");
            return true;
        }
        else {
            System.out.println("Склад переполнен!");
            return false;
        }
    }

    public Book findBook(int id) {
        for (Book b: books) {
            if (b.getBookId() == id) {
                return b;
            }
        }
        return null;
    }


    public List<Book> sortByName() {
        LetterComporator lettersComporators = new LetterComporator();
        books.sort(lettersComporators);
        return books;
    }

    public List<Book> sortByPrice() {
        PriceComporator priceComporators = new PriceComporator();
        books.sort(priceComporators);
        return books;
    }

    public List<Book> sortByYear() {
        books.sort(new YearComporator());
        return books;
    }

    public List<Book> sortByStatus() {
        books.sort(new AvailiableComporator());
        return books;
    }

}
