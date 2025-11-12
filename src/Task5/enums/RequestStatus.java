package Task5.enums;

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

}

