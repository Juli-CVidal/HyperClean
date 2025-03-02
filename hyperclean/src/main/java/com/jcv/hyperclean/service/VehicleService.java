package com.jcv.hyperclean.service;

import com.jcv.hyperclean.dto.VehicleDTO;
import com.jcv.hyperclean.dto.request.VehicleRequestDTO;
import com.jcv.hyperclean.model.Customer;
import com.jcv.hyperclean.model.Vehicle;
import com.jcv.hyperclean.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.jcv.hyperclean.util.ListUtils.mapList;

@Service
public class VehicleService {
    private final VehicleRepository vehicleRepository;
    private final CustomerService customerService;

    @Autowired
    public VehicleService(VehicleRepository vehicleRepository, CustomerService customerService) {
        this.vehicleRepository = vehicleRepository;
        this.customerService = customerService;
    }

    @Transactional
    public VehicleDTO create(VehicleRequestDTO requestDTO) {
        Customer customer = customerService.findById(requestDTO.getCustomerId());

        Vehicle vehicle = Vehicle.of(requestDTO);
        vehicle.setCustomer(customer);
        return VehicleDTO.from(vehicleRepository.save(vehicle));
    }

    @Transactional(readOnly = true)
    public Vehicle findById(Long id) {
        return vehicleRepository.getReferenceById(id);
    }

    @Transactional(readOnly = true)
    public List<VehicleDTO> findByCustomerId(Long customerId) {
        List<Vehicle> vehicles = vehicleRepository.findByCustomerId(customerId);
        return mapList(vehicles, VehicleDTO::from);
    }
}
