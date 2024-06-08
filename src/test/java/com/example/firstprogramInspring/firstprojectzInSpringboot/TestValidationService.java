package com.example.firstprogramInspring.firstprojectzInSpringboot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.firstprogramInspring.service.ValidationService;

@SpringBootTest
public class TestValidationService {
	@InjectMocks
	ValidationService validationService;
	
	@Test
	public void testIsValidDateReturnsTrue() {
		String today = LocalDate.now().toString();
		boolean actual = validationService.isValidDate(today);
		
		assertTrue(actual);
		
		
	}
	
	@Test
	public void testIsValidDateThrowsException() {
		String today = "anystring";
		boolean actual = validationService.isValidDate(today);
		
		assertFalse(actual);
		
		
	}
	
	@Test
	public void testChangeToDateSuccess() {
		LocalDate date1 =LocalDate.now() ;
		String today =date1.toString();
		
		LocalDate date = validationService.changeToDate(today);
		assertEquals(date1, date);
	}
	
	@Test
	public void testCheckMinMaxDateFormatReturnsFalse() {
		LocalDate date1 = LocalDate.now();
		LocalDate date2 = date1.plusDays(2);
		boolean actual = validationService.check_min_max_date_format(date1, date2);
		assertFalse(actual);
	}
	
	@Test
	public void testCheckMinMaxDateFormatReturnsTrue() {
		LocalDate date1 = LocalDate.now();
		LocalDate date2 = date1.plusDays(2);
		boolean actual = validationService.check_min_max_date_format(date2, date1);
		assertTrue(actual);
	}
}
