package com.thms.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Person (User) entity
 * Demonstrates relationships with Location hierarchy
 */
@Entity
@Table(name = "people")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 100)
    private String firstName;

    @NotBlank
    @Size(max = 100)
    private String lastName;

    @Email
    @NotBlank
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank
    @Size(max = 20)
    private String phoneNumber;

    @Size(max = 16)
    @Column(unique = true)
    private String nationalId; // Rwandan ID number

    // Relationships with Rwandan Location hierarchy
    // Each person must have a location

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "province_id", nullable = false)
    private Province province;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "district_id", nullable = false)
    private District district;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sector_id", nullable = false)
    private Sector sector;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cell_id", nullable = false)
    private Cell cell;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "village_id", nullable = false)
    private Village village;

    // User authentication fields (if needed for your system)
    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role = Role.CUSTOMER;

    public enum Role {
        ADMIN, MANAGER, EMPLOYEE, CUSTOMER
    }

    // Helper method to set complete location
    public void setLocation(Province province, District district, Sector sector, Cell cell, Village village) {
        this.province = province;
        this.district = district;
        this.sector = sector;
        this.cell = cell;
        this.village = village;
    }

    // Get full location as string
    public String getFullLocation() {
        return String.format("%s, %s, %s, %s, %s",
                village != null ? village.getName() : "N/A",
                cell != null ? cell.getName() : "N/A",
                sector != null ? sector.getName() : "N/A",
                district != null ? district.getName() : "N/A",
                province != null ? province.getName() : "N/A");
    }
}