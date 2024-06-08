package com.example.firstprogramInspring.firstprojectzInSpringboot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.firstprogramInspring.Model.BookData;
import com.example.firstprogramInspring.Model.CheckOutRequest;
import com.example.firstprogramInspring.Model.CheckingInRequest;
import com.example.firstprogramInspring.Model.DateRequestCheckin;
import com.example.firstprogramInspring.Model.MaxBookCountResponse;
import com.example.firstprogramInspring.Model.RegisteredStudentForBookResponse;
import com.example.firstprogramInspring.Model.ResponseData;
import com.example.firstprogramInspring.controller.LibraryController;
import com.example.firstprogramInspring.service.CheckInService;
import com.example.firstprogramInspring.service.CheckOutService;
import com.example.firstprogramInspring.service.LibraryService;

@SpringBootTest
public class TestLibraryController {
	
	
	@Mock
	LibraryService libraryService;
	@Mock
	CheckInService checkInService;
	@Mock
	CheckOutService checkOutService;
	@InjectMocks
	LibraryController libraryController;
	
	public TestLibraryController() {
		MockitoAnnotations.openMocks(this);;
	}
	
	
	
	//tests for get book by name
	@Test
	public void testGetBookByNameSuccess() {
		String bookname = "test book ";
		BookData book = new BookData ("test book", "test author", 5, 10);
		when(libraryService.findBookByName(bookname)).thenReturn(book);
		
		ResponseEntity<ResponseData<BookData>> responseEntity = libraryController.getBookByName(bookname);
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Data retrieved", responseEntity.getBody().getMsg());
        assertEquals("Success", responseEntity.getBody().getStatus());
        assertEquals(book, responseEntity.getBody().getData());
	}
	
	@Test
	public void testGetBookByNameEmptyDataAccessException() {
		String bookname = "Non existence book";
		when(libraryService.findBookByName(bookname)).thenThrow(EmptyResultDataAccessException.class);
		
		ResponseEntity<ResponseData<BookData>> responseEntity = libraryController.getBookByName(bookname);
		
		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Data Not retrieved", responseEntity.getBody().getMsg());
        assertEquals("Error occured", responseEntity.getBody().getStatus());
       
	}
	
	@Test
	public void testGetBookByNameAnyException() {
		 String bookname = "Test Book";
	     String errorMessage = "An error occurred";
		when(libraryService.findBookByName(anyString())).thenThrow(new RuntimeException(errorMessage));
		
		ResponseEntity<ResponseData<BookData>> responseEntity = libraryController.getBookByName(bookname);
		
		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(errorMessage, responseEntity.getBody().getMsg());
        assertEquals("Error occured", responseEntity.getBody().getStatus());
		
		
	}
	
	//tests for get book by author name
	@Test
	public void testGetBookByAuthorNameSuccess() {
		BookData request = new  BookData();
		request.setAuthor_name("test author");
		String authorName = "test author";
		List<BookData> books = new ArrayList<BookData>();
		books.add(new BookData ("test book", "test author", 5, 10));
				
		when(libraryService.find_book_by_author(authorName)).thenReturn(books);
		ResponseEntity<ResponseData<List<BookData>>> responseEntity = libraryController.getBookByAuthor(request);
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Data retrieved", responseEntity.getBody().getMsg());
        assertEquals("Success", responseEntity.getBody().getStatus());
        assertEquals(books, responseEntity.getBody().getData());
		
	}
	
	
	@Test
	public void testGetBookByAuthorNameDataAccessException() {
		 BookData request = new BookData();
	        request.setAuthor_name("AuthorName");

	        
	        when(libraryService.find_book_by_author("AuthorName")).thenThrow(new DataAccessException("Database Error") {

				private static final long serialVersionUID = 1L;});

	       
	        ResponseEntity<ResponseData<List<BookData>>> responseEntity = libraryController.getBookByAuthor(request);

	       
	        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
	        assertEquals("Database Error", responseEntity.getBody().getMsg());
	        assertEquals("Error occured", responseEntity.getBody().getStatus());
	        assertEquals(null, responseEntity.getBody().getData());
	}
	@Test
	public void testGetBookByAuthorNameException() {
		 BookData request = new BookData();
	        request.setAuthor_name("AuthorName");

	        
	        when(libraryService.find_book_by_author("AuthorName")).thenThrow(new RuntimeException("throws runtime"));

	       
	        ResponseEntity<ResponseData<List<BookData>>> responseEntity = libraryController.getBookByAuthor(request);

	       
	        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
	        assertEquals("Data Not retrieved", responseEntity.getBody().getMsg());
	        assertEquals("Error occured", responseEntity.getBody().getStatus());
	        assertEquals(null, responseEntity.getBody().getData());
	}
	
	//test to get all books
	@Test
	public void testGetAllBooksSuccess() {
		List<BookData> books = new ArrayList<BookData>();
		books.add(new BookData ("test book", "test author", 5, 10));
		books.add(new BookData ("test book2", "test author2", 5, 10));
		
		when(libraryService.findAllBooks()).thenReturn(books);
		
		ResponseEntity<ResponseData<List<BookData>>> responseEntity = libraryController.getAllBooks();
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Data retrieved", responseEntity.getBody().getMsg());
        assertEquals("Success", responseEntity.getBody().getStatus());
        assertEquals(books, responseEntity.getBody().getData());
        assertEquals(books.size(), responseEntity.getBody().getCount());
		
	}
	
	@Test
	public void testGetAllBooksDataAccessException() {
		when(libraryService.findAllBooks()).thenThrow(new DataAccessException("DataBase Error") {

			private static final long serialVersionUID = 1L;});
		ResponseEntity<ResponseData<List<BookData>>> responseEntity = libraryController.getAllBooks();
		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("DataBase Error", responseEntity.getBody().getMsg());
        assertEquals("Error occured", responseEntity.getBody().getStatus());
        assertEquals(null, responseEntity.getBody().getData());
        assertEquals(0, responseEntity.getBody().getCount());
		
	}
	
	@Test
	public void testGetAllBooksException() {
		when(libraryService.findAllBooks()).thenThrow(new RuntimeException("this is error"));
		ResponseEntity<ResponseData<List<BookData>>> responseEntity = libraryController.getAllBooks();
		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("this is error", responseEntity.getBody().getMsg());
        assertEquals("Error occured", responseEntity.getBody().getStatus());
        assertEquals(null, responseEntity.getBody().getData());
        assertEquals(0, responseEntity.getBody().getCount());
		
	}
	
	//test for insert book
	@Test
	public void testInsertBookSuccess() {
		BookData book = new BookData();
		ResponseData<String> response = new ResponseData<String>("inserted successfully", "success");
		when(libraryService.insertBook(book)).thenReturn(response);
		ResponseEntity<ResponseData<String>> responseEntity = libraryController.insertBook(book);
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals("inserted successfully", responseEntity.getBody().getMsg());
        assertEquals("success", responseEntity.getBody().getStatus());
        assertEquals(null, responseEntity.getBody().getData());
        assertEquals(0, responseEntity.getBody().getCount());
		
	}
	
	@Test
	public void testInsertBookDataAccessException() {
		BookData book = new BookData();
		
		when(libraryService.insertBook(book)).thenThrow(new DataAccessException("Database Error") {

			private static final long serialVersionUID = 1L;
		});
		ResponseEntity<ResponseData<String>> responseEntity = libraryController.insertBook(book);
		
		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
		assertEquals("Database Error", responseEntity.getBody().getMsg());
        assertEquals("Error occured", responseEntity.getBody().getStatus());
        assertEquals(null, responseEntity.getBody().getData());
        assertEquals(0, responseEntity.getBody().getCount());
		
	}
	
	@Test
	public void testInsertBookAnyException() {
		BookData book = new BookData();
		
		when(libraryService.insertBook(book)).thenThrow(new RuntimeException("Throws Error"));
		ResponseEntity<ResponseData<String>> responseEntity = libraryController.insertBook(book);
		
		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
		assertEquals("Data Not retrieved", responseEntity.getBody().getMsg());
        assertEquals("Error occured", responseEntity.getBody().getStatus());
        assertEquals(null, responseEntity.getBody().getData());
        assertEquals(0, responseEntity.getBody().getCount());
		
	}
	
	//test for update quantity
	@Test
	public void testUpdateQuantity() {
		int book_id = 2;
		int quantity = 10;
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("quantity", quantity);
		ResponseData<String> response = new ResponseData<String>("updated successfully", "success");
		when(libraryService.updateQuantity(quantity, book_id)).thenReturn(response);
		ResponseEntity<ResponseData<String>> responseEntity = libraryController.updateQuantity(map, book_id);
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals("updated successfully", responseEntity.getBody().getMsg());
		assertEquals("success", responseEntity.getBody().getStatus());
		
	}
	
	@Test
	public void testUpdateQuantityException() {
		int book_id = 2;
		int quantity = 10;
		String error = "throw error";
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("quantity", quantity);
		
		when(libraryService.updateQuantity(quantity, book_id)).thenThrow(new RuntimeException(error));
		ResponseEntity<ResponseData<String>> responseEntity = libraryController.updateQuantity(map, book_id);
		
		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
		assertEquals("Update not happened", responseEntity.getBody().getMsg());
		assertEquals("Error occured", responseEntity.getBody().getStatus());
		
	}
	
	//test for get all student with book
	@Test
	public void testGetAllStudentWithBookSuccess() {
		List<RegisteredStudentForBookResponse> list = new ArrayList<RegisteredStudentForBookResponse>();
		list.add(new RegisteredStudentForBookResponse());
		list.add(new RegisteredStudentForBookResponse());
		
		when(libraryService.getStudent_book_register()).thenReturn(list);
		
		ResponseEntity<ResponseData<List<RegisteredStudentForBookResponse>>> responseEntity = libraryController.getAllStudentWithBook();
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals("Data retrieved", responseEntity.getBody().getMsg());
        assertEquals("Success", responseEntity.getBody().getStatus());
        assertEquals(list, responseEntity.getBody().getData());
        assertEquals(list.size(), responseEntity.getBody().getCount());
	}
	
	@Test
	public void testGetAllStudentWithBookDataAccessException() {
		
		when(libraryService.getStudent_book_register()).thenThrow(new DataAccessException("Database Error") {
			private static final long serialVersionUID = 1L;});
		
		ResponseEntity<ResponseData<List<RegisteredStudentForBookResponse>>> responseEntity = libraryController.getAllStudentWithBook();
		
		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
		assertEquals("Database Error", responseEntity.getBody().getMsg());
        assertEquals("Error occured", responseEntity.getBody().getStatus());
        assertEquals(null, responseEntity.getBody().getData());
        assertEquals(0, responseEntity.getBody().getCount());
	}
	
	@Test
	public void testGetAllStudentWithBookException() {
		
		when(libraryService.getStudent_book_register()).thenThrow(new RuntimeException("this is the error")) ;
		
		ResponseEntity<ResponseData<List<RegisteredStudentForBookResponse>>> responseEntity = libraryController.getAllStudentWithBook();
		
		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
		assertEquals("Data Not retrieved", responseEntity.getBody().getMsg());
        assertEquals("Error occured", responseEntity.getBody().getStatus());
        assertEquals(null, responseEntity.getBody().getData());
        assertEquals(0, responseEntity.getBody().getCount());
	}
	
	//test for get most book taken within the date
	@Test
	public void testGetMaximumBookByDateSuccess() {
		DateRequestCheckin request = new DateRequestCheckin();
		
		MaxBookCountResponse maxBookCountResponse = new MaxBookCountResponse();
		ResponseData<MaxBookCountResponse> response = new ResponseData<MaxBookCountResponse>("success", "success", maxBookCountResponse, 1);
		when(libraryService.get_max_book_checkin_with_date(request)).thenReturn(response);
		
		ResponseEntity<ResponseData<MaxBookCountResponse>> responseEntity = libraryController.getMaximumBookByDate(request);
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals("success", responseEntity.getBody().getMsg());
        assertEquals("success", responseEntity.getBody().getStatus());
        assertEquals(maxBookCountResponse, responseEntity.getBody().getData());
        assertEquals(1, responseEntity.getBody().getCount());
		
	}
	
	
	@Test
	public void testGetMaximumBookByDateException() {
		DateRequestCheckin request = new DateRequestCheckin();
		
		when(libraryService.get_max_book_checkin_with_date(request)).thenThrow(new RuntimeException("Error"));
		
		ResponseEntity<ResponseData<MaxBookCountResponse>> responseEntity = libraryController.getMaximumBookByDate(request);
		
		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
		assertEquals("Error", responseEntity.getBody().getMsg());
        assertEquals("Error occured", responseEntity.getBody().getStatus());
        assertEquals(null, responseEntity.getBody().getData());
        assertEquals(0, responseEntity.getBody().getCount());
	}
	
	//test for most book available in book list
	@Test
	public void testGetMostAvailableBookSuccess() {
		List<BookData> books = new ArrayList<BookData>();
		books.add(new BookData ("test book", "test author", 5, 10));
		books.add(new BookData ("test book2", "test author2", 5, 10));
		
		when(libraryService.get_max_avail_book()).thenReturn(books);
		
		ResponseEntity<ResponseData<List<BookData>>> responseEntity = libraryController.getMostAvailableBook();
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals("Data retrieved", responseEntity.getBody().getMsg());
        assertEquals("Success", responseEntity.getBody().getStatus());
        assertEquals(books, responseEntity.getBody().getData());
        assertEquals(books.size(), responseEntity.getBody().getCount());
		
	}
	
	@Test
	public void testGetMostAvailableBookDataAccessException() {
		
		
		when(libraryService.get_max_avail_book()).thenThrow(new DataAccessException("Database Error") {

			private static final long serialVersionUID = 1L;
		});
		
		ResponseEntity<ResponseData<List<BookData>>> responseEntity = libraryController.getMostAvailableBook();
		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
		assertEquals("Database Error", responseEntity.getBody().getMsg());
        assertEquals("Error occured", responseEntity.getBody().getStatus());
        assertEquals(null, responseEntity.getBody().getData());
        assertEquals(0, responseEntity.getBody().getCount());
		
	}
	
	@Test
	public void testGetMostAvailableBookException() {
		
		
		when(libraryService.get_max_avail_book()).thenThrow(new RuntimeException("runtime Error"));
		
		ResponseEntity<ResponseData<List<BookData>>> responseEntity = libraryController.getMostAvailableBook();
		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
		assertEquals("runtime Error", responseEntity.getBody().getMsg());
        assertEquals("Error occured", responseEntity.getBody().getStatus());
        assertEquals(null, responseEntity.getBody().getData());
        assertEquals(0, responseEntity.getBody().getCount());
		
	}
	
	//test for minimum available book
	@Test
	public void testGetMinimumAvailableBookSuccess() {
		List<BookData> books = new ArrayList<BookData>();
		books.add(new BookData ("test book", "test author", 5, 10));
		books.add(new BookData ("test book2", "test author2", 5, 10));
		
		when(libraryService.get_min_avail_book()).thenReturn(books);
		
		ResponseEntity<ResponseData<List<BookData>>> responseEntity = libraryController.getMinimumAvailableBook();
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals("Data retrieved", responseEntity.getBody().getMsg());
        assertEquals("Success", responseEntity.getBody().getStatus());
        assertEquals(books, responseEntity.getBody().getData());
        assertEquals(books.size(), responseEntity.getBody().getCount());
	}
	
	
	@Test
	public void testGetMinimumAvailableBookException() {
		
		when(libraryService.get_min_avail_book()).thenThrow(new RuntimeException("runtime Error"));
		
		ResponseEntity<ResponseData<List<BookData>>> responseEntity = libraryController.getMinimumAvailableBook();
		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
		assertEquals("runtime Error", responseEntity.getBody().getMsg());
        assertEquals("Error occured", responseEntity.getBody().getStatus());
        assertEquals(null, responseEntity.getBody().getData());
        assertEquals(0, responseEntity.getBody().getCount());
		
	}
	
//	@Test
//	public void CheckInSuccess() {
//		CheckingInRequest request = new CheckingInRequest();
//		request.setStudent_name("Akshaya");
//		request.setRegister_id(23);
//		request.setNo_of_books(2);
//		List<String> book_names = new ArrayList<String>();
//		book_names.add("book of akshaya");
//		book_names.add("book 1");
//		request.setBook_name(book_names);
//		
//		when(checkInService.CheckInStudent(request)).thenReturn("value");
//		
//		ResponseEntity<ResponseData<String>> responseEntity = libraryController.checkIn(request);
//		
//		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//		assertEquals("value", responseEntity.getBody().getData());
//		assertEquals("Success", responseEntity.getBody().getStatus());
//		
//	}
	
//	@Test
//	public void CheckInDataAccessException() {
//		CheckingInRequest request = new CheckingInRequest();
//		request.setStudent_name("Akshaya");
//		request.setRegister_id(23);
//		request.setNo_of_books(2);
//		List<String> book_names = new ArrayList<String>();
//		book_names.add("book of akshaya");
//		book_names.add("book 1");
//		request.setBook_name(book_names);
//		
//		when(checkInService.CheckInStudent(request)).thenThrow(new DataAccessException("Database Error") {
//			private static final long serialVersionUID = 1L; });
//		
//		ResponseEntity<ResponseData<String>> responseEntity = libraryController.checkIn(request);
//		
//		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
//		assertEquals("Database Error", responseEntity.getBody().getMsg());
//		assertEquals("Error occured", responseEntity.getBody().getStatus());
//		
//	}
//	
//	@Test
//	public void CheckInException() {
//		CheckingInRequest request = new CheckingInRequest();
//		request.setStudent_name("Akshaya");
//		request.setRegister_id(23);
//		request.setNo_of_books(2);
//		List<String> book_names = new ArrayList<String>();
//		book_names.add("book of akshaya");
//		book_names.add("book 1");
//		request.setBook_name(book_names);
//		
//		when(checkInService.CheckInStudent(request)).thenThrow(new RuntimeException("runtime Error"));
//		
//		ResponseEntity<ResponseData<String>> responseEntity = libraryController.checkIn(request);
//		
//		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
//		assertEquals("Data Not retrieved", responseEntity.getBody().getMsg());
//		assertEquals("Error occured", responseEntity.getBody().getStatus());
//		
//	}
//	
//	@Test
//	public void CheckoutRequestSuccess() {
//		CheckOutRequest request = new CheckOutRequest();
//		request.setBook_name("book 1");
//		request.setStudent_name("akshaya");
//		request.setRegister_no(12);
//		
//		when(checkOutService.CheckOutBook(request)).thenReturn("value");
//		
//		ResponseEntity<ResponseData<String>> responseEntity = libraryController.checkout(request);
//		
//		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//		assertEquals("Success", responseEntity.getBody().getStatus());
//		assertEquals("value", responseEntity.getBody().getMsg());
//	}
//	
	@Test
	public void checkOutThrowsDataAccessException() {
		CheckOutRequest request = new CheckOutRequest();
		request.setBook_name("book 1");
		request.setStudent_name("akshaya");
		request.setRegister_no(12);
		
		when(checkOutService.CheckOutBook(request)).thenThrow(new DataAccessException("Database Error") {

			private static final long serialVersionUID = 1L;});
		
		ResponseEntity<ResponseData<String>> responseEntity = libraryController.checkout(request);
		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
		assertEquals("Error occured", responseEntity.getBody().getStatus());
		assertEquals("Database Error", responseEntity.getBody().getMsg());
		
	}
	
	@Test
	public void checkOutThrowsException() {
		CheckOutRequest request = new CheckOutRequest();
		request.setBook_name("book 1");
		request.setStudent_name("akshaya");
		request.setRegister_no(12);
		
		when(checkOutService.CheckOutBook(request)).thenThrow(new RuntimeException("Error"));
		
		ResponseEntity<ResponseData<String>> responseEntity = libraryController.checkout(request);
		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
		assertEquals("Error occured", responseEntity.getBody().getStatus());
		assertEquals("Check out didn't happen", responseEntity.getBody().getMsg());
		
	}
	
	@Test
	public void testGetMaxWaitingBookSuccess() {
		BookData bookData = new BookData();
		bookData.setAuthor_name("test author");
		bookData.setBook_name("Test book");
		bookData.setQuantity(10);
		bookData.setRead_time(9);
		
		when(libraryService.get_max_waiting_book()).thenReturn(bookData);
		
		ResponseEntity<ResponseData<BookData>> responseEntity = libraryController.getMaxWaitingBook();
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals("Success", responseEntity.getBody().getStatus());
		assertEquals("Data retrieved", responseEntity.getBody().getMsg());
		assertEquals(bookData, responseEntity.getBody().getData());
		
	}
	
	@Test
	public void testGetMaxWaitingBookThrowsException() {
		when(libraryService.get_max_waiting_book()).thenThrow(new RuntimeException("Run time exception"));
		ResponseEntity<ResponseData<BookData>> responseEntity = libraryController.getMaxWaitingBook();
		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
		assertEquals("Error Occured", responseEntity.getBody().getStatus());
		assertEquals("Run time exception", responseEntity.getBody().getMsg());
		assertEquals(null, responseEntity.getBody().getData());
		
	}
	
	@Test
	public void testGetMaxWaitingBookThrowsDataAccessException() {
		when(libraryService.get_max_waiting_book()).thenThrow(new DataAccessException("Database Error") {});
		ResponseEntity<ResponseData<BookData>> responseEntity = libraryController.getMaxWaitingBook();
		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
		assertEquals("Error Occured", responseEntity.getBody().getStatus());
		assertEquals("Database Error", responseEntity.getBody().getMsg());
		assertEquals(null, responseEntity.getBody().getData());
		
	}
	
	@Test
	public void testGetMinimumWaitingBookSuccess() {
		BookData bookData = new BookData();
		bookData.setAuthor_name("test author");
		bookData.setBook_name("Test book");
		bookData.setQuantity(10);
		bookData.setRead_time(9);
		
		when(libraryService.get_min_waiting_book()).thenReturn(bookData);
		
		ResponseEntity<ResponseData<BookData>> responseEntity = libraryController.getMinimumWaitingBook();
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals("Success", responseEntity.getBody().getStatus());
		assertEquals("Data retrieved", responseEntity.getBody().getMsg());
		assertEquals(bookData, responseEntity.getBody().getData());
	}
	
	@Test
	public void testGetMinimumWaitingBookThrowsDataAccessException() {
		
		when(libraryService.get_min_waiting_book()).thenThrow(new DataAccessException("Database Error") {

			private static final long serialVersionUID = 1L;});
		
		ResponseEntity<ResponseData<BookData>> responseEntity = libraryController.getMinimumWaitingBook();
		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
		assertEquals("Error occured", responseEntity.getBody().getStatus());
		assertEquals("Database Error", responseEntity.getBody().getMsg());
		assertEquals(null, responseEntity.getBody().getData());
	}
	
	@Test
	public void testGetMinimumWaitingBookThrowsException() {
		
		when(libraryService.get_min_waiting_book()).thenThrow(new RuntimeException("Run time Error"));
		
		ResponseEntity<ResponseData<BookData>> responseEntity = libraryController.getMinimumWaitingBook();
		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
		assertEquals("Error occured", responseEntity.getBody().getStatus());
		assertEquals("Data not retrieved", responseEntity.getBody().getMsg());
		assertEquals(null, responseEntity.getBody().getData());
	}
	
	@Test
	public void testGetAvailableBookSuccesss() {
		List<BookData> bookDatas = new ArrayList<BookData>();
		bookDatas.add(new BookData("book 1", "author 1", 10, 5));
		bookDatas.add(new BookData("book 2", "author 2", 10, 5));
		
		when(libraryService.get_avail_books()).thenReturn(bookDatas);
		
		ResponseEntity<ResponseData<List<BookData>>> responseEntity = libraryController.getAvailableBook();
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals("Success", responseEntity.getBody().getStatus());
		assertEquals("Data retrieved", responseEntity.getBody().getMsg());
		assertEquals(bookDatas, responseEntity.getBody().getData());
		assertEquals(bookDatas.size(), responseEntity.getBody().getCount());	
				
	}
	
	@Test
	public void testGetAvailableBookThrowsException() {
		
		when(libraryService.get_avail_books()).thenThrow(new RuntimeException("Error"));
		
		ResponseEntity<ResponseData<List<BookData>>> responseEntity = libraryController.getAvailableBook();
		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
		assertEquals("Error occured", responseEntity.getBody().getStatus());
		assertEquals("Data not retrieved", responseEntity.getBody().getMsg());
		assertEquals(null, responseEntity.getBody().getData());
		
	}
	
	@Test
	public void testGetAvailableBookThrowsDataAccessException() {
		when(libraryService.get_avail_books()).thenThrow(new DataAccessException("Database Error") {});
		
		ResponseEntity<ResponseData<List<BookData>>> responseEntity = libraryController.getAvailableBook();
		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
		assertEquals("Error occured", responseEntity.getBody().getStatus());
		assertEquals("Database Error", responseEntity.getBody().getMsg());
		assertEquals(null, responseEntity.getBody().getData());
	}
}
