package bookstore.dependesies.config;

import bookstore.service.csv.BookCsvService;
import bookstore.service.csv.CustomerCsvService;
import bookstore.service.csv.OrderCsvService;
import bookstore.service.csv.RequestBookCsvService;
import bookstore.ui.action_factory.DefaultActionFactory;

import java.util.Map;

public class JavaConfiguration implements Configuration {
    @Override
    public String getPackageToScan() {
        return "bookstore";
    }

    @Override
    public Map<String, Class> getInterfaceToImplementation() {
        return Map.ofEntries(
                Map.entry("bookCsvService", BookCsvService.class),
                Map.entry("orderCsvService", OrderCsvService.class),
                Map.entry("customerCsvService", CustomerCsvService.class),
                Map.entry("requestBookCsvService", RequestBookCsvService.class),
                Map.entry("actionFactory", DefaultActionFactory.class)
        );
    }
}
