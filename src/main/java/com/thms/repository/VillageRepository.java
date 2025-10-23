package com.thms.repository;

import com.thms.model.Village;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VillageRepository extends JpaRepository<Village, Long> {

	// findBy queries
	Optional<Village> findByName(String name);
	List<Village> findByNameContaining(String name);
	List<Village> findByCellId(Long cellId);
	List<Village> findByCellName(String cellName);
	List<Village> findByCellSectorId(Long sectorId);
	List<Village> findByCellSectorDistrictId(Long districtId);
	List<Village> findByCellSectorDistrictProvinceId(Long provinceId);
	List<Village> findByCellSectorDistrictProvinceCode(String provinceCode);

	// existsBy queries
	boolean existsByName(String name);
	boolean existsByCellId(Long cellId);

	// Sorting
	List<Village> findAll(Sort sort);
	List<Village> findByCellId(Long cellId, Sort sort);

	// Pagination
	Page<Village> findAll(Pageable pageable);
	Page<Village> findByCellId(Long cellId, Pageable pageable);
	Page<Village> findByNameContaining(String name, Pageable pageable);
}