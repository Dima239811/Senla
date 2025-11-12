package Task5.ui.actions.completed_orders;

import Task5.model.DataManager;
import Task5.ui.actions.IAction;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class ShowTotalRevenueAction implements IAction {
    private DataManager dataManager;

    public ShowTotalRevenueAction(DataManager dataManager) {
        this.dataManager = dataManager;
    }


    @Override
    public void execute() {
        Scanner scanner = new Scanner(System.in);
        try {
            // Запрос периода у пользователя
            System.out.println("Введите начальную дату (формат: дд.мм.гггг):");
            Date from = parseDate(scanner.nextLine());

            System.out.println("Введите конечную дату (формат: дд.мм.гггг):");
            Date to = parseDate(scanner.nextLine());

            // Получение и вывод отсортированных заказов
            var price = dataManager.calculateIncomeForPeriod(from, to);

            System.out.println("за период заработано " + price);

        } catch (ParseException e) {
            System.out.println("Неверный формат даты. Используйте дд.мм.гггг");
        }
    }

    private Date parseDate(String dateStr) throws ParseException {
        return new SimpleDateFormat("dd.MM.yyyy").parse(dateStr);
    }
}
