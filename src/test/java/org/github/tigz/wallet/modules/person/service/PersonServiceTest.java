package org.github.tigz.wallet.modules.person.service;

import org.github.tigz.wallet.modules.person.dto.PersonDTO;
import org.github.tigz.wallet.modules.person.model.Person;
import org.github.tigz.wallet.modules.person.repository.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private PersonService personService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllPersons() {
        Person person1 = new Person("Mr", "John", "Doe", "1990-01-01");
        Person person2 = new Person("Ms", "Jane", "Doe", "1991-02-02");
        when(personRepository.findAll()).thenReturn(Arrays.asList(person1, person2));

        List<PersonDTO> result = personService.getAllPersons();

        assertEquals(2, result.size());
        verify(personRepository, times(1)).findAll();
    }

    @Test
    void getPersonById() {
        UUID id = UUID.randomUUID();
        Person person = new Person("Mr", "John", "Doe", "1990-01-01");
        person.setId(id);
        when(personRepository.findById(id)).thenReturn(Optional.of(person));

        Optional<PersonDTO> result = personService.getPersonById(id);

        assertTrue(result.isPresent());
        assertEquals(id, result.get().getId());
        verify(personRepository, times(1)).findById(id);
    }

    @Test
    void createPerson() {
        PersonDTO personDTO = new PersonDTO(null, "Mr", "John", "Doe", "1990-01-01", null);
        Person person = new Person("Mr", "John", "Doe", "1990-01-01");
        person.setId(UUID.randomUUID());
        when(personRepository.save(any(Person.class))).thenReturn(person);

        PersonDTO result = personService.createPerson(personDTO);

        assertNotNull(result.getId());
        assertEquals(personDTO.getFirstName(), result.getFirstName());
        verify(personRepository, times(1)).save(any(Person.class));
    }

    @Test
    void updatePerson() {
        UUID id = UUID.randomUUID();
        PersonDTO personDTO = new PersonDTO(id, "Mr", "John", "Doe", "1990-01-01", LocalDateTime.now());
        Person existingPerson = new Person("Mr", "John", "Smith", "1990-01-01");
        existingPerson.setId(id);
        when(personRepository.findById(id)).thenReturn(Optional.of(existingPerson));
        when(personRepository.save(any(Person.class))).thenReturn(existingPerson);

        Optional<PersonDTO> result = personService.updatePerson(id, personDTO);

        assertTrue(result.isPresent());
        assertEquals(personDTO.getLastName(), result.get().getLastName());
        verify(personRepository, times(1)).findById(id);
        verify(personRepository, times(1)).save(any(Person.class));
    }

    @Test
    void deletePerson() {
        UUID id = UUID.randomUUID();
        when(personRepository.existsById(id)).thenReturn(true);

        boolean result = personService.deletePerson(id);

        assertTrue(result);
        verify(personRepository, times(1)).existsById(id);
        verify(personRepository, times(1)).deleteById(id);
    }
}