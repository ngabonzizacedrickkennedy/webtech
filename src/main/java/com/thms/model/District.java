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
 * District entity - Second level of Rwandan administrative structure
 * Demonstrates MANY-TO-ONE relationship with Province
 * and ONE-TO-MANY relationship with Sector
 */
@Entity
@Table(name = "districts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class District {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 50)
    @Column(nullable = false)
    private String code;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false)
    private String name;

    // MANY-TO-ONE relationship with Province
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "province_id", nullable = false)
    private Province province;

    // ONE-TO-MANY relationship with Sector
    @OneToMany(mappedBy = "district", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Sector> sectors = new HashSet<>();

    // ONE-TO-MANY relationship with Person/User
    @OneToMany(mappedBy = "district")
    private Set<Person> people = new HashSet<>();

    public District(String code, String name) {
        this.code = code;
        this.name = name;
    }

    // Helper methods
    public void addSector(Sector sector) {
        sectors.add(sector);
        sector.setDistrict(this);
    }

    public void removeSector(Sector sector) {
        sectors.remove(sector);
        sector.setDistrict(null);
    }
}