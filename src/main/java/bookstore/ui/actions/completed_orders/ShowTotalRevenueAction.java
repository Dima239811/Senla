package bookstore.ui.actions.completed_orders;

import bookstore.controller.OrderController;
import bookstore.exception.DataManagerException;
import bookstore.ui.actions.IAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class ShowTotalRevenueAction implements IAction {
    private final OrderController orderController;
    private static final Logger logger = LoggerFactory.getLogger(ShowTotalRevenueAction.class);

    public ShowTotalRevenueAction(OrderController orderController) {
        this.orderController = orderController;
    }


    @Override
    public void execute() {
        logger.info("Выполнение действия: Подсчёт заработанных средств за период.");
        Scanner scanner = new Scanner(System.in);
        try {
            // Запрос периода у пользователя
            System.out.println("Введите начальную дату (формат: дд.мм.гггг):");
            Date from = parseDate(scanner.nextLine());

            System.out.println("Введите конечную дату (формат: дд.мм.гггг):");
            Date to = parseDate(scanner.nextLine());

            try {
                // Получение и вывод отсортированных заказов
                var price = orderController.calculateIncomeForPeriod(from, to);

                System.out.println("за период заработано " + price);
                logger.info("Количество заработанных средств за период: {}", price);
            } catch (DataManagerException ex) {
                System.err.println("Ошибка при подсчете заработанных средств за период: " + ex.getMessage());
                logger.error("Ошибка при подсчете заработанных средств за период: ", ex);
            }
        } catch (ParseException e) {
            logger.error("Ошибка парсинга даты {}", e.getMessage());
            System.out.println("Неверный формат даты. Используйте дд.мм.гггг");
        }
    }

    private Date parseDate(String dateStr) throws ParseException {
        return new SimpleDateFormat("dd.MM.yyyy").parse(dateStr);
    }
}
