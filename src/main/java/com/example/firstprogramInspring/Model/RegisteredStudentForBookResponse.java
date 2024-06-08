package com.example.firstprogramInspring.Model;

public class RegisteredStudentForBookResponse {
	private String student_name;
	private String book_name;
	private String check_in_time;
	public String getCheck_in_time() {
		return check_in_time;
	}
	public void setCheck_in_time(String check_in_time) {
		this.check_in_time = check_in_time;
	}
	public RegisteredStudentForBookResponse() {
		
		// TODO Auto-generated constructor stub
	}
	public RegisteredStudentForBookResponse(String student_name, String book_name, String check_in_time) {
		super();
		this.student_name = student_name;
		this.book_name = book_name;
		this.check_in_time = check_in_time;
	}
	@Override
	public String toString() {
		return "RegisteredStudentForBookResponse [student_name=" + student_name + ", book_name=" + book_name
				+ ", check_in_time=" + check_in_time + "]";
	}
	public String getBook_name() {
		return book_name;
	}
	public void setBook_name(String book_name) {
		this.book_name = book_name;
	}
	public String getStudent_name() {
		return student_name;
	}
	public void setStudent_name(String student_name) {
		this.student_name = student_name;
	}
}
