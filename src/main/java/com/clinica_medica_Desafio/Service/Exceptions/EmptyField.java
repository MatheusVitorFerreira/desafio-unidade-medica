package com.clinica_medica_Desafio.Service.Exceptions;

public class EmptyField extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public EmptyField(String msg) {
		super(msg);
	}
}
