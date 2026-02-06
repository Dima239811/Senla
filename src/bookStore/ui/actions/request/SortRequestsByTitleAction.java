package bookStore.ui.actions.request;

import  bookStore.model.DataManager;
import  bookStore.model.RequestBook;
import bookStore.ui.actions.IAction;

import java.util.List;

public class SortRequestsByTitleAction implements IAction {
    private DataManager dataManager;
    public SortRequestsByTitleAction(DataManager dataManager) {
        this.dataManager = dataManager;
    }


    @Override
    public void execute() {
        System.out.println("Сортировка по алфавиту: ");
        List<RequestBook> requestBooks = dataManager.sortRequest("по алфавиту");

        if (requestBooks.isEmpty()) {
            System.out.println("заказы не найдены");
        } else {
            requestBooks.forEach(book -> System.out.println(book));
        }

        System.out.println("-----------------------------------------------");
    }
}
