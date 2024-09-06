package com.vasiu_catalina.beauty_salon.entity;

import java.util.Set;

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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="employees")
@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(name="first_name", nullable = false)
    private String firstName;

    @NonNull
    @Column(name="last_name", nullable = false)
    private String lastName;

    @NonNull
    @Column(nullable = false, unique = true)
    private String email;

    @NonNull
    @Column(name="phone_number", nullable = false, unique = true)
    private String phoneNumber;

    @NonNull
    @Column(nullable = false)
    private String address;

    @NonNull 
    @Column(name="birth_date", nullable = false)
    private LocalDate birthDate;

    @NonNull
    @Column(nullable = false)
    private String role = "employee";

    @NonNull
    @Column(nullable = false)
    private String specialization;

    @NonNull
    @Column(precision = 10, scale=2, nullable = false)
    private BigDecimal salary;

    @Column(name="created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name="updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    @JsonIgnore
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    private Set<Appointment> appointments;

    @JsonIgnore
    @ManyToMany
    @JoinTable(name="employee_services",
    joinColumns = @JoinColumn(name="employee_id", referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name="service_id", referencedColumnName = "id"))
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
