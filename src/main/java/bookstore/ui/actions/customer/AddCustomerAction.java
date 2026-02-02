package bookstore.ui.actions.customer;

import bookstore.exception.DataManagerException;
import bookstore.exception.DataValidationException;
import bookstore.model.entity.Customer;
import bookstore.model.DataManager;
import bookstore.ui.actions.IAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class AddCustomerAction implements IAction {
    private static final Logger logger = LoggerFactory.getLogger(AddCustomerAction.class);
    private final DataManager dataManager;

    public AddCustomerAction(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void execute() {
        logger.info("Пользователь выбрал команду: добавление клиента");
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.println("\n=== Добавление нового клиента ===");

            System.out.print("Введите ФИО клиента: ");
            String fullName = scanner.nextLine();
            if (fullName == null || fullName.trim().isEmpty()) {
                throw new DataValidationException("ФИО не может быть пустым");
            }


            int age = 0;
            try {
                System.out.print("Введите возраст клиента: ");
                age = Integer.parseInt(scanner.nextLine());
                if (age <= 0 || age > 120) {
                    throw new DataValidationException("Возраст должен быть от 1 до 120 лет");
                }
            } catch (NumberFormatException e) {
                throw new DataValidationException("Некорректный формат возраста. Введите целое число");
            }

            // Ввод телефона
            System.out.print("Введите телефон клиента: ");
            String phone = scanner.nextLine();
            if (phone == null || phone.trim().isEmpty()) {
                throw new DataValidationException("Телефон не может быть пустым");
            }

            // Ввод email
            System.out.print("Введите email клиента: ");
            String email = scanner.nextLine();
            if (email != null && !email.isEmpty() && !email.contains("@")) {
                throw new DataValidationException("Некорректный формат email");
            }

            System.out.print("Введите адрес клиента: ");
            String address = scanner.nextLine();

            // Создание и сохранение клиента
            Customer customer = new Customer(fullName, age, phone, email, address);
            dataManager.addCustomer(customer);

            logger.info("Успешно добавлен клиент: {}", fullName);
            System.out.println("\nКлиент '" + fullName + "' успешно добавлен!");
        } catch (DataValidationException e) {
            logger.error("Ошибка валидации при добавлении клиента: {}", e.getMessage());
            System.out.println("Ошибка: " + e.getMessage());
        } catch (DataManagerException e) {
            logger.error("Ошибка из datamanager при добавлении клиента: {}", e.getMessage());
            System.out.println("Ошибка: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Неожиданная ошибка при добавлении клиента", e);
            System.out.println("Произошла ошибка: " + e.getMessage());
        }
    }
}
