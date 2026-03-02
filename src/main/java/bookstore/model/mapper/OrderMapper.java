package bookstore.model.mapper;

import bookstore.dto.OrderResponse;
import bookstore.enums.OrderStatus;
import bookstore.model.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderMapper {
    @Mapping(source = "book.bookId", target = "bookId")
    @Mapping(source = "book.name", target = "bookTitle")
    @Mapping(source = "book.author", target = "bookAuthor")
    @Mapping(source = "customer.customerID", target = "customerId")
    @Mapping(source = "customer.fullName", target = "customerName")
    @Mapping(source = "status", target = "status")
    OrderResponse toOrderResponse(Order order);

    default String map(OrderStatus status) {
        return status != null ? status.getValue() : null;
    }

    List<OrderResponse> toOrderResponseList(List<Order> orders);
}
