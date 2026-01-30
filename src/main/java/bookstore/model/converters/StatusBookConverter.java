package bookstore.model.converters;

import bookstore.enums.StatusBook;
import jakarta.persistence.AttributeConverter;

public class StatusBookConverter implements AttributeConverter<StatusBook, String> {
    @Override
    public String convertToDatabaseColumn(StatusBook statusBook) {
        String value = statusBook != null ? statusBook.getValue() : null;
        return value;
    }

    @Override
    public StatusBook convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        String trimmedValue = dbData.trim();
        return StatusBook.fromValue(trimmedValue);
    }
}
