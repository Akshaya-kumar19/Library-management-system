package com.example.firstprogramInspring.firstprojectzInSpringboot;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.firstprogramInspring.DAO.LibraryDAO;
import com.example.firstprogramInspring.Model.WaitingList;
import com.example.firstprogramInspring.service.ScheduleMultiThread;

@SpringBootTest
public class TestScheduleMultiThread {
	
	@Mock
	LibraryDAO libraryDAO;
	@InjectMocks
	ScheduleMultiThread  scheduleMultiThread;
	
	@Test
	public void testHandleBookReturn() {
		List<WaitingList> list = new ArrayList<WaitingList>();
		WaitingList waiting = new WaitingList();
		waiting.setBook_id(1);
		waiting.setStudent_id(2);
		waiting.setWaiting_flag("waiting");
		
		when(libraryDAO.getListOfWaitingForBook(anyInt())).thenReturn(list);
		when(libraryDAO.getQuantity(anyInt())).thenReturn(3);
		
		
	}

}
