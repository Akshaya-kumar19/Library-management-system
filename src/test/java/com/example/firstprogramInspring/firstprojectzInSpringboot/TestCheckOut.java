package com.example.firstprogramInspring.firstprojectzInSpringboot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.firstprogramInspring.DAO.LibraryDAO;
import com.example.firstprogramInspring.DAO.StudentDAO;
import com.example.firstprogramInspring.Model.CheckOutRequest;
import com.example.firstprogramInspring.Model.WaitingList;
import com.example.firstprogramInspring.service.CheckOutService;

@SpringBootTest
public class TestCheckOut {
	@Mock
	LibraryDAO libraryDAO;
	
	@Mock
	StudentDAO studentDAO;
	
	@InjectMocks
	CheckOutService checkOutService;
	
	@Test
	public void testCheckOutSuccess() {
		CheckOutRequest checkOutRequest = new CheckOutRequest();
		checkOutRequest.setBook_name("book 1");
		checkOutRequest.setRegister_no(123);
		checkOutRequest.setStudent_name("akshaya");
		
		when(studentDAO.checkRegisterNo(anyInt())).thenReturn(false);
		when(studentDAO.getStudentNameByRegisterID(anyInt())).thenReturn("akshaya");
		when(libraryDAO.checkBookName(anyString())).thenReturn(false);
		when(libraryDAO.getStudentIDUsingNameRegisterNO(anyString(), anyInt())).thenReturn(1);
		when(libraryDAO.getBookIDUsingBookName(anyString())).thenReturn(2);
		when(libraryDAO.checkForEntryInStudentBookRegister(anyInt(), anyInt())).thenReturn(true);
		when(libraryDAO.checkForBookReturn(anyInt(), anyInt())).thenReturn(true);
		when(libraryDAO.setCheckoutTime(anyInt(),anyInt())).thenReturn(1);
		when(libraryDAO.getQuantity(anyInt())).thenReturn(12);
		when(libraryDAO.upgradeQuantity(anyInt(), anyInt())).thenReturn(1);
		
		String Expected = "Credentials are checked, you are the registered user \n " +
				"this is book is registered in library : book 1\n " +
				"You taken this book \n " +
				"Your book is returned \n ";
		String actual = checkOutService.CheckOutBook(checkOutRequest);
		
		assertEquals(Expected, actual);
	}
	
	@Test
	public void testCheckOutRegisterNumberNotPresent() {
		CheckOutRequest checkOutRequest = new CheckOutRequest();
		checkOutRequest.setBook_name("book 1");
		checkOutRequest.setRegister_no(123);
		checkOutRequest.setStudent_name("akshaya");
		
		when(studentDAO.checkRegisterNo(anyInt())).thenReturn(true);
		when(studentDAO.getStudentNameByRegisterID(anyInt())).thenReturn("akshaya");
//		when(libraryDAO.checkBookName(anyString())).thenReturn(false);
//		when(libraryDAO.getStudentIDUsingNameRegisterNO(anyString(), anyInt())).thenReturn(1);
//		when(libraryDAO.getBookIDUsingBookName(anyString())).thenReturn(2);
//		when(libraryDAO.checkForEntryInStudentBookRegister(anyInt(), anyInt())).thenReturn(true);
//		when(libraryDAO.checkForBookReturn(anyInt(), anyInt())).thenReturn(true);
//		when(libraryDAO.setCheckoutTime(anyInt(),anyInt())).thenReturn(1);
//		when(libraryDAO.getQuantity(anyInt())).thenReturn(12);
//		when(libraryDAO.upgradeQuantity(anyInt(), anyInt())).thenReturn(1);
		
		String Expected = "Student name or register number is incorrect \n ";
		String actual = checkOutService.CheckOutBook(checkOutRequest);
		
		assertEquals(Expected, actual);
	}
	
	@Test
	public void testCheckOutNameIsWrong() {
		CheckOutRequest checkOutRequest = new CheckOutRequest();
		checkOutRequest.setBook_name("book 1");
		checkOutRequest.setRegister_no(123);
		checkOutRequest.setStudent_name("akshaya");
		
		when(studentDAO.checkRegisterNo(anyInt())).thenReturn(false);
		when(studentDAO.getStudentNameByRegisterID(anyInt())).thenReturn("aksha");
//		when(libraryDAO.checkBookName(anyString())).thenReturn(false);
//		when(libraryDAO.getStudentIDUsingNameRegisterNO(anyString(), anyInt())).thenReturn(1);
//		when(libraryDAO.getBookIDUsingBookName(anyString())).thenReturn(2);
//		when(libraryDAO.checkForEntryInStudentBookRegister(anyInt(), anyInt())).thenReturn(true);
//		when(libraryDAO.checkForBookReturn(anyInt(), anyInt())).thenReturn(true);
//		when(libraryDAO.setCheckoutTime(anyInt(),anyInt())).thenReturn(1);
//		when(libraryDAO.getQuantity(anyInt())).thenReturn(12);
//		when(libraryDAO.upgradeQuantity(anyInt(), anyInt())).thenReturn(1);
		
		String Expected = "Student name or register number is incorrect \n ";
		String actual = checkOutService.CheckOutBook(checkOutRequest);
		
		assertEquals(Expected, actual);
	}
	
	@Test
	public void testCheckOutBookNotInLibrary() {
		CheckOutRequest checkOutRequest = new CheckOutRequest();
		checkOutRequest.setBook_name("book 1");
		checkOutRequest.setRegister_no(123);
		checkOutRequest.setStudent_name("akshaya");
		
		when(studentDAO.checkRegisterNo(anyInt())).thenReturn(false);
		when(studentDAO.getStudentNameByRegisterID(anyInt())).thenReturn("akshaya");
		when(libraryDAO.checkBookName(anyString())).thenReturn(true);
//		when(libraryDAO.getStudentIDUsingNameRegisterNO(anyString(), anyInt())).thenReturn(1);
//		when(libraryDAO.getBookIDUsingBookName(anyString())).thenReturn(2);
//		when(libraryDAO.checkForEntryInStudentBookRegister(anyInt(), anyInt())).thenReturn(true);
//		when(libraryDAO.checkForBookReturn(anyInt(), anyInt())).thenReturn(true);
//		when(libraryDAO.setCheckoutTime(anyInt(),anyInt())).thenReturn(1);
//		when(libraryDAO.getQuantity(anyInt())).thenReturn(12);
//		when(libraryDAO.upgradeQuantity(anyInt(), anyInt())).thenReturn(1);
		
		String Expected = "Credentials are checked, you are the registered user \n "+
				"this is book is not in library itself \n ";
		String actual = checkOutService.CheckOutBook(checkOutRequest);
		
		assertEquals(Expected, actual);
	}
	
	@Test
	public void testCheckOutBookIsNotTaken() {
		CheckOutRequest checkOutRequest = new CheckOutRequest();
		checkOutRequest.setBook_name("book 1");
		checkOutRequest.setRegister_no(123);
		checkOutRequest.setStudent_name("akshaya");
		
		when(studentDAO.checkRegisterNo(anyInt())).thenReturn(false);
		when(studentDAO.getStudentNameByRegisterID(anyInt())).thenReturn("akshaya");
		when(libraryDAO.checkBookName(anyString())).thenReturn(false);
		when(libraryDAO.getStudentIDUsingNameRegisterNO(anyString(), anyInt())).thenReturn(1);
		when(libraryDAO.getBookIDUsingBookName(anyString())).thenReturn(2);
		when(libraryDAO.checkForEntryInStudentBookRegister(anyInt(), anyInt())).thenReturn(false);
//		when(libraryDAO.checkForBookReturn(anyInt(), anyInt())).thenReturn(true);
//		when(libraryDAO.setCheckoutTime(anyInt(),anyInt())).thenReturn(1);
//		when(libraryDAO.getQuantity(anyInt())).thenReturn(12);
//		when(libraryDAO.upgradeQuantity(anyInt(), anyInt())).thenReturn(1);
		
		String Expected = "Credentials are checked, you are the registered user \n "+
				"this is book is registered in library : book 1\n " +
				"You didn't take this book at all. give the correct inputs \n ";
		String actual = checkOutService.CheckOutBook(checkOutRequest);
		
		assertEquals(Expected, actual);
	}
	
	@Test
	public void testCheckOutBookAlreadyReturned() {
		CheckOutRequest checkOutRequest = new CheckOutRequest();
		checkOutRequest.setBook_name("book 1");
		checkOutRequest.setRegister_no(123);
		checkOutRequest.setStudent_name("akshaya");
		
		when(studentDAO.checkRegisterNo(anyInt())).thenReturn(false);
		when(studentDAO.getStudentNameByRegisterID(anyInt())).thenReturn("akshaya");
		when(libraryDAO.checkBookName(anyString())).thenReturn(false);
		when(libraryDAO.getStudentIDUsingNameRegisterNO(anyString(), anyInt())).thenReturn(1);
		when(libraryDAO.getBookIDUsingBookName(anyString())).thenReturn(2);
		when(libraryDAO.checkForEntryInStudentBookRegister(anyInt(), anyInt())).thenReturn(true);
		when(libraryDAO.checkForBookReturn(anyInt(), anyInt())).thenReturn(false);
//		when(libraryDAO.setCheckoutTime(anyInt(),anyInt())).thenReturn(1);
//		when(libraryDAO.getQuantity(anyInt())).thenReturn(12);
//		when(libraryDAO.upgradeQuantity(anyInt(), anyInt())).thenReturn(1);
		
		String Expected = "Credentials are checked, you are the registered user \n "+
				"this is book is registered in library : book 1\n " +
				"You taken this book \n "+
				"Book is already returned \n ";
		String actual = checkOutService.CheckOutBook(checkOutRequest);
		
		assertEquals(Expected, actual);
	}
	
	@Test
	public void testCheckOutBookAvailableQuantityIsZero() {
		CheckOutRequest checkOutRequest = new CheckOutRequest();
		checkOutRequest.setBook_name("book 1");
		checkOutRequest.setRegister_no(123);
		checkOutRequest.setStudent_name("akshaya");
		
		when(studentDAO.checkRegisterNo(anyInt())).thenReturn(false);
		when(studentDAO.getStudentNameByRegisterID(anyInt())).thenReturn("akshaya");
		when(libraryDAO.checkBookName(anyString())).thenReturn(false);
		when(libraryDAO.getStudentIDUsingNameRegisterNO(anyString(), anyInt())).thenReturn(1);
		when(libraryDAO.getBookIDUsingBookName(anyString())).thenReturn(2);
		when(libraryDAO.checkForEntryInStudentBookRegister(anyInt(), anyInt())).thenReturn(true);
		when(libraryDAO.checkForBookReturn(anyInt(), anyInt())).thenReturn(true);
		when(libraryDAO.setCheckoutTime(anyInt(),anyInt())).thenReturn(1);
		when(libraryDAO.getQuantity(anyInt())).thenReturn(0);
		when(libraryDAO.getListOfWaitingForBook(anyInt())).thenReturn(Collections.emptyList());
		when(libraryDAO.upgradeQuantity(anyInt(), anyInt())).thenReturn(1);
		
		String Expected = "Credentials are checked, you are the registered user \n "+
				"this is book is registered in library : book 1\n " +
				"You taken this book \n "+
				"Your book is returned \n ";
		String actual = checkOutService.CheckOutBook(checkOutRequest);
		
		assertEquals(Expected, actual);
	}
	
	@Test
	public void testCheckOutBookwaitingListisThere() {
		CheckOutRequest checkOutRequest = new CheckOutRequest();
		checkOutRequest.setBook_name("book 1");
		checkOutRequest.setRegister_no(123);
		checkOutRequest.setStudent_name("akshaya");
		
		when(studentDAO.checkRegisterNo(anyInt())).thenReturn(false);
		when(studentDAO.getStudentNameByRegisterID(anyInt())).thenReturn("akshaya");
		when(libraryDAO.checkBookName(anyString())).thenReturn(false);
		when(libraryDAO.getStudentIDUsingNameRegisterNO(anyString(), anyInt())).thenReturn(1);
		when(libraryDAO.getBookIDUsingBookName(anyString())).thenReturn(2);
		when(libraryDAO.checkForEntryInStudentBookRegister(anyInt(), anyInt())).thenReturn(true);
		when(libraryDAO.checkForBookReturn(anyInt(), anyInt())).thenReturn(true);
		when(libraryDAO.setCheckoutTime(anyInt(),anyInt())).thenReturn(1);
		when(libraryDAO.getQuantity(anyInt())).thenReturn(0);
		List<WaitingList> lists = new ArrayList<WaitingList>();
		WaitingList waitingList = new WaitingList();
		waitingList.setBook_id(2);
		waitingList.setStudent_id(1);
		waitingList.setWaiting_flag("WAITING");
		lists.add(waitingList);
		when(libraryDAO.getListOfWaitingForBook(anyInt())).thenReturn(lists);
		when(libraryDAO.upgradeQuantity(anyInt(), anyInt())).thenReturn(1);
		when(libraryDAO.insertDataStudentBookRegister(any())).thenReturn(1);
		when(libraryDAO.changeWaitingFlag(anyInt(), anyInt())).thenReturn(1);
		String Expected = "Credentials are checked, you are the registered user \n "+
				"this is book is registered in library : book 1\n " +
				"You taken this book \n "+
				"Your book is returned \n ";
		String actual = checkOutService.CheckOutBook(checkOutRequest);
		
		assertEquals(Expected, actual);
	}
}
