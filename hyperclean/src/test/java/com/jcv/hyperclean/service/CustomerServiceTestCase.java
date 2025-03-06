package com.jcv.hyperclean.service;

import com.jcv.hyperclean.dto.CustomerDTO;
import com.jcv.hyperclean.dto.request.CustomerRequestDTO;
import com.jcv.hyperclean.exception.HCNotFoundException;
import com.jcv.hyperclean.model.Customer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

@SpringBootTest
class CustomerServiceTestCase extends BaseServiceTest {

    @Test
    @Rollback
    void testCreateCustomer() {
        String name = "testCreateCustomer";
        String email = "testCreateCustomer@gmail.com";
        String phone = getRandomNumber();
        CustomerRequestDTO requestDTO = new CustomerRequestDTO(name, email, phone);

        Customer customer = Assertions.assertDoesNotThrow(() -> customerService.create(requestDTO));
        Assertions.assertNotNull(customer);

        assertFields(customer, name, email, phone);
    }

    @Test
    void testBaseCreation() {
        // Base method - no parameters
        Customer customer = createCustomer();
        assertHasFields(customer);

        // Complete method
        String name = "testBaseCreation";
        String email = "testBaseCreation@gmail.com";
        String phone = getRandomNumber();
        customer = createCustomer(name, email, phone);
        assertFields(customer, name, email, phone);
    }

    @Test
    void testFindById() {
        Customer customer = createCustomer();
        Assertions.assertNotNull(customerService.findById(customer.getId()));
    }

    @Test
    void testFindByEmail() {
        Customer customer = createCustomer();
        Assertions.assertNotNull(customerService.findByEmail(customer.getEmail()));
    }

    @Test
    void testFindByPhone() {
        Customer customer = createCustomer();
        Assertions.assertNotNull(customerService.findByPhone(customer.getPhone()));
    }

    @Test
    void testFindAll() {
        Assertions.assertTrue(customerService.findAll().isEmpty());
        Integer expectedSize = 10;

        for (int i = 0; i < expectedSize; i++) {
            createCustomer();
        }
        List<CustomerDTO> customers = customerService.findAll();
        Assertions.assertEquals(expectedSize, customers.size());
    }

    @Test
    void testNotFound() {
        Assertions.assertThrows(HCNotFoundException.class, () -> customerService.findById(1L));
    }

    private void assertHasFields(Customer customer) {
        Assertions.assertNotNull(customer.getId(), "Doesn't have id");
        Assertions.assertNotNull(customer.getName(), "Doesn't have name");
        Assertions.assertNotNull(customer.getEmail(), "Doesn't have email");
        Assertions.assertNotNull(customer.getPhone(), "Doesn't have phone");
    }

    private void assertFields(Customer customer, String name, String email, String phone) {
        Assertions.assertNotNull(customer.getId(), "Doesn't have id");
        Assertions.assertEquals(name, customer.getName(), "Name doesn't match");
        Assertions.assertEquals(email, customer.getEmail(), "Email doesn't match");
        Assertions.assertEquals(phone, customer.getPhone(), "Phone doesn't match");
    }
}
