package com.thms.repository;

import com.thms.model.Sector;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SectorRepository extends JpaRepository<Sector, Long> {

    // findBy queries
    Optional<Sector> findByName(String name);
    List<Sector> findByNameContaining(String name);
    List<Sector> findByDistrictId(Long districtId);
    List<Sector> findByDistrictName(String districtName);
    List<Sector> findByDistrictProvinceId(Long provinceId);
    List<Sector> findByDistrictProvinceName(String provinceName);

    // existsBy queries
    boolean existsByName(String name);
    boolean existsByDistrictId(Long districtId);

    // countBy queries - ADD THIS LINE
    long countByDistrictId(Long districtId);

    // Sorting
    List<Sector> findAll(Sort sort);
    List<Sector> findByDistrictId(Long districtId, Sort sort);

    // Pagination
    Page<Sector> findAll(Pageable pageable);
    Page<Sector> findByDistrictId(Long districtId, Pageable pageable);
    Page<Sector> findByNameContaining(String name, Pageable pageable);
}