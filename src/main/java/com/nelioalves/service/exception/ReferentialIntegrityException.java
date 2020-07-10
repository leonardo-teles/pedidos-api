package com.nelioalves.service.exception;

public class ReferentialIntegrityException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public ReferentialIntegrityException(String msg) {
		super(msg);
	}
	
	public ReferentialIntegrityException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
