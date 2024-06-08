package com.example.firstprogramInspring.exception;

public class StudentIdNotFound extends RuntimeException{
	
	private static final long serialVersionUID = 1L;
	
	public StudentIdNotFound()
	{
		
	}
	
	public StudentIdNotFound(String msg)
	{
		super(msg);
	}

}