package com.jcv.hyperclean.service;

import com.jcv.hyperclean.cache.RedisItemCache;
import com.jcv.hyperclean.cache.RedisListCache;
import com.jcv.hyperclean.dto.VehicleDTO;
import com.jcv.hyperclean.dto.request.VehicleRequestDTO;
import com.jcv.hyperclean.model.Customer;
import com.jcv.hyperclean.model.Vehicle;
import com.jcv.hyperclean.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.jcv.hyperclean.util.ListUtils.mapList;

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
    public VehicleDTO create(VehicleRequestDTO requestDTO) {
        Customer customer = customerService.findById(requestDTO.getCustomerId());

        Vehicle vehicle = Vehicle.of(requestDTO);
        vehicle.setCustomer(customer);

        vehicle = vehicleRepository.save(vehicle);
        putInCache(String.valueOf(vehicle.getId()), vehicle);
        return VehicleDTO.from(vehicle);
    }

    @Transactional(readOnly = true)
    public Vehicle findById(Long id) {
        String cacheKey = String.valueOf(id);
        Optional<Vehicle> cachedVehicle = getCached(cacheKey);
        if (cachedVehicle.isPresent()) {
            return cachedVehicle.get();
        }

        Vehicle vehicle = vehicleRepository.getReferenceById(id);
        putInCache(cacheKey, vehicle);
        return vehicle;
    }

    @Transactional(readOnly = true)
    public List<VehicleDTO> findByCustomerId(Long customerId) {
        String cacheKey = String.valueOf(customerId);
        Optional<List<Vehicle>> cachedVehicles = getCachedList(cacheKey);
        if (cachedVehicles.isPresent()) {
            return mapList(cachedVehicles.get(), VehicleDTO::from);
        }

        List<Vehicle> vehicles = vehicleRepository.findByCustomerId(customerId);
        setListCache(cacheKey, vehicles);
        return mapList(vehicles, VehicleDTO::from);
    }

    @Transactional
    public VehicleDTO assignToCustomer(Long id, Long customerId) {
        Vehicle vehicle = findById(id);

        // No changes needed
        if (Objects.equals(vehicle.getCustomer().getId(), customerId)) {
            return VehicleDTO.from(vehicle);
        }

        Customer customer = customerService.findById(customerId);
        vehicle.setCustomer(customer);
        invalidateCache(String.valueOf(vehicle.getId()));
        return VehicleDTO.from(vehicleRepository.save(vehicle));
    }
}
