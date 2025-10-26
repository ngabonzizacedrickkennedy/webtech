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
 * Sector entity - Third level of Rwandan administrative structure
 * Demonstrates MANY-TO-ONE relationship with District
 * and ONE-TO-MANY relationship with Cell
 */
@Entity
@Table(name = "sectors")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sector {

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

    // MANY-TO-ONE relationship with District
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "district_id", nullable = false)
    private District district;

    // ONE-TO-MANY relationship with Cell
    @OneToMany(mappedBy = "sector", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Cell> cells = new HashSet<>();

    // ONE-TO-MANY relationship with Person/User
    @OneToMany(mappedBy = "sector")
    private Set<Person> people = new HashSet<>();

    public Sector(String code, String name) {
        this.code = code;
        this.name = name;
    }

    // Helper methods
    public void addCell(Cell cell) {
        cells.add(cell);
        cell.setSector(this);
    }

    public void removeCell(Cell cell) {
        cells.remove(cell);
        cell.setSector(null);
    }
}