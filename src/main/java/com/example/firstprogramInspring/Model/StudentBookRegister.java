package com.example.firstprogramInspring.Model;

public class StudentBookRegister {
	private int book_issue_id;
	private int student_id;
	private int book_id;
	private String check_in_time;
	private String check_out_time;
	private int time_left;
	public int getStudent_id() {
		return student_id;
	}
	public void setStudent_id(int student_id) {
		this.student_id = student_id;
	}
	public int getBook_id() {
		return book_id;
	}
	public void setBook_id(int book_id) {
		this.book_id = book_id;
	}
	public String getCheck_in_time() {
		return check_in_time;
	}
	public void setCheck_in_time(String check_in_time) {
		this.check_in_time = check_in_time;
	}
	public String getCheck_out_time() {
		return check_out_time;
	}
	public void setCheck_out_time(String check_out_time) {
		this.check_out_time = check_out_time;
	}
	public int getTime_left() {
		return time_left;
	}
	public void setTime_left(int time_left) {
		this.time_left = time_left;
	}
	public int getBook_issue_id() {
		return book_issue_id;
	}
	public void setBook_issue_id(int book_issue_id) {
		this.book_issue_id = book_issue_id;
	}
	@Override
	public String toString() {
		return "Student_book_register [book_issue_id=" + book_issue_id + ", student_id=" + student_id + ", book_id="
				+ book_id + ", check_in_time=" + check_in_time + ", check_out_time=" + check_out_time + ", time_left="
				+ time_left + "]";
	}
	
	
}
