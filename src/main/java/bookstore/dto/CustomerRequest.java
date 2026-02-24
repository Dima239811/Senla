package bookstore.dto;

public record CustomerRequest(
        String fullName,
        int age,
        String phoneNumber,
        String email,
        String address
) { }
