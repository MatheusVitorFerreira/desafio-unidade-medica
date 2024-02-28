package com.clinica_medica_Desafio.Service.Exceptions;

public class DuplicateExecption extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public DuplicateExecption(String msg) {
		super();
	}

	public DuplicateExecption(String msg, Throwable cause) {
		super(msg, cause);
	}

}
