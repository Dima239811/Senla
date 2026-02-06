package bookstore.model.entity;


import bookstore.enums.RequestStatus;
import bookstore.enums.StatusBook;
import bookstore.model.converters.RequestStatusConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "request")
public class RequestBook {
    @ManyToOne
    @JoinColumn(name = "customerid", nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "bookid", nullable = false)
    private Book book;

    @Convert(converter = RequestStatusConverter.class)
    @Column(name = "status", nullable = false)
    private RequestStatus status;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int requestId;

    public RequestBook() { }

    public RequestBook(Customer customer, Book book) {
        this.customer = customer;
        this.book = book;
        this.status = RequestStatus.OPEN;
    }

    public RequestBook(Customer customer, Book book, RequestStatus status, int requestId) {
        this.customer = customer;
        this.book = book;
        this.status = status;
        this.requestId = requestId;
    }

    public void setStatusBook(StatusBook statusBook) {
        System.out.println("статус книги изменен на " + statusBook);
        book.setStatus(statusBook);
    }

    @Override
    public String toString() {
        return "RequestBook{" +
                "customer=" + customer +
                ", book=" + book +
                ", status=" + status +
                '}';
    }
}
