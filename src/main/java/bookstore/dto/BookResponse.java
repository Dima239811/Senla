package bookstore.dto;

public record BookResponse (
        int bookId,
        String name,
        String author,
        int year,
        double price,
        String status
) { }
