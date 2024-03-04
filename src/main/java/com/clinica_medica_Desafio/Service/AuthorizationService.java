package com.clinica_medica_Desafio.Service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.clinica_medica_Desafio.DTO.AuthenticationDTO;
import com.clinica_medica_Desafio.DTO.LoginResponseDTO;
import com.clinica_medica_Desafio.DTO.RegisterDTO;
import com.clinica_medica_Desafio.Repository.UserRepository;
import com.clinica_medica_Desafio.model.User;

import jakarta.validation.Valid;

@Service
public class AuthorizationService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TokenService tokenService;

	@Autowired
	private org.springframework.context.ApplicationContext context;

	@Override
	public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
		return userRepository.findByLogin(login);
	}

	public ResponseEntity<Object> login(@RequestBody @Valid AuthenticationDTO data) {
		AuthenticationManager authenticationManager = context.getBean(AuthenticationManager.class);
		var userNamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
		var auth = authenticationManager.authenticate(userNamePassword);
		var tokenResponse = tokenService.generateToken((User) auth.getPrincipal());
		LoginResponseDTO loginResponseDTO = new LoginResponseDTO(tokenResponse.getToken(),
				tokenResponse.getExpiration());
		return ResponseEntity.ok(loginResponseDTO);
	}

	public ResponseEntity<Object> register(@RequestBody @Valid RegisterDTO registerDTO) {
		if (this.userRepository.findByLogin(registerDTO.login()) != null) {
			return ResponseEntity.badRequest().build();
		}
		String encryptedPassword = new BCryptPasswordEncoder().encode(registerDTO.password());
		User newUser = new User(registerDTO.login(), encryptedPassword, registerDTO.role());
		newUser.setCreatedAt(new Date(System.currentTimeMillis()));
		this.userRepository.save(newUser);

		return ResponseEntity.ok().build();
	}
}
