package com.example.firstprogramInspring.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.firstprogramInspring.DAO.LibraryDAO;
import com.example.firstprogramInspring.Model.StudentBookRegister;
import com.example.firstprogramInspring.Model.WaitingList;

@Service
public class SchedulingService {
	
//	@Autowired
//	Library_DAO library_DAO;
//	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//	
//	
//	//@Scheduled(cron = "0 */3 * * * *")
//	public void update_check_out() {
//		
//		List<Student_book_register> list= library_DAO.get_list_of_ongoing_reading();
//		
//		for (Student_book_register student_book_register : list) {
//			
//			System.out.println(student_book_register);
//			
//			LocalDateTime checkinTime = LocalDateTime.parse(student_book_register.getCheck_in_time(), formatter);
//			
//			
//			long TimeFinished = ChronoUnit.MINUTES.between( checkinTime, LocalDateTime.now());
//			System.out.println("time finished : " + TimeFinished);
//			int timeleft = library_DAO.getReadTime(student_book_register.getBook_id());
//			System.out.println("original time left: " + timeleft);
//			int new_time_left = timeleft - (int) TimeFinished;
//			System.out.println("New time left : " + new_time_left );
//			int available_quantity = library_DAO.get_quantity(student_book_register.getBook_id());
//			
//			if(new_time_left > 0) {
//				
//				library_DAO.updateTimeLeft(new_time_left, student_book_register.getBook_issue_id());
//				
//			}
//			else {
//				
//				library_DAO.updateTimeLeft(0, student_book_register.getBook_issue_id());
//				library_DAO.set_checkout_time(student_book_register.getBook_issue_id());
//				List<waiting_list> waitings = library_DAO.get_List_of_waiting_for_Book(student_book_register.getBook_id());
//				
//				if(available_quantity ==0) {
//					
//					if(waitings.size() > 0) {
//						
//						waiting_list waiting_list = waitings.get(0);
//						Student_book_register registerstudent = new Student_book_register();
//						registerstudent.setBook_id(student_book_register.getBook_id());
//						registerstudent.setStudent_id(waiting_list.getStudent_id());
//						library_DAO.insert_data_student_book_register(registerstudent);
//						library_DAO.change_waiting_flag(waiting_list.getStudent_id(), student_book_register.getBook_id());
//						
//					}
//					else {
//						
//						library_DAO.upgradeQuantity(1, student_book_register.getBook_id());
//						
//					}
//				}
//				else {
//					
//					library_DAO.upgradeQuantity(available_quantity + 1, student_book_register.getBook_id());
//					
//				}
//				
//			}
//			
//			
//		}
//		
//		
//	}
}
