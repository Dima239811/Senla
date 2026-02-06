package bookstore.ui.actions.request;

import  bookstore.model.DataManager;
import  bookstore.model.RequestBook;
import bookstore.ui.actions.IAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SortRequestsByCountAction implements IAction {
    private DataManager dataManager;
    private static final Logger logger = LoggerFactory.getLogger(SortRequestsByCountAction.class);

    public SortRequestsByCountAction(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void execute() {
        logger.info("Пользователь выбрал команду: сортировка всех запросов по количеству");
        System.out.println("Сортировка по кол-ву запросов: ");
        List<RequestBook> requestBooks = dataManager.sortRequest("по количеству запросов");

        if (requestBooks.isEmpty()) {
            logger.info("список запросов пуст");
            System.out.println("заказы не найдены");
        } else {
            logger.info("выведено {} запросов на книги", requestBooks.size());
            requestBooks.forEach(book -> System.out.println(book));
        }
        System.out.println("-----------------------------------------------");
    }
}
