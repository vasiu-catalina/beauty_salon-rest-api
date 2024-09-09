package com.vasiu_catalina.beauty_salon.service.impl;

import java.util.List;
import java.util.Optional;

import com.vasiu_catalina.beauty_salon.entity.Employee;
import com.vasiu_catalina.beauty_salon.entity.Product;
import com.vasiu_catalina.beauty_salon.entity.Service;
import com.vasiu_catalina.beauty_salon.exception.product.ProductNotFoundException;
import com.vasiu_catalina.beauty_salon.exception.service.ServiceAlreadyExistsException;
import com.vasiu_catalina.beauty_salon.exception.service.ServiceNotFoundException;
import com.vasiu_catalina.beauty_salon.repository.EmployeeRepository;
import com.vasiu_catalina.beauty_salon.repository.ProductRepository;
import com.vasiu_catalina.beauty_salon.repository.ServiceRepository;
import com.vasiu_catalina.beauty_salon.service.ServiceService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@org.springframework.stereotype.Service
public class ServiceServiceImpl implements ServiceService {

    private ServiceRepository serviceRepository;
    private EmployeeRepository employeeRepository;
    private ProductRepository productRepository;

    @Override
    public List<Service> getAllServices() {
        return (List<Service>) serviceRepository.findAll();
    }

    @Override
    public Service createService(Service service) {
        if (existsServiceByName(service.getName()))
            throw new ServiceAlreadyExistsException(service.getName());
        return serviceRepository.save(service);
    }

    @Override
    public Service getService(Long id) {
        return serviceRepository.findById(id).orElseThrow(() -> new ServiceNotFoundException(id));
    }

    @Override
    public Service updateService(Long id, Service service) {
        Service existingService = serviceRepository.findById(id).orElseThrow(() -> new ServiceNotFoundException(id));
        if (!existingService.getName().equals(service.getName())) {
            if (existsServiceByName(service.getName()))
                throw new ServiceAlreadyExistsException(service.getName());
            existingService.setName(service.getName());
        }
        existingService.setDescription(service.getDescription());
        existingService.setPrice(service.getPrice());
        existingService.setDuration(service.getDuration());
        return serviceRepository.save(service);
    }

    @Override
    public void deleteService(Long id) {
        serviceRepository.deleteById(id);
    }

    @Override
    public List<Employee> getEmployeesByService(Long serviceId) {
        return (List<Employee>) employeeRepository.findAllByServiceId(serviceId);
    }

    @Override
    public List<Product> getProductsByService(Long serviceId) {
        return (List<Product>) productRepository.findAllByServiceId(serviceId);
    }

    @Override
    public Product addProductToService(Long productId, Long serviceId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException(productId));
        Service service = serviceRepository.findById(serviceId).orElseThrow(() -> new ServiceNotFoundException(serviceId));
        service.getProducts().add(product);
        serviceRepository.save(service);
        return product;
    }

    @Override
    public void deleteProductFromService(Long productId, Long serviceId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException(productId));
        Service service = serviceRepository.findById(serviceId).orElseThrow(() -> new ServiceNotFoundException(serviceId));
        service.getProducts().remove(product);
        serviceRepository.save(service);
    }

    private boolean existsServiceByName(String name) {
        Optional<Service> service = serviceRepository.findByName(name);
        return service.isPresent();
    }

}
