package bookstore.model.converters;

import bookstore.enums.RequestStatus;
import jakarta.persistence.AttributeConverter;

public class RequestStatusConverter implements AttributeConverter<RequestStatus, String> {

    @Override
    public String convertToDatabaseColumn(RequestStatus attribute) {
        return attribute != null ? attribute.getValue() : null;
    }

    @Override
    public RequestStatus convertToEntityAttribute(String dbData) {
        return RequestStatus.fromValue(dbData);
    }
}
