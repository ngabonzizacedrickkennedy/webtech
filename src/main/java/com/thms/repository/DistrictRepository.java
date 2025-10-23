package com.thms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.thms.model.District;

@Repository
public interface DistrictRepository extends JpaRepository<District, Long> {
    java.util.List<District> findByProvince_Id(Long provinceId);
    long countByProvince_Id(Long provinceId);
}
