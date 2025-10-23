package com.thms.repository;

import com.thms.model.Cell;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CellRepository extends JpaRepository<Cell, Long> {

    // findBy queries
    Optional<Cell> findByName(String name);
    List<Cell> findByNameContaining(String name);
    List<Cell> findBySectorId(Long sectorId);
    List<Cell> findBySectorName(String sectorName);
    List<Cell> findBySectorDistrictId(Long districtId);
    List<Cell> findBySectorDistrictProvinceId(Long provinceId);

    // existsBy queries
    boolean existsByName(String name);
    boolean existsBySectorId(Long sectorId);

    // Sorting
    List<Cell> findAll(Sort sort);
    List<Cell> findBySectorId(Long sectorId, Sort sort);

    // Pagination
    Page<Cell> findAll(Pageable pageable);
    Page<Cell> findBySectorId(Long sectorId, Pageable pageable);
    Page<Cell> findByNameContaining(String name, Pageable pageable);
}