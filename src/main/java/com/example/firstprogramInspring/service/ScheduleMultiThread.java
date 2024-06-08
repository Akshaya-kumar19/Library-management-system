package com.example.firstprogramInspring.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.firstprogramInspring.DAO.LibraryDAO;
import com.example.firstprogramInspring.Model.StudentBookRegister;
import com.example.firstprogramInspring.Model.WaitingList;

@Service
public class ScheduleMultiThread {
	
	@Autowired
	private LibraryDAO library_DAO;
	
	
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public void scheduleIndividualTask(int bookIssueId) {
		logger.info("Work started on the another thread to update time");
		Runnable task = ()-> {
					while(true) {
							boolean checking_book_time = updateIndividualCheckout(bookIssueId);
							
							if(checking_book_time == false) {
								break;
							}
							
							try {
								int minute = 3;
								Thread.sleep(minute*60*1000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
				};
		
		Thread thread = new Thread(task);
		System.out.println("book issue id : "+ bookIssueId+ " running on: "+ Thread.currentThread());
        thread.start();
		
	}

	
	public boolean updateIndividualCheckout(int bookIssueId) {
		StudentBookRegister studentBookRegister = library_DAO.findCheckoutById(bookIssueId);
		
		if (studentBookRegister.getCheck_out_time() == null) {
			
			LocalDateTime checkinTime = LocalDateTime.parse(studentBookRegister.getCheck_in_time(), formatter);
			System.out.println("check in time : " + checkinTime.toString());
			
			long timefinished = ChronoUnit.MINUTES.between(checkinTime, LocalDateTime.now());
			System.out.println("Time finished: " + timefinished);
			
			int originalTimeLeft = library_DAO.getReadTime(studentBookRegister.getBook_id());
			if(originalTimeLeft == -1) {
				return false;
			}
			System.out.println("original read time: "+ originalTimeLeft);
			
			int newTimeLeft = originalTimeLeft - (int) timefinished;
			System.out.println("new time left: "+ newTimeLeft);
			
			if (newTimeLeft > 0) {
				System.out.println("time updated by : "+ newTimeLeft);
				library_DAO.updateTimeLeft(newTimeLeft, studentBookRegister.getBook_issue_id());
				return true;
			} else {
				System.out.println("time updated by 0");
				library_DAO.updateTimeLeft(0, studentBookRegister.getBook_issue_id());
				library_DAO.setCheckoutTime(studentBookRegister.getBook_issue_id());
				handleBookReturn(studentBookRegister.getBook_id(), studentBookRegister.getBook_issue_id());
				return false;
			}
		}
		
		System.out.println("Process ended");
		return false;
	}

	
	private void handleBookReturn(int bookId, int bookIssueId) {
		
		List<WaitingList> waitingList = library_DAO.getListOfWaitingForBook(bookId);

		
		int availableQuantity = library_DAO.getQuantity(bookId);

		if (waitingList != null && !waitingList.isEmpty()) {
			
			WaitingList waiting = waitingList.get(0);

			
			StudentBookRegister newCheckout = new StudentBookRegister();
			newCheckout.setBook_id(bookId);
			newCheckout.setStudent_id(waiting.getStudent_id());
			library_DAO.insertDataStudentBookRegister(newCheckout);

			
			library_DAO.changeWaitingFlag(waiting.getStudent_id(), bookId);
		} else {
			
			library_DAO.upgradeQuantity(availableQuantity + 1, bookId);
		}
	}
}

