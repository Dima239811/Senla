package bookstore.service;

import bookstore.exception.DataExportException;
import bookstore.exception.DataImportException;
import bookstore.exception.DataManagerException;
import bookstore.model.entity.Book;
import bookstore.model.entity.Customer;
import bookstore.model.entity.Order;
import bookstore.model.entity.RequestBook;
import bookstore.service.csv.ICsvService;
import bookstore.service.entityService.BookService;
import bookstore.service.entityService.CustomerService;
import bookstore.service.entityService.OrderService;
import bookstore.service.entityService.RequestBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DataTransferService {
    @Autowired
    private BookService bookService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private RequestBookService requestBookService;

    @Autowired
    @Qualifier("bookCsvService")
    private ICsvService<Book> bookCsvService;

    @Autowired
    @Qualifier("orderCsvService")
    private ICsvService<Order> orderCsvService;

    @Autowired
    @Qualifier("customerCsvService")
    private ICsvService<Customer> customerCsvService;

    @Autowired
    @Qualifier("requestBookCsvService")
    private ICsvService<RequestBook> requestBookCsvService;

    @Transactional(readOnly = true)
    public void exportBooksToCsv(String filePath) throws DataExportException {
        List<Book> books = bookService.getAll();
        System.out.println("передано на экспорт " + books.size() + " книг");
        bookCsvService.exportToCsv(books, filePath);
    }

    @Transactional
    public List<Book> importBooksFromCsv(String filePath) throws DataImportException {
            try {
                List<Book> imported = bookCsvService.importFromCsv(filePath);
                for (Book b : imported) {
                    bookService.add(b);
                }
                return imported;
            } catch (DataImportException e) {
                throw e;
            } catch (Exception e) {
                throw new DataManagerException("fail in data manager importBooksFromCsv", e);
            }
    }

    @Transactional(readOnly = true)
    public void exportOrdersToCsv(String filePath) throws DataExportException {
        List<Order> orders = orderService.getAll();
        orderCsvService.exportToCsv(orders, filePath);
    }

    @Transactional
    public List<Order> importOrdersFromCsv(String filePath) throws DataImportException {
            try {
                List<Order> imported = orderCsvService.importFromCsv(filePath);
                for (Order b : imported) {
                    orderService.add(b);
                    bookService.add(b.getBook());
                    customerService.add(b.getCustomer());
                }
                return imported;
            } catch (DataImportException e) {
                throw e;
            } catch (Exception e) {
                throw new DataManagerException("fail in data manager importOrdersFromCsv ", e);
            }
        }
    @Transactional(readOnly = true)
    public void exportCustomersToCsv(String filePath) throws DataExportException {
        List<Customer> customers = customerService.getAll();
        System.out.println("передано на экспорт " + customers.size() + " клиентов");
        customerCsvService.exportToCsv(customers, filePath);
    }

    @Transactional
    public List<Customer> importCustomersFromCsv(String filePath) throws DataImportException {
        List<Customer> imported = customerCsvService.importFromCsv(filePath);
        for (Customer b: imported) {
            customerService.add(b);
        }
        return imported;
    }

    @Transactional(readOnly = true)
    public void exportRequestToCsv(String filePath) throws DataExportException {
        List<RequestBook> requestBooks = requestBookService.getAll();
        requestBookCsvService.exportToCsv(requestBooks, filePath);
    }

    @Transactional
    public List<RequestBook> importRequestFromCsv(String filePath) throws DataImportException {
            try {
                List<RequestBook> imported = requestBookCsvService.importFromCsv(filePath);
                for (RequestBook b : imported) {
                    requestBookService.add(b);
                    bookService.add(b.getBook());
                    customerService.add(b.getCustomer());
                }
                return imported;
            } catch (DataImportException e) {
                throw e;
            } catch (Exception e) {
                throw new DataManagerException("fail in data manager importRequestFromCsv ", e);
            }
        }
}
