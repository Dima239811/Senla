package bookstore.service.entityService;

import bookstore.comporator.request.LetterRequestComporator;
import bookstore.enums.RequestStatus;
import bookstore.exception.DaoException;
import bookstore.exception.ServiceException;
import bookstore.model.entity.Book;
import bookstore.model.entity.Customer;
import bookstore.model.entity.RequestBook;
import bookstore.repo.dao.RequestBookDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RequestBookService implements IService<RequestBook> {

    @Autowired
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
        } catch (DaoException e) {
            throw new ServiceException("Fail to create request for book id: " + book.getBookId() +
                    " in RequestBookService in createRequest()", e);
        }
    }

    public List<RequestBook> sortRequest(String criteria) {
        try {
            List<RequestBook> requestBooks = requestBookDAO.getAllWithBooksAndCustomers();
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
        } catch (DaoException e) {
            throw new ServiceException("Fail to create request in RequestBookService in sortRequest()", e);
        }
    }

    @Override
    public List<RequestBook> getAll() {
        try {
            return requestBookDAO.getAllWithBooksAndCustomers();
        } catch (DaoException e) {
            throw new ServiceException("Fail to get all requests in RequestBookService in getAll()", e);
        }
    }

    @Override
    public RequestBook getById(int id) {
        try {
            return requestBookDAO.findById(id);
        } catch (DaoException e) {
            throw new ServiceException("Fail to get request by id: " + id +
                    " in RequestBookService in getById()", e);
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
        } catch (DaoException e) {
            throw new ServiceException("Fail to add request with id: " + item.getRequestId() +
                    " in RequestBookService in add()", e);
        }
    }

    @Override
    public void update(RequestBook item) {
        try {
            requestBookDAO.update(item);
        } catch (DaoException e) {
            throw new ServiceException("Fail to update request with id: " + item.getRequestId() +
                    " in RequestBookService in update()", e);
        }
    }
}
