package com.clinica_medica_Desafio.Service.Exceptions;

public class ClinicaNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ClinicaNotFoundException(String msg) {
		super(msg);
	}
}
