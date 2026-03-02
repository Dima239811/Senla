package bookstore.dto;

public record RequestBookRequest(
        CustomerRequest customerRequest,
        int bookId
) { }
