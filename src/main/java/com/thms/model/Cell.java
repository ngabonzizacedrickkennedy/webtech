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
 * Cell entity - Fourth level of Rwandan administrative structure
 * Demonstrates MANY-TO-ONE relationship with Sector
 * and ONE-TO-MANY relationship with Village
 */
@Entity
@Table(name = "cells")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cell {

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

    // MANY-TO-ONE relationship with Sector
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sector_id", nullable = false)
    private Sector sector;

    // ONE-TO-MANY relationship with Village
    @OneToMany(mappedBy = "cell", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Village> villages = new HashSet<>();

    // ONE-TO-MANY relationship with Person/User
    @OneToMany(mappedBy = "cell")
    private Set<Person> people = new HashSet<>();

    public Cell(String code, String name) {
        this.code = code;
        this.name = name;
    }

    // Helper methods
    public void addVillage(Village village) {
        villages.add(village);
        village.setCell(this);
    }

    public void removeVillage(Village village) {
        villages.remove(village);
        village.setCell(null);
    }
}