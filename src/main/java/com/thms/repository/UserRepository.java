package com.thms.repository;

import com.thms.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Existing queries
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    // Location-based queries - REQUIRED FOR MIDTERM
    // Find users by province code
    List<User> findByVillageCellSectorDistrictProvinceCode(String provinceCode);

    // Find users by province name (case insensitive)
    List<User> findByVillageCellSectorDistrictProvinceNameIgnoreCase(String provinceName);

    // Find users by district
    List<User> findByVillageCellSectorDistrictId(Long districtId);
    List<User> findByVillageCellSectorDistrictName(String districtName);

    // Find users by sector
    List<User> findByVillageCellSectorId(Long sectorId);
    List<User> findByVillageCellSectorName(String sectorName);

    // Find users by cell
    List<User> findByVillageCellId(Long cellId);
    List<User> findByVillageCellName(String cellName);

    // Find users by village
    List<User> findByVillageId(Long villageId);
    List<User> findByVillageName(String villageName);

    // Find users by role
    List<User> findByRole(User.Role role);

    // Exists queries
    boolean existsByVillageId(Long villageId);
    boolean existsByVillageCellSectorDistrictProvinceCode(String provinceCode);

    // Sorting
    List<User> findAll(Sort sort);
    List<User> findByRole(User.Role role, Sort sort);

    // Pagination
    Page<User> findAll(Pageable pageable);
    Page<User> findByRole(User.Role role, Pageable pageable);
    Page<User> findByVillageCellSectorDistrictProvinceCode(String provinceCode, Pageable pageable);

    // Custom query for getting user count by province
    @Query("SELECT COUNT(u) FROM User u WHERE u.village.cell.sector.district.province.code = :provinceCode")
    long countByProvinceCode(@Param("provinceCode") String provinceCode);

    // Custom query for getting user count by district
    @Query("SELECT COUNT(u) FROM User u WHERE u.village.cell.sector.district.id = :districtId")
    long countByDistrictId(@Param("districtId") Long districtId);
}