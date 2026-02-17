package bookstore.ui.actions.order;

import bookstore.controller.OrderController;
import bookstore.exception.DataManagerException;
import bookstore.exception.IncorrectNumberException;
import bookstore.ui.actions.IAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class CancelOrderAction implements IAction {
    private final OrderController orderController;
    private static final Logger logger = LoggerFactory.getLogger(CancelOrderAction.class);

    public CancelOrderAction(OrderController orderController) {
        this.orderController = orderController;
    }

    @Override
    public void execute() {
        logger.info("Пользователь выбрал команду: отмена заказа");
        System.out.println("Введите id заказа для отмены");

        Scanner scanner = new Scanner(System.in);
        try {
            if (!scanner.hasNextInt()) {
                throw new IncorrectNumberException();
            }

            int id = scanner.nextInt();
            scanner.nextLine();

            if (id <= 0) {
                logger.error("Пользователь ввел число <= 0");
                throw new IncorrectNumberException("ID заказа должен быть положительным числом");
            }

            orderController.cancelOrder(id);
            System.out.println("Заказ отменен");
            logger.info("Заказа с id {} успешно отменен", id);
        } catch (IncorrectNumberException e) {
            logger.info("Некорректный ввод числа {}", e.getMessage());
            System.out.println(e.getMessage());
        } catch (DataManagerException e) {
            logger.error("Ошибка при отмене заказа: {}", e.getMessage());
            System.out.println("Ошибка при отмене заказа: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Неожиданная ошибка при отмене заказа", e);
            System.out.println("Неожиданная ошибка: " + e.getMessage());
        }
    }
}
