package com.thms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.thms.model.Cell;

@Repository
public interface CellRepository extends JpaRepository<Cell, Long> {
    java.util.List<Cell> findBySector_Id(Long sectorId);
    long countBySector_Id(Long sectorId);
}
