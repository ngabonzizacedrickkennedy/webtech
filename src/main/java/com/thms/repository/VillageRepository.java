package com.thms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.thms.model.Village;

@Repository
public interface VillageRepository extends JpaRepository<Village, Long> {
	java.util.List<Village> findByCell_Id(Long cellId);
	long countByCell_Id(Long cellId);
}
