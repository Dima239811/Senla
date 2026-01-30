package bookstore.service.csv;

import bookstore.exception.DataExportException;
import bookstore.exception.DataImportException;

import java.util.List;

public interface ICsvService<T> {
    void exportToCsv(List<T> items, String filePath) throws DataExportException;
    List<T> importFromCsv(String filePath) throws DataImportException;
}
