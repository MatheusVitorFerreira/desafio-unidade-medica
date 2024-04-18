package com.clinica_medica_Desafio.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clinica_medica_Desafio.DTO.LoginResponseDTO;
import com.clinica_medica_Desafio.DTO.RegisterDTO;
import com.clinica_medica_Desafio.DTO.UserDTO;
import com.clinica_medica_Desafio.Service.TokenService;
import com.clinica_medica_Desafio.Service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("auth")
public class AuthenticationController {

    private final UserService userService;

    public AuthenticationController(TokenService tokenService, UserService userService) {
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

	@GetMapping("/login")
	public String showLoginForm() {
		return "login";
	}
}
