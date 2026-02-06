package bookstore.ui.actions.order;

import  bookstore.model.DataManager;
import  bookstore.model.Order;
import bookstore.ui.actions.IAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SortOrdersByPriceAction implements IAction {
    private DataManager dataManager;
    private static final Logger logger = LoggerFactory.getLogger(SortOrdersByPriceAction.class);

    public SortOrdersByPriceAction(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void execute() {
        logger.info("Пользователь выбрал команду: сортировка всех заказов по статусу выполнения");
        System.out.println("Сортировка по статусу выполнения: ");
        List<Order> orders = dataManager.sortOrders("по цене");

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
