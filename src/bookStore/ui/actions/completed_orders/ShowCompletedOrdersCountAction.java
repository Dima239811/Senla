package bookStore.ui.actions.completed_orders;

import  bookStore.model.DataManager;
import bookStore.ui.actions.IAction;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class ShowCompletedOrdersCountAction implements IAction {
    private DataManager dataManager;

    public ShowCompletedOrdersCountAction(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    public void execute() {
        Scanner scanner = new Scanner(System.in);
        try {
            // Запрос периода у пользователя
            System.out.println("Введите начальную дату (формат: дд.мм.гггг):");
            Date from = parseDate(scanner.nextLine());

            System.out.println("Введите конечную дату (формат: дд.мм.гггг):");
            Date to = parseDate(scanner.nextLine());

            int count = dataManager.getCountPerformedOrder(from, to);

            System.out.println("Количество выполненных заказов " + count + " за период с " + from + " по " + to);

        } catch (ParseException e) {
            System.out.println("Неверный формат даты. Используйте дд.мм.гггг");
        }
    }

    private Date parseDate(String dateStr) throws ParseException {
        return new SimpleDateFormat("dd.MM.yyyy").parse(dateStr);
    }
}
