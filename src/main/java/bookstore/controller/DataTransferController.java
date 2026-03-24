package bookstore.controller;

import bookstore.exception.DataExportException;
import bookstore.exception.DataImportException;
import bookstore.model.entity.Book;
import bookstore.model.entity.Customer;
import bookstore.model.entity.Order;
import bookstore.model.entity.RequestBook;
import bookstore.service.DataTransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/data")
public class DataTransferController {

    private final DataTransferService dataTransferService;

    @Autowired
    public DataTransferController(DataTransferService dataTransferService) {
        this.dataTransferService = dataTransferService;
    }

    @GetMapping("/export/books")
    @PreAuthorize("hasRole('ADMIN')")
    public void exportBooksToCsv(@RequestParam String filePath) throws DataExportException {
        dataTransferService.exportBooksToCsv(filePath);
    }

    @PostMapping("/import/books")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Book> importBooksFromCsv(@RequestParam String filePath) throws DataImportException {
        return dataTransferService.importBooksFromCsv(filePath);
    }

    @GetMapping("/export/orders")
    @PreAuthorize("hasRole('ADMIN')")
    public void exportOrdersToCsv(@RequestParam String filePath) throws DataExportException {
        dataTransferService.exportOrdersToCsv(filePath);
    }

    @PostMapping("/import/orders")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Order> importOrdersFromCsv(@RequestParam String filePath) throws DataImportException {
        return dataTransferService.importOrdersFromCsv(filePath);
    }

    @GetMapping("/export/customers")
    @PreAuthorize("hasRole('ADMIN')")
    public void exportCustomersToCsv(@RequestParam String filePath) throws DataExportException {
        dataTransferService.exportCustomersToCsv(filePath);
    }

    @PostMapping("/import/customers")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Customer> importCustomersFromCsv(@RequestParam String filePath) throws DataImportException {
        return dataTransferService.importCustomersFromCsv(filePath);
    }

    @GetMapping("/export/requests")
    @PreAuthorize("hasRole('ADMIN')")
    public void exportRequestToCsv(@RequestParam String filePath) throws DataExportException {
        dataTransferService.exportRequestToCsv(filePath);
    }

    @PostMapping("/import/requests")
    @PreAuthorize("hasRole('ADMIN')")
    public List<RequestBook> importRequestFromCsv(@RequestParam String filePath) throws DataImportException {
        return dataTransferService.importRequestFromCsv(filePath);
    }
}
