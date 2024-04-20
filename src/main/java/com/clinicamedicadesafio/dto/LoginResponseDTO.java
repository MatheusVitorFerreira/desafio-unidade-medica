package com.clinicamedicadesafio.dto;

public class LoginResponseDTO {
	public String token;
	public String expiration;

	public LoginResponseDTO() {
	}

	public LoginResponseDTO(String token, String expiration) {
		this.token = token;
		this.expiration = expiration;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getExpiration() {
		return expiration;
	}

	public void setExpiration(String expiration) {
		this.expiration = expiration;
	}
}