package bookStore.ui.actions.csv.book;

import exception.DataExportException;
import  bookStore.model.DataManager;
import bookStore.ui.actions.IAction;

import java.util.Scanner;

public class ExportBookAction implements IAction {
    private final DataManager dataManager;
    private final Scanner scanner;

    public ExportBookAction(DataManager dataManager) {
        this.dataManager = dataManager;
        this.scanner = new Scanner(System.in);
    }
    @Override
    public void execute() {
        try {
            System.out.println("\n=== Экспорт книг в CSV ===");
            System.out.print("Введите путь для сохранения файла: ");
            String path = scanner.nextLine().trim();

            dataManager.exportBooksToCsv(path);
            System.out.println("Экспорт успешно завершен");

        } catch (DataExportException e) {
            System.err.println("Ошибка экспорта: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Неожиданная ошибка: " + e.getMessage());
        }
    }
}
