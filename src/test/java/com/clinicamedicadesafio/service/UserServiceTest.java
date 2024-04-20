package com.clinicamedicadesafio.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;

import com.clinicamedicadesafio.dto.RegisterDTO;
import com.clinicamedicadesafio.model.User;
import com.clinicamedicadesafio.model.UserRole;
import com.clinicamedicadesafio.repository.UserRepository;

class UserServiceTest {

    @Mock
    private TokenService tokenService;
    
    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegister() {
        RegisterDTO registerDTO = new RegisterDTO("username", "password",UserRole.ADMIN);
        when(userRepository.findByLogin("username")).thenReturn(null);
        ResponseEntity<RegisterDTO> responseEntity = userService.register(registerDTO);
        verify(userRepository, times(1)).save(any(User.class));
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
}
