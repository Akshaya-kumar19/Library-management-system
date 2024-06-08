package com.example.firstprogramInspring.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.firstprogramInspring.DAO.LibraryDAO;
import com.example.firstprogramInspring.DAO.StudentDAO;
import com.example.firstprogramInspring.Model.CheckOutRequest;
import com.example.firstprogramInspring.Model.StudentBookRegister;
import com.example.firstprogramInspring.Model.WaitingList;
import com.google.gson.Gson;

@Service
public class CheckOutService {
	@Autowired
	LibraryDAO lib_dao;
	@Autowired
	StudentDAO stu_dao;
	Logger logger = LoggerFactory.getLogger(getClass());
	
	public String CheckOutBook(CheckOutRequest request) {
		String student_name = request.getStudent_name();
		int register_id = request.getRegister_no();
		String book_name = request.getBook_name();
		String msg = "";
		if(!stu_dao.checkRegisterNo(register_id) && student_name.equalsIgnoreCase(stu_dao.getStudentNameByRegisterID(register_id))) {
			msg += "Credentials are checked, you are the registered user \n ";
			
//			System.out.println("we have this register number in the student table");
//			System.out.println("You name is checked. Time to progress");
				
				if(!lib_dao.checkBookName(book_name)) {
					msg +="this is book is registered in library : " + book_name + "\n ";
					System.out.println("this is book is registered in library : " + book_name);
					int student_id = lib_dao.getStudentIDUsingNameRegisterNO(student_name, register_id);
					int book_id = lib_dao.getBookIDUsingBookName(book_name);
					
					if(lib_dao.checkForEntryInStudentBookRegister(student_id, book_id)) {
						msg+= "You taken this book \n ";
						System.out.println("You taken this book");
						
						if(lib_dao.checkForBookReturn(student_id, book_id)) {
							msg += "Your book is returned \n ";
							System.out.println("Now only book is gonna be returned");
							lib_dao.setCheckoutTime(student_id, book_id);
							int available_quantity = lib_dao.getQuantity(book_id);
							
							if(available_quantity ==0) {
								
								System.out.println("it's time to check the book_id in the waiting list to allocate the book the another person");
								
								List<WaitingList> waiting = lib_dao.getListOfWaitingForBook(book_id);
								Gson gson = new Gson();
								System.out.println(gson.toJson(waiting));
								if(waiting.size() > 0) {
									
									WaitingList waiting_list = waiting.get(0);
									StudentBookRegister registerstudent = new StudentBookRegister();
									registerstudent.setBook_id(book_id);
									registerstudent.setStudent_id(waiting_list.getStudent_id());
									lib_dao.insertDataStudentBookRegister(registerstudent);
									
									lib_dao.changeWaitingFlag(waiting_list.getStudent_id(), book_id);
									
								}
								else {
									lib_dao.upgradeQuantity(1, book_id);
								}
								
							}
							else {
								
								available_quantity = available_quantity + 1;
								lib_dao.upgradeQuantity(available_quantity, book_id);
							}
						}
						else {
							msg += "Book is already returned \n ";
							System.out.println("Book is already returned");
						}
					}
					else {
						msg +="You didn't take this book at all. give the correct inputs \n ";
						System.out.println("You didn't take this book at all. give the correct inputs");
					}
					
				}
				else {
					msg +="this is book is not in library itself \n ";
					System.out.println("this is book is not in library itself");
				}
			
		}
		else {
			msg+="Student name or register number is incorrect \n ";
			System.out.println("Student name or register number is incorrect");
		}
		logger.info(msg);
		return msg;
	}
	
	
}
