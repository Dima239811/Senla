package bookstore.ui.actions.request;

import bookstore.exception.DataManagerException;
import bookstore.model.DataManager;
import bookstore.model.entity.RequestBook;
import bookstore.ui.actions.IAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ShowAllBookRequestsAction implements IAction {
    private final DataManager dataManager;
    private static final Logger logger = LoggerFactory.getLogger(ShowAllBookRequestsAction.class);

    public ShowAllBookRequestsAction(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void execute() {
        logger.info("Пользователь выбрал команду: просмотр всех запросов на книги");

        System.out.println("Список всех запросов");
        System.out.println("вывод из списка запросов");

        try {
            List<RequestBook> requestBooks = dataManager.getAllRequestBook();
            System.out.println("\n=== СПИСОК ВСЕХ запросов ===");
            System.out.println("Всего запросов: " + requestBooks.size());
            System.out.println("-----------------------------------------------");

            if (requestBooks.isEmpty()) {
                logger.info("список запросов пуст");
                System.out.println("запросы не найдены");
            } else {
                logger.info("выведено {} запросов на книги", requestBooks.size());
                requestBooks.forEach(System.out::println);
            }
            System.out.println("-----------------------------------------------");
        } catch (DataManagerException ex) {
            System.out.println("Ошибка получения запросов на книги " + ex.getCause());
            logger.error("Ошибка получения запросов на книги {}", String.valueOf(ex.getCause()));
        }
    }
}
