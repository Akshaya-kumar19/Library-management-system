package com.example.firstprogramInspring.service;

import org.springframework.stereotype.Service;
import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Service
public class ValidationService {
	
	//static String format = "yyyy-MM-dd";
	
	public boolean isValidDate(String dateStr) {
        try {
            //DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            LocalDate.parse(dateStr);
          
            return true;
        } catch (DateTimeParseException e) {
            
            return false;
        }
    }
	
	
	public LocalDate changeToDate(String dateText) {
		return LocalDate.parse(dateText);
	}
	
	public boolean check_min_max_date_format(LocalDate date1, LocalDate date2) {
		if(date1.compareTo(date2) >= 0) {
			return true;
		}
		return false;
	}
}
