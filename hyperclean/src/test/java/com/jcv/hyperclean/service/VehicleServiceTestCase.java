package com.jcv.hyperclean.service;

import com.jcv.hyperclean.dto.request.VehicleRequestDTO;
import com.jcv.hyperclean.enums.VehicleType;
import com.jcv.hyperclean.exception.HCNotFoundException;
import com.jcv.hyperclean.model.Customer;
import com.jcv.hyperclean.model.Vehicle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Set;

import static com.jcv.hyperclean.util.ListUtils.mapList;
import static com.jcv.hyperclean.util.SetUtils.listToSet;

@SpringBootTest
class VehicleServiceTestCase extends BaseServiceTest {
    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = createCustomer();
    }

    @Test
    void testCreateVehicle() {
        String model = "Fitito";
        String licensePlate = "AAA000";
        VehicleType vehicleType = VehicleType.SUPERCAR;
        Long customerId = customer.getId();

        VehicleRequestDTO requestDTO = new VehicleRequestDTO(model, licensePlate, customerId, vehicleType);
        Vehicle vehicle = Assertions.assertDoesNotThrow(() -> vehicleService.create(requestDTO));
        Assertions.assertNotNull(vehicle);
        assertFields(vehicle, model, licensePlate, customerId, vehicleType);
    }

    @Test
    void testBaseCreation() {
        // Base method - no parameters
        Vehicle vehicle = createVehicle();
        assertHasFields(vehicle);

        // Only customer
        vehicle = createVehicle(customer);
        Assertions.assertEquals(vehicle.getCustomer(), customer);

        // Complete method
        String model = "Fitito";
        String licensePlate = "AAA000";
        VehicleType vehicleType = VehicleType.SUPERCAR;
        vehicle = createVehicle(model, licensePlate, customer, vehicleType);
        assertFields(vehicle, model, licensePlate, customer.getId(), vehicleType);
    }

    @Test
    void testFindById() {
        Vehicle vehicle = createVehicle();
        Assertions.assertNotNull(vehicleService.findById(vehicle.getId()));
    }

    @Test
    void assertNotFoundById() {
        Assertions.assertThrows(HCNotFoundException.class, () -> vehicleService.findById(1L));
    }

    @Test
    void findByCustomerId() {
        List<Vehicle> vehicles = List.of(createVehicle(customer), createVehicle(customer), createVehicle(customer));

        List<Vehicle> foundVehicles = vehicleService.findByCustomerId(customer.getId());
        Assertions.assertEquals(vehicles.size(), foundVehicles.size());

        List<Long> vehicleIds = mapList(vehicles, Vehicle::getId);
        List<Long> foundIds = mapList(foundVehicles, Vehicle::getId);
        Assertions.assertEquals(vehicleIds, foundIds);

        Set<Customer> customers = listToSet(foundVehicles, Vehicle::getCustomer);
        Assertions.assertEquals(1, customers.size());
        Assertions.assertTrue(customers.contains(customer));

        // A new car
        Vehicle newVehicle = createVehicle(customer);
        foundVehicles = vehicleService.findByCustomerId(customer.getId());
        Assertions.assertEquals(newVehicle.getId(), foundVehicles.getLast().getId());
    }

    @Test
    void assertNotFoundByCustomerId() {
        List<Vehicle> foundVehicles = vehicleService.findByCustomerId(customer.getId());
        Assertions.assertTrue(foundVehicles.isEmpty());
    }

    @Test
    void testAssignToCustomer() {
        Vehicle vehicle = createVehicle(customer);

        // No changes
        vehicle = vehicleService.assignToCustomer(vehicle.getId(), customer.getId());
        List<Vehicle> foundVehicles = vehicleService.findByCustomerId(customer.getId());
        Assertions.assertEquals(1, foundVehicles.size());

        // New customer
        Customer newCustomer = createCustomer();
        vehicle = vehicleService.assignToCustomer(vehicle.getId(), newCustomer.getId());
        Assertions.assertEquals(newCustomer.getId(), vehicle.getCustomer().getId());

        foundVehicles = vehicleService.findByCustomerId(customer.getId());
        Assertions.assertTrue(foundVehicles.isEmpty());

        foundVehicles = vehicleService.findByCustomerId(newCustomer.getId());
        Assertions.assertEquals(1, foundVehicles.size());
    }

    private void assertFields(Vehicle vehicle, String model, String licensePlate, Long customerId, VehicleType vehicleType) {
        Assertions.assertNotNull(vehicle.getId(), "Doesn't have id");
        Assertions.assertEquals(model, vehicle.getModel(), "Model doesn't match");
        Assertions.assertEquals(licensePlate, vehicle.getLicensePlate(), "License plate doesn't match");
        Assertions.assertNotNull(vehicle.getCustomer(), "Doesn't have customer");
        Assertions.assertEquals(customerId, vehicle.getCustomer().getId(), "Customer doesn't match");
        Assertions.assertEquals(vehicleType, vehicle.getType(), "Type doesn't match");
    }

    private void assertHasFields(Vehicle vehicle) {
        Assertions.assertNotNull(vehicle.getId(), "Doesn't have id");
        Assertions.assertNotNull(vehicle.getModel(), "Doesn't have model");
        Assertions.assertNotNull(vehicle.getLicensePlate(), "Doesn't have licensePlate");
        Assertions.assertNotNull(vehicle.getCustomer(), "Doesn't have customer");
        Assertions.assertNotNull(vehicle.getType(), "Doesn't have type");
    }
}
