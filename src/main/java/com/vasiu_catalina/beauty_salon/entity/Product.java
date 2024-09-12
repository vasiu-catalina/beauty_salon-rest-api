package com.vasiu_catalina.beauty_salon.entity;

import java.util.Set;

import org.springframework.format.annotation.NumberFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "products")
@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @NotBlank(message = "Name is required.")
    @Column(nullable = false, unique = true)
    private String name;

    @Min(value = 0, message = "Quantity must be at least 0.")
    private Integer quantity = 0;

    @NonNull
    @NotNull(message = "Price is required.")
    @Digits(integer = 10, fraction = 2, message = "Price has invalid currency format.")
    @DecimalMin(value = "0.00", inclusive = true, message = "Price must be greater or equal than 0.")
    @NumberFormat(style = NumberFormat.Style.CURRENCY)
    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal price;

    
    @NonNull
    @NotNull(message = "Expiry date is required")
    @Future(message = "Expiry date cannot be in the past.")
    @Column(name = "expiry_date", nullable = false)
    private LocalDate expiryDate;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @JsonIgnore
    @ManyToMany(mappedBy = "products", cascade = CascadeType.REMOVE)
    private Set<Service> services;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
