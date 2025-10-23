package com.thms.service;

import com.thms.model.Sector;

import java.util.List;
import java.util.Optional;

public interface SectorService {
    List<Sector> findAll();
    Optional<Sector> findById(Long id);
    Sector save(Sector sector);
    void deleteById(Long id);
    List<Sector> findByDistrictId(Long districtId);
}
