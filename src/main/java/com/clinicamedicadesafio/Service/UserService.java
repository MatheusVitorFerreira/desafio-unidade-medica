package com.clinicamedicadesafio.service;

import java.time.LocalDateTime;
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

import com.clinicamedicadesafio.dto.LoginResponseDTO;
import com.clinicamedicadesafio.dto.RegisterDTO;
import com.clinicamedicadesafio.dto.UserDTO;
import com.clinicamedicadesafio.model.User;
import com.clinicamedicadesafio.repository.UserRepository;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;

@Service
public class UserService implements UserDetailsService {

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

	public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid UserDTO data) {
		AuthenticationManager authenticationManager = context.getBean(AuthenticationManager.class);
		var userNamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
		var auth = authenticationManager.authenticate(userNamePassword);
		var tokenResponse = tokenService.generateToken((User) auth.getPrincipal());
		LoginResponseDTO loginResponseDTO = new LoginResponseDTO(tokenResponse.getToken(),
				tokenResponse.getExpiration());
		return ResponseEntity.ok(loginResponseDTO);
	}

	public ResponseEntity<RegisterDTO> register(@RequestBody @Valid RegisterDTO registerDTO) {
	    if (this.userRepository.findByLogin(registerDTO.login()) != null) {
	        return ResponseEntity.badRequest().build();
	    }
	    String encryptedPassword = new BCryptPasswordEncoder().encode(registerDTO.password());
	    LocalDateTime now = LocalDateTime.now();
	    User newUser = new User(registerDTO.login(), encryptedPassword, registerDTO.role(), now);
	    this.userRepository.save(newUser);

	    return ResponseEntity.ok().build();
	}


}
