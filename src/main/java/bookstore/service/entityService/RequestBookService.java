package bookstore.service.entityService;

import bookstore.comporator.request.LetterRequestComporator;
import bookstore.dto.RequestBookResponse;
import bookstore.enums.RequestStatus;
import bookstore.exception.DaoException;
import bookstore.exception.ServiceException;
import bookstore.model.entity.Book;
import bookstore.model.entity.RequestBook;
import bookstore.model.mapper.RequestBookMapper;
import bookstore.repo.dao.RequestBookDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class RequestBookService {

    private final RequestBookDAO requestBookDAO;
    private final RequestBookMapper requestBookMapper;

    @Autowired
    public RequestBookService(RequestBookDAO requestBookDAO, RequestBookMapper requestBookMapper) {
        this.requestBookDAO = requestBookDAO;
        this.requestBookMapper = requestBookMapper;
    }

    @Transactional
    public void closeRequest(String name, String author) {
        List<RequestBook> requestBooks = getAllRequest();
        for (RequestBook requests : requestBooks) {
            if (Objects.equals(requests.getBook().getName(), name) &&
                    Objects.equals(requests.getBook().getAuthor(), author)) {
                requests.setStatus(RequestStatus.CLOSED);
                update(requests);
            }
        }
    }

    @Transactional(readOnly = true)
    public List<RequestBookResponse> sortRequest(String criteria) {
        try {
            List<RequestBook> requestBooks = requestBookDAO.getAllWithBooksAndCustomers();
            if (criteria.equals("по алфавиту")) {
                requestBooks.sort(new LetterRequestComporator());
                return requestBookMapper.toRequestBookResponseList(requestBooks);
            } else if (criteria.equals("по количеству запросов")) {
                var requestsByBook = requestBooks.stream()
                        .collect(Collectors.groupingBy(RequestBook::getBook, Collectors.counting()));

                return requestBookMapper.toRequestBookResponseList(requestBooks.stream()
                        .sorted((o1, o2) -> {
                            long countO1 = requestsByBook.get(o1.getBook());
                            long countO2 = requestsByBook.get(o2.getBook());
                            return Long.compare(countO1, countO2);
                        })
                        .collect(Collectors.toList()));
            } else {
                System.out.println("такого критерия сортировки нет");
                return requestBookMapper.toRequestBookResponseList(requestBooks);
            }
        } catch (DaoException e) {
            throw new ServiceException("Fail to create request in RequestBookService in sortRequest()", e);
        }
    }

    @Transactional(readOnly = true)
    public List<RequestBookResponse> getAll() {
        try {
            List<RequestBook> requestBooks = requestBookDAO.getAllWithBooksAndCustomers();
            return requestBookMapper.toRequestBookResponseList(requestBooks);
        } catch (DaoException e) {
            throw new ServiceException("Fail to get all requests in RequestBookService in getAll()", e);
        }
    }

    @Transactional(readOnly = true)
    public RequestBookResponse getById(int id) {
        try {
            RequestBook requestBook = requestBookDAO.findById(id);
            return requestBookMapper.toRequestBookResponse(requestBook);
        } catch (DaoException e) {
            throw new ServiceException("Fail to get request by id: " + id +
                    " in RequestBookService in getById()", e);
        }
    }

    @Transactional
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

    @Transactional
    public void update(RequestBook item) {
        try {
            requestBookDAO.update(item);
        } catch (DaoException e) {
            throw new ServiceException("Fail to update request with id: " + item.getRequestId() +
                    " in RequestBookService in update()", e);
        }
    }

    public List<RequestBook> getAllRequest() {
        return requestBookDAO.getAll();
    }
}
