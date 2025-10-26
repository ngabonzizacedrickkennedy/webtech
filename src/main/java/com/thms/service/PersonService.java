package com.thms.service;

import com.thms.exception.ResourceNotFoundException;
import com.thms.model.Person;
import com.thms.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PersonService {

    private final PersonRepository personRepository;

    // CREATE
    public Person createPerson(Person person) {
        // Validate unique constraints
        if (person.getEmail() != null && personRepository.existsByEmail(person.getEmail())) {
            throw new IllegalStateException("Email already exists: " + person.getEmail());
        }

        if (person.getNationalId() != null && personRepository.existsByNationalId(person.getNationalId())) {
            throw new IllegalStateException("National ID already exists: " + person.getNationalId());
        }

        return personRepository.save(person);
    }

    // READ - All
    public Page<Person> getAllPeople(Pageable pageable) {
        return personRepository.findAll(pageable);
    }

    // READ - By ID
    public Person getPersonById(Long id) {
        return personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Person not found with id: " + id));
    }

    // READ - By ID with complete location
    public Person getPersonWithCompleteLocation(Long id) {
        return personRepository.findByIdWithCompleteLocation(id)
                .orElseThrow(() -> new ResourceNotFoundException("Person not found with id: " + id));
    }

    // READ - By Email
    public Person getPersonByEmail(String email) {
        return personRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Person not found with email: " + email));
    }

    // REQUIRED: Get people by Province CODE
    public List<Person> getPeopleByProvinceCode(String code) {
        return personRepository.findByProvinceCode(code);
    }

    // REQUIRED: Get people by Province CODE (paginated)
    public Page<Person> getPeopleByProvinceCode(String code, Pageable pageable) {
        return personRepository.findByProvinceCode(code, pageable);
    }

    // REQUIRED: Get people by Province NAME
    public List<Person> getPeopleByProvinceName(String name) {
        return personRepository.findByProvinceName(name);
    }

    // REQUIRED: Get people by Province NAME (paginated)
    public Page<Person> getPeopleByProvinceName(String name, Pageable pageable) {
        return personRepository.findByProvinceName(name, pageable);
    }

    // Get people by Province ID
    public Page<Person> getPeopleByProvinceId(Long provinceId, Pageable pageable) {
        return personRepository.findByProvinceId(provinceId, pageable);
    }

    // Get people by District ID
    public Page<Person> getPeopleByDistrictId(Long districtId, Pageable pageable) {
        return personRepository.findByDistrictId(districtId, pageable);
    }

    // Get people by Sector ID
    public Page<Person> getPeopleBySectorId(Long sectorId, Pageable pageable) {
        return personRepository.findBySectorId(sectorId, pageable);
    }

    // Get people by Cell ID
    public Page<Person> getPeopleByCellId(Long cellId, Pageable pageable) {
        return personRepository.findByCellId(cellId, pageable);
    }

    // Get people by Village ID
    public Page<Person> getPeopleByVillageId(Long villageId, Pageable pageable) {
        return personRepository.findByVillageId(villageId, pageable);
    }

    // Search by name
    public Page<Person> searchByName(String keyword, Pageable pageable) {
        return personRepository.searchByName(keyword, pageable);
    }

    // Get by role
    public Page<Person> getPeopleByRole(Person.Role role, Pageable pageable) {
        return personRepository.findByRole(role, pageable);
    }

    // UPDATE
    public Person updatePerson(Long id, Person personDetails) {
        Person person = getPersonById(id);

        // Check email uniqueness
        if (personDetails.getEmail() != null &&
                !person.getEmail().equals(personDetails.getEmail()) &&
                personRepository.existsByEmail(personDetails.getEmail())) {
            throw new IllegalStateException("Email already exists: " + personDetails.getEmail());
        }

        // Check national ID uniqueness
        if (personDetails.getNationalId() != null &&
                !person.getNationalId().equals(personDetails.getNationalId()) &&
                personRepository.existsByNationalId(personDetails.getNationalId())) {
            throw new IllegalStateException("National ID already exists: " + personDetails.getNationalId());
        }

        // Update fields
        person.setFirstName(personDetails.getFirstName());
        person.setLastName(personDetails.getLastName());
        person.setEmail(personDetails.getEmail());
        person.setPhoneNumber(personDetails.getPhoneNumber());
        person.setNationalId(personDetails.getNationalId());
        person.setProvince(personDetails.getProvince());
        person.setDistrict(personDetails.getDistrict());
        person.setSector(personDetails.getSector());
        person.setCell(personDetails.getCell());
        person.setVillage(personDetails.getVillage());
        person.setRole(personDetails.getRole());

        return personRepository.save(person);
    }

    // DELETE
    public void deletePerson(Long id) {
        Person person = getPersonById(id);
        personRepository.delete(person);
    }
}