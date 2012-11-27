package com.manus.roldan.fastblockin.protocol;

public class ProtocolException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String message;
	public ProtocolException(String message) {
		// TODO Auto-generated constructor stub
		this.message = message;
	}
	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return message;
	}
}
