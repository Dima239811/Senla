package bookstore.ui.actions.order;

import bookstore.exception.DataManagerException;
import bookstore.model.DataManager;
import bookstore.model.entity.Order;
import bookstore.ui.actions.IAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ShowAllOrdersAction implements IAction {
    private final DataManager dataManager;
    private static final Logger logger = LoggerFactory.getLogger(ShowAllOrdersAction.class);

    public ShowAllOrdersAction(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void execute() {
        logger.info("Пользователь выбрал команду: просмотр  всех заказов");
        System.out.println("Список всех заказов");
        System.out.println("вывод из списка заказов");

        try {
            List<Order> orders = dataManager.getAllOrder();
            System.out.println("\n=== СПИСОК ВСЕХ заказов ===");
            System.out.println("Всего заказов: " + orders.size());
            System.out.println("-----------------------------------------------");

            if (orders.isEmpty()) {
                logger.info("список заказов пуст");
                System.out.println("заказы не найдены");
            } else {
                logger.info("выведено {} заказов", orders.size());
                orders.forEach(System.out::println);
            }

            System.out.println("-----------------------------------------------");
        } catch (DataManagerException ex) {
            System.out.println("Ошибка при получении всех заказов из бд " + ex.getCause());
            logger.error("Ошибка при получении всех заказов из бд {}", String.valueOf(ex.getCause()));
        } catch (Exception ex) {
            System.out.println("Неожиданная ошибка" + ex.getMessage());
        }
    }
}
