package com.thms.service;

import com.thms.model.Cell;

import java.util.List;
import java.util.Optional;

public interface CellService {
    List<Cell> findAll();
    Optional<Cell> findById(Long id);
    Cell save(Cell cell);
    void deleteById(Long id);
    List<Cell> findBySectorId(Long sectorId);
}
