package com.thms.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

/**
 * Province entity - Top level of Rwandan administrative structure
 * Demonstrates ONE-TO-MANY relationship with District
 */
@Entity
@Table(name = "provinces")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Province {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 50)
    @Column(unique = true, nullable = false)
    private String code; // e.g., "KIG", "EST", "WEST", "NORTH", "SOUTH"

    @NotBlank
    @Size(max = 100)
    @Column(unique = true, nullable = false)
    private String name; // e.g., "Kigali City", "Eastern Province"

    // ONE-TO-MANY relationship with District
    @OneToMany(mappedBy = "province", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<District> districts = new HashSet<>();

    // ONE-TO-MANY relationship with Person/User
    @OneToMany(mappedBy = "province")
    private Set<Person> people = new HashSet<>();

    public Province(String code, String name) {
        this.code = code;
        this.name = name;
    }

    // Helper method
    public void addDistrict(District district) {
        districts.add(district);
        district.setProvince(this);
    }

    public void removeDistrict(District district) {
        districts.remove(district);
        district.setProvince(null);
    }
}