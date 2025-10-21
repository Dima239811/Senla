package Task_4.enums;

public enum OrderStatus {
    NEW("новый"),
    PROCESSING("в обработке"),
    COMPLETED("выполнен"),
    CANCELLED("отменен");

    private String value;

    OrderStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

