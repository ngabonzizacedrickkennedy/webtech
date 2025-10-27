package com.thms.service.impl;

import com.thms.model.Sector;
import com.thms.repository.SectorRepository;
import com.thms.repository.CellRepository;
import com.thms.service.SectorService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class SectorServiceImpl implements SectorService {

    private final SectorRepository sectorRepository;
    private final CellRepository cellRepository;

    public SectorServiceImpl(SectorRepository sectorRepository, CellRepository cellRepository) {
        this.sectorRepository = sectorRepository;
        this.cellRepository = cellRepository;
    }

    @Override
    public List<Sector> findAll() {
        return sectorRepository.findAll();
    }

    @Override
    public Optional<Sector> findById(Long id) {
        return sectorRepository.findById(id);
    }

    @Override
    @Transactional
    public Sector save(Sector sector) {
        return sectorRepository.save(sector);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        // CHANGE FROM: cellRepository.countBySector_Id(id)
        // TO: cellRepository.countBySectorId(id)
        if (cellRepository.countBySectorId(id) > 0) {
            throw new IllegalStateException("Cannot delete sector with existing cells");
        }
        sectorRepository.deleteById(id);
    }

    @Override
    public List<Sector> findByDistrictId(Long districtId) {
        // CHANGE FROM: sectorRepository.findByDistrict_Id(districtId)
        // TO: sectorRepository.findByDistrictId(districtId)
        return sectorRepository.findByDistrictId(districtId);
    }
}