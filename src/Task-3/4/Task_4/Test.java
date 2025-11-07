package Task_4;

import Task_4.enums.OrderStatus;
import Task_4.enums.StatusBook;
import Task_4.model.Book;
import Task_4.model.Customer;
import Task_4.model.DataManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        // сортировка книг
        DataManager dataManager = new DataManager();
//        List<Book> books = new ArrayList<>();
//
//        dataManager.addBookToWareHouse(new Book("1984", "George Orwell", 1949, 12.99, StatusBook.IN_STOCK));
//        dataManager.addBookToWareHouse(new Book("To Kill a Mockingbird", "Harper Lee", 1960, 10.50, StatusBook.IN_STOCK));
//        dataManager.addBookToWareHouse(new Book("The Great Gatsby", "F. Scott Fitzgerald", 1925, 9.99, StatusBook.OUT_OF_STOCK));
//        dataManager.addBookToWareHouse(new Book("Pride and Prejudice", "Jane Austen", 1813, 8.25, StatusBook.IN_STOCK));
//        dataManager.addBookToWareHouse(new Book("The Hobbit", "J.R.R. Tolkien", 1937, 15.75, StatusBook.IN_STOCK));
//
//        System.out.println(" Все книги до сортировки:");
//        dataManager.getAllBooks().forEach(System.out::println);
//
//        System.out.println("Сортировка по алфавиту:");
//        dataManager.sortBooks("по алфавиту").forEach(System.out::println);
//
//        System.out.println("Сортировка по цене:");
//        dataManager.sortBooks("по цене").forEach(System.out::println);
//
//        System.out.println("Сортировка по году издания:");
//        dataManager.sortBooks("по году издания").forEach(System.out::println);
//
//        System.out.println("Сортировка по наличию на складе:");
//        dataManager.sortBooks("по наличию на складе").forEach(System.out::println);


        // сортировка заказа
        Book book = new Book("Война и мир", "Лев Толстой", 1949, 8500, StatusBook.IN_STOCK);
        Book book1 = new Book("Анна Каренина", "Лев Толстой", 1949, 3000, StatusBook.OUT_OF_STOCK);
        Book book2 = new Book("Брест", "Лев Толстой", 1949, 4500, StatusBook.IN_STOCK);
        Customer customer = new Customer("dima", 19, "+7555555", "john@example.com", "adress");
//
        dataManager.createOrder(book, customer, new Date(2022, 3, 3));
        dataManager.createOrder(book1, customer, new Date(2025, 6, 7));
        dataManager.createOrder(book2, customer, new Date(2022, 3, 4));
        dataManager.createOrder(book2, customer, new Date(2022, 3, 8));

        dataManager.changeStatusOrder(3, OrderStatus.COMPLETED);
        dataManager.changeStatusOrder(0, OrderStatus.COMPLETED);


        System.out.println(" Все заказы до сортировки:");
        dataManager.getAllOrder().forEach(System.out::println);
//
//        System.out.println("Сортировка по дате:");
//        dataManager.sortOrders("по дате").forEach(System.out::println);
//
//        System.out.println("Сортировка по цене:");
//        dataManager.sortOrders("по цене").forEach(System.out::println);
//
//        System.out.println("Сортировка по статусу:");
//        dataManager.sortOrders("по статусу").forEach(System.out::println);


        ///  сортировка запросов
//        dataManager.addRequest(book, customer);
//        dataManager.addRequest(book1, customer);
//        dataManager.addRequest(book2, customer);
//        dataManager.addRequest(book, customer);
//
//        System.out.println("до сортировки");
//        System.out.println(dataManager.getAllRequestBook());
//
//        System.out.println("сортировка по алфавиту");
//        dataManager.sortRequest("по алфавиту").forEach(System.out::println);
//
//        System.out.println("сортировка по количеству запросов");
//        dataManager.sortRequest("по количеству запросов").forEach(System.out::println);

        // сортировка выполненных заказов
//        System.out.println("после сортировки");
//        dataManager.sortPerformOrdersForPeriod("по цене", new Date(2022, 3, 2), new Date(2022, 3, 8)).forEach(System.out::println);


        // сумма заработанных средств за период времени
        dataManager.sortPerformOrdersForPeriod("по цене", new Date(2022, 3, 2), new Date(2022, 3, 8)).forEach(System.out::println);
        System.out.println(dataManager.calculateIncomeForPeriod(new Date(2022, 3, 2), new Date(2022, 3, 8)));
    }
}
