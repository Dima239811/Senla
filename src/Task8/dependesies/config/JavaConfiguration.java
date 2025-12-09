package Task8.dependesies.config;

import Task8.service.csv.*;
import Task8.ui.action_factory.DefaultActionFactory;

import java.util.Map;

public class JavaConfiguration implements Configuration{
    @Override
    public String getPackageToScan() {
        return "Task8";
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
