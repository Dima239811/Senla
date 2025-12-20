package Task7.ui.actions.request;

import Task7.model.DataManager;
import Task7.model.RequestBook;
import Task7.ui.actions.IAction;

import java.util.List;

public class SortRequestsByCountAction implements IAction {

    private DataManager dataManager;
    public SortRequestsByCountAction(DataManager dataManager) {
        this.dataManager = dataManager;
    }


    @Override
    public void execute() {
        System.out.println("Сортировка по кол-ву запросов: ");
        List<RequestBook> requestBooks = dataManager.sortRequest("по количеству запросов");

        if (requestBooks.isEmpty()) {
            System.out.println("заказы не найдены");
        } else {
            requestBooks.forEach(book -> System.out.println(book));
        }

        System.out.println("-----------------------------------------------");
    }
}
