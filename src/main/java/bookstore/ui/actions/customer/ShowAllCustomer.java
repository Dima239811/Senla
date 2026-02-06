package bookstore.ui.actions.customer;

import bookstore.exception.DataManagerException;
import bookstore.model.entity.Customer;
import bookstore.model.DataManager;
import bookstore.ui.actions.IAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ShowAllCustomer implements IAction {
    private DataManager dataManager;
    private static final Logger logger = LoggerFactory.getLogger(ShowAllCustomer.class);

    public ShowAllCustomer(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void execute() {
        logger.info("Пользователь выбрал команду: вывод всех клиентов");
        System.out.println("Список всех клиентов");
        System.out.println("вывод из клиентов книг");

        try {
            List<Customer> customers = dataManager.getAllCustomers();
            System.out.println("\n=== СПИСОК ВСЕХ КЛИЕНТОВ ===");
            System.out.println("Всего клиентов: " + customers.size());
            System.out.println("-----------------------------------------------");

            if (customers.isEmpty()) {
                logger.info("Список клиентов пуст");
                System.out.println("Клиенты не найдены");
            } else {
                logger.info("Выведено {} клиентов", customers.size());
                customers.forEach(c -> System.out.println(c));
            }

            System.out.println("-----------------------------------------------");
        } catch (DataManagerException ex) {
            System.out.println("Ошибка получения всех пользователей " + ex.getMessage());
            logger.error("Ошибка получения всех пользователей {}", ex.getMessage());
        }
    }
}
