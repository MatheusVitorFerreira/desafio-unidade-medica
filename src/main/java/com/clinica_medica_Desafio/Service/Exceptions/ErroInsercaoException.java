package com.clinica_medica_Desafio.Service.Exceptions;

public class ErroInsercaoException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ErroInsercaoException(String msg) {
		 super(msg);
	}

	public ErroInsercaoException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
