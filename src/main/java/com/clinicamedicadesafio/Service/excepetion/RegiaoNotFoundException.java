package com.clinicamedicadesafio.service.excepetion;

public class RegiaoNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public RegiaoNotFoundException(String msg) {
		super(msg);
	}
}
