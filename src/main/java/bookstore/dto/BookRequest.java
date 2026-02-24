package bookstore.dto;

public record BookRequest(
        String name,
        String author,
        int year,
        double price
) { }
