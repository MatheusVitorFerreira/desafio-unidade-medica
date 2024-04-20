package com.clinicamedicadesafio.service.excepetion;

public class ClinicaNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ClinicaNotFoundException(String msg) {
		super(msg);
	}
}
