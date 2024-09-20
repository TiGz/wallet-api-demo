package org.github.tigz.wallet.modules.person.service;

import org.github.tigz.wallet.modules.person.dto.PersonDTO;
import org.github.tigz.wallet.modules.person.model.Person;
import org.github.tigz.wallet.modules.person.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PersonService {

    private final PersonRepository personRepository;

    @Autowired
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Transactional(readOnly = true)
    public List<PersonDTO> getAllPersons() {
        return personRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<PersonDTO> getPersonById(UUID id) {
        return personRepository.findById(id).map(this::convertToDTO);
    }

    @Transactional
    public PersonDTO createPerson(PersonDTO personDTO) {
        Person person = convertToEntity(personDTO);
        Person savedPerson = personRepository.save(person);
        return convertToDTO(savedPerson);
    }

    @Transactional
    public Optional<PersonDTO> updatePerson(UUID id, PersonDTO personDTO) {
        return personRepository.findById(id)
                .map(existingPerson -> {
                    updatePersonFromDTO(existingPerson, personDTO);
                    Person updatedPerson = personRepository.save(existingPerson);
                    return convertToDTO(updatedPerson);
                });
    }

    @Transactional
    public boolean deletePerson(UUID id) {
        if (personRepository.existsById(id)) {
            personRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private PersonDTO convertToDTO(Person person) {
        return new PersonDTO(
                person.getId(),
                person.getTitle(),
                person.getFirstName(),
                person.getLastName(),
                person.getDob(),
                person.getCreatedAt()
        );
    }

    private Person convertToEntity(PersonDTO personDTO) {
        Person person = new Person(
                personDTO.getTitle(),
                personDTO.getFirstName(),
                personDTO.getLastName(),
                personDTO.getDob()
        );
        person.setId(personDTO.getId());
        person.setCreatedAt(personDTO.getCreatedAt());
        return person;
    }

    private void updatePersonFromDTO(Person person, PersonDTO personDTO) {
        person.setTitle(personDTO.getTitle());
        person.setFirstName(personDTO.getFirstName());
        person.setLastName(personDTO.getLastName());
        person.setDob(personDTO.getDob());
    }
}