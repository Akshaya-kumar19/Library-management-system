package com.example.firstprogramInspring.exception;

public class UpdateIdNotFound extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public UpdateIdNotFound()
	{
		
	}
	public UpdateIdNotFound(String msg)
	{
		super(msg);
	}

	
}
