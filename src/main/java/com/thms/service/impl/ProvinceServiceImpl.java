package com.thms.service.impl;

import com.thms.model.Province;
import com.thms.repository.ProvinceRepository;
import com.thms.repository.DistrictRepository;
import com.thms.service.ProvinceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProvinceServiceImpl implements ProvinceService {

    private final ProvinceRepository provinceRepository;
    private final DistrictRepository districtRepository;

    public ProvinceServiceImpl(ProvinceRepository provinceRepository, DistrictRepository districtRepository) {
        this.provinceRepository = provinceRepository;
        this.districtRepository = districtRepository;
    }

    @Override
    public List<Province> findAll() {
        return provinceRepository.findAll();
    }

    @Override
    public Optional<Province> findById(Long id) {
        return provinceRepository.findById(id);
    }

    @Override
    @Transactional
    public Province save(Province province) {
        return provinceRepository.save(province);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        // CHANGE FROM: districtRepository.countByProvince_Id(id)
        // TO: districtRepository.countByProvinceId(id)
        if (districtRepository.countByProvinceId(id) > 0) {
            throw new IllegalStateException("Cannot delete province with existing districts");
        }
        provinceRepository.deleteById(id);
    }
}