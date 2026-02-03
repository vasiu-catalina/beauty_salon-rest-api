package com.vasiu_catalina.beauty_salon.entity;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.function.Supplier;

import org.junit.jupiter.api.Test;

class EntityLifecycleTest {

    @Test
    void employeeLifecycleUpdatesTimestamps() {
        Employee employee = new Employee();
        assertLifecycle(employee::getCreatedAt, employee::getUpdatedAt, employee::onCreate, employee::onUpdate);
    }

    @Test
    void clientLifecycleUpdatesTimestamps() {
        Client client = new Client();
        assertLifecycle(client::getCreatedAt, client::getUpdatedAt, client::onCreate, client::onUpdate);
    }

    @Test
    void productLifecycleUpdatesTimestamps() {
        Product product = new Product();
        assertLifecycle(product::getCreatedAt, product::getUpdatedAt, product::onCreate, product::onUpdate);
    }

    @Test
    void serviceLifecycleUpdatesTimestamps() {
        Service service = new Service();
        assertLifecycle(service::getCreatedAt, service::getUpdatedAt, service::onCreate, service::onUpdate);
    }

    @Test
    void reviewLifecycleUpdatesTimestamps() {
        Review review = new Review();
        assertLifecycle(review::getCreatedAt, review::getUpdatedAt, review::onCreate, review::onUpdate);
    }

    @Test
    void appointmentLifecycleUpdatesTimestamps() {
        Appointment appointment = new Appointment();
        assertLifecycle(appointment::getCreatedAt, appointment::getUpdatedAt, appointment::onCreate,
                appointment::onUpdate);
    }

    private void assertLifecycle(Supplier<LocalDateTime> createdSupplier,
            Supplier<LocalDateTime> updatedSupplier,
            Runnable onCreate,
            Runnable onUpdate) {
        onCreate.run();
        LocalDateTime createdAt = createdSupplier.get();
        LocalDateTime updatedAt = updatedSupplier.get();
        assertNotNull(createdAt);
        assertNotNull(updatedAt);

        onUpdate.run();
        LocalDateTime updatedAtAfter = updatedSupplier.get();
        assertNotNull(updatedAtAfter);
        assertTrue(!updatedAtAfter.isBefore(createdAt));
    }
}
