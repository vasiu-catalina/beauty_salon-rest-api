package com.vasiu_catalina.beauty_salon.entity;

import java.util.HashSet;
import java.util.Set;

import org.springframework.format.annotation.NumberFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "services")
@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @NotBlank(message = "Name is required.")
    @Column(nullable = false)
    private String name;

    @NonNull
    @NotBlank(message = "Description is required.")
    @Column(nullable = false)
    @Lob // large object
    private String description;

    @NonNull
    @NotNull(message = "Price is required.")
    @Digits(integer = 10, fraction = 2, message = "Price has invalid currency format.")
    @DecimalMin(value = "0.00", inclusive = true, message = "Price must be greater or equal than 0.")
    @NumberFormat(style = NumberFormat.Style.CURRENCY)
    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal price;

    @NonNull
    @NotNull(message = "Duration is required.")
    @Digits(integer = 10, fraction = 0, message = "Duration has invalid format.")
    @Column(nullable = false)
    private Integer duration;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @JsonIgnore
    @OneToMany(mappedBy = "service", orphanRemoval = true)
    private Set<AppointmentService> appointmentServices;

    @JsonIgnore
    @ManyToMany(mappedBy = "services")
    private Set<Employee> employees;

    @JsonIgnore
    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name = "service_products", joinColumns = @JoinColumn(name = "service_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "product_id", referencedColumnName = "id"))
    private Set<Product> products = new HashSet<>();

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
