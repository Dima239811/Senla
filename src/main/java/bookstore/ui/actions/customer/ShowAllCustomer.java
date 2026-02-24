package bookstore.ui.actions.customer;

import bookstore.controller.CustomerController;
import bookstore.dto.CustomerResponse;
import bookstore.exception.DataManagerException;
import bookstore.ui.actions.IAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ShowAllCustomer implements IAction {
    private final CustomerController customerController;
    private static final Logger logger = LoggerFactory.getLogger(ShowAllCustomer.class);

    public ShowAllCustomer(CustomerController customerController) {
        this.customerController = customerController;
    }

    @Override
    public void execute() {
        logger.info("Пользователь выбрал команду: вывод всех клиентов");
        System.out.println("Список всех клиентов");
        System.out.println("вывод из клиентов книг");

        try {
            List<CustomerResponse> customers = customerController.getAllCustomer();
            System.out.println("\n=== СПИСОК ВСЕХ КЛИЕНТОВ ===");
            System.out.println("Всего клиентов: " + customers.size());
            System.out.println("-----------------------------------------------");

            if (customers.isEmpty()) {
                logger.info("Список клиентов пуст");
                System.out.println("Клиенты не найдены");
            } else {
                logger.info("Выведено {} клиентов", customers.size());
                customers.forEach(System.out::println);
            }

            System.out.println("-----------------------------------------------");
        } catch (DataManagerException ex) {
            System.out.println("Ошибка получения всех пользователей " + ex.getMessage());
            logger.error("Ошибка получения всех пользователей {}", ex.getMessage());
        }
    }
}
