package bookStore.service.entityService;

import bookStore.comporator.request.LetterRequestComporator;
import bookStore.dependesies.annotation.Inject;
import bookStore.enums.RequestStatus;
import bookStore.model.Book;
import bookStore.model.Customer;
import bookStore.model.RequestBook;
import bookStore.repo.dao.RequestBookDAO;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class RequestBookService implements IService<RequestBook> {

    @Inject
    private RequestBookDAO requestBookDAO;

    public void closeRequest(Book book) {
        List<RequestBook> requestBooks = getAll();
        for (RequestBook requests : requestBooks) {
            if (requests.getBook().getBookId() == book.getBookId()) {
                requests.setStatus(RequestStatus.CLOSED);
                update(requests);
            }
        }
    }

    public void createRequest(Book book, Customer customer) {
        RequestBook requestBook = new RequestBook(customer, book);
        try {
            requestBookDAO.create(requestBook);
        } catch (SQLException e) {
            throw new RuntimeException("Fail to create request for book id: " + book.getBookId() +
                    " in RequestBookService in createRequest()" + e.getMessage());
        }
    }

    public List<RequestBook> sortRequest(String criteria) {
        try {
            List<RequestBook> requestBooks = requestBookDAO.getAll();
            if (criteria.equals("по алфавиту")) {
                requestBooks.sort(new LetterRequestComporator());
                return requestBooks;
            } else if (criteria.equals("по количеству запросов")) {
                var requestsByBook = requestBooks.stream()
                        .collect(Collectors.groupingBy(RequestBook::getBook, Collectors.counting()));

                return requestBooks.stream()
                        .sorted((o1, o2) -> {
                            long countO1 = requestsByBook.get(o1.getBook());
                            long countO2 = requestsByBook.get(o2.getBook());
                            return Long.compare(countO1, countO2);
                        })
                        .collect(Collectors.toList());
            } else {
                System.out.println("такого критерия сортировки нет");
                return requestBooks;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Fail to create request in RequestBookService in sortRequest()" + e.getMessage());
        }

    }

    @Override
    public List<RequestBook> getAll() {
        try {
            return requestBookDAO.getAll();
        } catch (SQLException e) {
            throw new RuntimeException("Fail to get all requests in RequestBookService in getAll()" + e.getMessage());
        }
    }

    @Override
    public RequestBook getById(int id) {
        try {
            return requestBookDAO.findById(id);
        } catch (SQLException e) {
            throw new RuntimeException("Fail to get request by id: " + id +
                    " in RequestBookService in getById()" + e.getMessage());
        }
    }

    @Override
    public void add(RequestBook item) {
        try {
            RequestBook existing = requestBookDAO.findById(item.getRequestId());
            if (existing != null) {
                update(item);
            } else {
                requestBookDAO.create(item);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Fail to add request with id: " + item.getRequestId() +
                    " in RequestBookService in add()" + e.getMessage());
        }
    }

    @Override
    public void update(RequestBook item) {
        try {
            requestBookDAO.update(item);
        } catch (SQLException e) {
            throw new RuntimeException("Fail to update request with id: " + item.getRequestId() +
                    " in RequestBookService in update()" + e.getMessage());
        }
    }
}
