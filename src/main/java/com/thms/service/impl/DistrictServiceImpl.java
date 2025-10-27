package com.thms.service.impl;

import com.thms.model.District;
import com.thms.repository.DistrictRepository;
import com.thms.repository.SectorRepository;
import com.thms.service.DistrictService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class DistrictServiceImpl implements DistrictService {

    private final DistrictRepository districtRepository;
    private final SectorRepository sectorRepository;

    public DistrictServiceImpl(DistrictRepository districtRepository, SectorRepository sectorRepository) {
        this.districtRepository = districtRepository;
        this.sectorRepository = sectorRepository;
    }

    @Override
    public List<District> findAll() {
        return districtRepository.findAll();
    }

    @Override
    public Optional<District> findById(Long id) {
        return districtRepository.findById(id);
    }

    @Override
    @Transactional
    public District save(District district) {
        return districtRepository.save(district);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        // CHANGE FROM: sectorRepository.countByDistrict_Id(id)
        // TO: sectorRepository.countByDistrictId(id)
        if (sectorRepository.countByDistrictId(id) > 0) {
            throw new IllegalStateException("Cannot delete district with existing sectors");
        }
        districtRepository.deleteById(id);
    }

    @Override
    public List<District> findByProvinceId(Long provinceId) {
        // CHANGE FROM: districtRepository.findByProvince_Id(provinceId)
        // TO: districtRepository.findByProvinceId(provinceId)
        return districtRepository.findByProvinceId(provinceId);
    }
}