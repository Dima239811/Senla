package Task6.ui.actions.order;

import Task6.exception.IncorrectNumberException;
import Task6.model.DataManager;
import Task6.ui.actions.IAction;

import java.util.Scanner;

public class CancelOrderAction implements IAction {
    private DataManager dataManager;

    public CancelOrderAction(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void execute() {
        System.out.println("Введите id заказа для отмены");

        Scanner scanner = new Scanner(System.in);
        try {
            if (!scanner.hasNextInt()) {
                throw new IncorrectNumberException();
            }

            int id = scanner.nextInt();
            scanner.nextLine();

            if (id <= 0) {
                throw new IncorrectNumberException("ID заказа должен быть положительным числом");
            }

            dataManager.cancelOrder(id);
            System.out.println("Заказ отменен");

        } catch (IncorrectNumberException e) {
            System.out.println(e.getMessage());
        }
    }
}
