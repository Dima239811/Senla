package Task8.ui.actions.order;

import Task8.model.DataManager;
import Task8.model.Order;
import Task8.ui.actions.IAction;

import java.util.List;

public class SortOrdersByDateAction implements IAction {
    private DataManager dataManager;

    public SortOrdersByDateAction(DataManager dataManager) {
        this.dataManager = dataManager;
    }
    @Override
    public void execute() {
        System.out.println("Сортировка по дате: ");
        List<Order> orders = dataManager.sortOrders("по дате");

        if (orders.isEmpty()) {
            System.out.println("заказы не найдены");
        } else {
            orders.forEach(book -> System.out.println(book));
        }

        System.out.println("-----------------------------------------------");
    }
}
