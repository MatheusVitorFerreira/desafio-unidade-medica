package com.clinicamedicadesafio.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clinicamedicadesafio.dto.LoginResponseDTO;
import com.clinicamedicadesafio.dto.RegisterDTO;
import com.clinicamedicadesafio.dto.UserDTO;
import com.clinicamedicadesafio.service.TokenService;
import com.clinicamedicadesafio.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("user")
public class UserController {

    private final UserService userService;

    public UserController(TokenService tokenService, UserService userService) {
        this.userService = userService;
    }

	@PostMapping("/login")
	public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid UserDTO userDTO) {
		return userService.login(userDTO);
	}

	@PostMapping("/register")
	public ResponseEntity<RegisterDTO> register(@RequestBody @Valid RegisterDTO registerDTO) {
		return userService.register(registerDTO);
	}
}
