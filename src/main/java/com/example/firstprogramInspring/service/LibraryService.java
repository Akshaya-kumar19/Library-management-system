package com.example.firstprogramInspring.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.firstprogramInspring.DAO.DataTableDAO;
import com.example.firstprogramInspring.DAO.LibraryDAO;
import com.example.firstprogramInspring.DAO.StudentDAO;
import com.example.firstprogramInspring.Model.BookData;
import com.example.firstprogramInspring.Model.DateRequestCheckin;
import com.example.firstprogramInspring.Model.MaxBookCountResponse;
import com.example.firstprogramInspring.Model.PageRequest;
import com.example.firstprogramInspring.Model.RegisteredStudentForBookResponse;
import com.example.firstprogramInspring.Model.ResponseData;
import com.example.firstprogramInspring.Model.StudentBookRegister;
import com.example.firstprogramInspring.Model.StudentData;
import com.example.firstprogramInspring.Model.WaitingList;
import com.google.gson.Gson;


@Service
public class LibraryService {
	@Autowired
	StudentDAO stu_DAO;
	@Autowired
	LibraryDAO lib_DAO;
	@Autowired
	ValidationService validation_service;
	
	@Autowired
	DataTableDAO dataTableDAO;
	
	@Autowired
	ScheduleMultiThread multiThread;

	Logger logger = LoggerFactory.getLogger(LibraryService.class);
	
	public List<BookData> findAllBooks(){
		logger.info("Getting books from the Book list");
		List<BookData> list = lib_DAO.getAllBooks();
		
		System.out.println(list.toString());
		return list;
	}
	
	public BookData findBookByName(String book_name) {
		BookData book = null;
		if(!lib_DAO.checkBookName(book_name)) {
			
			book = lib_DAO.getBookByName(book_name);
			
		}
		logger.info("Finding Book with book name : "+ book_name);
		return book;
	};
	
	public List<BookData> find_book_by_author(String author_name){
		logger.info("Finding Book of author : "+ author_name);
		List<BookData> books=lib_DAO.getBooksByAuthor(author_name);
		logger.info("Success in service");
		return books;
	}
	
	public ResponseData<String> insertBook(BookData book) {
		ResponseData<String> response;
//		System.out.println(book.getQuantity() + " book.getAvailable_quantity()");
		if(book.getQuantity() >0) {
			System.out.println(lib_DAO.checkBookName(book.getBook_name()) + "      lib_DAO.checkBookName(book.getBook_name())");
			if(lib_DAO.checkBookName(book.getBook_name())) {
				
				logger.info("Trying to insert book with data : "+ book);
				int result = lib_DAO.insertBook(book);
				
				System.out.println(result + "    result");
				if(result == 0) {
					logger.warn("Some values are missing");
					response = new ResponseData<String>("Some values are missing", "Failure");
					return response;
				}
			}else {
				logger.warn("Book is already in list");
				response = new ResponseData<String>("Book is already in list", "Failure");
				return response;
			}
		}else {
			logger.warn("Quantity has to more than 0");
			response = new ResponseData<String>("Quantity has to more than 0", "Failure");
			return response;
		}
		logger.info("Book Inserted Successfully");
		response = new ResponseData<String>("Book Inserted Successfully", "Success");
		return response;
	}
	
	public ResponseData<String> updateQuantity(int quantity, int book_id) {

		ResponseData<String> response;
		int query_result = 0;
		if(quantity > 0) {
			int available_quantity_in_book_list = lib_DAO.getQuantity(book_id);
			int result = quantity;
			List<WaitingList> waiting = lib_DAO.getListOfWaitingForBook(book_id);
			
			Gson gson = new Gson();
			System.out.println(gson.toJson(waiting));
			
			if(waiting.size() > 0) {
				logger.info("the book has the waiting list for this book_id : "+ book_id);
				if(result > waiting.size()) {
					query_result = lib_DAO.upgradeQuantity(available_quantity_in_book_list + (result-waiting.size()), book_id);
					logger.info("Quantity is updated for the id : "+ book_id + " with a quantity of "+ available_quantity_in_book_list + (result-waiting.size()));
				}
				else if(result <= waiting.size()) {
					
					query_result = lib_DAO.upgradeQuantity(available_quantity_in_book_list + (0), book_id);
					logger.info("Quantity is updated for the id : "+ book_id + " with a quantity of "+ available_quantity_in_book_list + (0));
					waiting = waiting.subList(0, result);					
				}
				System.out.println(waiting);
				for (WaitingList waitingList : waiting) {
					StudentBookRegister registerstudent = new StudentBookRegister();
					registerstudent.setBook_id(book_id);
					registerstudent.setStudent_id(waitingList.getStudent_id());
					int issue_id = lib_DAO.insertDataStudentBookRegister(registerstudent);
					multiThread.scheduleIndividualTask(issue_id);
					logger.info("waiting list book was registered in student book register with id : " + issue_id);
					lib_DAO.changeWaitingFlag(waitingList.getStudent_id(), book_id);
				}
			}
			else {
			query_result = lib_DAO.upgradeQuantity(available_quantity_in_book_list + result, book_id);
			logger.info("Quantity is updated for the id : "+ book_id + " with a quantity of "+ available_quantity_in_book_list);

			}
			
			if(query_result == 0) {
				response = new ResponseData<String>("Book ID not found", "Failure");
				logger.warn("Book ID not found");
				return response;
			}
			else {
				response = new ResponseData<String>("Quantity updated Successfully", "Success");
				logger.info("Quantity updated Successfully");
				return response;
			}
		}
		else {
			response = new ResponseData<String>("Quantity has to be more than 0 to be updated", "Failure");
			logger.info("Quantity has to be more than 0 to be updated");
			return response;
		}
	}
	
	public List<RegisteredStudentForBookResponse> getStudent_book_register (){
		List<RegisteredStudentForBookResponse> list = new ArrayList<RegisteredStudentForBookResponse>();
		List<StudentBookRegister> student_book_register = lib_DAO.getStudentBookRegister();
		for (StudentBookRegister student_book : student_book_register) {
			
			int student_id = student_book.getStudent_id();
			String student_name = stu_DAO.getStudentNameByStudentID(student_id);
			int book_id = student_book.getBook_id();
			String book_name = lib_DAO.getBookNameUsingBookID(book_id);
			String checkintime = student_book.getCheck_in_time();
			list.add(new RegisteredStudentForBookResponse(student_name, book_name, checkintime));
			
		}
		logger.info("Getting the value from student register");
		return list;
		
		
	}
	
	public ResponseData<MaxBookCountResponse> get_max_book_checkin_with_date(DateRequestCheckin request){

		
		ResponseData<MaxBookCountResponse> response;
		
		if(validation_service.isValidDate(request.getMax_date()) && validation_service.isValidDate(request.getMin_date())) {
			
			LocalDate today = LocalDate.now();
			LocalDate max_date = validation_service.changeToDate(request.getMax_date());
			LocalDate min_date = validation_service.changeToDate(request.getMin_date());
			
			if(validation_service.check_min_max_date_format(max_date, min_date)) {
				
				if(validation_service.check_min_max_date_format(today, max_date)) {
					
					MaxBookCountResponse result = new MaxBookCountResponse();
					List<Map<String, Integer>> list = lib_DAO.getBookCountByBookID(request);
					System.out.println(list.size());
					if(list.size() > 0) {
						
						System.out.println(list.toString());
						result.setBook_name(lib_DAO.getBookNameUsingBookID(list.get(0).get("book_id")));
						result.setBook_count(list.get(0).get("book_count"));
						Gson gson = new Gson();
						System.out.println(gson.toJson(result));
						response = new ResponseData<MaxBookCountResponse>("Data Retreived", "Success", result,1);
						logger.info("Success in service");
						return response;
						
						}
					else {
						
						response = new ResponseData<MaxBookCountResponse>("There is no book given between that date range", "Success");
						return response;
						
						}
					}
				else {
					
					response = new ResponseData<MaxBookCountResponse>("max_date cannot be more than today", "Failure");
					return response;
					
					}
				}
			else {
				
				response = new ResponseData<MaxBookCountResponse>("Min_Date cannot be more than the Max_date", "Failure");
				return response;
				
				}
			}
		else {
			
			response = new ResponseData<MaxBookCountResponse>("Enter the correct date format(yyyy-MM-dd)", "Failure");
			return response;
			
			}
		}
	
	public List<BookData> get_max_avail_book(){
		
		logger.info("Success in service");
		return lib_DAO.getMaxAvailableBook();
		}
	
	public List<BookData> get_min_avail_book(){
		logger.info("Success in service");
		return lib_DAO.getMinimumAvailableBook();
		}
	
	public BookData get_max_waiting_book() {
		int book_id = lib_DAO.getBookIDOfMaxWaiting();
		logger.info("Success in service");
		BookData book = lib_DAO.getBookDataByBookID(book_id);
		return book;
		}
	
	public BookData get_min_waiting_book() {
		int book_id = lib_DAO.getBookIdOfMinimumWaiting();
		logger.info("Success in service");
		BookData book = lib_DAO.getBookDataByBookID(book_id);
		return book;
		}
	
	public List<BookData> get_avail_books(){
		logger.info("Success in service");
		return lib_DAO.getAvailableBooks();
	}
	
	public List<BookData> bookDataTable(PageRequest request){
		return dataTableDAO.BookDataTable(request);
	}
	
	}



