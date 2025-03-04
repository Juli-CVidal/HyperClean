package com.jcv.hyperclean.service;

import com.jcv.hyperclean.cache.RedisItemCache;
import com.jcv.hyperclean.cache.RedisListCache;
import com.jcv.hyperclean.dto.request.VehicleRequestDTO;
import com.jcv.hyperclean.model.Customer;
import com.jcv.hyperclean.model.Vehicle;
import com.jcv.hyperclean.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class VehicleService extends CacheableService<Vehicle> {
    private final VehicleRepository vehicleRepository;
    private final CustomerService customerService;

    @Autowired
    public VehicleService(VehicleRepository vehicleRepository, CustomerService customerService, RedisItemCache<Vehicle> vehicleCache, RedisListCache<Vehicle> vehicleListCache) {
        super(vehicleCache, vehicleListCache);
        this.vehicleRepository = vehicleRepository;
        this.customerService = customerService;
    }

    @Transactional
    public Vehicle save(Vehicle vehicle) {
        invalidateListCache(String.valueOf(vehicle.getCustomer().getId()));
        return vehicleRepository.save(vehicle);
    }

    @Transactional
    public Vehicle create(VehicleRequestDTO requestDTO) {
        Customer customer = customerService.findById(requestDTO.getCustomerId());

        Vehicle vehicle = Vehicle.of(requestDTO);
        vehicle.setCustomer(customer);

        vehicle = save(vehicle);
        putInCache(String.valueOf(vehicle.getId()), vehicle);
        invalidateListCache(String.valueOf(customer.getId()));
        return vehicle;
    }

    @Transactional(readOnly = true)
    public Vehicle findById(Long id) {
        return findBy(id, vehicleRepository::findById);
    }

    @Transactional(readOnly = true)
    public List<Vehicle> findByCustomerId(Long customerId) {
        return safeFindListBy(customerId, vehicleRepository::findByCustomerId);
    }

    @Transactional
    public Vehicle assignToCustomer(Long id, Long customerId) {
        Vehicle vehicle = findById(id);
        Long oldCustomerId = vehicle.getCustomer().getId();

        // No changes needed
        if (Objects.equals(vehicle.getCustomer().getId(), customerId)) {
            return vehicle;
        }

        Customer customer = customerService.findById(customerId);
        vehicle.setCustomer(customer);

        invalidateCache(String.valueOf(vehicle.getId()));
        invalidateListCache(String.valueOf(customerId));
        invalidateListCache(String.valueOf(oldCustomerId));
        return save(vehicle);
    }
}
