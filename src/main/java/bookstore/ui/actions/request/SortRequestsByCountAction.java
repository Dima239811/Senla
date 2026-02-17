package bookstore.ui.actions.request;

import bookstore.controller.RequestBookController;
import bookstore.exception.DataManagerException;
import bookstore.model.entity.RequestBook;
import bookstore.ui.actions.IAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SortRequestsByCountAction implements IAction {
    private final RequestBookController requestBookController;
    private static final Logger logger = LoggerFactory.getLogger(SortRequestsByCountAction.class);

    public SortRequestsByCountAction(RequestBookController requestBookController) {
        this.requestBookController = requestBookController;
    }

    @Override
    public void execute() {
        logger.info("Пользователь выбрал команду: сортировка всех запросов по количеству");
        System.out.println("Сортировка по кол-ву запросов: ");

        try {
            List<RequestBook> requestBooks = requestBookController.sortRequest("по количеству запросов");

            if (requestBooks.isEmpty()) {
                logger.info("список запросов пуст");
                System.out.println("заказы не найдены");
            } else {
                logger.info("выведено {} запросов на книги", requestBooks.size());
                requestBooks.forEach(System.out::println);
            }
            System.out.println("-----------------------------------------------");
        } catch (DataManagerException ex) {
            System.out.println("Ошибка при сортировке запросов по количеству " + ex.getCause());
            logger.error("Ошибка при сортировке запросов по количеству {}", String.valueOf(ex.getCause()));
        }
    }
}
