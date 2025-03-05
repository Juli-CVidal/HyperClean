package com.jcv.hyperclean.service;

import com.jcv.hyperclean.enums.AppointmentStatus;
import com.jcv.hyperclean.enums.PaymentType;
import com.jcv.hyperclean.enums.ServiceType;
import com.jcv.hyperclean.enums.VehicleType;
import com.jcv.hyperclean.model.Appointment;
import com.jcv.hyperclean.model.Customer;
import com.jcv.hyperclean.model.Payment;
import com.jcv.hyperclean.model.Vehicle;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

@Rollback
@Transactional
@SpringBootTest
@ActiveProfiles("test")
public abstract class BaseServiceTest {

    @Autowired
    protected PaymentService paymentService;

    @Autowired
    protected AppointmentService appointmentService;

    @Autowired
    protected VehicleService vehicleService;

    @Autowired
    protected CustomerService customerService;

    @AfterEach
    void clearCaches() {
        appointmentService.flushCaches();
        customerService.flushCaches();
        paymentService.flushCaches();
        vehicleService.flushCaches();
    }

    protected Customer createCustomer(String name, String email, String phone) {
        Customer customer = Customer.builder().name(name).email(email).phone(phone).build();
        return customerService.save(customer);
    }

    protected Customer createCustomer() {
        String email = String.format("lioMessi+%s@gmail.com", UUID.randomUUID());
        String phoneNumber = getRandomNumber();
        return createCustomer("Lionel Messi", email, phoneNumber);
    }

    protected String getRandomNumber() {
        return "261" + String.format("%05d", new Random().nextInt(100000));
    }

    protected Vehicle createVehicle(String model, String licensePlate, Customer customer, VehicleType type) {
        Vehicle vehicle = Vehicle.builder().model(model).licensePlate(licensePlate).customer(customer).type(type).build();
        return vehicleService.save(vehicle);
    }

    protected Vehicle createVehicle(Customer customer) {
        return createVehicle("Fitito", "AAA000", customer, VehicleType.SUPERCAR);
    }

    protected Vehicle createVehicle() {
        Customer customer = createCustomer();
        return createVehicle(customer);
    }

    protected Appointment createAppointment(LocalDateTime date, AppointmentStatus status, ServiceType serviceType, Vehicle vehicle) {
        Appointment appointment = Appointment.builder().appointmentDate(date).status(status).type(serviceType).vehicle(vehicle).build();
        return appointmentService.save(appointment);
    }

    protected Appointment createAppointment(Vehicle vehicle, LocalDateTime date) {
        return createAppointment(date, AppointmentStatus.PENDING, ServiceType.COMPLETE, vehicle);
    }

    protected Appointment createAppointment(Vehicle vehicle) {
        return createAppointment(LocalDateTime.now(), AppointmentStatus.PENDING, ServiceType.COMPLETE, vehicle);
    }

    protected Appointment createAppointment(Customer customer) {
        Vehicle vehicle = createVehicle(customer);
        return createAppointment(LocalDateTime.now(), AppointmentStatus.PENDING, ServiceType.COMPLETE, vehicle);
    }

    protected Appointment createAppointment() {
        Vehicle vehicle = createVehicle();
        return createAppointment(LocalDateTime.now(), AppointmentStatus.PENDING, ServiceType.COMPLETE, vehicle);
    }

    protected Appointment createAppointment(LocalDateTime date) {
        Vehicle vehicle = createVehicle();
        return createAppointment(date, AppointmentStatus.PENDING, ServiceType.COMPLETE, vehicle);
    }

    protected Payment createPayment(Double amount, LocalDateTime date, Appointment appointment, PaymentType type) {
        Payment payment = Payment.builder().amount(amount).paymentDate(date).appointment(appointment).type(type).build();
        appointment.setStatus(AppointmentStatus.PAID);
        appointmentService.save(appointment);
        return paymentService.save(payment);
    }

    protected Payment createPayment(LocalDateTime date, Appointment appointment, PaymentType type) {
        Double amount = appointment.getCostOfCleaning();
        return createPayment(amount, date, appointment, type);
    }

    protected Payment createPayment(LocalDateTime date, Appointment appointment) {
        Double amount = appointment.getCostOfCleaning();
        return createPayment(amount, date, appointment, PaymentType.CASH);
    }

    protected Payment createPayment(Appointment appointment) {
        return createPayment(LocalDateTime.now(), appointment, PaymentType.CASH);
    }

    protected Payment createPayment(LocalDateTime date) {
        Customer customer = createCustomer();
        Vehicle vehicle = createVehicle(customer);
        Appointment appointment = createAppointment(vehicle);
        return createPayment(date, appointment);
    }

    protected Payment createPayment() {
        return createPayment(LocalDateTime.now());
    }
}