package com.thms.repository;

import com.thms.model.District;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DistrictRepository extends JpaRepository<District, Long> {

    // findBy queries
    Optional<District> findByName(String name);
    List<District> findByNameContaining(String name);
    List<District> findByProvinceId(Long provinceId);
    List<District> findByProvinceName(String provinceName);
    List<District> findByProvinceCode(String provinceCode);

    // existsBy queries
    boolean existsByName(String name);
    boolean existsByProvinceId(Long provinceId);

    // Sorting
    List<District> findAll(Sort sort);
    List<District> findByProvinceId(Long provinceId, Sort sort);

    // Pagination
    Page<District> findAll(Pageable pageable);
    Page<District> findByProvinceId(Long provinceId, Pageable pageable);
    Page<District> findByNameContaining(String name, Pageable pageable);
}