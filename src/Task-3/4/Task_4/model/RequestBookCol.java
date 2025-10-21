package Task_4.model;



import Task_4.enums.RequestStatus;
import Task_4.enums.StatusBook;

import java.util.ArrayList;
import java.util.List;


public class RequestBookCol {
    private List<RequestBook> requests = new ArrayList<>();

    public List<RequestBook> getRequests() {
        return requests;
    }

    public void setRequests(List<RequestBook> requests) {
        this.requests = requests;
    }

    public void addRequest(Customer customer, Book book) {
        requests.add(new RequestBook(customer,book));
        System.out.println("Запрос на книгу оставлен.");
    }

    public void closeRequest(Book book) {
        for (RequestBook requestBook: requests) {
            if (book.getAuthtor().equals(requestBook.getBook().getAuthtor()) & book.getName().equals(requestBook.getBook().getName())) {
                requestBook.setStatus(RequestStatus.CLOSED);
                requestBook.setStatusBook(StatusBook.IN_STOCK);
                System.out.println("запрос на книгу закрыт");
            }
        }
    }

    public void createRequest(Book book, Customer customer) {
        requests.add(new RequestBook(customer, book));
        System.out.println("Запрос на книгу создан");
    }

}
