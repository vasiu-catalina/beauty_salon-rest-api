package com.vasiu_catalina.beauty_salon.service.impl;

import java.util.Set;
import java.util.List;
import java.util.Optional;

import com.vasiu_catalina.beauty_salon.entity.Employee;
import com.vasiu_catalina.beauty_salon.entity.Product;
import com.vasiu_catalina.beauty_salon.entity.Service;
import com.vasiu_catalina.beauty_salon.exception.product.ProductNotFoundException;
import com.vasiu_catalina.beauty_salon.exception.service.ServiceAlreadyExistsException;
import com.vasiu_catalina.beauty_salon.exception.service.ServiceNotFoundException;
import com.vasiu_catalina.beauty_salon.repository.ProductRepository;
import com.vasiu_catalina.beauty_salon.repository.ServiceRepository;
import com.vasiu_catalina.beauty_salon.service.ServiceService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@org.springframework.stereotype.Service
public class ServiceServiceImpl implements ServiceService {

    private ServiceRepository serviceRepository;
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
        Service existingService = this.getService(id);

        if (!existingService.getName().equals(service.getName())) {
            if (existsServiceByName(service.getName()))
                throw new ServiceAlreadyExistsException(service.getName());
            existingService.setName(service.getName());
        }

        existingService.setDescription(service.getDescription());
        existingService.setDuration(service.getDuration());
        existingService.setPrice(service.getPrice());

        return serviceRepository.save(service);
    }

    @Override
    public void deleteService(Long id) {
        serviceRepository.deleteById(id);
    }

    @Override
    public Set<Employee> getEmployeesByService(Long serviceId) {
        Service service = this.getService(serviceId);
        return service.getEmployees();
    }

    @Override
    public Set<Product> getProductsByService(Long serviceId) {
        Service service = this.getService(serviceId);
        return service.getProducts();
    }

    @Override
    public Service addProductToService(Long productId, Long serviceId) {
        Product product = this.getProduct(productId);
        Service service = this.getService(serviceId);

        service.getProducts().add(product);
        return serviceRepository.save(service);
    }

    @Override
    public void deleteProductFromService(Long productId, Long serviceId) {
        Product product = this.getProduct(productId);
        Service service = this.getService(serviceId);

        service.getProducts().remove(product);
        serviceRepository.save(service);
    }

    private boolean existsServiceByName(String name) {
        Optional<Service> service = serviceRepository.findByName(name);
        return service.isPresent();
    }

    private Product getProduct(Long productId) {
        return productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException(productId));
    }

    static Service unwrappedService(Optional<Service> service, Long id) {
         if (service.isPresent()) return service.get();
         throw new ServiceNotFoundException(id);
    }

}
