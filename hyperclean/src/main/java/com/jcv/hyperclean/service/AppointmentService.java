package com.jcv.hyperclean.service;

import com.jcv.hyperclean.cache.RedisItemCache;
import com.jcv.hyperclean.cache.RedisListCache;
import com.jcv.hyperclean.dto.AppointmentDTO;
import com.jcv.hyperclean.dto.request.AppointmentRequestDTO;
import com.jcv.hyperclean.enums.AppointmentStatus;
import com.jcv.hyperclean.model.Appointment;
import com.jcv.hyperclean.model.Vehicle;
import com.jcv.hyperclean.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.jcv.hyperclean.util.ListUtils.mapList;

@Service
public class AppointmentService extends CacheableService<Appointment> {
    private final AppointmentRepository appointmentRepository;
    private final VehicleService vehicleService;

    @Autowired
    public AppointmentService(AppointmentRepository appointmentRepository, VehicleService vehicleService, RedisItemCache<Appointment> cache, RedisListCache<Appointment> listCache) {
        super(cache, listCache);
        this.appointmentRepository = appointmentRepository;
        this.vehicleService = vehicleService;
    }

    @Transactional
    public Appointment save(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }

    @Transactional
    public Appointment create(AppointmentRequestDTO requestDTO) {
        Vehicle vehicle = vehicleService.findById(requestDTO.getVehicleId());
        Appointment appointment = Appointment.of(requestDTO);
        appointment.setVehicle(vehicle);

        if (appointment.getStatus() == null) {
            appointment.setStatus(AppointmentStatus.PENDING);
        }

        appointment = save(appointment);
        putInCache(String.valueOf(appointment.getId()), appointment);
        return appointment;
    }

    @Transactional(readOnly = true)
    public Appointment findById(Long id) {
        return findBy(id, appointmentRepository::findById);
    }

    @Transactional(readOnly = true)
    public List<AppointmentDTO> findByVehicleId(Long vehicleId) {
        String cacheKey = String.valueOf(vehicleId);
        Optional<List<Appointment>> cachedAppointments = getCachedList(cacheKey);
        if (cachedAppointments.isPresent()) {
            return mapList(cachedAppointments.get(), AppointmentDTO::from);
        }

        List<Appointment> appointments = appointmentRepository.findByVehicleId(vehicleId);
        setListCache(cacheKey, appointments);
        return mapList(appointments, AppointmentDTO::from);
    }

    @Transactional
    public AppointmentDTO markAsInProgress(Long id) {
        Appointment appointment = findById(id);
        if (!appointment.isPending()) {
            throw new IllegalArgumentException("Appointment is not in pending state");
        }

        return updateStatusAndSave(appointment, AppointmentStatus.IN_PROGRESS);
    }

    @Transactional
    public AppointmentDTO markAsFinished(Long id) {
        Appointment appointment = findById(id);
        if (!appointment.hasFinished()) {
            throw new IllegalStateException("Appointment has not finished");
        }

        return updateStatusAndSave(appointment, AppointmentStatus.FINISHED);
    }

    @Transactional
    public void markAsPaid(Appointment appointment) {
        validateApplicableForPayment(appointment);

        updateStatusAndSave(appointment, AppointmentStatus.PAID);
    }

    private AppointmentDTO updateStatusAndSave(Appointment appointment, AppointmentStatus status) {
        appointment.setStatus(status);
        appointmentRepository.save(appointment);
        invalidateCache(String.valueOf(appointment.getId()));
        return AppointmentDTO.from(appointment);
    }

    private void validateApplicableForPayment(Appointment appointment) {
        if (appointment.wasPaid()) {
            throw new IllegalArgumentException("Appointment already paid");
        }

        if (!appointment.isApplicableForPayment()) {
            throw new IllegalArgumentException("Appointment is not applicable for payment");
        }
    }
}
