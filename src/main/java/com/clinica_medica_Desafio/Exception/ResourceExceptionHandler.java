package com.clinica_medica_Desafio.Exception;

import java.io.Serializable;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.clinica_medica_Desafio.Service.Exceptions.DuplicateExecption;
import com.clinica_medica_Desafio.Service.Exceptions.EmptyField;
import com.clinica_medica_Desafio.Service.Exceptions.EspecialidadeNotFoundException;
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
	@ExceptionHandler(EspecialidadeNotFoundException.class)
	public ResponseEntity<StandardError> especialidadeNotFound(EspecialidadeNotFoundException e,
			HttpServletRequest request) {
		StandardError err = new StandardError(System.currentTimeMillis(), HttpStatus.NOT_FOUND.value(),
				"Especialidade não encontrada", "Insira um Id valido", request.getRequestURI());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
	}
	@ExceptionHandler(EmptyField.class)
	public ResponseEntity<StandardError> FieldInvalide(EmptyField e, HttpServletRequest request) {
		StandardError err = new StandardError(System.currentTimeMillis(), HttpStatus.BAD_REQUEST.value(),
				"Campo Vázio ", e.getMessage(), request.getRequestURI());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
	}

	@ExceptionHandler(DuplicateExecption.class)
	public ResponseEntity<StandardError> DuplicateExecption(DuplicateExecption e, HttpServletRequest request) {
		StandardError err = new StandardError(System.currentTimeMillis(), HttpStatus.CONFLICT.value(),
				"Elemento Duplicado ", e.getMessage(), request.getRequestURI());
		return ResponseEntity.status(HttpStatus.CONFLICT).body(err);
	}
}
