package Task8.ui.actions.customer;

import Task8.model.Customer;
import Task8.model.DataManager;
import Task8.ui.actions.IAction;

import java.util.List;

public class ShowAllCustomer implements IAction {
    private DataManager dataManager;

    public ShowAllCustomer(DataManager dataManager) {
        this.dataManager = dataManager;
    }
    @Override
    public void execute() {
        System.out.println("Список всех клиентов");
        System.out.println("вывод из клиентов книг");
        List<Customer> customers = dataManager.getAllCustomers();
        System.out.println("\n=== СПИСОК ВСЕХ КЛИЕНТОВ ===");
        System.out.println("Всего клиентов: " + customers.size());
        System.out.println("-----------------------------------------------");

        if (customers.isEmpty()) {
            System.out.println("Клиенты не найдены");
        } else {
            customers.forEach(c -> System.out.println(c));
        }

        System.out.println("-----------------------------------------------");
    }
}
