package bookstore.dto;

public record RegisterRequest(
    String login,
    String password,
    String fullName,
    int age,
    String phoneNumber,
    String email,
    String address
) { }
