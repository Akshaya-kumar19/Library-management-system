package com.example.firstprogramInspring.firstprojectzInSpringboot;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.firstprogramInspring.DAO.LibraryDAO;
import com.example.firstprogramInspring.DAO.StudentDAO;
import com.example.firstprogramInspring.Model.BookData;
import com.example.firstprogramInspring.Model.DateRequestCheckin;
import com.example.firstprogramInspring.Model.MaxBookCountResponse;
import com.example.firstprogramInspring.Model.RegisteredStudentForBookResponse;
import com.example.firstprogramInspring.Model.ResponseData;
import com.example.firstprogramInspring.Model.StudentBookRegister;
import com.example.firstprogramInspring.Model.WaitingList;
import com.example.firstprogramInspring.service.LibraryService;
import com.example.firstprogramInspring.service.ScheduleMultiThread;
import com.example.firstprogramInspring.service.ValidationService;

@SpringBootTest
public class TestLibraryService {
	
	@Mock
	LibraryDAO libraryDAO;
	
	@Mock
	StudentDAO studentDAO;
	
	@Mock
	ScheduleMultiThread scheduleMultiThread;
	
	@Mock
	ValidationService validationService;
	
	@InjectMocks
	LibraryService libraryService;
	
	public TestLibraryService() {
		MockitoAnnotations.openMocks(this);
	}
	
	@Test
	public void testFindAllBooksSuccess() {
		List<BookData> list = new ArrayList<BookData>();
		list.add(new BookData("book 1", "author 1", 5, 10));
		list.add(new BookData("book 2", "author 2", 5, 10));
		
		when(libraryDAO.getAllBooks()).thenReturn(list);
		
		List<BookData> result = libraryService.findAllBooks();
		
		assertEquals(list, result);
	}
	
	@Test
	public void testFindBookByName() {
		BookData bookData = new BookData("book 1", "author 1", 5, 10);
		
		when(libraryDAO.checkBookName(anyString())).thenReturn(false);
		when(libraryDAO.getBookByName(anyString())).thenReturn(bookData);
		
		BookData data = libraryService.findBookByName("book 1");
		
		assertEquals(bookData, data);
	}
	
	@Test
	public void testFindBookByNameWhenBookNameNotFound() {
		BookData bookData = new BookData("book 1", "author 1", 5, 10);
		
		when(libraryDAO.checkBookName(anyString())).thenReturn(true);
		when(libraryDAO.getBookByName(anyString())).thenReturn(bookData);
		
		BookData data = libraryService.findBookByName("book 1");
		
		assertEquals(null, data);
	}
	
	@Test
	public void testFind_book_by_author() {
		List<BookData> list = new ArrayList<BookData>();
		list.add(new BookData("book 1", "author 1", 5, 10));
		list.add(new BookData("book 2", "author 1", 5, 10));
		
		when(libraryDAO.getBooksByAuthor(anyString())).thenReturn(list);
		List<BookData> book = libraryService.find_book_by_author("author 1");
		assertEquals(list, book);
		
	}
	
	
	//tests for the Insert book
	@Test
	public void TestInsertBookSuccess() {
//		BookData bookData = new BookData("book 1", "author 1", 10, -1);
//		when((new BookData("book 1", "author 1", 10, -1)).getQuantity()).thenReturn(10);
		when(libraryDAO.checkBookName(anyString())).thenReturn(true);
		when(libraryDAO.insertBook(any())).thenReturn(1);
		ResponseData<String> response = libraryService.insertBook(new BookData("book 1", "author 1", 10, -1));
		
		assertEquals("Book Inserted Successfully", response.getMsg());
		assertEquals("Success", response.getStatus());
		
	}
	
	@Test
	public void TestInsertBookQuantityIsLessThanZero() {
		when(libraryDAO.checkBookName(anyString())).thenReturn(true);
		when(libraryDAO.insertBook(any())).thenReturn(1);
		ResponseData<String> response = libraryService.insertBook(new BookData("book 1", "author 1", 0, -1));
		
		assertEquals("Quantity has to more than 0", response.getMsg());
		assertEquals("Failure", response.getStatus());
		
	}
	
	@Test
	public void testInsertBookAlreadyBookPresent() {
		when(libraryDAO.checkBookName(anyString())).thenReturn(false);
		ResponseData<String> response = libraryService.insertBook(new BookData("book 1", "author 1", 10, -1));
		
		assertEquals("Book is already in list", response.getMsg());
		assertEquals("Failure", response.getStatus());
		
	}
	
	@Test
	public void testInsertSomeValuesMissing() {
		when(libraryDAO.checkBookName(anyString())).thenReturn(true);
		when(libraryDAO.insertBook(any())).thenReturn(0);
		ResponseData<String> response = libraryService.insertBook(new BookData("book 1", "author 1", 10, -1));
		
		assertEquals("Some values are missing", response.getMsg());
		assertEquals("Failure", response.getStatus());
		
	}
	
	//test for the update quantity
	@Test
	public void testUpdateQuantityWhereQuantityIsLessThanZero() {
		int quantity = 0;
		int book_id = 1;
		
		ResponseData<String> responseData = libraryService.updateQuantity(quantity, book_id);
		assertEquals("Failure", responseData.getStatus());
		assertEquals("Quantity has to be more than 0 to be updated", responseData.getMsg());
	}
	
	@Test
	public void testUpdateQuantitySuccess() {
		int quantity = 1;
		int book_id = 1;
		
		when(libraryDAO.getListOfWaitingForBook(book_id)).thenReturn(new ArrayList<WaitingList>());
		when(libraryDAO.upgradeQuantity(quantity, book_id)).thenReturn(1);
		
		ResponseData<String> responseData= libraryService.updateQuantity(quantity, book_id);
		assertEquals("Quantity updated Successfully", responseData.getMsg());
		assertEquals("Success", responseData.getStatus());
		
	}
	@Test
	public void testUpdateQuantityBookIDNotFound() {
		int quantity = 1;
		int book_id = 1;
		
		when(libraryDAO.getListOfWaitingForBook(book_id)).thenReturn(new ArrayList<WaitingList>());
		when(libraryDAO.upgradeQuantity(quantity, book_id)).thenReturn(0);
		
		ResponseData<String> responseData= libraryService.updateQuantity(quantity, book_id);
		assertEquals("Book ID not found", responseData.getMsg());
		assertEquals("Failure", responseData.getStatus());
		
	}
	
	@Test
	public void testUpdateQuantityWaitingIsPresent() {
		int quantity = 1;
		int book_id = 1;
		List<WaitingList> list = new ArrayList<WaitingList>();
		list.add(new WaitingList());
		list.add(new WaitingList());
		list.get(0).setBook_id(1);
		list.get(0).setStudent_id(2);
		list.get(1).setBook_id(1);
		list.get(1).setStudent_id(2);
		
		when(libraryDAO.getQuantity(anyInt())).thenReturn(0);
		when(libraryDAO.getListOfWaitingForBook(book_id)).thenReturn(list);
		when(libraryDAO.upgradeQuantity(quantity, book_id)).thenReturn(0);
		
		ResponseData<String> responseData= libraryService.updateQuantity(quantity, book_id);
		assertEquals("Book ID not found", responseData.getMsg());
		assertEquals("Failure", responseData.getStatus());
		
	}
	
	@Test
	public void testUpdateQuantityWhereWaitingLessthanQuantity() {
		int quantity = 3;
		int book_id = 1;
		List<WaitingList> list = new ArrayList<WaitingList>();
		list.add(new WaitingList());
		list.add(new WaitingList());
		list.get(0).setBook_id(1);
		list.get(0).setStudent_id(2);
		list.get(1).setBook_id(1);
		list.get(1).setStudent_id(2);
		
		when(libraryDAO.getQuantity(anyInt())).thenReturn(0);
		when(libraryDAO.getListOfWaitingForBook(book_id)).thenReturn(list);
		when(libraryDAO.upgradeQuantity(quantity, book_id)).thenReturn(0);
		
		ResponseData<String> responseData= libraryService.updateQuantity(quantity, book_id);
		assertEquals("Book ID not found", responseData.getMsg());
		assertEquals("Failure", responseData.getStatus());
		
	}
	
	@Test
	public void testUpdateQuantityWhereWaitingEqualsQuantity() {
		int quantity = 2;
		int book_id = 1;
		List<WaitingList> list = new ArrayList<WaitingList>();
		list.add(new WaitingList());
		list.add(new WaitingList());
		list.get(0).setBook_id(1);
		list.get(0).setStudent_id(2);
		list.get(1).setBook_id(1);
		list.get(1).setStudent_id(2);
		
		when(libraryDAO.getQuantity(anyInt())).thenReturn(0);
		when(libraryDAO.getListOfWaitingForBook(book_id)).thenReturn(list);
		when(libraryDAO.upgradeQuantity(quantity, book_id)).thenReturn(0);
		
		ResponseData<String> responseData= libraryService.updateQuantity(quantity, book_id);
		assertEquals("Book ID not found", responseData.getMsg());
		assertEquals("Failure", responseData.getStatus());
		
	}
	
	//tests for the get Student with book
	@Test
	public void testGetStudent_book_register() {
		List<StudentBookRegister> list = new ArrayList<StudentBookRegister>();
		
		list.add(new StudentBookRegister());
		list.add(new StudentBookRegister());
		
		list.get(0).setBook_id(1);
		list.get(0).setCheck_in_time("string");
		list.get(0).setStudent_id(2);
		
		list.get(1).setBook_id(1);
		list.get(1).setCheck_in_time("string 1");
		list.get(1).setStudent_id(2);
		
		when(libraryDAO.getStudentBookRegister()).thenReturn(list);
		when(studentDAO.getStudentNameByStudentID(anyInt())).thenReturn("student name");
		when(libraryDAO.getBookNameUsingBookID(anyInt())).thenReturn("book name");
		List<RegisteredStudentForBookResponse> expected = new ArrayList<RegisteredStudentForBookResponse>();
		expected.add(new RegisteredStudentForBookResponse("student name", "book name", "string"));
		expected.add(new RegisteredStudentForBookResponse("student name", "book name", "string 1"));
		List<RegisteredStudentForBookResponse> bookResponses = libraryService.getStudent_book_register();
//		assertEquals(expected, bookResponses);
		assertThat(bookResponses).usingFieldByFieldElementComparator().containsExactlyElementsOf(expected);
		
		
	}
	
	//test for max to min date to get the max book between
	@Test
	public void testGet_max_book_checkin_with_dateSuccess() {
		LocalDate time = LocalDate.now();
		when(validationService.isValidDate((time.minusDays(1)).toString())).thenReturn(true);
		when(validationService.isValidDate(time.toString())).thenReturn(true);
		when(validationService.check_min_max_date_format(any(),any())).thenReturn(true);
//        when(validationService.check_min_max_date_format(time, time)).thenReturn(true);
		DateRequestCheckin requestCheckin = new DateRequestCheckin();
        requestCheckin.setMax_date(time.toString());
        requestCheckin.setMin_date((time.minusDays(1)).toString());
        
        List<Map<String, Integer>> list = new ArrayList<Map<String,Integer>>();
        list.add(new HashMap<String, Integer>());
        list.get(0).put("book_id", 1);
        list.get(0).put("book_count", 12);
        when(libraryDAO.getBookCountByBookID(requestCheckin)).thenReturn(list);
        when(libraryDAO.getBookNameUsingBookID(1)).thenReturn("bookname");
        
        
        ResponseData<MaxBookCountResponse> response = libraryService.get_max_book_checkin_with_date(requestCheckin);
        
        assertEquals("Success", response.getStatus());
        assertEquals("bookname", response.getData().getBook_name());
        assertEquals("Data Retreived", response.getMsg());
        
	}
	
	@Test
	public void testGet_max_book_checkin_with_date_not_valid_date() {
		String date1 = "any stirng";
		String date2 = "any string";
		when(validationService.isValidDate(date1)).thenReturn(false);
		when(validationService.isValidDate(date2)).thenReturn(true);
		DateRequestCheckin requestCheckin = new DateRequestCheckin();
        requestCheckin.setMax_date(date1);
        requestCheckin.setMin_date(date2);
        ResponseData<MaxBookCountResponse> response = libraryService.get_max_book_checkin_with_date(requestCheckin);
        assertEquals("Failure", response.getStatus());
        assertEquals(null, response.getData());
        assertEquals("Enter the correct date format(yyyy-MM-dd)", response.getMsg());
        
	}
	
	@Test
	public void testGet_max_book_checkin_with_two_not_valid_date() {
		String date1 = "any stirng";
		String date2 = "any string";
		when(validationService.isValidDate(date1)).thenReturn(false);
		when(validationService.isValidDate(date2)).thenReturn(false);
		DateRequestCheckin requestCheckin = new DateRequestCheckin();
        requestCheckin.setMax_date(date1);
        requestCheckin.setMin_date(date2);
        ResponseData<MaxBookCountResponse> response = libraryService.get_max_book_checkin_with_date(requestCheckin);
        assertEquals("Failure", response.getStatus());
        assertEquals(null, response.getData());
        assertEquals("Enter the correct date format(yyyy-MM-dd)", response.getMsg());
        
	}
	
	@Test
	public void testGet_max_book_checkin_with_one_not_valid_date() {
		String date1 = "any stirng";
		String date2 = "any string";
		when(validationService.isValidDate(date1)).thenReturn(true);
		when(validationService.isValidDate(date2)).thenReturn(false);
		DateRequestCheckin requestCheckin = new DateRequestCheckin();
        requestCheckin.setMax_date(date1);
        requestCheckin.setMin_date(date2);
        ResponseData<MaxBookCountResponse> response = libraryService.get_max_book_checkin_with_date(requestCheckin);
        assertEquals("Failure", response.getStatus());
        assertEquals(null, response.getData());
        assertEquals("Enter the correct date format(yyyy-MM-dd)", response.getMsg());
        
	}
	
	@Test
	public void testGet_max_book_checkin_max_date_less_than_min_date() {
		LocalDate time = LocalDate.now();
		when(validationService.isValidDate((time.minusDays(1)).toString())).thenReturn(true);
		when(validationService.isValidDate(time.toString())).thenReturn(true);
		when(validationService.check_min_max_date_format(time, time.minusDays(1))).thenReturn(false);
		DateRequestCheckin requestCheckin = new DateRequestCheckin();
        requestCheckin.setMax_date(time.toString());
        requestCheckin.setMin_date(time.minusDays(1).toString());
        ResponseData<MaxBookCountResponse> response = libraryService.get_max_book_checkin_with_date(requestCheckin);
        assertEquals("Failure", response.getStatus());
        assertEquals(null, response.getData());
        assertEquals("Min_Date cannot be more than the Max_date", response.getMsg());
		
	}
	
	@Test
	public void testGet_max_book_checkin_max_date_more_than_today() {
		 when(validationService.isValidDate(any())).thenReturn(true);
		    DateRequestCheckin requestCheckin = new DateRequestCheckin();
		    LocalDate today = LocalDate.now();
		    requestCheckin.setMax_date(today.plusDays(1).toString());
		    requestCheckin.setMin_date(today.minusDays(1).toString());
		    when(validationService.check_min_max_date_format(any(), any())).thenReturn(true, false);
		    ResponseData<MaxBookCountResponse> response = libraryService.get_max_book_checkin_with_date(requestCheckin);

		    assertEquals("max_date cannot be more than today", response.getMsg());
		    assertEquals("Failure", response.getStatus());
		    assertNull(response.getData());
		
	}
	
	@Test
	public void testGet_max_book_checkin_no_entry_between_dates() {
		when(validationService.isValidDate(any())).thenReturn(true);
	    DateRequestCheckin requestCheckin = new DateRequestCheckin();
	    LocalDate today = LocalDate.now();
	    requestCheckin.setMax_date(today.plusDays(1).toString());
	    requestCheckin.setMin_date(today.minusDays(1).toString());
	    when(validationService.check_min_max_date_format(any(), any())).thenReturn(true, true);
	    List<Map<String, Integer>> list = Collections.emptyList();
	    when(libraryDAO.getBookCountByBookID(requestCheckin)).thenReturn(list);
	    
	    ResponseData<MaxBookCountResponse> response = libraryService.get_max_book_checkin_with_date(requestCheckin);

	    assertEquals("There is no book given between that date range", response.getMsg());
	    assertEquals("Success", response.getStatus());
	    assertNull(response.getData());
	}
	
	@Test
	public void testGet_max_avail_book() {
		List<BookData> list = new ArrayList<BookData>();
		list.add(new BookData("book 1", "author_name", 10,8));
		when(libraryDAO.getMaxAvailableBook()).thenReturn(list);
		
		List<BookData> result= libraryService.get_max_avail_book();
		
		assertEquals(list, result);
	}
	
	@Test
	public void testGet_min_avail_book() {
		List<BookData> list = new ArrayList<BookData>();
		list.add(new BookData("book 1", "author_name", 10,8));
		when(libraryDAO.getMinimumAvailableBook()).thenReturn(list);
		
		List<BookData> result= libraryService.get_min_avail_book();
		
		assertEquals(list, result);
	}
	
	@Test
	public void testGet_max_waiting_book(){
		BookData book = new BookData("book 1", "author_name", 10,8);
		
		when(libraryDAO.getBookIDOfMaxWaiting()).thenReturn(1);
		when(libraryDAO.getBookDataByBookID(anyInt())).thenReturn(book);
		
		BookData result= libraryService.get_max_waiting_book();
		
		assertEquals(book, result);
	}
	
	@Test
	public void testGet_min_waiting_book(){
		BookData book = new BookData("book 1", "author_name", 10,8);
		
		when(libraryDAO.getBookIdOfMinimumWaiting()).thenReturn(1);
		when(libraryDAO.getBookDataByBookID(anyInt())).thenReturn(book);
		
		BookData result= libraryService.get_min_waiting_book();
		
		assertEquals(book, result);
	}
	
	@Test
	public void testGet_avail_books(){
		BookData book = new BookData("book 1", "author_name", 10,8);
		List<BookData> books = new ArrayList<BookData>();
		books.add(book);
		when(libraryDAO.getAvailableBooks()).thenReturn(books);
		
		List<BookData> result= libraryService.get_avail_books();
		
		assertEquals(books, result);
	}
	
	
}
