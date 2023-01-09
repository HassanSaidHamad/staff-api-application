package com.staff.application.service.impl;

import com.staff.application.enumeration.Role;
import com.staff.application.model.User;
import com.staff.application.model.UserPrincipal;
import com.staff.application.repository.UserRepository;
import com.staff.application.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.staff.application.enumeration.Role.ROLE_USER;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User user = userRepository.findUserByUsername(username);
//        if (user == null) {
//            log.error("NO_USER_FOUND_BY_USERNAME" + username);
//            throw new UsernameNotFoundException("NO_USER_FOUND_BY_USERNAME" + username);
//        } else {
//
//            UserPrincipal userPrincipal = new UserPrincipal(user);
//            log.info("FOUND_USER_BY_USERNAME" + username);
//
//            return userPrincipal;
//
//        }
//    }


    @Override
    public User register(String name, String username, String password) {
        User user = new User();
        user.setUserId(generateUserId());
        user.setName(name);
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(ROLE_USER.name());
        user.setAuthorities(ROLE_USER.getAuthorities());
        user.setActive(true);
        user.setNotLocked(true);
        userRepository.save(user);
        log.info("New user registered: {}", user.getUsername() + " with password: " + password);
        return user;
    }

    private Role getRoleEnum(String role) {
        return Role.valueOf(role.toUpperCase());
    }

    private String generateUserId() {
        return RandomStringUtils.randomNumeric(10);
    }

    @Override
    public User addNewUser(String name, String username, String password, String role, String isActive, String isNotLocked) {
        return null;
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }
}
