package Task6.ui.actions.book;

import Task6.model.DataManager;
import Task6.ui.actions.IAction;

import java.util.Scanner;

public class WriteOffBookAction implements IAction {
    private DataManager dataManager;

    public WriteOffBookAction(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void execute() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите id списываемой книги: ");

        try {
            int id = scanner.nextInt();
            dataManager.writeOffBook(id);
        } catch (Exception ex) {
            System.out.println("Некорректный ввод, введите целое число");
            return;
        }


    }
}
