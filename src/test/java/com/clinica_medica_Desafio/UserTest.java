package com.clinica_medica_Desafio;

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

import com.clinica_medica_Desafio.DTO.RegisterDTO;
import com.clinica_medica_Desafio.Repository.UserRepository;
import com.clinica_medica_Desafio.Service.TokenService;
import com.clinica_medica_Desafio.Service.UserService;
import com.clinica_medica_Desafio.model.User;
import com.clinica_medica_Desafio.model.UserRole;

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
