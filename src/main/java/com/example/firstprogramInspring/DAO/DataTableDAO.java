package com.example.firstprogramInspring.DAO;

import java.util.List;

import com.example.firstprogramInspring.Model.BookData;
import com.example.firstprogramInspring.Model.PageRequest;
import com.example.firstprogramInspring.Model.PageRequest2;
import com.example.firstprogramInspring.Model.StudentData;

public interface DataTableDAO {
	
	//first created data table
	
	List<BookData> BookDataTable(PageRequest request);
	
	List<StudentData> studentDataTable(PageRequest request);
	
	
	
	int getCountFilteredRecords(PageRequest request);
	
	int getTotalPages(PageRequest request);
	
	//data table using Javascript response
	
	
	List<StudentData> studentDataTableWithSearchElement(PageRequest2 request);
	
	List<StudentData> studentDataTableWithoutSearchElement(PageRequest2 request);
	
	int getCountFilteredRecords(PageRequest2 request);
	
	
	//common for both the datatables
	int getTotalStudentRecords();
	
	
}
