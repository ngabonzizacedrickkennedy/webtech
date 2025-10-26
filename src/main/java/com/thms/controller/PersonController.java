package com.thms.controller;

import com.thms.dto.ApiResponse;
import com.thms.model.Person;
import com.thms.service.PersonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/people")
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;

    // CREATE
    @PostMapping
    public ResponseEntity<ApiResponse<Person>> createPerson(@Valid @RequestBody Person person) {
        Person created = personService.createPerson(person);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Person created successfully"));
    }

    // READ - All (with pagination and sorting)
    @GetMapping
    public ResponseEntity<ApiResponse<Page<Person>>> getAllPeople(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "firstName") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {

        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<Person> people = personService.getAllPeople(pageable);
        return ResponseEntity.ok(ApiResponse.success(people));
    }

    // READ - By ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Person>> getPersonById(@PathVariable Long id) {
        Person person = personService.getPersonById(id);
        return ResponseEntity.ok(ApiResponse.success(person));
    }

    // READ - By ID with complete location
    @GetMapping("/{id}/with-location")
    public ResponseEntity<ApiResponse<Person>> getPersonWithCompleteLocation(@PathVariable Long id) {
        Person person = personService.getPersonWithCompleteLocation(id);
        return ResponseEntity.ok(ApiResponse.success(person));
    }

    // REQUIRED: Get people by Province CODE
    @GetMapping("/by-province-code/{code}")
    public ResponseEntity<ApiResponse<List<Person>>> getPeopleByProvinceCode(@PathVariable String code) {
        List<Person> people = personService.getPeopleByProvinceCode(code);
        return ResponseEntity.ok(ApiResponse.success(people,
                "Found " + people.size() + " people in province with code: " + code));
    }

    // REQUIRED: Get people by Province CODE (paginated)
    @GetMapping("/by-province-code/{code}/paginated")
    public ResponseEntity<ApiResponse<Page<Person>>> getPeopleByProvinceCodePaginated(
            @PathVariable String code,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Person> people = personService.getPeopleByProvinceCode(code, pageable);
        return ResponseEntity.ok(ApiResponse.success(people));
    }

    // REQUIRED: Get people by Province NAME
    @GetMapping("/by-province-name/{name}")
    public ResponseEntity<ApiResponse<List<Person>>> getPeopleByProvinceName(@PathVariable String name) {
        List<Person> people = personService.getPeopleByProvinceName(name);
        return ResponseEntity.ok(ApiResponse.success(people,
                "Found " + people.size() + " people in province: " + name));
    }

    // REQUIRED: Get people by Province NAME (paginated)
    @GetMapping("/by-province-name/{name}/paginated")
    public ResponseEntity<ApiResponse<Page<Person>>> getPeopleByProvinceNamePaginated(
            @PathVariable String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Person> people = personService.getPeopleByProvinceName(name, pageable);
        return ResponseEntity.ok(ApiResponse.success(people));
    }

    // Get people by Province ID
    @GetMapping("/by-province/{provinceId}")
    public ResponseEntity<ApiResponse<Page<Person>>> getPeopleByProvince(
            @PathVariable Long provinceId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Person> people = personService.getPeopleByProvinceId(provinceId, pageable);
        return ResponseEntity.ok(ApiResponse.success(people));
    }

    // Get people by District
    @GetMapping("/by-district/{districtId}")
    public ResponseEntity<ApiResponse<Page<Person>>> getPeopleByDistrict(
            @PathVariable Long districtId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Person> people = personService.getPeopleByDistrictId(districtId, pageable);
        return ResponseEntity.ok(ApiResponse.success(people));
    }

    // Get people by Sector
    @GetMapping("/by-sector/{sectorId}")
    public ResponseEntity<ApiResponse<Page<Person>>> getPeopleBySector(
            @PathVariable Long sectorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Person> people = personService.getPeopleBySectorId(sectorId, pageable);
        return ResponseEntity.ok(ApiResponse.success(people));
    }

    // Get people by Cell
    @GetMapping("/by-cell/{cellId}")
    public ResponseEntity<ApiResponse<Page<Person>>> getPeopleByCell(
            @PathVariable Long cellId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Person> people = personService.getPeopleByCellId(cellId, pageable);
        return ResponseEntity.ok(ApiResponse.success(people));
    }

    // Get people by Village
    @GetMapping("/by-village/{villageId}")
    public ResponseEntity<ApiResponse<Page<Person>>> getPeopleByVillage(
            @PathVariable Long villageId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Person> people = personService.getPeopleByVillageId(villageId, pageable);
        return ResponseEntity.ok(ApiResponse.success(people));
    }

    // Search by name
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<Person>>> searchPeople(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Person> people = personService.searchByName(keyword, pageable);
        return ResponseEntity.ok(ApiResponse.success(people));
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Person>> updatePerson(
            @PathVariable Long id,
            @Valid @RequestBody Person person) {
        Person updated = personService.updatePerson(id, person);
        return ResponseEntity.ok(ApiResponse.success(updated, "Person updated successfully"));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePerson(@PathVariable Long id) {
        personService.deletePerson(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Person deleted successfully"));
    }

    // Get by email
    @GetMapping("/email/{email}")
    public ResponseEntity<ApiResponse<Person>> getPersonByEmail(@PathVariable String email) {
        Person person = personService.getPersonByEmail(email);
        return ResponseEntity.ok(ApiResponse.success(person));
    }

    // Get by role
    @GetMapping("/by-role/{role}")
    public ResponseEntity<ApiResponse<Page<Person>>> getPeopleByRole(
            @PathVariable Person.Role role,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Person> people = personService.getPeopleByRole(role, pageable);
        return ResponseEntity.ok(ApiResponse.success(people));
    }
}