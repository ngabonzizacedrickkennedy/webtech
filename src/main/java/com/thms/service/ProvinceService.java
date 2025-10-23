package com.thms.service;

import com.thms.model.Province;

import java.util.List;
import java.util.Optional;

public interface ProvinceService {
    List<Province> findAll();
    Optional<Province> findById(Long id);
    Province save(Province province);
    void deleteById(Long id);
}
