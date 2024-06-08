package com.example.firstprogramInspring.Model;

public class CheckOutRequest {
	
	private String student_name;
	
	private int register_no;
	
	private String book_name;

	public CheckOutRequest() {
		// TODO Auto-generated constructor stub
	}

	public CheckOutRequest(String student_name, int register_no, String book_name) {
		super();
		this.student_name = student_name;
		this.register_no = register_no;
		this.book_name = book_name;
	}

	public String getStudent_name() {
		return student_name;
	}

	public void setStudent_name(String student_name) {
		this.student_name = student_name;
	}

	public int getRegister_no() {
		return register_no;
	}

	public void setRegister_no(int register_no) {
		this.register_no = register_no;
	}

	public String getBook_name() {
		return book_name;
	}

	public void setBook_name(String book_name) {
		this.book_name = book_name;
	}
}
