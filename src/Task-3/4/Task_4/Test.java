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
        DataManager dataManager = new DataManager();
        List<Book> books = new ArrayList<>();

        dataManager.addBookToWareHouse(new Book("1984", "George Orwell", 1949, 12.99, StatusBook.IN_STOCK));
        dataManager.addBookToWareHouse(new Book("To Kill a Mockingbird", "Harper Lee", 1960, 10.50, StatusBook.IN_STOCK));
        dataManager.addBookToWareHouse(new Book("The Great Gatsby", "F. Scott Fitzgerald", 1925, 9.99, StatusBook.OUT_OF_STOCK));
        dataManager.addBookToWareHouse(new Book("Pride and Prejudice", "Jane Austen", 1813, 8.25, StatusBook.IN_STOCK));
        dataManager.addBookToWareHouse(new Book("The Hobbit", "J.R.R. Tolkien", 1937, 15.75, StatusBook.IN_STOCK));

        System.out.println(" Все книги до списания:");
        System.out.println(dataManager.getAllBooks());

        dataManager.writeOffBook(1);
        dataManager.writeOffBook(2);
        System.out.println(dataManager.getAllBooks());


        // заказы
        Book book = new Book("Война и мир", "Лев Толстой", 1949, 8500, StatusBook.IN_STOCK);
        Book book1 = new Book("Анна Каренина", "Лев Толстой", 1949, 3000, StatusBook.OUT_OF_STOCK);
        Book book2 = new Book("Брест", "Лев Толстой", 1949, 4500, StatusBook.IN_STOCK);
        Customer customer = new Customer("dima", 19, "+7555555", "john@example.com", "adress");
//
        dataManager.createOrder(book, customer, new Date(2022, 3, 3));
        dataManager.createOrder(book1, customer, new Date(2025, 6, 7));
        dataManager.createOrder(book2, customer, new Date(2022, 3, 4));
        dataManager.createOrder(book2, customer, new Date(2022, 3, 8));

        // изменение статуса заказа
        dataManager.changeStatusOrder(3, OrderStatus.COMPLETED);
        dataManager.changeStatusOrder(0, OrderStatus.CANCELLED);


        System.out.println(" Все заказы:");
        dataManager.getAllOrder().forEach(System.out::println);


        ///  запросы
        dataManager.addRequest(book, customer);
        dataManager.addRequest(book1, customer);
        dataManager.addRequest(book2, customer);
        dataManager.addRequest(book, customer);

        System.out.println("запросы");
        System.out.println(dataManager.getAllRequestBook());


    }
}
