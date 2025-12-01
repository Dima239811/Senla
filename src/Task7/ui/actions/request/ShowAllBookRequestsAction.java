package Task7.ui.actions.request;

import Task7.model.DataManager;
import Task7.model.RequestBook;
import Task7.ui.actions.IAction;

import java.util.List;

public class ShowAllBookRequestsAction implements IAction {
    private DataManager dataManager;

    public ShowAllBookRequestsAction(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void execute() {
        System.out.println("Список всех запросов");
        System.out.println("вывод из списка запросов");
        List<RequestBook> requestBooks = dataManager.getAllRequestBook();
        System.out.println("\n=== СПИСОК ВСЕХ запросов ===");
        System.out.println("Всего запросов: " + requestBooks.size());
        System.out.println("-----------------------------------------------");

        if (requestBooks.isEmpty()) {
            System.out.println("запросы не найдены");
        } else {
            requestBooks.forEach(order -> System.out.println(order));
        }

        System.out.println("-----------------------------------------------");
    }
}
