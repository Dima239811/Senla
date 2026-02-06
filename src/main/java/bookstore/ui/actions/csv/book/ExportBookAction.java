package bookstore.ui.actions.csv.book;

import bookstore.exception.DataExportException;
import bookstore.model.DataManager;
import bookstore.ui.actions.IAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class ExportBookAction implements IAction {
    private final DataManager dataManager;
    private final Scanner scanner;
    private static final Logger logger = LoggerFactory.getLogger(ExportBookAction.class);

    public ExportBookAction(DataManager dataManager) {
        this.dataManager = dataManager;
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void execute() {
        logger.info("Запуск действия: экспорт книг в CSV.");
        try {
            System.out.println("\n=== Экспорт книг в CSV ===");
            System.out.print("Введите путь для сохранения файла: ");
            String path = scanner.nextLine().trim();

            dataManager.exportBooksToCsv(path);
            System.out.println("Экспорт успешно завершен");
            logger.info("Экспорт книг завершён успешно. Файл: {}", path);
        } catch (DataExportException e) {
            logger.error("Ошибка при экспорте книг: {}", e.getMessage());
            System.err.println("Ошибка экспорта: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Неожиданная ошибка при экспорте книг {}", e.getMessage());
            System.err.println("Неожиданная ошибка: " + e.getMessage());
        }
    }
}
