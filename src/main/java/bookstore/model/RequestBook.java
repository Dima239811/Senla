package bookstore.model;


import bookstore.enums.RequestStatus;
import bookstore.enums.StatusBook;

public class RequestBook {
    private Customer customer;
    private Book book;
    private RequestStatus status;
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

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
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
