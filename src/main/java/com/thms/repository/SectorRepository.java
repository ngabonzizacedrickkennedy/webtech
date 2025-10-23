package com.thms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.thms.model.Sector;

@Repository
public interface SectorRepository extends JpaRepository<Sector, Long> {
    java.util.List<Sector> findByDistrict_Id(Long districtId);
    long countByDistrict_Id(Long districtId);
}
