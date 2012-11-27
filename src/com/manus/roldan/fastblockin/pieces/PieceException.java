package com.manus.roldan.fastblockin.pieces;

public class PieceException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String message;
	
	public PieceException(String message) {
		// TODO Auto-generated constructor stub
		this.message = message;
	}
	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return message;
	}
	
}
