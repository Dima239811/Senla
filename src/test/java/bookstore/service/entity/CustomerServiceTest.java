package bookstore.service.entity;

import bookstore.dto.CustomerRequest;
import bookstore.dto.CustomerResponse;
import bookstore.exception.DaoException;
import bookstore.exception.ServiceException;
import bookstore.model.entity.Customer;
import bookstore.model.mapper.CustomerMapper;
import bookstore.repo.CustomerDAO;
import bookstore.service.entityService.CustomerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {
    @Mock
    private CustomerDAO customerDAO;

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private CustomerService customerService;

    @Test
    void getAll_shouldReturnCustomerList() {
        List<Customer> customers = List.of(new Customer());
        List<CustomerResponse> responses = List.of(new CustomerResponse(12, "Dima"));

        Mockito.when(customerDAO.getAll()).thenReturn(customers);
        Mockito.when(customerMapper.toCustomerResponceList(customers))
                .thenReturn(responses);

        List<CustomerResponse> result = customerService.getAll();

        assertEquals(1, result.size());
        Mockito.verify(customerDAO).getAll();
    }

    @Test
    void getAll_shouldThrowServiceException() {
        Mockito.when(customerDAO.getAll()).thenThrow(
                new DaoException()
        );

        assertThrows(ServiceException.class,
                () -> {
                    customerService.getAll();
                });
        Mockito.verify(customerDAO).getAll();
    }


    @Test
    void getById_shouldReturnCustomerList() {
        Customer customer = new Customer();
        CustomerResponse response = new CustomerResponse(1, "Dima");

        Mockito.when(customerDAO.findById(1)).thenReturn(customer);
        Mockito.when(customerMapper.toCustomerResponse(customer))
                .thenReturn(response);

        CustomerResponse result = customerService.getById(1);

        assertEquals(response, result);
        Mockito.verify(customerDAO).findById(1);
    }

    @Test
    void getById_shouldThrowServiceException() {
        Mockito.when(customerDAO.findById(1)).thenThrow(
                new DaoException()
        );

        assertThrows(ServiceException.class,
                () -> {
                    customerService.getById(1);
                });
        Mockito.verify(customerDAO).findById(1);
    }

    @Test
    void add_shouldCallDaoCreate() {
        var customerRequest = new CustomerRequest("Dima", 19, "+7902565232",
                "dima@mail.ru", "address");
        var customer = new Customer();

        Mockito.when(customerMapper.toEntity(customerRequest)).thenReturn(customer);
        customerService.add(customerRequest);
        Mockito.verify(customerDAO).create(customer);
    }

    @Test
    void add_shouldThrowException() {
        var customerRequest = new CustomerRequest("Dima", 19, "+7902565232",
                "dima@mail.ru", "address");

        Mockito.when(customerMapper.toEntity(customerRequest))
                .thenThrow(new DaoException());

        assertThrows(ServiceException.class, () -> {
            customerService.add(customerRequest);
        });
    }

    @Test
    void update_shouldCallDaoUpdate() {
        var customerRequest = new CustomerRequest("Dima", 19, "+7902565232",
                "dima@mail.ru", "address");
        var customer = new Customer();

        Mockito.when(customerMapper.toEntity(customerRequest)).thenReturn(customer);
        customerService.update(customerRequest);
        Mockito.verify(customerDAO).update(customer);
    }

    @Test
    void update_shouldThrowException() {
        var customerRequest = new CustomerRequest("Dima", 19, "+7902565232",
                "dima@mail.ru", "address");

        Mockito.when(customerMapper.toEntity(customerRequest))
                .thenThrow(new DaoException());

        assertThrows(ServiceException.class, () -> {
            customerService.update(customerRequest);
        });
    }

    @Test
    void getEntityById_shouldReturnCustomer() {
        var customer = new Customer();
        Mockito.when(customerDAO.findById(1)).thenReturn(customer);

        Customer result = customerService.getEntityById(1);
        assertEquals(customer, result);
        verify(customerDAO).findById(1);
    }

    @Test
    void getEntityById_shouldThrowException() {
        Mockito.when(customerDAO.findById(1))
                .thenThrow(new DaoException());

        assertThrows(ServiceException.class, () -> {
            customerService.getEntityById(1);
        });
    }

    @Test
    void findByEmail_shouldReturnCustomer() {
        var customer = new Customer();
        Mockito.when(customerDAO.findByEmail("email")).thenReturn(customer);

        var res = customerService.findByEmail("email");
        assertEquals(res, customer);
        verify(customerDAO).findByEmail("email");
    }

    @Test
    void findByEmail_shouldThrowException() {
        Mockito.when(customerDAO.findByEmail("email"))
                .thenThrow(new DaoException());

        assertThrows(ServiceException.class, () -> {
            customerService.findByEmail("email");
        });
    }

    @Test
    void getAllCustomer_shouldReturnCustomers() {
        List<Customer> customers = List.of(new Customer(), new Customer());
        Mockito.when(customerDAO.getAll()).thenReturn(customers);

        var res = customerService.getAllCustomer();
        assertEquals(res, customers);
        verify(customerDAO).getAll();
    }

    @Test
    void getAllCustomer_shouldThrowException() {
        Mockito.when(customerDAO.getAll())
                .thenThrow(new DaoException());

        assertThrows(ServiceException.class, () -> {
            customerService.getAllCustomer();
        });
    }

    @Test
    void addCustomerEntity_shouldCallDaoUpdate() {
        var customer = new Customer();
        customer.setCustomerID(1);

        Mockito.when(customerDAO.findById(1)).thenReturn(customer);
        customerService.addCustomerEntity(customer);
        verify(customerDAO).update(customer);
        verify(customerDAO, never()).create(any(Customer.class));
        verify(customerDAO).update(eq(customer));
    }

    @Test
    void addCustomerEntity_shouldCallDaoCreate() {
        var customer = new Customer();
        customer.setCustomerID(1);

        Mockito.when(customerDAO.findById(1)).thenReturn(null);
        customerService.addCustomerEntity(customer);
        verify(customerDAO).create(customer);
        verify(customerDAO, never()).update(any(Customer.class));
        verify(customerDAO).create(eq(customer));
    }

    @Test
    void addCustomerEntity_shouldThrowException() {
        Mockito.when(customerDAO.findById(1))
                .thenThrow(new DaoException());

        assertThrows(ServiceException.class, () -> {
            var customer = new Customer();
            customer.setCustomerID(1);
            customerService.addCustomerEntity(customer);
        });
    }
}
