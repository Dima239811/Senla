package Task7.ui.actions.order;

import Task7.model.DataManager;
import Task7.model.Order;
import Task7.ui.actions.IAction;

import java.util.List;

public class SortOrdersByStatusAction implements IAction {
    private DataManager dataManager;

    public SortOrdersByStatusAction(DataManager dataManager) {
        this.dataManager = dataManager;
    }
    @Override
    public void execute() {
        System.out.println("Сортировка по цене: ");
        List<Order> orders = dataManager.sortOrders("по статусу");

        if (orders.isEmpty()) {
            System.out.println("заказы не найдены");
        } else {
            orders.forEach(book -> System.out.println(book));
        }

        System.out.println("-----------------------------------------------");
    }
}
