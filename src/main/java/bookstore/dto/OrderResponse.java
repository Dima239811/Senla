package bookstore.dto;

import java.util.Date;

public record OrderResponse(
        int orderId,
        int bookId,
        String bookTitle,
        String bookAuthor,
        int customerId,
        String customerName,
        Date orderDate,
        double finalPrice,
        String status
) { }
