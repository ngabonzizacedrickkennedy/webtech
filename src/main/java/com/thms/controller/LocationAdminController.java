package com.thms.controller;

import com.thms.model.*;
import com.thms.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/locations")
public class LocationAdminController {

    private final ProvinceService provinceService;
    private final DistrictService districtService;
    private final SectorService sectorService;
    private final CellService cellService;
    private final VillageService villageService;

    public LocationAdminController(ProvinceService provinceService,
                                   DistrictService districtService,
                                   SectorService sectorService,
                                   CellService cellService,
                                   VillageService villageService) {
        this.provinceService = provinceService;
        this.districtService = districtService;
        this.sectorService = sectorService;
        this.cellService = cellService;
        this.villageService = villageService;
    }

    // ========== PROVINCE ENDPOINTS ==========
    
    @GetMapping("/provinces")
    public ResponseEntity<List<Province>> getAllProvinces() {
        return ResponseEntity.ok(provinceService.findAll());
    }

    @GetMapping("/provinces/{id}")
    public ResponseEntity<Province> getProvinceById(@PathVariable Long id) {
        return provinceService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/provinces")
    public ResponseEntity<Province> createProvince(@RequestBody Province province) {
        Province saved = provinceService.save(province);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/provinces/{id}")
    public ResponseEntity<Province> updateProvince(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        return provinceService.findById(id).map(existing -> {
            if (updates.containsKey("code")) {
                existing.setCode(updates.get("code").toString());
            }
            if (updates.containsKey("name")) {
                existing.setName(updates.get("name").toString());
            }
            Province updated = provinceService.save(existing);
            return ResponseEntity.ok(updated);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/provinces/{id}")
    public ResponseEntity<Void> deleteProvince(@PathVariable Long id) {
        if (provinceService.findById(id).isPresent()) {
            provinceService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // ========== DISTRICT ENDPOINTS ==========
    
    @GetMapping("/districts")
    public ResponseEntity<List<District>> getAllDistricts() {
        return ResponseEntity.ok(districtService.findAll());
    }

    @GetMapping("/districts/{id}")
    public ResponseEntity<District> getDistrictById(@PathVariable Long id) {
        return districtService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/districts")
    public ResponseEntity<District> createDistrict(@RequestBody District district) {
        District saved = districtService.save(district);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/districts/{id}")
    public ResponseEntity<District> updateDistrict(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        return districtService.findById(id).map(existing -> {
            if (updates.containsKey("code")) {
                existing.setCode(updates.get("code").toString());
            }
            if (updates.containsKey("name")) {
                existing.setName(updates.get("name").toString());
            }
            District updated = districtService.save(existing);
            return ResponseEntity.ok(updated);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/districts/{id}")
    public ResponseEntity<Void> deleteDistrict(@PathVariable Long id) {
        if (districtService.findById(id).isPresent()) {
            districtService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // ========== SECTOR ENDPOINTS ==========
    
    @GetMapping("/sectors")
    public ResponseEntity<List<Sector>> getAllSectors() {
        return ResponseEntity.ok(sectorService.findAll());
    }

    @GetMapping("/sectors/{id}")
    public ResponseEntity<Sector> getSectorById(@PathVariable Long id) {
        return sectorService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/sectors")
    public ResponseEntity<Sector> createSector(@RequestBody Sector sector) {
        Sector saved = sectorService.save(sector);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/sectors/{id}")
    public ResponseEntity<Sector> updateSector(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        return sectorService.findById(id).map(existing -> {
            if (updates.containsKey("code")) {
                existing.setCode(updates.get("code").toString());
            }
            if (updates.containsKey("name")) {
                existing.setName(updates.get("name").toString());
            }
            Sector updated = sectorService.save(existing);
            return ResponseEntity.ok(updated);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/sectors/{id}")
    public ResponseEntity<Void> deleteSector(@PathVariable Long id) {
        if (sectorService.findById(id).isPresent()) {
            sectorService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // ========== CELL ENDPOINTS ==========
    
    @GetMapping("/cells")
    public ResponseEntity<List<Cell>> getAllCells() {
        return ResponseEntity.ok(cellService.findAll());
    }

    @GetMapping("/cells/{id}")
    public ResponseEntity<Cell> getCellById(@PathVariable Long id) {
        return cellService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/cells")
    public ResponseEntity<Cell> createCell(@RequestBody Cell cell) {
        Cell saved = cellService.save(cell);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/cells/{id}")
    public ResponseEntity<Cell> updateCell(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        return cellService.findById(id).map(existing -> {
            if (updates.containsKey("code")) {
                existing.setCode(updates.get("code").toString());
            }
            if (updates.containsKey("name")) {
                existing.setName(updates.get("name").toString());
            }
            Cell updated = cellService.save(existing);
            return ResponseEntity.ok(updated);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/cells/{id}")
    public ResponseEntity<Void> deleteCell(@PathVariable Long id) {
        if (cellService.findById(id).isPresent()) {
            cellService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // ========== VILLAGE ENDPOINTS ==========
    
    @GetMapping("/villages")
    public ResponseEntity<List<Village>> getAllVillages() {
        return ResponseEntity.ok(villageService.findAll());
    }

    @GetMapping("/villages/{id}")
    public ResponseEntity<Village> getVillageById(@PathVariable Long id) {
        return villageService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/villages")
    public ResponseEntity<Village> createVillage(@RequestBody Village village) {
        Village saved = villageService.save(village);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/villages/{id}")
    public ResponseEntity<Village> updateVillage(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        return villageService.findById(id).map(existing -> {
            if (updates.containsKey("code")) {
                existing.setCode(updates.get("code").toString());
            }
            if (updates.containsKey("name")) {
                existing.setName(updates.get("name").toString());
            }
            Village updated = villageService.save(existing);
            return ResponseEntity.ok(updated);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/villages/{id}")
    public ResponseEntity<Void> deleteVillage(@PathVariable Long id) {
        if (villageService.findById(id).isPresent()) {
            villageService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}