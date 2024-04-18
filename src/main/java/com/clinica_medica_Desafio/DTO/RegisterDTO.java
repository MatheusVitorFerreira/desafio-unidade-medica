package com.clinica_medica_Desafio.DTO;



import com.clinica_medica_Desafio.model.UserRole;

import jakarta.validation.constraints.NotNull;

public record RegisterDTO (@NotNull String login,@NotNull String password,@NotNull UserRole role) {

}
