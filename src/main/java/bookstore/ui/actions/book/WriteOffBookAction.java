package bookstore.ui.actions.book;

import bookstore.controller.BookController;
import bookstore.ui.actions.IAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class WriteOffBookAction implements IAction {
    private final BookController bookController;
    private static final Logger logger = LoggerFactory.getLogger(WriteOffBookAction.class);

    public WriteOffBookAction(BookController bookController) {
        this.bookController = bookController;
    }

    @Override
    public void execute() {
        logger.info("Пользователь выбрал действие списание книги");
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите id списываемой книги: ");

        try {
            int id = scanner.nextInt();
            bookController.writeOffBook(id);
            logger.info("Книга с id " + id + " успешно списана");
        } catch (Exception ex) {
            logger.error("Введено некорректное число");
            System.out.println("Некорректный ввод, введите целое число");
        }
    }
}
