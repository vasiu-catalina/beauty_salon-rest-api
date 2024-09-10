package com.vasiu_catalina.beauty_salon.service;

import java.util.Set;
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

    Set<Employee> getEmployeesByService(Long serviceId);

    Set<Product> getProductsByService(Long serviceId);

    Service addProductToService(Long productId, Long serviceId);

    void deleteProductFromService(Long productId, Long serviceId);
}
