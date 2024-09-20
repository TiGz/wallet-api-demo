package org.github.tigz.wallet.modules.person.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class PersonDTO {
    private UUID id;
    private String title;
    private String firstName;
    private String lastName;
    private String dob;
    private LocalDateTime createdAt;

    // Default constructor
    public PersonDTO() {}

    // Constructor with all fields
    public PersonDTO(UUID id, String title, String firstName, String lastName, String dob, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
        this.createdAt = createdAt;
    }

    // Getters and setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}