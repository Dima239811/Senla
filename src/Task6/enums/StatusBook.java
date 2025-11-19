package Task6.enums;

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
}
