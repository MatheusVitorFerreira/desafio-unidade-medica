package com.clinica_medica_Desafio.Service.Exceptions;

public class DataAcessException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public DataAcessException(String msg) {
		super();
	}

	public DataAcessException(String msg, Throwable cause) {
		super(msg, cause);
	}

}