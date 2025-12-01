package Task7.service.csv;


import Task7.exception.DataExportException;
import Task7.exception.DataImportException;
import Task7.model.Customer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CustomerCsvService implements ICsvService<Customer> {
    @Override
    public void exportToCsv(List<Customer> items, String filePath) throws Exception {
        if (items == null) {
            throw new DataExportException("Список клиентов не может быть пустым!");
        }
        File file = new File(filePath.replace("\"", ""));

        try (PrintWriter printWriter = new PrintWriter(file, "UTF-8")) {

            file.getParentFile().mkdirs();
            printWriter.println("id,fullName,age,phoneNumber,email,address");


            for (Customer b: items) {
                if (b == null) {
                    System.out.println("[WARN] Обнаружен null-клиент");
                    continue;
                }

                printWriter.println(String.format(Locale.US,
                        "%d,\"%s\",%d,\"%s\",\"%s\",\"%s\"",
                        b.getCustomerID(),
                        escapeCsvField(b.getFullName()),
                        b.getAge(),
                        escapeCsvField(b.getPhoneNumber()),
                        escapeCsvField(b.getEmail()),
                        escapeCsvField(b.getAddress())  // Добавлен недостающий параметр
                ));
            }

            printWriter.flush();

        } catch (IOException exception) {
            throw new DataExportException("Файл " + filePath + " не найден");
        }
    }

    private String escapeCsvField(String field) {
        if (field == null) {
            return "";
        }
        // Экранируем кавычки и обрамляем всё поле в кавычки, если содержит запятые или кавычки
        String escaped = field.replace("\"", "\"\"");
        if (escaped.contains(",") || escaped.contains("\"")) {
            return "\"" + escaped + "\"";
        }
        return escaped;
    }

    @Override
    public List<Customer> importFromCsv(String filePath) throws DataImportException {
        List<Customer> customers = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(filePath), "UTF-8"))) {

            String headerLine = reader.readLine();
            if (headerLine == null) {
                throw new DataImportException("CSV-файл пустой: " + filePath);
            }

            String line;
            int lineNumber = 1;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                try {
                    String[] parts = parseCsvLine(line);

                    if (parts.length < 6) {
                        System.out.println("[WARN] Пропущена строка " + lineNumber +
                                ": некорректное количество полей (" + parts.length + ")");
                        continue;
                    }

                    // Парсим данные клиента
                    int customerId = Integer.parseInt(parts[0]);
                    String fullName = unescapeCsv(parts[1]);
                    int age = Integer.parseInt(parts[2]);
                    String phone = unescapeCsv(parts[3]);
                    String email = unescapeCsv(parts[4]);
                    String address = unescapeCsv(parts[5]);

                    customers.add(new Customer(fullName, age, phone, email, address, customerId));

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

        return customers;
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
}
