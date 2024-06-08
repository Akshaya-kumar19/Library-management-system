package com.example.firstprogramInspring.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.example.firstprogramInspring.Model.BookData;
import com.example.firstprogramInspring.Model.CheckOutRequest;
import com.example.firstprogramInspring.Model.DateRequestCheckin;
import com.example.firstprogramInspring.Model.MaxBookCountResponse;
import com.example.firstprogramInspring.Model.PageRequest;
import com.example.firstprogramInspring.Model.RegisteredStudentForBookResponse;
import com.example.firstprogramInspring.Model.ResponseData;
import com.example.firstprogramInspring.Model.CheckingInRequest;
import com.example.firstprogramInspring.service.CheckInService;
import com.example.firstprogramInspring.service.CheckOutService;
import com.example.firstprogramInspring.service.LibraryService;
import com.example.firstprogramInspring.service.StudentService;
import com.google.gson.Gson;

import ch.qos.logback.classic.Logger;
import io.swagger.v3.oas.annotations.tags.Tag;


import org.slf4j.LoggerFactory;

@RestController
@Tag(description = "Controller for library management", name = "Library")
public class LibraryController {
	@Autowired
	LibraryService library_service;
	@Autowired
	Gson gson;
	@Autowired
	CheckInService check_in_service;
	@Autowired
	CheckOutService check_out_service;
	@Autowired
	StudentService student_service;

	Logger logger =(Logger) LoggerFactory.getLogger(LibraryController.class);

	@Autowired
	public LibraryController(LibraryService libraryService) {
		// TODO Auto-generated constructor stub
		this.library_service = libraryService;

	}

	@GetMapping("/get-all-books")
	public ResponseEntity<ResponseData<List<BookData>>> getAllBooks(){
		try {
			//			System.out.println(student_service);

			ResponseData<List<BookData>> response;
			logger.info("API started on library controller ");
			System.out.println("this is the log");
			logger.debug("this is test for the logger storing");
			List<BookData> list = library_service.findAllBooks();

			response = new ResponseData<List<BookData>>("Data retrieved", "Success", list, list.size());
			ResponseEntity<ResponseData<List<BookData>>> resp = new ResponseEntity<ResponseData<List<BookData>>>(response, HttpStatus.OK);
			return resp;
		}catch (DataAccessException e) {
			// TODO: handle exception
			logger.error("Error Occured --> " + e.getCause() + "\n Error Message: " + e.getMessage());
			return new ResponseEntity<ResponseData<List<BookData>>>(new ResponseData<List<BookData>>(e.getMessage(), "Error occured"), HttpStatus.BAD_REQUEST);
		}
		catch (Exception e) {
			logger.error("Error Occured --> " + e.getCause() + "\n Error Message: " + e.getMessage());
			return new ResponseEntity<ResponseData<List<BookData>>>(new ResponseData<List<BookData>>(e.getMessage(), "Error occured"), HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/get-book-by-author")
	public ResponseEntity<ResponseData<List<BookData>>> getBookByAuthor(@RequestBody BookData request){
		try {
			ResponseData<List<BookData>> response;
			String author = request.getAuthor_name();
			logger.info("Finding book of author : " + author);
			List<BookData> books = library_service.find_book_by_author(author);

			response = new ResponseData<List<BookData>>("Data retrieved", "Success", books, books.size());
			return new ResponseEntity<ResponseData<List<BookData>>>(response, HttpStatus.OK);
		}catch (DataAccessException e) {
			logger.error("Error Occured --> " + e.getCause() + "\n Error Message: " + e.getMessage());
			return new ResponseEntity<ResponseData<List<BookData>>>(new ResponseData<List<BookData>>(e.getMessage(), "Error occured"), HttpStatus.BAD_REQUEST);
		}
		catch (Exception e) {
			logger.error("Error Occured --> " + e.getCause() + "\n Error Message: " + e.getMessage());
			return new ResponseEntity<ResponseData<List<BookData>>>(new ResponseData<List<BookData>>("Data Not retrieved", "Error occured"), HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/get-book-by-name/{book_name}")
	public ResponseEntity<ResponseData<BookData>> getBookByName(@PathVariable String book_name){
		try {
			logger.info("Finding book with a name : " + book_name);
			BookData book = library_service.findBookByName(book_name);

			return new ResponseEntity<ResponseData<BookData>>(new ResponseData<BookData>("Data retrieved", "Success", book, 1), HttpStatus.OK);
		}catch(EmptyResultDataAccessException e) {
			logger.error("Error Occured --> " + e.getCause() + "\n Error Message: " + e.getMessage());
			return new ResponseEntity<ResponseData<BookData>>(new ResponseData<BookData>("Data Not retrieved", "Error occured"), HttpStatus.BAD_REQUEST);
		}catch (Exception e) {
			logger.error("Error Occured --> " + e.getCause() + "\n Error Message: " + e.getMessage());
			return new ResponseEntity<ResponseData<BookData>>(new ResponseData<BookData>(e.getMessage(), "Error occured"), HttpStatus.BAD_REQUEST);

		}
	}

	@PostMapping("/insert-book")
	public ResponseEntity<ResponseData<String>> insertBook(@RequestBody BookData book ){
		try {
			logger.info("Inserting Book with data : "+ book.toString());
			ResponseData<String> response = library_service.insertBook(book);
			//			System.out.println(gson.toJson(book));
			//			System.out.println(library_service.insertBook(book) + "       lib_ser.insertBook(book)");

			return new ResponseEntity<ResponseData<String>>(response, HttpStatus.OK);
		}catch (DataAccessException e) {
			logger.error("Error Occured --> " + e.getCause() + "\n Error Message: " + e.getMessage());
			return new ResponseEntity<ResponseData<String>>(new ResponseData<String>(e.getMessage(), "Error occured"), HttpStatus.BAD_REQUEST);

		}
		catch (Exception e) {
			logger.error("Error Occured --> " + e.getCause() + "\n Error Message: " + e.getMessage());
			return new ResponseEntity<ResponseData<String>>(new ResponseData<String>("Data Not retrieved", "Error occured"), HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping("/upgrade-quantity/{book_id}")
	public ResponseEntity<ResponseData<String>> updateQuantity(@RequestBody Map<String, Integer> request,@PathVariable int book_id ){
		try {
			int quantity = request.get("quantity");
			logger.info("Updating for the book with id : "+ book_id + "with the quantity of "+ quantity);
			ResponseData<String> response = library_service.updateQuantity(quantity, book_id);

			return new ResponseEntity<ResponseData<String>>(response, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error Occured --> " + e.getCause() + "\n Error Message: " + e.getMessage());
			return new ResponseEntity<ResponseData<String>>(new ResponseData<String>("Update not happened", "Error occured"), HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/Check-in-book")
	public ResponseEntity<?> checkIn(@RequestBody CheckingInRequest request){
		try {
			logger.info("The check in process is started for the request: "+ request.toString());
			Object result = check_in_service.CheckInStudent(request);

			if(result instanceof byte[]) {
				
				String studentName = request.getStudent_name().replace(" ", "_");
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy_HHmmss");
				String date = LocalDateTime.now().format(formatter);
				return ResponseEntity.ok()
						.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+studentName+"_"+request.getRegister_id()+"_"+date+".pdf")
						.contentType(MediaType.APPLICATION_PDF)
						.body(result);
			}

			return ResponseEntity.badRequest().body(result);

		}
		catch (DataAccessException e) {
			logger.error("Error Occured --> " + e.getCause() + "\n Error Message: "+ e.getMessage());
			return new ResponseEntity<ResponseData<String>>(new ResponseData<String>(e.getMessage(), "Error occured"), HttpStatus.BAD_REQUEST);

		}
		catch (Exception e) {
			logger.error("Error Occured --> " + e.getCause() + "\n Error Message: " + 2+ e.getMessage());

			return new ResponseEntity<ResponseData<String>>(new ResponseData<String>(e.getMessage(), "Error occured"), HttpStatus.BAD_REQUEST);
		}

	}

	@GetMapping("/get-all-student-with-book")
	public ResponseEntity<ResponseData<List<RegisteredStudentForBookResponse>>> getAllStudentWithBook(){
		try {
			logger.info("get all student who has the book in hand");
			List<RegisteredStudentForBookResponse> list = library_service.getStudent_book_register();

			return new ResponseEntity<ResponseData<List<RegisteredStudentForBookResponse>>>(new ResponseData<List<RegisteredStudentForBookResponse>>("Data retrieved", "Success", list, list.size()), HttpStatus.OK);
		}catch (DataAccessException e) {
			logger.error("Error Occured --> " + e.getCause() + "\n Error Message: " + e.getMessage());
			return new ResponseEntity<ResponseData<List<RegisteredStudentForBookResponse>>>(new ResponseData<List<RegisteredStudentForBookResponse>>(e.getMessage(), "Error occured"), HttpStatus.BAD_REQUEST);

		} 
		catch (Exception e) {
			logger.error("Error Occured --> " + e.getCause() + "\n Error Message: " + e.getMessage());
			logger.info("Data retrived from database");
			return new ResponseEntity<ResponseData<List<RegisteredStudentForBookResponse>>>(new ResponseData<List<RegisteredStudentForBookResponse>>("Data Not retrieved", "Error occured"), HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/check-out-book")
	public ResponseEntity<ResponseData<String>> checkout(@RequestBody CheckOutRequest request) {
		try {
			logger.info("Checkout book for the request : "+ request.toString());
			String mg = check_out_service.CheckOutBook(request);

			return new ResponseEntity<ResponseData<String>>(new ResponseData<String>(mg, "Success"), HttpStatus.OK);
		}catch (DataAccessException e) {
			logger.error("Error Occured --> " + e.getCause() + "\n Error Message: " + e.getMessage());
			return new ResponseEntity<ResponseData<String>>(new ResponseData<String>(e.getMessage(), "Error occured"), HttpStatus.BAD_REQUEST);

		} 
		catch (Exception e) {
			logger.error("Error Occured --> " + e.getCause() + "\n Error Message: " + e.getMessage());
			return new ResponseEntity<ResponseData<String>>(new ResponseData<String>("Check out didn't happen", "Error occured"), HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/get-most-checked-in-book-by-date")
	public ResponseEntity<ResponseData<MaxBookCountResponse>> getMaximumBookByDate(@RequestBody DateRequestCheckin request){
		ResponseData<MaxBookCountResponse> result;
		try {
			result = library_service.get_max_book_checkin_with_date(request);
			logger.info("Data retrived from database");
			return new ResponseEntity<ResponseData<MaxBookCountResponse>>(result, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error Occured --> " + e.getCause() + "\n Error Message: " + e.getMessage());
			return new ResponseEntity<ResponseData<MaxBookCountResponse>>(new  ResponseData<MaxBookCountResponse>(e.getMessage(),"Error occured" ), HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/get-most-avail-book")
	public ResponseEntity<ResponseData<List<BookData>>> getMostAvailableBook(){
		try {
			//			System.out.println("controller");
			List<BookData> list = library_service.get_max_avail_book();
			logger.info("Data retrived from database");
			return new ResponseEntity<ResponseData<List<BookData>>>(new ResponseData<List<BookData>>("Data retrieved", "Success", list, list.size()), HttpStatus.OK);
		}catch (DataAccessException e) {
			logger.error("Error Occured --> " + e.getCause() + "\n Error Message: " + e.getMessage());
			return new ResponseEntity<ResponseData<List<BookData>>>(new ResponseData<List<BookData>>(e.getMessage(),"Error occured" ), HttpStatus.BAD_REQUEST);
		}
		catch (Exception e) {
			logger.error("Error Occured --> " + e.getCause() + "\n Error Message: " + e.getMessage());
			return new ResponseEntity<ResponseData<List<BookData>>>(new ResponseData<List<BookData>>(e.getMessage(),"Error occured" ), HttpStatus.BAD_REQUEST);

		}

	}

	@GetMapping("/get-min-avail-books")
	public ResponseEntity<ResponseData<List<BookData>>> getMinimumAvailableBook(){
		try {
			List<BookData> list = library_service.get_min_avail_book();
			logger.info("Data retrived from database");
			return new ResponseEntity<ResponseData<List<BookData>>>(new ResponseData<List<BookData>>("Data retrieved", "Success", list, list.size()), HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error Occured --> " + e.getCause() + "\n Error Message: " + e.getMessage());

			return new ResponseEntity<ResponseData<List<BookData>>>(new ResponseData<List<BookData>>(e.getMessage(), "Error occured"), HttpStatus.BAD_REQUEST);

		}
	}

	@GetMapping("/get-maximum-waiting-book")
	public ResponseEntity<ResponseData<BookData>> getMaxWaitingBook(){
		try {
			BookData list = library_service.get_max_waiting_book();
			logger.info("Data retrived from database");
			return new ResponseEntity<ResponseData<BookData>>(new ResponseData<BookData>("Data retrieved", "Success", list,1), HttpStatus.OK);
		}catch (DataAccessException e) {
			return new ResponseEntity<ResponseData<BookData>>(new ResponseData<BookData>(e.getMessage(), "Error Occured"), HttpStatus.BAD_REQUEST);
		} 
		catch (Exception e) {
			logger.error("Error Occured --> " + e.getCause() + "\n Error Message: " + e.getMessage());
			return new ResponseEntity<ResponseData<BookData>>(new ResponseData<BookData>(e.getMessage(), "Error Occured"), HttpStatus.BAD_REQUEST);

		}
	}

	@GetMapping("/get-minimum-waiting-book")
	public ResponseEntity<ResponseData<BookData>> getMinimumWaitingBook(){
		try {
			BookData list = library_service.get_min_waiting_book();
			logger.info("Data retrived from database");
			return new ResponseEntity<ResponseData<BookData>>(new ResponseData<BookData>("Data retrieved", "Success", list, 1), HttpStatus.OK);
		} catch (DataAccessException e) {
			logger.error("Error Occured --> " + e.getCause() + "\n Error Message: " + e.getMessage());
			return new ResponseEntity<ResponseData<BookData>>(new ResponseData<BookData>(e.getMessage(), "Error occured"), HttpStatus.BAD_REQUEST);

		} 
		catch (Exception e) {
			logger.error("Error Occured --> " + e.getCause() + "\n Error Message: " + e.getMessage());
			return new ResponseEntity<ResponseData<BookData>>(new ResponseData<BookData>("Data not retrieved", "Error occured"), HttpStatus.BAD_REQUEST);

		}
	}

	@GetMapping("/get-available-books")
	public ResponseEntity<ResponseData<List<BookData>>> getAvailableBook(){

		try {
			List<BookData> list = library_service.get_avail_books();
			logger.info("Data retrived from database");
			return new ResponseEntity<ResponseData<List<BookData>>>(new ResponseData<List<BookData>>("Data retrieved", "Success", list, list.size()), HttpStatus.OK);
		}catch (DataAccessException e) {
			logger.error("Error Occured --> " + e.getCause() + "\n Error Message: " + e.getMessage());
			return new ResponseEntity<ResponseData<List<BookData>>>(new ResponseData<List<BookData>>(e.getMessage(), "Error occured"), HttpStatus.BAD_REQUEST);

		}
		catch (Exception e) {

			logger.error("Error Occured --> " + e.getCause() + "\n Error Message: " + e.getMessage());
			return new ResponseEntity<ResponseData<List<BookData>>>(new ResponseData<List<BookData>>("Data not retrieved", "Error occured"), HttpStatus.BAD_REQUEST);

		}
	}

	@PostMapping("/bookSearchingSortingDataTable")
	public ResponseEntity<List<BookData>> bookDataTable(PageRequest request){
		return new ResponseEntity<List<BookData>>(library_service.bookDataTable(request), HttpStatus.OK);
	}
}
