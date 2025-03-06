package com.jcv.hyperclean.service;

import com.jcv.hyperclean.dto.request.AppointmentRequestDTO;
import com.jcv.hyperclean.enums.AppointmentStatus;
import com.jcv.hyperclean.enums.ServiceType;
import com.jcv.hyperclean.exception.HCNotFoundException;
import com.jcv.hyperclean.exception.HCValidationFailedException;
import com.jcv.hyperclean.model.Appointment;
import com.jcv.hyperclean.model.Customer;
import com.jcv.hyperclean.model.Vehicle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;

import static com.jcv.hyperclean.util.ListUtils.mapList;
import static com.jcv.hyperclean.util.SetUtils.listToSet;

@SpringBootTest
class AppointmentServiceTestCase extends BaseServiceTest {
    private Vehicle vehicle;

    @BeforeEach
    void setUp() {
        vehicle = createVehicle();
    }

    @Test
    void testCreateAppointment() {
        LocalDateTime date = LocalDateTime.now();
        AppointmentStatus status = AppointmentStatus.PENDING;
        ServiceType type = ServiceType.COMPLETE;
        Long vehicleId = vehicle.getId();

        AppointmentRequestDTO requestDTO = new AppointmentRequestDTO(date, status, type, vehicleId);
        Appointment appointment = Assertions.assertDoesNotThrow(() -> appointmentService.create(requestDTO));
        Assertions.assertNotNull(appointment);
        assertFields(appointment, date, status, type, vehicleId);
    }

    @Test
    void testBaseCreation() {
        // Base method - no parameters
        Appointment appointment = createAppointment();
        assertHasFields(appointment);

        // Only vehicle
        appointment = createAppointment(vehicle);
        Assertions.assertEquals(vehicle.getId(), appointment.getVehicle().getId());

        // Only customer
        Customer customer = vehicle.getCustomer();
        appointment = createAppointment(customer);
        Assertions.assertEquals(customer.getId(), appointment.getVehicle().getCustomer().getId());

        // Complete method
        LocalDateTime date = LocalDateTime.now();
        AppointmentStatus status = AppointmentStatus.PENDING;
        ServiceType type = ServiceType.COMPLETE;
        vehicle = createVehicle(customer);
        appointment = createAppointment(date, status, type, vehicle);
        assertFields(appointment, date, status, type, vehicle.getId());
    }

    @Test
    void testFindById() {
        Appointment appointment = createAppointment();
        Assertions.assertNotNull(appointmentService.findById(appointment.getId()));
    }

    @Test
    void testNotFoundById() {
        Assertions.assertThrows(HCNotFoundException.class, () -> appointmentService.findById(1L));
    }

    @Test
    void testFindByVehicleId() {
        LocalDateTime now = LocalDateTime.now();
        List<Appointment> appointments = List.of(
                createAppointment(vehicle, now), createAppointment(vehicle, now.plusMinutes(1)), createAppointment(vehicle, now.plusMinutes(2)));

        List<Appointment> foundAppointments = appointmentService.findByVehicleId(vehicle.getId());
        Assertions.assertEquals(appointments.size(), foundAppointments.size());

        List<Long> appointmentIds = mapList(appointments, Appointment::getId);
        List<Long> foundIds = mapList(foundAppointments, Appointment::getId);
        Assertions.assertEquals(appointmentIds, foundIds);

        Set<Vehicle> vehicles = listToSet(foundAppointments, Appointment::getVehicle);
        Assertions.assertEquals(1, vehicles.size());
        Assertions.assertTrue(vehicles.contains(vehicle));
    }

    @Test
    void testMarkAsInProgress() throws HCValidationFailedException {
        Appointment appointment = createAppointment();
        Assertions.assertTrue(appointment.isPending());
        appointmentService.markAsInProgress(appointment.getId());

        Appointment updatedAppointment = appointmentService.findById(appointment.getId());
        Assertions.assertTrue(updatedAppointment.isInProgress());

        // Shouldn't allow updating the state if it's different from PENDING
        Assertions.assertThrows(HCValidationFailedException.class, () -> appointmentService.markAsInProgress(updatedAppointment.getId()));
    }

    @Test
    void testMarkAsFinished() throws HCValidationFailedException {
        Appointment appointment = createAppointment();

        // It shouldn't work if the appointment is not in progress
        Assertions.assertThrows(HCValidationFailedException.class, () -> appointmentService.markAsFinished(appointment.getId()));
        appointmentService.markAsInProgress(appointment.getId());

        // The appointment hasn't finished due to its time to completion (method appointment.hasFinished())
        Assertions.assertThrows(HCValidationFailedException.class, () -> appointmentService.markAsFinished(appointment.getId()));

        // Forcing it to update by changing its date
        appointment.setAppointmentDate(appointment.getAppointmentDate().minusDays(1));
        appointmentService.save(appointment);
        appointmentService.markAsFinished(appointment.getId());

        Appointment updatedAppointment = appointmentService.findById(appointment.getId());
        Assertions.assertTrue(updatedAppointment.wasMarkedAsFinished());
    }

    @Test
    void testMarkAsPaid() throws HCValidationFailedException {
        Appointment appointment = createAppointment(LocalDateTime.now().minusDays(1));
        // It should work only if the appointment has finished
        Assertions.assertThrows(HCValidationFailedException.class, () -> appointmentService.markAsPaid(appointment));

        appointmentService.markAsInProgress(appointment.getId());
        appointmentService.markAsFinished(appointment.getId());

        appointmentService.markAsPaid(appointment);
        Appointment updatedAppointment = appointmentService.findById(appointment.getId());
        Assertions.assertTrue(updatedAppointment.wasPaid());

        // It should fail if you try to re-pay for the appointment
        Assertions.assertThrows(HCValidationFailedException.class, () -> appointmentService.markAsPaid(appointment));
    }

    private void assertFields(Appointment appointment, LocalDateTime date, AppointmentStatus status, ServiceType type, Long vehicleId) {
        Assertions.assertNotNull(appointment.getId(), "Doesn't have id");
        Assertions.assertEquals(
                date.truncatedTo(ChronoUnit.SECONDS),
                appointment.getAppointmentDate().truncatedTo(ChronoUnit.SECONDS),
                "Appointment date doesn't match"
        );
        Assertions.assertEquals(status, appointment.getStatus(), "Appointment status doesn't match");
        Assertions.assertEquals(type, appointment.getType(), "Appointment type doesn't match");
        Assertions.assertEquals(vehicleId, appointment.getVehicle().getId(), "Vehicle doesn't match");
    }

    private void assertHasFields(Appointment appointment) {
        Assertions.assertNotNull(appointment.getId(), "Doesn't have id");
        Assertions.assertNotNull(appointment.getAppointmentDate(), "Doesn't have appointmentDate");
        Assertions.assertNotNull(appointment.getStatus(), "Doesn't have status");
        Assertions.assertNotNull(appointment.getType(), "Doesn't have type");
        Assertions.assertNotNull(appointment.getVehicle(), "Doesn't have vehicle");
    }
}