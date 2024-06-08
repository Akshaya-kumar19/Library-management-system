package com.example.firstprogramInspring.DAO;

import java.util.List;
import java.util.Map;

import com.example.firstprogramInspring.Model.BookData;
import com.example.firstprogramInspring.Model.DateRequestCheckin;
import com.example.firstprogramInspring.Model.StudentBookRegister;
import com.example.firstprogramInspring.Model.WaitingList;

public interface LibraryDAO {
	public int insertBook(BookData book);
	
	public int upgradeQuantity(int quantity, int book_id);
	
	public List<BookData> getAllBooks();
	
	public BookData getBookByName(String book_name);
	
	public List<BookData> getBooksByAuthor(String author_name);
	
	public boolean checkBookName(String bookname);
	
	//public int studentCheckingIn(int student_id, int book_id);

	public boolean checkStduentIDUsingNameRegisterNO(String student_name, int register_no);
	
	public boolean checkBookIDUsingBookName(String book_name);
	
	public int getStudentIDUsingNameRegisterNO(String student_name, int register_no);
	
	public int getBookIDUsingBookName(String book_name);
	
	public int insertStudentInWaitingList(WaitingList waitingStudent);
	
	public int getQuantity(int book_id);
	
	public int insertDataStudentBookRegister(StudentBookRegister registerStudent);
	
	public boolean checkWaitingStudentEntry(int student_id , int book_id);
	
	public boolean checkForStudentCheckInRegister(int student_id, int book_id);
	
	public String getBookNameUsingBookID(int book_id);
	
	public List<StudentBookRegister> getStudentBookRegister();
	
	public boolean checkForEntryInStudentBookRegister(int student_id,int book_id);
	
	public int setCheckoutTime(int student_id, int book_id);
	
	public boolean checkForBookReturn(int student_id, int book_id);
	
	public List<WaitingList> getListOfWaitingForBook(int book_id);
	
	int changeWaitingFlag(int student_id, int book_id);
	
	
	public List<Map<String, Integer>> getBookCountByBookID(DateRequestCheckin request);

	List<BookData> getMaxAvailableBook();

	List<BookData> getMinimumAvailableBook();

	int getBookIDOfMaxWaiting();

	BookData getBookDataByBookID(int book_id);

	int getBookIdOfMinimumWaiting();

	List<BookData> getAvailableBooks();

	Integer getReadTime(int book_id);

	int setCheckoutTime(int book_issue_no);

	List<StudentBookRegister> getListOfOngoingReading();

	int updateTimeLeft(int timeleft, int book_issue_id);

	public StudentBookRegister findCheckoutById(int bookIssueId);

	
}
