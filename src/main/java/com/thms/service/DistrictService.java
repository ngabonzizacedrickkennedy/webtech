package com.thms.service;

import com.thms.model.District;

import java.util.List;
import java.util.Optional;

public interface DistrictService {
    List<District> findAll();
    Optional<District> findById(Long id);
    District save(District district);
    void deleteById(Long id);
    List<District> findByProvinceId(Long provinceId);
}
