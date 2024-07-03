package com.clinicamedicadesafio.service.excepetion;

public class ClinicaIdInvalidException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ClinicaIdInvalidException(String msg) {
		super(msg);
	}

}
