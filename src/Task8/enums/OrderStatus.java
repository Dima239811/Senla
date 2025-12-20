package Task8.enums;

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
}

