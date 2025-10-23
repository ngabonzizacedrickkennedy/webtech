package com.thms.repository;

import com.thms.model.Province;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProvinceRepository extends JpaRepository<Province, Long> {

    // findBy queries
    Optional<Province> findByCode(String code);
    Optional<Province> findByName(String name);
    Optional<Province> findByNameIgnoreCase(String name);
    List<Province> findByCodeContaining(String code);
    List<Province> findByNameContaining(String name);

    // existsBy queries
    boolean existsByCode(String code);
    boolean existsByName(String name);
    boolean existsByCodeIgnoreCase(String code);

    // Sorting
    List<Province> findAll(Sort sort);

    // Pagination
    Page<Province> findAll(Pageable pageable);
    Page<Province> findByNameContaining(String name, Pageable pageable);
}