package bookStore.service.csv;

import java.util.List;

public interface ICsvService<T> {
    void exportToCsv(List<T> items, String filePath) throws Exception;
    List<T> importFromCsv(String filePath) throws Exception;
}
