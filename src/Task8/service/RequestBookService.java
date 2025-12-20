package Task8.service;

import Task8.dependesies.annotation.Inject;
import Task8.model.Book;
import Task8.model.Customer;
import Task8.model.RequestBook;
import Task8.model.RequestBookCol;

import java.util.List;

public class RequestBookService {

    @Inject
    private RequestBookCol requestBookCol;


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
