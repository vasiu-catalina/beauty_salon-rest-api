package com.vasiu_catalina.beauty_salon.service;

import java.util.List;

import com.vasiu_catalina.beauty_salon.entity.Employee;
import com.vasiu_catalina.beauty_salon.entity.Product;
import com.vasiu_catalina.beauty_salon.entity.Service;

public interface ServiceService {
    
    List<Service> getAllServices();

    Service createService(Service service);

    Service getService(Long id);

    Service updateService(Long id, Service service);

    void deleteService(Long id);

    List<Employee> getEmployeesByService(Long serviceId);

    List<Product> getProductsByService(Long serviceId);
}
