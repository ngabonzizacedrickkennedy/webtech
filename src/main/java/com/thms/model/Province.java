package com.thms.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "provinces")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Province {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String code; // e.g., "01", "02"

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "province", cascade = CascadeType.ALL)
    private Set<District> districts = new HashSet<>();

    // Explicit setters to avoid IDE/processor issues when Lombok isn't active
    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }
}
