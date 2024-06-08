package com.example.firstprogramInspring.Model;

import java.util.List;

public class CheckingInRequest {

	private String student_name;
	private int register_id;
	private int no_of_books;
	private List<String> book_name;
	public String getStudent_name() {
		return student_name;
	}
	public void setStudent_name(String student_name) {
		this.student_name = student_name;
	}
	public int getRegister_id() {
		return register_id;
	}
	public void setRegister_id(int register_id) {
		this.register_id = register_id;
	}
	public List<String> getBook_name() {
		return book_name;
	}
	public void setBook_name(List<String> book_name) {
		this.book_name = book_name;
	}
	public int getNo_of_books() {
		return no_of_books;
	}
	public void setNo_of_books(int no_of_books) {
		this.no_of_books = no_of_books;
	}
}
