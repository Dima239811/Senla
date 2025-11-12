package Task5.model;


import Task5.enums.StatusBook;

public class Book {
    private String name;
    private String authtor;
    private int year;
    private double price;
    private StatusBook status;  // в наличии или отсутствует
    private int bookId;
    private static int count = 0;

    public Book(String name, String authtor, int year, double price) {
        this.name = name;
        this.authtor = authtor;
        this.year = year;
        this.price = price;
        this.status = StatusBook.IN_STOCK;
        this.bookId = count;
        count++;
    }

    public Book(String name, String authtor, int year, double price, StatusBook status) {
        this.name = name;
        this.authtor = authtor;
        this.year = year;
        this.price = price;
        this.status = status;
        this.bookId = count;
        count++;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthtor() {
        return authtor;
    }

    public void setAuthtor(String authtor) {
        this.authtor = authtor;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setStatus(StatusBook statusBook) {
        this.status = statusBook;
    }

    public StatusBook getStatus() {
        return status;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    @Override
    public String toString() {
        return "Book{" +
                "name='" + name + '\'' +
                ", authtor='" + authtor + '\'' +
                ", year=" + year +
                ", price=" + price +
                ", status=" + status +
                ", bookId=" + bookId +
                '}';
    }
}
