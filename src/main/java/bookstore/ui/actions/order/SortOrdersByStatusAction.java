package bookstore.ui.actions.order;

import  bookstore.model.DataManager;
import  bookstore.model.Order;
import bookstore.ui.actions.IAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SortOrdersByStatusAction implements IAction {
    private DataManager dataManager;
    private static final Logger logger = LoggerFactory.getLogger(SortOrdersByStatusAction.class);

    public SortOrdersByStatusAction(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void execute() {
        logger.info("Пользователь выбрал команду: сортировка всех заказов по дате");
        System.out.println("Сортировка по цене: ");
        List<Order> orders = dataManager.sortOrders("по статусу");

        if (orders.isEmpty()) {
            logger.info("список заказов пуст");
            System.out.println("заказы не найдены");
        } else {
            logger.info("выведено {} заказов", orders.size());
            orders.forEach(book -> System.out.println(book));
        }
        System.out.println("-----------------------------------------------");
    }
}
