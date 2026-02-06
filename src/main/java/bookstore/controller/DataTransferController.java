package bookstore.controller;

import bookstore.exception.DataExportException;
import bookstore.exception.DataImportException;
import bookstore.model.entity.Book;
import bookstore.model.entity.Customer;
import bookstore.model.entity.Order;
import bookstore.model.entity.RequestBook;
import bookstore.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class DataTransferController {

    private final ApplicationService applicationService;

    @Autowired
    public DataTransferController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }


    public void exportBooksToCsv(String filePath) throws DataExportException {
        applicationService.exportBooksToCsv(filePath);
    }

    public List<Book> importBooksFromCsv(String filePath) throws DataImportException {
        return applicationService.importBooksFromCsv(filePath);
    }

    public void exportOrdersToCsv(String filePath) throws DataExportException {
        applicationService.exportOrdersToCsv(filePath);
    }

    public List<Order> importOrdersFromCsv(String filePath) throws DataImportException {
        return applicationService.importOrdersFromCsv(filePath);
    }

    public void exportCustomersToCsv(String filePath) throws DataExportException {
        applicationService.exportCustomersToCsv(filePath);
    }

    public List<Customer> importCustomersFromCsv(String filePath) throws DataImportException {
        return applicationService.importCustomersFromCsv(filePath);
    }

    public void exportRequestToCsv(String filePath) throws DataExportException {
        applicationService.exportRequestToCsv(filePath);
    }

    public List<RequestBook> importRequestFromCsv(String filePath) throws DataImportException {
        return applicationService.importRequestFromCsv(filePath);
    }
}
