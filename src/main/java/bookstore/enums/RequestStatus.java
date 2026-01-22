package bookstore.enums;

public enum RequestStatus {
    OPEN("Открыт"),
    CLOSED("Закрыт");

    private String value;

    RequestStatus(String displayName) {
        this.value = displayName;
    }

    public String getValue() {
        return value;
    }

    public static RequestStatus fromValue(String value) {
        for (RequestStatus status : RequestStatus.values()) {
            if (status.getValue().equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Неизвестный статус заказа: " + value);
    }
}

