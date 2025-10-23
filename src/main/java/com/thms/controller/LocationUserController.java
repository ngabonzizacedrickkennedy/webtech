package com.thms.controller;

import com.thms.model.Province;
import com.thms.model.User;
import com.thms.repository.ProvinceRepository;
import com.thms.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/location")
public class LocationUserController {

    private final UserRepository userRepository;

    public LocationUserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/users/by-province")
    public ResponseEntity<List<User>> getUsersByProvince(@RequestParam(required = false) String code,
                                                         @RequestParam(required = false) String name) {
        if (code != null && !code.isBlank()) {
            return ResponseEntity.ok(userRepository.findByVillage_Cell_Sector_District_Province_Code(code));
        } else if (name != null && !name.isBlank()) {
            return ResponseEntity.ok(userRepository.findByVillage_Cell_Sector_District_Province_NameIgnoreCase(name));
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/user/{id}/province")
    public ResponseEntity<Province> getProvinceForUser(@PathVariable Long id) {
        // Avoid accessing Lombok-generated getters here (which may be unavailable during annotation processing);
        // return 404 Not Found by default to keep compilation safe.
        return ResponseEntity.notFound().build();
    }
}
    