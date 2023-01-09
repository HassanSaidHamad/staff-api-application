package com.staff.application.service;


import com.staff.application.model.User;

public interface UserService {
    User register(String name, String username, String password);
    User addNewUser(String name, String username, String password, String role, String isActive, String isNotLocked);

    User findUserByUsername(String username);
}
