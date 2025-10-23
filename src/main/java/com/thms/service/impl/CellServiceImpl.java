package com.thms.service.impl;

import com.thms.model.Cell;
import com.thms.repository.CellRepository;
import com.thms.repository.VillageRepository;
import com.thms.service.CellService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CellServiceImpl implements CellService {

    private final CellRepository cellRepository;
    private final VillageRepository villageRepository;

    public CellServiceImpl(CellRepository cellRepository, VillageRepository villageRepository) {
        this.cellRepository = cellRepository;
        this.villageRepository = villageRepository;
    }

    @Override
    public List<Cell> findAll() {
        return cellRepository.findAll();
    }

    @Override
    public Optional<Cell> findById(Long id) {
        return cellRepository.findById(id);
    }

    @Override
    @Transactional
    public Cell save(Cell cell) {
        return cellRepository.save(cell);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (villageRepository.countByCell_Id(id) > 0) {
            throw new IllegalStateException("Cannot delete cell with existing villages");
        }
        cellRepository.deleteById(id);
    }

    @Override
    public List<Cell> findBySectorId(Long sectorId) {
        return cellRepository.findBySector_Id(sectorId);
    }
}
