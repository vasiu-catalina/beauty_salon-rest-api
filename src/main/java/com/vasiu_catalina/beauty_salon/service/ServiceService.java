package com.vasiu_catalina.beauty_salon.service;

import java.util.Set;

import com.vasiu_catalina.beauty_salon.entity.Product;
import com.vasiu_catalina.beauty_salon.entity.Service;

public interface ServiceService {
    
    Set<Service> getAllServices();

    Service createService(Service service);

    Service getService(Long id);

    Service updateService(Long id, Service service);

    void deleteService(Long id);

    Set<Service> getServicesByEmployee(Long employeeId);

    Set<Product> getProductsBySalon(Long salonId);
}
