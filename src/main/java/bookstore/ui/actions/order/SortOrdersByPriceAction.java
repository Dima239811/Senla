package bookstore.ui.actions.order;

import bookstore.controller.OrderController;
import bookstore.dto.OrderResponse;
import bookstore.exception.DataManagerException;
import bookstore.ui.actions.IAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SortOrdersByPriceAction implements IAction {
    private final OrderController orderController;
    private static final Logger logger = LoggerFactory.getLogger(SortOrdersByPriceAction.class);

    public SortOrdersByPriceAction(OrderController orderController) {
        this.orderController = orderController;
    }

    @Override
    public void execute() {
        logger.info("Пользователь выбрал команду: сортировка всех заказов по статусу выполнения");
        System.out.println("Сортировка по статусу выполнения: ");

        try {
            List<OrderResponse> orders = orderController.sortOrders("по цене");

            if (orders.isEmpty()) {
                logger.info("список заказов пуст");
                System.out.println("заказы не найдены");
            } else {
                logger.info("выведено {} заказов", orders.size());
                orders.forEach(System.out::println);
            }
            System.out.println("-----------------------------------------------");
        } catch (DataManagerException ex) {
            System.out.println("Ошибка при сортировке пользователей по цене " + ex.getCause());
            logger.error("Ошибка при сортировке пользователей по цене {}", String.valueOf(ex.getCause()));
        }
    }
}
