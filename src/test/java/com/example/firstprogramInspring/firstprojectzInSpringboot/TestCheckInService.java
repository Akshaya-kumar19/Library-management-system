package com.example.firstprogramInspring.firstprojectzInSpringboot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.firstprogramInspring.DAO.LibraryDAO;
import com.example.firstprogramInspring.DAO.StudentDAO;
import com.example.firstprogramInspring.Model.CheckingInRequest;
import com.example.firstprogramInspring.service.CheckInService;
import com.example.firstprogramInspring.service.ScheduleMultiThread;

@SpringBootTest
public class TestCheckInService {

//	@Mock
//	LibraryDAO libraryDAO;
//
//	@Mock
//	StudentDAO studentDAO;
//
//	@Mock
//	ScheduleMultiThread scheduleMultiThread;
//
//	@InjectMocks
//	CheckInService checkInService;
//
//	public TestCheckInService() {
//		MockitoAnnotations.openMocks(this);
//	}
//
//	@Test
//	public void testCheckInStudent_RegisterExistsAndMatchesNameAndBooks_Success() {
//
//		CheckingInRequest request = new CheckingInRequest();
//		request.setStudent_name("Akshaya");
//		request.setRegister_id(12345);
//		request.setNo_of_books(2);
//		List<String> books = new ArrayList<>();
//		books.add("Book1");
//		books.add("Book2");
//		request.setBook_name(books);
//
//		when(studentDAO.checkRegisterNo(anyInt())).thenReturn(false);
//		when(studentDAO.getStudentNameByRegisterID(12345)).thenReturn("Akshaya");
//		when(libraryDAO.getStudentIDUsingNameRegisterNO("Akshaya", 12345)).thenReturn(1);
//		when(libraryDAO.checkBookName("Book1")).thenReturn(false);
//		when(libraryDAO.checkBookName("Book2")).thenReturn(false);
//		when(libraryDAO.getBookIDUsingBookName("Book1")).thenReturn(1);
//		when(libraryDAO.getBookIDUsingBookName("Book2")).thenReturn(2);
//		when(libraryDAO.getQuantity(anyInt())).thenReturn(1);
//		when(libraryDAO.getReadTime(anyInt())).thenReturn(10);
//
//		String result = checkInService.CheckInStudent(request);
//
//		String expectedResult = "Register number is checked it is in the table \n " + "Names matched \n"
//				+ "no of books matches with the length of the books \n " + "book is present : Book1\n "
//				+ "you have taken the book : Book1\n" + "book is present : Book2\n "
//				+ "you have taken the book : Book2\n";
//
//		assertEquals(expectedResult, result);
//	}
//
//	@Test
//	public void testCheckInRegisterNumberFails() {
//		CheckingInRequest request = new CheckingInRequest();
//		request.setStudent_name("Akshaya");
//		request.setRegister_id(12345);
//		request.setNo_of_books(2);
//		List<String> books = new ArrayList<>();
//		books.add("Book1");
//		books.add("Book2");
//		request.setBook_name(books);
//
//		when(studentDAO.checkRegisterNo(anyInt())).thenReturn(true);
//		when(studentDAO.saveStudentCheckIn("Akshaya", 12345)).thenReturn(1);
//		when(studentDAO.saveStudentCheckIn("Akshaya", 12345)).thenReturn(0);
//
//		String result = checkInService.CheckInStudent(request);
//		String expectedResult = "New account is created for you \n";
//		assertEquals(expectedResult, result);
//	}
//
//	@Test
//	public void testCheckInStudentNameFails() {
//		CheckingInRequest request = new CheckingInRequest();
//		request.setStudent_name("Akshaya");
//		request.setRegister_id(12345);
//		request.setNo_of_books(2);
//		List<String> books = new ArrayList<>();
//		books.add("Book1");
//		books.add("Book2");
//		request.setBook_name(books);
//
//		when(studentDAO.checkRegisterNo(anyInt())).thenReturn(false);
//		when(studentDAO.getStudentNameByRegisterID(12345)).thenReturn("anything");
//		String result = checkInService.CheckInStudent(request);
//		String expectedResult = "Register number is checked it is in the table \n "
//				+ "Names doesn't match check your credentials\n";
//		assertEquals(expectedResult, result);
//
//	}
//
//	@Test
//	public void testCheckInNumberOfBookNotEqualsWithBookList() {
//		CheckingInRequest request = new CheckingInRequest();
//		request.setStudent_name("Akshaya");
//		request.setRegister_id(12345);
//		request.setNo_of_books(1);
//		List<String> books = new ArrayList<>();
//		books.add("Book1");
//		books.add("Book2");
//		request.setBook_name(books);
//
//		when(studentDAO.checkRegisterNo(anyInt())).thenReturn(false);
//		when(studentDAO.getStudentNameByRegisterID(12345)).thenReturn("Akshaya");
//		// when(request.getNo_of_books()).thenReturn(1);
//		// when(request.getBook_name().size()).thenReturn(2);
//
//		String result = checkInService.CheckInStudent(request);
//		String expectedResult = "Register number is checked it is in the table \n " + "Names matched \n"
//				+ "List of books and number of books has to match\n";
//		assertEquals(expectedResult, result);
//		// assertFalse(request.getNo_of_books() != request.getBook_name().size());
//
//	}
//
//	@Test
//	public void testCheckInBookIsNotInLibrary() {
//		CheckingInRequest request = new CheckingInRequest();
//		request.setStudent_name("Akshaya");
//		request.setRegister_id(12345);
//		request.setNo_of_books(2);
//		List<String> books = new ArrayList<>();
//		books.add("Book1");
//		books.add("Book2");
//		request.setBook_name(books);
//
//		when(studentDAO.checkRegisterNo(anyInt())).thenReturn(false);
//		when(studentDAO.getStudentNameByRegisterID(12345)).thenReturn("Akshaya");
//		when(libraryDAO.getStudentIDUsingNameRegisterNO("Akshaya", 12345)).thenReturn(1);
//		when(libraryDAO.checkBookName("Book1")).thenReturn(true);
//		when(libraryDAO.checkBookName("Book2")).thenReturn(true);
//
//		String result = checkInService.CheckInStudent(request);
//		String expectedResult = "Register number is checked it is in the table \n " + "Names matched \n"
//				+ "no of books matches with the length of the books \n "
//				+ "book is not in the library itself -- book name : Book1\n"
//				+ "book is not in the library itself -- book name : Book2\n";
//		assertEquals(expectedResult, result);
//
//	}
//
//	@Test
//	public void testCheckInReadTimeIsNull() {
//		CheckingInRequest request = new CheckingInRequest();
//		request.setStudent_name("Akshaya");
//		request.setRegister_id(12345);
//		request.setNo_of_books(1);
//		List<String> books = new ArrayList<>();
//		books.add("Book1");
//		request.setBook_name(books);
//
//		when(studentDAO.checkRegisterNo(anyInt())).thenReturn(false);
//		when(studentDAO.getStudentNameByRegisterID(12345)).thenReturn("Akshaya");
//		when(libraryDAO.getStudentIDUsingNameRegisterNO("Akshaya", 12345)).thenReturn(1);
//		when(libraryDAO.checkBookName("Book1")).thenReturn(false);
//		when(libraryDAO.getBookIDUsingBookName("Book1")).thenReturn(1);
//		when(libraryDAO.getQuantity(anyInt())).thenReturn(2);
//		when(libraryDAO.getReadTime(anyInt())).thenReturn(null);
//
//		String result = checkInService.CheckInStudent(request);
//		String expectedResult = "Register number is checked it is in the table \n " + "Names matched \n"
//				+ "no of books matches with the length of the books \n " + "book is present : Book1\n "
//				+ "you have taken the book : Book1\n";
//		assertEquals(expectedResult, result);
//
//	}
//
//	@Test
//	public void testCheckInQuantityEqualsZero() {
//		CheckingInRequest request = new CheckingInRequest();
//		request.setStudent_name("Akshaya");
//		request.setRegister_id(12345);
//		request.setNo_of_books(2);
//		List<String> books = new ArrayList<>();
//		books.add("Book1");
//		books.add("Book2");
//		request.setBook_name(books);
//
//		when(studentDAO.checkRegisterNo(anyInt())).thenReturn(false);
//		when(studentDAO.getStudentNameByRegisterID(12345)).thenReturn("Akshaya");
//		when(libraryDAO.getStudentIDUsingNameRegisterNO("Akshaya", 12345)).thenReturn(1);
//		when(libraryDAO.checkBookName("Book1")).thenReturn(false);
//		when(libraryDAO.checkBookName("Book2")).thenReturn(false);
//		when(libraryDAO.getBookIDUsingBookName("Book1")).thenReturn(1);
//		when(libraryDAO.getBookIDUsingBookName("Book2")).thenReturn(2);
//		when(libraryDAO.getQuantity(anyInt())).thenReturn(0);
//		when(libraryDAO.getReadTime(anyInt())).thenReturn(0);
//
//		String result = checkInService.CheckInStudent(request);
//		String expectedResult = "Register number is checked it is in the table \n " + "Names matched \n"
//				+ "no of books matches with the length of the books \n " + "book is present : Book1\n "
//				+ "Sorry, currently book is not available -- book name: Book1\n"
//				+ "you will get this book when it is available \n" + "book is present : Book2\n "
//				+ "Sorry, currently book is not available -- book name: Book2\n"
//				+ "you will get this book when it is available \n";
//		assertEquals(expectedResult, result);
//
//	}
//
//	@Test
//	public void testCheckInStudentWithNewRegister() {
//		// Arrange
//		CheckingInRequest request = new CheckingInRequest();
//		request.setStudent_name("John");
//		request.setRegister_id(123);
//		request.setNo_of_books(1);
//		request.setBook_name(new ArrayList<String>());
//		request.getBook_name().add("Book1");
//		when(studentDAO.checkRegisterNo(123)).thenReturn(true);
//		when(studentDAO.saveStudentCheckIn("John", 123)).thenReturn(1); // Assuming successful save
//
//		// Stub the recursive behavior to stop after one iteration
//		CheckInService checkInServiceSpy = Mockito.spy(checkInService);
//		doReturn("New account is created for you \n").when(checkInServiceSpy).CheckInStudent(request);
//		String result = checkInServiceSpy.CheckInStudent(request);
//
//		assertEquals("New account is created for you \n", result);
//
//	}

}
