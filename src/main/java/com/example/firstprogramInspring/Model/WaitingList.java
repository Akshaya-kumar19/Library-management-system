package com.example.firstprogramInspring.Model;

public class WaitingList {
	private int book_id;
	private int student_id;
	public int getBook_id() {
		return book_id;
	}
	public void setBook_id(int book_id) {
		this.book_id = book_id;
	}
	public int getStudent_id() {
		return student_id;
	}
	public void setStudent_id(int student_id) {
		this.student_id = student_id;
	}
	public String getWaiting_flag() {
		return waiting_flag;
	}
	public void setWaiting_flag(String waiting_flag) {
		this.waiting_flag = waiting_flag;
	}
	private String waiting_flag;
	
	}

	
	
