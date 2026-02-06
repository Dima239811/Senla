package bookstore.model.entity;


import bookstore.enums.StatusBook;
import bookstore.model.converters.StatusBookConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "book")
public class Book {
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private int year;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false, name = "status")
    @Convert(converter = StatusBookConverter.class)
    private StatusBook status;  // в наличии или отсутствует

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int bookId;

    public Book() { }

    public Book(String name, String author, int year, double price, StatusBook status) {
        this.name = name;
        this.author = author;
        this.year = year;
        this.price = price;
        this.status = status;
    }

    public Book(String name, String author, int year, double price, StatusBook status, int bookId) {
        this.name = name;
        this.author = author;
        this.year = year;
        this.price = price;
        this.status = status;
        this.bookId = bookId;
    }

    @Override
    public String toString() {
        return "Book{" +
                "name='" + name + '\'' +
                ", authtor='" + author + '\'' +
                ", year=" + year +
                ", price=" + price +
                ", status=" + status +
                ", bookId=" + bookId +
                '}';
    }
}
