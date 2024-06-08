package com.example.firstprogramInspring.exception;

public class InvalidGender extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public InvalidGender()
	{
		
	}
	public InvalidGender(String msg)
	{
		super(msg);
	}
}
