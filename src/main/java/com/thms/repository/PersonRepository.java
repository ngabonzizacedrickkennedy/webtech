package com.thms.repository;

import com.thms.model.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    // Find by email
    Optional<Person> findByEmail(String email);

    // Find by national ID
    Optional<Person> findByNationalId(String nationalId);

    // Check if email exists
    boolean existsByEmail(String email);

    // Check if national ID exists
    boolean existsByNationalId(String nationalId);

    // REQUIRED: Find people by Province CODE
    @Query("SELECT p FROM Person p WHERE p.province.code = :provinceCode")
    List<Person> findByProvinceCode(@Param("provinceCode") String provinceCode);

    // REQUIRED: Find people by Province CODE (paginated)
    @Query("SELECT p FROM Person p WHERE p.province.code = :provinceCode")
    Page<Person> findByProvinceCode(@Param("provinceCode") String provinceCode, Pageable pageable);

    // REQUIRED: Find people by Province NAME
    @Query("SELECT p FROM Person p WHERE p.province.name = :provinceName")
    List<Person> findByProvinceName(@Param("provinceName") String provinceName);

    // REQUIRED: Find people by Province NAME (paginated)
    @Query("SELECT p FROM Person p WHERE p.province.name = :provinceName")
    Page<Person> findByProvinceName(@Param("provinceName") String provinceName, Pageable pageable);

    // Find by Province ID
    List<Person> findByProvinceId(Long provinceId);
    Page<Person> findByProvinceId(Long provinceId, Pageable pageable);

    // Find by District
    List<Person> findByDistrictId(Long districtId);
    Page<Person> findByDistrictId(Long districtId, Pageable pageable);

    // Find by Sector
    List<Person> findBySectorId(Long sectorId);
    Page<Person> findBySectorId(Long sectorId, Pageable pageable);

    // Find by Cell
    List<Person> findByCellId(Long cellId);
    Page<Person> findByCellId(Long cellId, Pageable pageable);

    // Find by Village
    List<Person> findByVillageId(Long villageId);
    Page<Person> findByVillageId(Long villageId, Pageable pageable);

    // Complex query: Find people with complete location info
    @Query("SELECT p FROM Person p " +
            "LEFT JOIN FETCH p.province " +
            "LEFT JOIN FETCH p.district " +
            "LEFT JOIN FETCH p.sector " +
            "LEFT JOIN FETCH p.cell " +
            "LEFT JOIN FETCH p.village " +
            "WHERE p.id = :id")
    Optional<Person> findByIdWithCompleteLocation(@Param("id") Long id);

    // Search by name
    @Query("SELECT p FROM Person p WHERE " +
            "LOWER(p.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(p.lastName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Person> searchByName(@Param("keyword") String keyword, Pageable pageable);

    // Find by role
    List<Person> findByRole(Person.Role role);
    Page<Person> findByRole(Person.Role role, Pageable pageable);
}