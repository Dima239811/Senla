package bookStore.model;

import java.util.List;

public class AppState {
    private List<Book> books;
    private List<Order> orders;
    private List<Customer> customers;
    private List<RequestBook> requests;

    public AppState() {}

    public AppState(List<Book> books, List<Order> orders, List<Customer> customers, List<RequestBook> requests) {
        this.books = books;
        this.orders = orders;
        this.customers = customers;
        this.requests = requests;
    }

    public List<Book> getBooks() { return books; }
    public void setBooks(List<Book> books) { this.books = books; }

    public List<Order> getOrders() { return orders; }
    public void setOrders(List<Order> orders) { this.orders = orders; }

    public List<Customer> getCustomers() { return customers; }
    public void setCustomers(List<Customer> customers) { this.customers = customers; }

    public List<RequestBook> getRequests() { return requests; }
    public void setRequests(List<RequestBook> requests) { this.requests = requests; }
}
