package com.clinicamedicadesafio.service.excepetion;

public class DuplicateExecption extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public DuplicateExecption(String msg) {
		 super(msg);
	}
}
