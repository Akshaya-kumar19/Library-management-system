package com.example.firstprogramInspring.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.firstprogramInspring.DAO.LibraryDAO;
import com.example.firstprogramInspring.DAO.StudentDAO;
import com.example.firstprogramInspring.Model.StudentBookRegister;
import com.example.firstprogramInspring.Model.CheckingInRequest;
import com.example.firstprogramInspring.Model.WaitingList;

@Service
public class CheckInService {
	@Autowired
	StudentDAO stuDAO;
	@Autowired
	LibraryDAO lib_DAO;

	Logger logger = LoggerFactory.getLogger(CheckInService.class);


	@Autowired
	SchedulingDemo schedulingDemo;//using  scheduler

	@Autowired
	ScheduleMultiThread schedule_MultiThread;
	
	@Autowired
	DownloadService downloadService;

	public Object CheckInStudent(CheckingInRequest request) {
		String student_name = request.getStudent_name();
		int register_id = request.getRegister_id();
		int no_of_books = request.getNo_of_books();
		List<String> books = request.getBook_name();
		
		//for adding to the document
		List<StudentBookRegister> booksTaken = new ArrayList<StudentBookRegister>();
		List<WaitingList> waitingLists = new ArrayList<WaitingList>();
		List<String> booksNotInLibrary = new ArrayList<String>();
		
		String msg = "";
		if(!stuDAO.checkRegisterNo(register_id)) {
			msg = "Register number is checked it is in the table \n ";

			if(student_name.equalsIgnoreCase(stuDAO.getStudentNameByRegisterID(register_id))  ) {
				msg = msg + "Names matched \n";

				int Student_id = lib_DAO.getStudentIDUsingNameRegisterNO(student_name, register_id);

				int length_of_books = books.size();

				if(no_of_books==length_of_books) {
					msg = msg + "no of books matches with the length of the books \n ";
					logger.info("no of books matches with the length of the books");

					for (String book : books) {

						if(!lib_DAO.checkBookName(book)) {

							StudentBookRegister registerstudent = new StudentBookRegister();
							msg = msg + "book is present : " + book + "\n ";
							logger.info("book is present : " + book);
							int book_id = lib_DAO.getBookIDUsingBookName(book);

							int available_quantity = lib_DAO.getQuantity(book_id);
							Integer read_time = lib_DAO.getReadTime(book_id);
							if(available_quantity > 0) {

								logger.info("available quantity of book: "+ available_quantity);
								available_quantity = available_quantity -1;
								lib_DAO.upgradeQuantity(available_quantity, book_id);

								msg = msg + "you have taken the book : " + book + "\n";
								logger.info("you have taken the book : " + book);

								registerstudent.setBook_id(book_id);
								registerstudent.setStudent_id(Student_id);
								//for pdf
								booksTaken.add(registerstudent);
								if (read_time == null) {
									registerstudent.setTime_left(0);
								}
								else {
									registerstudent.setTime_left(read_time);
								}

								int bookissueId = lib_DAO.insertDataStudentBookRegister(registerstudent);
								logger.info("inserting student into the student book register with a issue id : "+ bookissueId);

								schedule_MultiThread.scheduleIndividualTask(bookissueId);


							}
							else {
								msg = msg + "Sorry, currently book is not available -- book name: "+ book + "\n" + "you will get this book when it is available \n";
								logger.info("Sorry, currently book is not available -- book name: "+ book);
								logger.info("you will get this book when it is available");
								WaitingList waiting = new WaitingList();
								waiting.setBook_id(book_id);
								waiting.setStudent_id(Student_id);
								waiting.setWaiting_flag("WAITING");
								lib_DAO.insertStudentInWaitingList(waiting);
								//for pdf
								waitingLists.add(waiting);
							}
						}
						else {
							msg = msg + "book is not in the library itself -- book name : "+ book + "\n";
							logger.info("book is not in the library itself -- book name : "+ book);
							
							//for pdf
							booksNotInLibrary.add(book);
						}
					}//for loop end
					
					return downloadService.generatePdfForCheckIn(request, booksTaken, waitingLists, booksNotInLibrary);

				}
				else {
					msg = msg + "List of books and number of books has to match\n";
					logger.info("List of books and number of books has to match");
				}

			}
			else {
				msg = msg + "Names doesn't match check your credentials\n";
				logger.info("Names doesn't match check your credentials");

			}
			logger.info(msg);
			return msg;
		}
		else {
			msg += "New account is created for you \n";
			logger.info("Register is not present in the student table, So student is created");
			int saved = stuDAO.saveStudentCheckIn(student_name, register_id);
			if(saved==0) {
				return msg;
			}
			else {
				CheckInStudent(request);
				logger.info(msg);
				return msg;
			}

		}

	}
}
