package Task7.service.csv;

import Task7.enums.RequestStatus;
import Task7.enums.StatusBook;
import Task7.exception.DataExportException;
import Task7.exception.DataImportException;
import Task7.model.Book;
import Task7.model.Customer;
import Task7.model.RequestBook;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RequestBookCsvService implements ICsvService<RequestBook> {

    @Override
    public void exportToCsv(List<RequestBook> items, String filePath) throws DataExportException {
        if (items == null) {
            throw new DataExportException("Список запросов не может быть пустым!");
        }
        File file = new File(filePath.replace("\"", ""));
        ensureDirectoryExists(file);

        try (PrintWriter printWriter = new PrintWriter(file, "UTF-8")) {
            printWriter.println("requestId,customerId,customerName,bookId,bookName,bookStatus,requestStatus");

            for (RequestBook request : items) {
                if (request == null) {
                    System.out.println("[WARN] Обнаружен null-запрос");
                    continue;
                }

                Customer customer = request.getCustomer();
                Book book = request.getBook();

                printWriter.println(String.format(Locale.US,
                        "%d,%d,%s,%d,%s,%s,%s",
                        request.getRequestId(),
                        customer.getCustomerID(),
                        escapeCsvField(customer.getFullName()),
                        book.getBookId(),
                        escapeCsvField(book.getName()),
                        book.getStatus().getValue(),
                        request.getStatus().getValue()
                ));
            }

            printWriter.flush();
        } catch (IOException e) {
            throw new DataExportException("Ошибка записи в файл: " + e.getMessage());
        }
    }

    @Override
    public List<RequestBook> importFromCsv(String filePath) throws DataImportException {
        List<RequestBook> requests = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(filePath), "UTF-8"))) {

            String header = reader.readLine();
            if (header == null) {
                throw new DataImportException("CSV-файл пустой: " + filePath);
            }

            String line;
            int lineNumber = 1;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                try {
                    String[] parts = parseCsvLine(line);

                    if (parts.length < 7) {
                        System.out.println("[WARN] Пропущена строка " + lineNumber +
                                ": некорректное количество полей (" + parts.length + ")");
                        continue;
                    }

                    // Парсинг данных
                    int requestId = Integer.parseInt(parts[0]);
                    int customerId = Integer.parseInt(parts[1]);
                    String customerName = unescapeCsv(parts[2]);
                    int bookId = Integer.parseInt(parts[3]);
                    String bookName = unescapeCsv(parts[4]);
                    String bookStatusValue = unescapeCsv(parts[5]);
                    String requestStatusValue = unescapeCsv(parts[6]);

                    // Создание объектов
                    Customer customer = new Customer(customerName, 0, "", "", "", customerId);
                    Book book = new Book(bookName, "", 0, 0.0, parseBookStatus(bookStatusValue),bookId);

                    RequestBook request = new RequestBook(
                            customer,
                            book,
                            parseRequestStatus(requestStatusValue),
                            requestId
                    );

                    requests.add(request);

                } catch (Exception ex) {
                    throw new DataImportException(
                            String.format("Ошибка в строке %d: %s. Строка: %s",
                                    lineNumber, ex.getMessage(), line)
                    );
                }
            }
        } catch (IOException e) {
            throw new DataImportException("Ошибка чтения файла: " + e.getMessage());
        }

        return requests;
    }

    // Вспомогательные методы

    private void ensureDirectoryExists(File file) throws DataExportException {
        try {
            File parentDir = file.getParentFile();
            if (parentDir != null) {
                parentDir.mkdirs();
                if (!parentDir.exists()) {
                    throw new DataExportException("Не удалось создать директорию: " + parentDir.getAbsolutePath());
                }
            }
        } catch (SecurityException e) {
            throw new DataExportException("Нет прав на создание директории: " + e.getMessage());
        }
    }

    private String[] parseCsvLine(String line) {
        List<String> result = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);
            if (ch == '\"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '\"') {
                    sb.append('\"');
                    i++;
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (ch == ',' && !inQuotes) {
                result.add(sb.toString());
                sb.setLength(0);
            } else {
                sb.append(ch);
            }
        }
        result.add(sb.toString());
        return result.toArray(new String[0]);
    }

    private String escapeCsvField(String field) {
        if (field == null) {
            return "";
        }
        String escaped = field.replace("\"", "\"\"");
        if (escaped.contains(",") || escaped.contains("\"")) {
            return "\"" + escaped + "\"";
        }
        return escaped;
    }

    private String unescapeCsv(String value) {
        if (value == null || value.isEmpty()) {
            return value;
        }
        if (value.startsWith("\"") && value.endsWith("\"")) {
            value = value.substring(1, value.length() - 1);
        }
        return value.replace("\"\"", "\"");
    }

    private RequestStatus parseRequestStatus(String statusValue) throws DataImportException {
        for (RequestStatus status : RequestStatus.values()) {
            if (status.getValue().equalsIgnoreCase(statusValue)) {
                return status;
            }
        }
        throw new DataImportException("Некорректный статус запроса: " + statusValue);
    }

    private StatusBook parseBookStatus(String statusValue) throws DataImportException {
        for (StatusBook status : StatusBook.values()) {
            if (status.getValue().equalsIgnoreCase(statusValue)) {
                return status;
            }
        }
        throw new DataImportException("Некорректный статус книги: " + statusValue);
    }
}