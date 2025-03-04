package com.jcv.hyperclean.service;

import com.jcv.hyperclean.model.Appointment;
import com.jcv.hyperclean.model.Customer;
import com.jcv.hyperclean.model.Payment;
import com.jcv.hyperclean.model.Vehicle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Rollback
@Transactional
@SpringBootTest
public abstract class BaseServiceTest {

    @Autowired
    protected PaymentService paymentService;

    @Autowired
    protected AppointmentService appointmentService;

    @Autowired
    protected VehicleService vehicleService;

    @Autowired
    protected CustomerService customerService;

    protected Customer createCustomer() {
        Customer customer = new Customer();
        customer.setName("John Doe");
        customer.setEmail("johndoe@example.com");
        return customerService.save(customer);
    }

    protected Vehicle createVehicle(Customer customer) {
        Vehicle vehicle = new Vehicle();
        vehicle.setModel("Toyota");
        vehicle.setCustomer(customer);
        return vehicleService.save(vehicle);
    }

    protected Appointment createAppointment(Vehicle vehicle) {
        Appointment appointment = new Appointment();
        appointment.setAppointmentDate(LocalDateTime.now());
        appointment.setVehicle(vehicle);
        return appointmentService.save(appointment);
    }

    protected Payment createPayment(Appointment appointment) {
        Payment payment = new Payment();
        payment.setAmount(100.0);
        payment.setAppointment(appointment);
        return paymentService.save(payment);
    }
}