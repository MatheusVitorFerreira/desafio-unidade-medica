package com.clinicamedicadesafio.service.excepetion;

public class InvalidCnpj extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InvalidCnpj(String msg) {
		super(msg);
	}

}
