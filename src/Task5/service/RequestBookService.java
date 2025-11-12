package Task5.service;

import Task5.model.Book;
import Task5.model.Customer;
import Task5.model.RequestBook;
import Task5.model.RequestBookCol;

import java.util.List;

public class RequestBookService {
    private final RequestBookCol requestBookCol;

    public RequestBookService() {
        this.requestBookCol = new RequestBookCol();
    }

    public void addRequest(Customer customer, Book book) {
        requestBookCol.addRequest(customer, book);
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
