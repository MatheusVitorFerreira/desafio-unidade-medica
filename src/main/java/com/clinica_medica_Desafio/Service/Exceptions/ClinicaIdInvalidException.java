package com.clinica_medica_Desafio.Service.Exceptions;

public class ClinicaIdInvalidException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ClinicaIdInvalidException(String msg) {
		super(msg);
	}

	public ClinicaIdInvalidException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
