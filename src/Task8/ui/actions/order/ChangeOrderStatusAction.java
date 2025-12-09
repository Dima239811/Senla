package Task8.ui.actions.order;

import Task8.enums.OrderStatus;
import Task8.exception.IncorrectNumberException;
import Task8.model.DataManager;
import Task8.ui.actions.IAction;

import java.util.Scanner;

public class ChangeOrderStatusAction implements IAction {
    private DataManager dataManager;

    public ChangeOrderStatusAction(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void execute() {
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.println("Введите id заказа для изменения статуса:");
            if (!scanner.hasNextInt()) {
                throw new IncorrectNumberException("Ошибка: ID должен быть числом.");
            }
            int id = scanner.nextInt();
            scanner.nextLine();

            System.out.println("Выберите цифру статуса заказа:");
            System.out.println("1. " + OrderStatus.NEW.getValue());
            System.out.println("2. " + OrderStatus.COMPLETED.getValue());
            System.out.println("3. " + OrderStatus.CANCELLED.getValue());
            System.out.println("4. " + OrderStatus.PROCESSING.getValue());

            if (!scanner.hasNextInt()) {
                throw new IncorrectNumberException("Ошибка: Введите число от 1 до 4.");
            }
            int choice = scanner.nextInt();
            scanner.nextLine();

            OrderStatus selectedStatus = switch (choice) {
                case 1 -> OrderStatus.NEW;
                case 2 -> OrderStatus.COMPLETED;
                case 3 -> OrderStatus.CANCELLED;
                case 4 -> OrderStatus.PROCESSING;
                default -> {
                    throw new IncorrectNumberException("Некорректный вариант. Попробуйте снова.");
                }
            };

            dataManager.changeStatusOrder(id, selectedStatus);
            System.out.println("Статус заказа успешно изменен.");

        } catch (IncorrectNumberException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }
}
