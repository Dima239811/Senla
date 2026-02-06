package bookstore.ui.actions.order;

import bookstore.controller.BookController;
import bookstore.controller.CustomerController;
import bookstore.controller.OrderController;
import bookstore.exception.DataManagerException;
import bookstore.model.entity.Book;
import bookstore.model.entity.Customer;
import bookstore.model.entity.Order;
import bookstore.ui.actions.IAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;

public class CreateOrderAction implements IAction {
    private final BookController bookController;
    private final CustomerController customerController;
    private final OrderController orderController;

    private static final Logger logger = LoggerFactory.getLogger(CreateOrderAction.class);

    public CreateOrderAction(BookController bookController, CustomerController customerController, OrderController orderController) {
        this.bookController = bookController;
        this.customerController = customerController;
        this.orderController = orderController;
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
            Book book = bookController.getBookById(id);

            if (book == null) {
                logger.error("Книга с ID {} не найдена", id);
                throw new IllegalArgumentException("Книга с ID " + id + " не найдена");
            }

            System.out.println("Ваша книга найдена!");

            // Поиск клиента
            System.out.println("\nКлиент должен быть в базе!");
            System.out.print("Введите ID клиента: ");
            int customerId = scanner.nextInt();
            scanner.nextLine();

            Customer customer = customerController.getCustomerById(customerId);
            if (customer == null) {
                throw new IllegalArgumentException("Клиент с ID " + customerId + " не найден");
            }
            System.out.println("Найден клиент: " + customer.getFullName());

            Order order = new Order(book, customer, new Date(), book.getPrice());
            orderController.createOrder(order);
        } catch (IllegalArgumentException e) {
            logger.error("Ошибка: {}", e.getMessage());
            System.out.println("Ошибка: " + e.getMessage());
        } catch (InputMismatchException e) {
            logger.error("Некорректно заполнено поле");
            System.out.println("Некорректно заполнено поле");
        } catch (DataManagerException e) {
            logger.error("Ошибка при создании заказа: {}", e.getMessage());
            System.out.println("Ошибка при создании заказа: " + e.getMessage());
        }
    }
}


