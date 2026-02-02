package com.vasiu_catalina.beauty_salon;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.vasiu_catalina.beauty_salon.entity.Employee;
import com.vasiu_catalina.beauty_salon.entity.Product;
import com.vasiu_catalina.beauty_salon.entity.Service;
import com.vasiu_catalina.beauty_salon.exception.service.ServiceAlreadyExistsException;
import com.vasiu_catalina.beauty_salon.exception.service.ServiceNotFoundException;
import com.vasiu_catalina.beauty_salon.repository.ProductRepository;
import com.vasiu_catalina.beauty_salon.repository.ServiceRepository;
import com.vasiu_catalina.beauty_salon.service.impl.ServiceServiceImpl;

@ExtendWith(MockitoExtension.class)
public class ServiceServiceTest {

    @Mock
    private ServiceRepository serviceRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ServiceServiceImpl serviceService;

    @Test
    public void testGetAllServices() {

        when(serviceRepository.findAll()).thenReturn(List.of(createOtherService(), createService()));

        List<Service> result = serviceService.getAllServices();

        assertEquals(2, result.size());
        assertEquals("Gene", result.get(0).getName());
        assertEquals(Integer.valueOf(40), result.get(1).getDuration());
        verify(serviceRepository, times(1)).findAll();
    }

    @Test
    public void testCreateService() {

        Service service = createService();
        when(serviceRepository.save(any(Service.class))).thenReturn(service);

        Service result = serviceService.createService(service);

        this.assertFullResult(result);
        verify(serviceRepository, times(1)).save(service);
    }

    @Test
    public void testNameAlreadyExistsForClientByEmployee() {

        Service service = createService();
        when(serviceRepository.findByName(service.getName())).thenReturn(Optional.of(service));

        ServiceAlreadyExistsException exception = assertThrows(ServiceAlreadyExistsException.class, () -> {
            serviceService.createService(service);
        });

        ServiceAlreadyExistsException expectedException = new ServiceAlreadyExistsException(service.getName());
        assertEquals(expectedException.getMessage(), exception.getMessage());
        verify(serviceRepository, times(1)).findByName(service.getName());
        verify(serviceRepository, never()).save(service);
    }

    @Test
    public void testGetService() {

        Long serviceId = 1L;
        Service service = createService();
        this.whenFindById(serviceId, service);

        Service result = serviceService.getService(serviceId);

        this.assertFullResult(result);
        verify(serviceRepository, times(1)).findById(serviceId);
    }

    @Test
    public void testServiceNotFound() {

        Long serviceId = 1L;
        ServiceNotFoundException exception = assertThrows(ServiceNotFoundException.class, () -> {
            serviceService.getService(serviceId);
        });

        ServiceNotFoundException expectedException = new ServiceNotFoundException(serviceId);

        assertEquals(expectedException.getMessage(), exception.getMessage());
        verify(serviceRepository, times(1)).findById(serviceId);
    }

    @Test
    public void testUpdateService() {

        Long serviceId = 1L;
        Service service = createOtherService();
        Service updatedService = createService();
        this.whenFindById(serviceId, service);
        when(serviceRepository.save(any(Service.class))).thenReturn(updatedService);

        Service result = serviceService.updateService(serviceId, updatedService);

        assertFullResult(result);
        verify(serviceRepository, times(1)).findById(serviceId);
        verify(serviceRepository, times(1)).save(service);
    }

    @Test
    public void testUpdateServiceNotFound() {

        Long serviceId = 1L;
        Service updatedService = createService();

        ServiceNotFoundException exception = assertThrows(ServiceNotFoundException.class, () -> {
            serviceService.updateService(serviceId, updatedService);
        });

        ServiceNotFoundException expectedException = new ServiceNotFoundException(serviceId);
        assertEquals(expectedException.getMessage(), exception.getMessage());
        verify(serviceRepository, times(1)).findById(serviceId);
        verify(serviceRepository, never()).save(any(Service.class));
    }

    @Test
    public void testDeleteService() {
        Long serviceId = 1L;
        Service service = createService();
        this.whenFindById(serviceId, service);

        serviceService.deleteService(serviceId);

        verify(serviceRepository, times(1)).findById(serviceId);
        verify(serviceRepository, times(1)).deleteById(serviceId);
    }

    @Test
    public void testdeleteServiceNotFound() {
        Long serviceId = 1L;

        ServiceNotFoundException exception = assertThrows(ServiceNotFoundException.class, () -> {
            serviceService.deleteService(serviceId);
        });

        ServiceNotFoundException expectedException = new ServiceNotFoundException(serviceId);
        assertEquals(expectedException.getMessage(), exception.getMessage());
        verify(serviceRepository, times(1)).findById(serviceId);
        verify(serviceRepository, never()).deleteById(serviceId);
    }

    @Test
    public void testGetEmplyoeesByService() {

        Set<Employee> employees = new HashSet<>(List.of(EmployeeServiceTest.createEmployee()));
        Long serviceId = 1L;
        Service service = createService();
        service.setEmployees(employees);

        this.whenFindById(serviceId, service);

        Set<Employee> result = serviceService.getEmployeesByService(serviceId);

        assertEquals(employees, result);
        verify(serviceRepository, times(1)).findById(serviceId);
    }

    @Test
    public void testGetProductsByService() {

        Set<Product> products = new HashSet<>(List.of(ProductServiceTest.createProduct()));
        Long serviceId = 1L;
        Service service = createService();
        service.setProducts(products);

        this.whenFindById(serviceId, service);

        Set<Product> result = serviceService.getProductsByService(serviceId);

        assertEquals(products, result);
        verify(serviceRepository, times(1)).findById(serviceId);
    }

    @Test
    public void testAddProductToService() {

        Long productId = 1L, serviceId = 1L;
        Product product = ProductServiceTest.createProduct();
        Service service = createService();

        whenFindById(serviceId, service);
        when(productRepository.findById(serviceId)).thenReturn(Optional.of(product));

        Product result = serviceService.addProductToService(productId, serviceId);

        assertEquals(product, result);
        verify(serviceRepository, times(1)).save(service);

    }

    @Test
    public void testDeleteProductFromService() {

        Long productId = 1L, serviceId = 1L;
        Product product = ProductServiceTest.createProduct();
        Service service = createService();
        service.getProducts().add(product);

        whenFindById(serviceId, service);
        when(productRepository.findById(serviceId)).thenReturn(Optional.of(product));

        serviceService.deleteProductFromService(productId, serviceId);

        assertEquals(new HashSet<>(), service.getProducts());
        verify(serviceRepository, times(1)).save(service);

    }

    private void whenFindById(Long serviceId, Service service) {
        when(serviceRepository.findById(serviceId)).thenReturn(Optional.of(service));
    }

    private void assertFullResult(Service result) {
        assertNotNull(result);
        assertEquals("Unghii", result.getName());
        assertEquals("Serviciu bun", result.getDescription());
        assertEquals(new BigDecimal(80), result.getPrice());
        assertEquals(Integer.valueOf(40), result.getDuration());
    }

  public static void assertServiceNotFoundException(ServiceNotFoundException exception, Long serviceId) {
        ServiceNotFoundException expected = new ServiceNotFoundException(serviceId);
        assertEquals(expected.getMessage(), exception.getMessage());
    }

    public static Service createService() {
        return new Service("Unghii", "Serviciu bun", new BigDecimal(80), Integer.valueOf(40));
    }

    public static Service createOtherService() {
        return new Service("Gene", "Serviciu recomandat", new BigDecimal(100), Integer.valueOf(60));
    }

}
