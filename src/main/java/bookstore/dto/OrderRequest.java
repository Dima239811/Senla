package bookstore.dto;

public record OrderRequest(
        int customerId,
        int bookId
) {}
