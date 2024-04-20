package com.clinicamedicadesafio.dto;



import com.clinicamedicadesafio.model.UserRole;

import jakarta.validation.constraints.NotNull;

public record RegisterDTO (@NotNull String login,@NotNull String password,@NotNull UserRole role) {

}
