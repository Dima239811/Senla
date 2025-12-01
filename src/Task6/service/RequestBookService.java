package Task6.service;

import Task6.model.Book;
import Task6.model.Customer;
import Task6.model.RequestBook;
import Task6.model.RequestBookCol;

import java.util.List;

public class RequestBookService {
    private final RequestBookCol requestBookCol;

    public RequestBookService() {
        this.requestBookCol = new RequestBookCol();
    }

    public void addRequest(Customer customer, Book book) {
        requestBookCol.addRequest(customer, book);
    }

    public void addRequest(RequestBook requestBook) {
        requestBookCol.addRequest(requestBook);
    }

    public void closeRequest(Book book) {
        requestBookCol.closeRequest(book);
    }

    public void createRequest(Book book, Customer customer) {
        requestBookCol.createRequest(book, customer);
    }

    public RequestBookCol getRequestBookCol() {
        return requestBookCol;
    }

    public List<RequestBook> getAllRequestBook() {
        return requestBookCol.getRequests();
    }

    public List<RequestBook> sortRequest(String criteria) {
        if (criteria.equals("по алфавиту")) {
            return requestBookCol.sortByLetter();
        } else if (criteria.equals("по количеству запросов")) {
            return requestBookCol.sortByCountRequest();
        } else {
            System.out.println("такого критерия сортировки нет");
            return requestBookCol.getRequests();
        }
    }

}
