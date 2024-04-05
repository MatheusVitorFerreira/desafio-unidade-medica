package com.clinica_medica_Desafio.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clinica_medica_Desafio.DTO.UserDTO;
import com.clinica_medica_Desafio.DTO.RegisterDTO;
import com.clinica_medica_Desafio.Service.UserService;
import com.clinica_medica_Desafio.Service.TokenService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("auth")
public class AuthenticationController { 
	@Autowired
    private UserService authorizationService;
    
    @Autowired
    TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody @Valid UserDTO authenticationDTO){
        return authorizationService.login(authenticationDTO);
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody @Valid RegisterDTO registerDTO) {
        return authorizationService.register(registerDTO);
    }
    
    @GetMapping("/login") 
    public String showLoginForm() {
        return "login"; 
    }
}
