package com.example.firstprogramInspring.Model;

/**
 * {@code Book_Data} is the book_list data for the student database.
 * 
 * 
 * 
 * 
 * */
//@JsonInclude(value = Include.NON_NULL)
public class BookData {
	private int book_id;
	private String book_name;
	private String author_name;
	private int quantity;
	private Integer read_time;
	public int getBook_id() {
		return book_id;
	}
	public void setBook_id(int book_id) {
		this.book_id = book_id;
	}
	public String getBook_name() {
		return book_name;
	}
	public void setBook_name(String book_name) {
		this.book_name = book_name;
	}
	public String getAuthor_name() {
		return author_name;
	}
	public void setAuthor_name(String author_name) {
		this.author_name = author_name;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	
	
	public BookData() {
		super();
	}
	public BookData(String book_name, String author_name, int quantity, int read_time) {
		super();
		this.book_name = book_name;
		this.author_name = author_name;
		this.quantity = quantity;
		this.read_time = read_time;
	}
	
	@Override
	public String toString() {
		return "Book_Data [book_id=" + book_id + ", book_name=" + book_name + ", author_name=" + author_name
				+ ", quantity=" + quantity + "]";
	}
	public int getRead_time() {
		return read_time == null ? -1 : read_time;
	}
	public void setRead_time(Integer read_time) {
		this.read_time = read_time;
	}
	
	
}
