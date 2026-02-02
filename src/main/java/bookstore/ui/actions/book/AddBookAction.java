package bookstore.ui.actions.book;

import bookstore.enums.StatusBook;
import bookstore.exception.DataManagerException;
import bookstore.exception.DataValidationException;
import bookstore.model.entity.Book;
import bookstore.model.DataManager;
import bookstore.ui.actions.IAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class AddBookAction implements IAction {

    private final DataManager dataManager;
    private static final Logger logger = LoggerFactory.getLogger(AddBookAction.class);

    public AddBookAction(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void execute() {
        logger.info("Пользователь начал выполнение команды: AddBookAction");
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.print("Введите название книги: ");
            String name = scanner.nextLine();
            if (name == null || name.trim().isEmpty()) {
                throw new DataValidationException("Название книги не может быть пустым");
            }

            System.out.print("Введите автора: ");
            String author = scanner.nextLine();
            if (author == null || author.trim().isEmpty()) {
                throw new DataValidationException("Имя автора не может быть пустым");
            }

            int year = 0;
            double price = 0.0;

            try {
                System.out.print("Введите год издания книги: ");
                year = scanner.nextInt();
                if (year < 0) {
                    throw new DataValidationException("Год издания не может быть отрицательным");
                }
            } catch (java.util.InputMismatchException e) {
                logger.error("Ошибка ввода года: ожидалось целое число (введено не число)");
                throw new DataValidationException("Некорректный формат года. Введите целое число");
            }

            try {
                System.out.print("Введите стоимость книги: ");
                price = scanner.nextDouble();
                if (price <= 0) {
                    throw new DataValidationException("Стоимость книги должна быть положительной");
                }
            } catch (java.util.InputMismatchException e) {
                logger.error("Ошибка ввода цены: некорректный формат, не введено число");
                throw new DataValidationException("Некорректный формат стоимости. Используйте точку как разделитель");
            } finally {
                scanner.nextLine(); // Очистка буфера
            }

            Book book = new Book(name, author, year, price, StatusBook.IN_STOCK);
            dataManager.addBookToWareHouse(book);
            System.out.println("Книга '" + name + "' автора '" + author + "' добавлена.");
            logger.info("Книга '{}' успешно добавлена автором '{}'", name, author);
        } catch (DataValidationException e) {
            logger.error("Ошибка валидации при добавлении книги: {}", e.getMessage());
            System.out.println("Ошибка валидации: " + e.getMessage());
        } catch (DataManagerException e) {
            logger.error("Ошибка при добавлении книги на склад: {}", e.getMessage());
            System.out.println("Ошибка при добавлении книги: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Неожиданная ошибка при добавлении книги", e);
            System.out.println("Неожиданная ошибка: " + e.getMessage());
        }
    }
}
