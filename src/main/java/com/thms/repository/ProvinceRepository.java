package com.thms.repository;

import com.thms.model.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProvinceRepository extends JpaRepository<Province, Long> {

    // Find by code
    Optional<Province> findByCode(String code);

    // Find by code (case-insensitive)
    Optional<Province> findByCodeIgnoreCase(String code);

    // Find by name
    Optional<Province> findByName(String name);

    // Find by name (case-insensitive)
    Optional<Province> findByNameIgnoreCase(String name);

    // Check existence by code
    boolean existsByCode(String code);

    // Check existence by name
    boolean existsByName(String name);

    // Custom query to find province with districts count
    @Query("SELECT p FROM Province p LEFT JOIN FETCH p.districts WHERE p.id = :id")
    Optional<Province> findByIdWithDistricts(@Param("id") Long id);
}