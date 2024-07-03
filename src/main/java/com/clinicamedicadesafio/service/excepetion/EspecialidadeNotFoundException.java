package com.clinicamedicadesafio.service.excepetion;

public class EspecialidadeNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public EspecialidadeNotFoundException(String msg) {
		 super(msg);
	}
}
