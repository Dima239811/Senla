package Task_4.model;


import Task_4.enums.RequestStatus;
import Task_4.enums.StatusBook;

public class RequestBook {
    private Customer customer;
    private Book book;
    private RequestStatus status;
    private int requestId;
    private static int count = 0;

    public RequestBook(Customer customer, Book book) {
        this.customer = customer;
        this.book = book;
        this.status = RequestStatus.OPEN;
        requestId = count;
        count++;
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
