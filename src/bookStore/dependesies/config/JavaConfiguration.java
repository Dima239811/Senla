package bookStore.dependesies.config;

import bookStore.service.csv.BookCsvService;
import bookStore.service.csv.CustomerCsvService;
import bookStore.service.csv.OrderCsvService;
import bookStore.service.csv.RequestBookCsvService;
import bookStore.ui.action_factory.DefaultActionFactory;

import java.util.Map;

public class JavaConfiguration implements Configuration {
    @Override
    public String getPackageToScan() {
        return "bookStore";
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
