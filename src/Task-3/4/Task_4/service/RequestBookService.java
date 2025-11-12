package Task_4.service;

import Task_4.model.Book;
import Task_4.model.Customer;
import Task_4.model.RequestBook;
import Task_4.model.RequestBookCol;

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
