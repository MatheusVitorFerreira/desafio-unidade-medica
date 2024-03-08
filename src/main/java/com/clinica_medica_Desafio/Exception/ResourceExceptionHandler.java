package com.clinica_medica_Desafio.Exception;

import java.io.IOException;
import java.nio.file.AccessDeniedException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.clinica_medica_Desafio.Service.Exceptions.ClinicaIdInvalidException;
import com.clinica_medica_Desafio.Service.Exceptions.ClinicaNotFoundException;
import com.clinica_medica_Desafio.Service.Exceptions.DuplicateExecption;
import com.clinica_medica_Desafio.Service.Exceptions.EmptyField;
import com.clinica_medica_Desafio.Service.Exceptions.ErroInsercaoException;
import com.clinica_medica_Desafio.Service.Exceptions.EspecialidadeNotFoundException;
import com.clinica_medica_Desafio.Service.Exceptions.InvalidCnpj;
import com.clinica_medica_Desafio.Service.Exceptions.RegiaoNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@ControllerAdvice
public class ResourceExceptionHandler {

	@ExceptionHandler(ClinicaNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<StandardError> ClinicaNotFoundException(ClinicaNotFoundException e,
			HttpServletRequest request) {

		StandardError err = new StandardError(HttpStatus.NOT_FOUND.value(), e.getMessage(), System.currentTimeMillis());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
	}

	@ExceptionHandler(RegiaoNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<StandardError> RegiaoNotFoundException(RegiaoNotFoundException e,
			HttpServletRequest request) {

		StandardError err = new StandardError(HttpStatus.NOT_FOUND.value(), e.getMessage(), System.currentTimeMillis());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
	}

	@ExceptionHandler(ErroInsercaoException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<StandardError> ErroInsercaoException(ErroInsercaoException e, HttpServletRequest request) {

		StandardError err = new StandardError(HttpStatus.NOT_FOUND.value(), e.getMessage(), System.currentTimeMillis());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
	}

	@ExceptionHandler(EspecialidadeNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<StandardError> EspecialidadeNotFoundException(EspecialidadeNotFoundException e,
			HttpServletRequest request) {

		StandardError err = new StandardError(HttpStatus.NOT_FOUND.value(), e.getMessage(), System.currentTimeMillis());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
	}

	@ExceptionHandler(InvalidCnpj.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<StandardError> InvalidCnpj(InvalidCnpj e, HttpServletRequest request) {

		StandardError err = new StandardError(HttpStatus.BAD_REQUEST.value(), e.getMessage(),
				System.currentTimeMillis());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
	}

	@ExceptionHandler(ClinicaIdInvalidException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<StandardError> ClinicaIdInvalidException(ClinicaIdInvalidException e,
			HttpServletRequest request) {

		StandardError err = new StandardError(HttpStatus.NOT_ACCEPTABLE.value(), e.getMessage(),
				System.currentTimeMillis());
		return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(err);
	}

	@ExceptionHandler(EmptyField.class)
	@ResponseStatus(HttpStatus. BAD_REQUEST)
	public ResponseEntity<StandardError> EmptyField(EmptyField e, HttpServletRequest request) {
		StandardError err = new StandardError(HttpStatus.BAD_REQUEST.value(), e.getMessage(),
				System.currentTimeMillis());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
	}

	@ExceptionHandler(DuplicateExecption.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public ResponseEntity<StandardError> DuplicateExecption(DuplicateExecption e, HttpServletRequest request) {
		StandardError err = new StandardError(HttpStatus.CONFLICT.value(), e.getMessage(), System.currentTimeMillis());
		return ResponseEntity.status(HttpStatus.CONFLICT).body(err);
	}

	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		StandardError err = new StandardError(HttpStatus.FORBIDDEN.value(), "Acesso negado",
				System.currentTimeMillis());
		((HttpServletResponse) response).setStatus(HttpStatus.FORBIDDEN.value());
		response.setContentType("application/json");
		response.getWriter().write(new ObjectMapper().writeValueAsString(err));

	}
}
