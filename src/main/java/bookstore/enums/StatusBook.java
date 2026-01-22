package bookstore.enums;

public enum StatusBook {
    IN_STOCK("в наличии"),
    OUT_OF_STOCK("отсутствует");

    private String value;

    StatusBook(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static StatusBook fromValue(String value) {
        for (StatusBook status : StatusBook.values()) {
            if (status.getValue().equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Неизвестный статус книги: " + value);
    }
}
