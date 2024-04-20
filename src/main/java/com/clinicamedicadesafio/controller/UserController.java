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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RestController
@RequestMapping("user")
public class UserController {

	private final UserService userService;

	public UserController(TokenService tokenService, UserService userService) {
		this.userService = userService;
	}

	@Operation(summary = "Login de usuário")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Login bem-sucedido"),
			@ApiResponse(responseCode = "400", description = "Requisição inválida"),
			@ApiResponse(responseCode = "401", description = "Credenciais inválidas"),
			@ApiResponse(responseCode = "500", description = "Erro interno no servidor") })
	@PostMapping("/login")
	public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid UserDTO userDTO) {
		return userService.login(userDTO);
	}

	@Operation(summary = "Registro de novo usuário")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Registro bem-sucedido"),
			@ApiResponse(responseCode = "400", description = "Requisição inválida"),
			@ApiResponse(responseCode = "500", description = "Erro interno no servidor") })
	@PostMapping("/register")
	public ResponseEntity<RegisterDTO> register(@RequestBody @Valid RegisterDTO registerDTO) {
		return userService.register(registerDTO);
	}
}
