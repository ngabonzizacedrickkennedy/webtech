package com.thms.service;

import com.thms.model.Village;

import java.util.List;
import java.util.Optional;

public interface VillageService {
    List<Village> findAll();

    Optional<Village> findById(Long id);

    Village save(Village village);

    void deleteById(Long id);

    List<Village> findByCellId(Long cellId);
}
 
