package com.clinicamedicadesafio.service.excepetion;

public class EmptyField extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public EmptyField(String msg) {
		super(msg);
	}
}
