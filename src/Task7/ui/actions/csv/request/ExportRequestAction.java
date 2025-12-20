package Task7.ui.actions.csv.request;

import Task7.exception.DataExportException;
import Task7.model.DataManager;
import Task7.ui.actions.IAction;

import java.util.Scanner;

public class ExportRequestAction implements IAction {
    private final DataManager dataManager;
    private final Scanner scanner;

    public ExportRequestAction(DataManager dataManager) {
        this.dataManager = dataManager;
        this.scanner = new Scanner(System.in);
    }
    @Override
    public void execute() {
        try {
            System.out.println("\n=== Экспорт запросов на книги в CSV ===");
            System.out.print("Введите путь для сохранения файла: ");
            String path = scanner.nextLine().trim();

            dataManager.exportRequestToCsv(path);
            System.out.println("Экспорт успешно завершен");

        } catch (DataExportException e) {
            System.err.println("Ошибка экспорта: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Неожиданная ошибка: " + e.getMessage());
        }
    }
}
