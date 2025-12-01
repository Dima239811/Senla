package Task6.ui.actions.csv.order;

import Task6.model.DataManager;
import Task6.exception.DataExportException;
import Task6.ui.actions.IAction;

import java.util.Scanner;

public class ExportOrderAction implements IAction {
    private final DataManager dataManager;
    private final Scanner scanner;

    public ExportOrderAction(DataManager dataManager) {
        this.dataManager = dataManager;
        this.scanner = new Scanner(System.in);
    }
    @Override
    public void execute() {
        try {
            System.out.println("\n=== Экспорт заказов в CSV ===");
            System.out.print("Введите путь для сохранения файла: ");
            String path = scanner.nextLine().trim();

            dataManager.exportOrdersToCsv(path);
            System.out.println("Экспорт успешно завершен");

        } catch (DataExportException e) {
            System.err.println("Ошибка экспорта: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Неожиданная ошибка: " + e.getMessage());
        }
    }
}
