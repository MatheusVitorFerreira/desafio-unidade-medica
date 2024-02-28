package com.clinica_medica_Desafio.Service.Exceptions;

public class InvalidCnpj extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InvalidCnpj(String msg) {
		super(msg);
	}

	public InvalidCnpj(String msg, Throwable cause) {
		super(msg, cause);
	}

}
