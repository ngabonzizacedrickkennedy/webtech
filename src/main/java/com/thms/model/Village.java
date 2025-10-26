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
 * Village entity - Fifth (lowest) level of Rwandan administrative structure
 * Demonstrates MANY-TO-ONE relationship with Cell
 */
@Entity
@Table(name = "villages")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Village {

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

    // MANY-TO-ONE relationship with Cell
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cell_id", nullable = false)
    private Cell cell;

    // ONE-TO-MANY relationship with Person/User
    @OneToMany(mappedBy = "village")
    private Set<Person> people = new HashSet<>();

    public Village(String code, String name) {
        this.code = code;
        this.name = name;
    }
}