package com.clinica_medica_Desafio.Exception;

import java.io.Serializable;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.clinica_medica_Desafio.Service.Exceptions.InvalidCnpj;

import jakarta.servlet.http.HttpServletRequest;

public class ResourceExceptionHandler implements Serializable {
	private static final long serialVersionUID = 1L;

	@ExceptionHandler(InvalidCnpj.class)
	public ResponseEntity<StandardError> invalidCnpj(InvalidCnpj e, HttpServletRequest request) {
		StandardError err = new StandardError(System.currentTimeMillis(), HttpStatus.BAD_REQUEST.value(),
				"CNPJ Inválido", e.getMessage(), request.getRequestURI());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
	}
}
