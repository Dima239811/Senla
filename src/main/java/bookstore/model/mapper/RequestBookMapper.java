package bookstore.model.mapper;

import bookstore.dto.RequestBookResponse;
import bookstore.enums.RequestStatus;
import bookstore.model.entity.RequestBook;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RequestBookMapper {
    @Mapping(source = "book.bookId", target = "bookId")
    @Mapping(source = "book.name", target = "bookTitle")
    @Mapping(source = "book.author", target = "bookAuthor")
    @Mapping(source = "customer.customerID", target = "customerId")
    @Mapping(source = "customer.fullName", target = "customerName")
    @Mapping(source = "status", target = "status")
    RequestBookResponse toRequestBookResponse(RequestBook order);

    default String map(RequestStatus status) {
        return status != null ? status.getValue() : null;
    }

    List<RequestBookResponse> toRequestBookResponseList(List<RequestBook> requestBooks);
}
