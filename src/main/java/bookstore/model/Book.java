package bookstore.model;


import bookstore.enums.StatusBook;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnTransformer;

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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "status")
    @ColumnTransformer(
            write = "(CASE ? WHEN 'IN_STOCK' THEN 'в наличии'::statusbook " +
                    "WHEN 'OUT_OF_STOCK' THEN 'отсутствует'::statusbook END)"
    )
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
