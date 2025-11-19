package Task6.ui.actions.csv.customer;

import Task6.model.DataManager;
import Task6.exception.DataImportException;
import Task6.model.Customer;
import Task6.ui.actions.IAction;

import java.util.List;
import java.util.Scanner;

public class ImportCustomerAction implements IAction {
    private final DataManager dataManager;
    private final Scanner scanner;

    public ImportCustomerAction(DataManager dataManager) {
        this.dataManager = dataManager;
        this.scanner = new Scanner(System.in);
    }
    @Override
    public void execute() {
        try {
            System.out.println("\n=== Импорт клиентов из CSV ===");
            System.out.print("Введите путь к файлу: ");
            String path = scanner.nextLine().trim();

//            System.out.print("Обновлять существующие записи? (y/n): ");
//            boolean update = scanner.nextLine().equalsIgnoreCase("y");

            List<Customer> imported = dataManager.importCustomersFromCsv(path);
            System.out.printf("Успешно импортировано %d клиентов\n", imported.size());

        } catch (DataImportException e) {
            System.err.println("Ошибка импорта: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Неожиданная ошибка: " + e.getMessage());
        }
    }
    }

