package com.example.firstprogramInspring.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.stereotype.Service;

import com.example.firstprogramInspring.DAO.LibraryDAO;
import com.example.firstprogramInspring.Model.StudentBookRegister;
import com.example.firstprogramInspring.Model.WaitingList;

@Service
public class SchedulingDemo {
	
//	@Autowired
//	private Library_DAO library_DAO;
//	
//	@Autowired
//	private TaskScheduler taskScheduler;
//	
//	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//
//	
//	public void scheduleIndividualTask(int bookIssueId) {
//		
//		PeriodicTrigger periodicTrigger = new PeriodicTrigger(1, TimeUnit.MINUTES);
//		
//		
//		taskScheduler.schedule(() -> updateIndividualCheckout(bookIssueId), periodicTrigger);
//	}
//
//	
//	public void updateIndividualCheckout(int bookIssueId) {
//		Student_book_register studentBookRegister = library_DAO.findCheckoutById(bookIssueId);
//		
//		if (studentBookRegister.getCheck_out_time() == null) {
//			
//			LocalDateTime checkinTime = LocalDateTime.parse(studentBookRegister.getCheck_in_time(), formatter);
//			System.out.println("check in time : " + checkinTime.toString());
//			
//			long timefinished = ChronoUnit.MINUTES.between(checkinTime, LocalDateTime.now());
//			System.out.println("Time finished: " + timefinished);
//			
//			int originalTimeLeft = library_DAO.getReadTime(studentBookRegister.getBook_id());
//			System.out.println("original read time: "+ originalTimeLeft);
//			
//			int newTimeLeft = originalTimeLeft - (int) timefinished;
//			System.out.println("new time left: "+ newTimeLeft);
//			
//			if (newTimeLeft > 0) {
//				System.out.println("time updated by : "+ newTimeLeft);
//				library_DAO.updateTimeLeft(newTimeLeft, studentBookRegister.getBook_issue_id());
//			} else {
//				System.out.println("time updated by 0");
//				library_DAO.updateTimeLeft(0, studentBookRegister.getBook_issue_id());
//				library_DAO.set_checkout_time(studentBookRegister.getBook_issue_id());
//
//				
//				handleBookReturn(studentBookRegister.getBook_id(), studentBookRegister.getBook_issue_id());
//			}
//		}
//		System.out.println("Process ended");
//	}
//
//	
//	private void handleBookReturn(int bookId, int bookIssueId) {
//		
//		List<waiting_list> waitingList = library_DAO.get_List_of_waiting_for_Book(bookId);
//
//		
//		int availableQuantity = library_DAO.get_quantity(bookId);
//
//		if (waitingList != null && !waitingList.isEmpty()) {
//			
//			waiting_list waiting = waitingList.get(0);
//
//			
//			Student_book_register newCheckout = new Student_book_register();
//			newCheckout.setBook_id(bookId);
//			newCheckout.setStudent_id(waiting.getStudent_id());
//			library_DAO.insert_data_student_book_register(newCheckout);
//
//			
//			library_DAO.change_waiting_flag(waiting.getStudent_id(), bookId);
//		} else {
//			
//			library_DAO.upgradeQuantity(availableQuantity + 1, bookId);
//		}
//	}
}
