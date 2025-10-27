package com.thms.controller;

import com.thms.model.Province;
import com.thms.model.User;
import com.thms.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/location")
public class LocationUserController {

    private final UserRepository userRepository;

    public LocationUserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/users/by-province")
    public ResponseEntity<List<User>> getUsersByProvince(
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String name) {

        if (code != null && !code.isBlank()) {
            return ResponseEntity.ok(
                    userRepository.findByVillageCellSectorDistrictProvinceCode(code)
            );
        } else if (name != null && !name.isBlank()) {
            return ResponseEntity.ok(
                    userRepository.findByVillageCellSectorDistrictProvinceNameIgnoreCase(name)
            );
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/user/{id}/province")
    public ResponseEntity<Province> getProvinceForUser(@PathVariable Long id) {
        User user = userRepository.findById(id).orElse(null);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        if (user.getVillage() != null &&
                user.getVillage().getCell() != null &&
                user.getVillage().getCell().getSector() != null &&
                user.getVillage().getCell().getSector().getDistrict() != null &&
                user.getVillage().getCell().getSector().getDistrict().getProvince() != null) {

            Province province = user.getVillage().getCell().getSector().getDistrict().getProvince();
            return ResponseEntity.ok(province);
        }

        return ResponseEntity.notFound().build();
    }
}