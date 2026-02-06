package bookstore.ui.actions.order;

import  bookstore.model.Book;
import  bookstore.model.Customer;
import  bookstore.model.DataManager;
import bookstore.model.Order;
import bookstore.ui.actions.IAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;

public class CreateOrderAction implements IAction {
    private DataManager dataManager;
    private static final Logger logger = LoggerFactory.getLogger(CreateOrderAction.class);

    public CreateOrderAction(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void execute() {
        // создание книги
        logger.info("Начало обработки команды: CreateOrderAction");
        Scanner scanner = new Scanner(System.in);
        System.out.println("Чтобы сформировать заказ на книгу она должна быть в базе!");
        System.out.println("Введите id книги для поиска");
        try {
            int id = scanner.nextInt();
            scanner.nextLine();
            Book book = dataManager.findBook(id);

            if (book == null) {
                logger.error("Книга с ID {} не найдена", id);
                throw new IllegalArgumentException("Книга с ID " + id + " не найдена");
            }

            System.out.println("Ваша книга найдена!");
            System.out.println("Введите имя клиента");
            String name = scanner.nextLine();
            System.out.println("Введите возраст клиента");
            int age = scanner.nextInt();
            scanner.nextLine();
            System.out.println("Введите email клиента");
            String email = scanner.nextLine();
            System.out.println("Введите адресс клиента");
            String address = scanner.nextLine();
            Customer customer = new Customer(name, age, "+79855566", email, address);

            Order order = new Order(book, customer, new Date(), book.getPrice());
            dataManager.createOrder(order);
        } catch (IllegalArgumentException e) {
            logger.error("Ошибка: {}", e.getMessage());
            System.out.println("Ошибка: " + e.getMessage());
        } catch (InputMismatchException e) {
            logger.error("Некорректно заполнено поле");
            System.out.println("Некорректно заполнено поле");
        } catch (Exception e) {
            logger.error("Неожиданная ошибка: {}", e.getMessage());
            System.out.println("Неожиданная ошибка: " + e.getMessage());
        }
    }
}


