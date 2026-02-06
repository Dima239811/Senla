package bookstore.enums;

public enum OrderStatus {
    NEW("новый"),
    PROCESSING("в обработке"),
    COMPLETED("выполнен"),
    CANCELLED("отменен"),
    WAITING_FOR_BOOK("ожидание книги");

    private String value;

    OrderStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static OrderStatus fromValue(String value) {
        for (OrderStatus status : OrderStatus.values()) {
            if (status.getValue().equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Неизвестный статус заказа: " + value);
    }
}

