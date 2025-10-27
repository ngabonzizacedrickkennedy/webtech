package com.thms.service.impl;

import com.thms.model.Village;
import com.thms.repository.VillageRepository;
import com.thms.service.VillageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class VillageServiceImpl implements VillageService {

    private final VillageRepository villageRepository;

    public VillageServiceImpl(VillageRepository villageRepository) {
        this.villageRepository = villageRepository;
    }

    @Override
    public List<Village> findAll() {
        return villageRepository.findAll();
    }

    @Override
    public Optional<Village> findById(Long id) {
        return villageRepository.findById(id);
    }

    @Override
    @Transactional
    public Village save(Village village) {
        return villageRepository.save(village);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        villageRepository.deleteById(id);
    }

    @Override
    public List<Village> findByCellId(Long cellId) {
        // CHANGE FROM: villageRepository.findByCell_Id(cellId)
        // TO: villageRepository.findByCellId(cellId)
        return villageRepository.findByCellId(cellId);
    }
}