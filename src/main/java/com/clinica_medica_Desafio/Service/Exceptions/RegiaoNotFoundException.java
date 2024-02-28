package com.clinica_medica_Desafio.Service.Exceptions;

public class RegiaoNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public RegiaoNotFoundException(String msg) {
		super(msg);
	}

	public RegiaoNotFoundException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
