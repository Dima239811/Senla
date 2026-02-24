package bookstore.dto;


public record RequestBookResponse(
        int requestId,
        int bookId,
        String bookTitle,
        String bookAuthor,
        int customerId,
        String customerName,
        String status
) { }
