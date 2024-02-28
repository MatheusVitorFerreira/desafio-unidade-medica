package com.clinica_medica_Desafio.Service.Exceptions;

public class EspecialidadeNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public EspecialidadeNotFoundException(String msg) {
		super();
	}

	public EspecialidadeNotFoundException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
