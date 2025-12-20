package Task7.enums;

public enum TypeSortBooks {
    BY_LETTER("по алфавиту"),
    BY_PRICE("по цене"),
    BY_YEAR("по году издания"),
    BY_STOCKS_IN_WAREHOUSE("по наличию на складе");

    private String value;

    TypeSortBooks(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
