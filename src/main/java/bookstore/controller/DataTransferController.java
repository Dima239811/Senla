package bookstore.controller;

import bookstore.exception.DataExportException;
import bookstore.exception.DataImportException;
import bookstore.model.entity.Book;
import bookstore.model.entity.Customer;
import bookstore.model.entity.Order;
import bookstore.model.entity.RequestBook;
import bookstore.service.DataTransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class DataTransferController {

    private final DataTransferService dataTransferService;

    @Autowired
    public DataTransferController(DataTransferService dataTransferService) {
        this.dataTransferService = dataTransferService;
    }


    public void exportBooksToCsv(String filePath) throws DataExportException {
        dataTransferService.exportBooksToCsv(filePath);
    }

    public List<Book> importBooksFromCsv(String filePath) throws DataImportException {
        return dataTransferService.importBooksFromCsv(filePath);
    }

    public void exportOrdersToCsv(String filePath) throws DataExportException {
        dataTransferService.exportOrdersToCsv(filePath);
    }

    public List<Order> importOrdersFromCsv(String filePath) throws DataImportException {
        return dataTransferService.importOrdersFromCsv(filePath);
    }

    public void exportCustomersToCsv(String filePath) throws DataExportException {
        dataTransferService.exportCustomersToCsv(filePath);
    }

    public List<Customer> importCustomersFromCsv(String filePath) throws DataImportException {
        return dataTransferService.importCustomersFromCsv(filePath);
    }

    public void exportRequestToCsv(String filePath) throws DataExportException {
        dataTransferService.exportRequestToCsv(filePath);
    }

    public List<RequestBook> importRequestFromCsv(String filePath) throws DataImportException {
        return dataTransferService.importRequestFromCsv(filePath);
    }
}
