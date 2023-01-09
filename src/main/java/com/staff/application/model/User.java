package com.staff.application.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "users")
@SuperBuilder
@JsonInclude(NON_DEFAULT)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userId;
    private String name;
    private String username;
    private String password;
    private String role;
    private String[] authorities;
    private boolean isActive;
    private boolean isNotLocked;

}
