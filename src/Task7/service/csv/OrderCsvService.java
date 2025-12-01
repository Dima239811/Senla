package Task7.service.csv;

import Task7.enums.OrderStatus;
import Task7.enums.StatusBook;
import Task7.exception.DataExportException;
import Task7.exception.DataImportException;
import Task7.model.Book;
import Task7.model.Customer;
import Task7.model.Order;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderCsvService implements ICsvService<Order> {

    @Override
    public void exportToCsv(List<Order> items, String filePath) throws Exception {
        if (items == null) {
            throw new DataExportException("Список заказов не может быть пустым!");
        }
        File file = new File(filePath.replace("\"", ""));

        try (PrintWriter printWriter = new PrintWriter(file, "UTF-8")) {
            file.getParentFile().mkdirs();

            printWriter.println("orderId,orderDate,finalPrice,orderStatus," +
                    "bookId,bookName,bookAuthor,bookYear,bookPrice,bookStatus," +
                    "customerId,customerFullName,customerAge,customerPhone,customerEmail,customerAddress");

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


            for (Order order: items) {
                if (order == null) {
                    System.out.println("[WARN] Обнаружен null-заказ");
                    continue;
                }

                Book b = order.getBook();
                Customer c = order.getCustomer();


                printWriter.println(String.format(Locale.US,
                        "%d,%s,%.2f,%s,%d,%s,%s,%d,%.2f,%s,%d,%s,%d,%s,%s,%s",
                        order.getOrderId(),
                        dateFormat.format(order.getOrderDate()),
                        order.getFinalPrice(),
                        order.getStatus().getValue(),

                        b.getBookId(),
                        quote(b.getName()),
                        quote(b.getAuthtor()),
                        b.getYear(),
                        b.getPrice(),
                        b.getStatus().getValue(),

                        c.getCustomerID(),
                        quote(c.getFullName()),
                        c.getAge(),
                        quote(c.getPhoneNumber()),
                        quote(c.getEmail()),
                        quote(c.getAddress())
                ));
            }

            printWriter.flush();

        } catch (IOException exception) {
            throw new DataExportException("Файл " + filePath + " не найден");
        }
    }

    private String quote(String s) {
        if (s == null) return "\"\"";
        return "\"" + s.replace("\"", "\"\"") + "\"";
    }


    @Override
    public List<Order> importFromCsv(String filePath) throws DataImportException {
        List<Order> orders = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(filePath), "UTF-8"))) {

            String header = reader.readLine(); // Пропустить заголовок
            if (header == null) {
                throw new DataImportException("CSV-файл пустой: " + filePath);
            }

            String line;
            int lineNumber = 1;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                try {
                    String[] parts = parseCsvLine(line);

                    if (parts.length < 16) {
                        System.out.println("[WARN] Пропущена строка " + lineNumber +
                                ": некорректное количество полей (" + parts.length + ")");
                        continue;
                    }

                    // Order
                    int orderId = Integer.parseInt(parts[0]);
                    Date orderDate = dateFormat.parse(parts[1]);
                    double finalPrice = Double.parseDouble(parts[2]);

                    OrderStatus orderStatus = null;
                    String orderStatusValue = parts[3].trim();
                    for (OrderStatus s : OrderStatus.values()) {
                        if (s.getValue().equalsIgnoreCase(orderStatusValue)) {
                            orderStatus = s;
                            break;
                        }
                    }

                    if (orderStatus == null) {
                        throw new DataImportException("Ошибка: Некорректный статус заказа: " + orderStatusValue);
                    }


                    // Book
                    int bookId = Integer.parseInt(parts[4]);
                    String bookName = unescapeCsv(parts[5]);
                    String bookAuthor = unescapeCsv(parts[6]);
                    int bookYear = Integer.parseInt(parts[7]);
                    double bookPrice = Double.parseDouble(parts[8]);

                    StatusBook bookStatus = null;
                    String bookStatusValue = parts[9].trim();
                    for (StatusBook s : StatusBook.values()) {
                        if (s.getValue().equalsIgnoreCase(bookStatusValue)) {
                            bookStatus = s;
                            break;
                        }
                    }

                    if (bookStatus == null) {
                        throw new DataImportException("Ошибка: Некорректный статус книги: " + bookStatusValue);
                    }

                    Book book = new Book(bookName, bookAuthor, bookYear, bookPrice, bookStatus, bookId);

                    // Customer
                    int customerId = Integer.parseInt(parts[10]);
                    String fullName = unescapeCsv(parts[11]);
                    int age = Integer.parseInt(parts[12]);
                    String phone = unescapeCsv(parts[13]);
                    String email = unescapeCsv(parts[14]);
                    String address = unescapeCsv(parts[15]);

                    Customer customer = new Customer(fullName, age, phone, email, address, customerId);

                    Order order = new Order(book, customer, orderDate, finalPrice, orderStatus, orderId);
                    orders.add(order);

                } catch (Exception ex) {
                    throw new DataImportException(
                            String.format("Ошибка в строке %d: %s. Строка: %s",
                                    lineNumber, ex.getMessage(), line)
                    );
                }
            }

        } catch (Exception exception) {
            throw new DataImportException("Ошибка чтения файла " + filePath + ": " + exception.getMessage());
        }

        return orders;
    }

    private String unescapeCsv(String value) {
        if (value == null || value.isEmpty()) {
            return value;
        }
        // Удаляем обрамляющие кавычки и заменяем удвоенные кавычки на одинарные
        if (value.startsWith("\"") && value.endsWith("\"")) {
            value = value.substring(1, value.length() - 1);
        }
        return value.replace("\"\"", "\"");
    }

    private String[] parseCsvLine(String line) {
        List<String> result = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);
            if (ch == '\"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '\"') {
                    sb.append('\"'); // Экранированная кавычка
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

}
