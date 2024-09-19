package com.vasiu_catalina.beauty_salon.entity;

import java.util.Set;
import java.util.HashSet;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vasiu_catalina.beauty_salon.validation.minAge.MinAge;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "clients")
@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @NotBlank(message = "First name is required.")
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NonNull
    @NotBlank(message = "Last Name is required.")
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NonNull
    @Email
    @NotBlank(message = "Email is required.")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Password is required.")
    @Column(nullable = false)
    private String password;

    @Transient
    @NotBlank(message = "Password confirm is required.")
    private String confirmPassword;

    @NonNull
    @NotBlank(message = "Phone number is required.")
    @Column(name = "phone_number", nullable = false, unique = true)
    private String phoneNumber;

    @NonNull
    @NotBlank(message = "Address is required.")
    @Column(nullable = false)
    private String address;

    @NonNull
    @MinAge(18)
    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @JsonIgnore
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private Set<Appointment> appointments = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private Set<Review> reviews = new HashSet<>();

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
