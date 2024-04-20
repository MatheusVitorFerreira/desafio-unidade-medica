package com.clinicamedicadesafio.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.clinicamedicadesafio.model.User;
import com.clinicamedicadesafio.model.UserRole;
import com.clinicamedicadesafio.repository.UserRepository;

@Service
public class DbService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public void instantiateDataBase() {
    	LocalDateTime now = LocalDateTime.now();
        String encodedPassword = passwordEncoder.encode("admin");
        User user1 = new User("admin", encodedPassword, UserRole.ADMIN,now);
        userRepository.save(user1);
    }
}
