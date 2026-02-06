package bookStore.ui.actions.book;

import bookStore.enums.StatusBook;
import exception.DataValidationException;
import bookStore.model.Book;
import bookStore.model.DataManager;
import bookStore.ui.actions.IAction;

import java.util.Scanner;

public class AddBookAction implements IAction {

    private DataManager dataManager;

    public AddBookAction(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void execute() {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.print("Введите название книги: ");
            String name = scanner.nextLine();
            if (name == null || name.trim().isEmpty()) {
                throw new DataValidationException("Название книги не может быть пустым");
            }

            System.out.print("Введите автора: ");
            String author = scanner.nextLine();
            if (author == null || author.trim().isEmpty()) {
                throw new DataValidationException("Имя автора не может быть пустым");
            }

            int year = 0;
            double price = 0.0;

            try {
                System.out.print("Введите год издания книги: ");
                year = scanner.nextInt();
                if (year < 0) {
                    throw new DataValidationException("Год издания не может быть отрицательным");
                }
            } catch (java.util.InputMismatchException e) {
                throw new DataValidationException("Некорректный формат года. Введите целое число");
            }

            try {
                System.out.print("Введите стоимость книги: ");
                price = scanner.nextDouble();
                if (price <= 0) {
                    throw new DataValidationException("Стоимость книги должна быть положительной");
                }
            } catch (java.util.InputMismatchException e) {
                throw new DataValidationException("Некорректный формат стоимости. Используйте точку как разделитель");
            } finally {
                scanner.nextLine(); // Очистка буфера
            }

            Book book = new Book(name, author, year, price, StatusBook.IN_STOCK);
            dataManager.addBookToWareHouse(book);
            System.out.println("Книга '" + name + "' автора '" + author + "' добавлена.");

        } catch (DataValidationException e) {
            System.out.println("Ошибка валидации: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Неожиданная ошибка: " + e.getMessage());
        }
    }
}
