package bookstore.model.mapper;

import bookstore.dto.CustomerRequest;
import bookstore.dto.CustomerResponse;
import bookstore.model.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CustomerMapper {
    CustomerResponse toCustomerResponse(Customer customer);
    List<CustomerResponse> toCustomerResponceList(List<Customer> customers);

    @Mapping(target = "customerID", ignore = true)
    Customer toEntity(CustomerRequest customerRequest);
}
