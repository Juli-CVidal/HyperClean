package com.jcv.hyperclean.service;

import com.jcv.hyperclean.enums.AppointmentStatus;
import com.jcv.hyperclean.enums.PaymentType;
import com.jcv.hyperclean.enums.ServiceType;
import com.jcv.hyperclean.enums.VehicleType;
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

    protected Customer createCustomer(String name, String email, String phone) {
        Customer customer = Customer.builder().name(name).email(email).phone(phone).build();
        return customerService.save(customer);
    }

    protected Customer createCustomer() {
        return createCustomer("Lionel Messi", "lioMessi@example.com", "2612222222");
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

    protected Payment createPayment(Double amount, LocalDateTime date, Appointment appointment, PaymentType type) {
        Payment payment = Payment.builder().amount(amount).paymentDate(date).appointment(appointment).type(type).build();
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

    protected Payment createPayment(Vehicle vehicle) {
        Appointment appointment = createAppointment(vehicle);
        return createPayment(LocalDateTime.now(), appointment, PaymentType.CASH);
    }

    protected Payment createPayment(Customer customer) {
        Vehicle vehicle = createVehicle(customer);
        return createPayment(vehicle);
    }

    protected Payment createPayment() {
        Customer customer = createCustomer();
        return createPayment(customer);
    }
}