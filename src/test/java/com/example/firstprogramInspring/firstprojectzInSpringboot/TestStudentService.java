package com.example.firstprogramInspring.firstprojectzInSpringboot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.firstprogramInspring.DAO.StudentDAO;
import com.example.firstprogramInspring.Model.ResponseData;
import com.example.firstprogramInspring.Model.StudentData;
import com.example.firstprogramInspring.service.StudentService;

@SpringBootTest
public class TestStudentService {
	
	@Mock
	StudentDAO studentDAO;
	
	@InjectMocks
	StudentService studentService;
	
	@Test
	public void testFindall() {
		StudentData studentData = new StudentData();
		studentData.setName("student name");
		
		List<StudentData> list = new ArrayList<StudentData>();
		list.add(studentData);
		
		when(studentDAO.findall()).thenReturn(list);
		
		List<StudentData> result = studentService.findall();
		
		assertEquals(list, result);
	}
	
	@Test
	public void testFindByCurrentStatusActive(){
		StudentData studentData = new StudentData();
		studentData.setName("student name");
		
		List<StudentData> list = new ArrayList<StudentData>();
		list.add(studentData);
		
		when(studentDAO.findByCurrentStatus(anyString())).thenReturn(list);
		String current_status = "active";
		List<StudentData> result = studentService.findByCurrentStatus(current_status);
		
		assertEquals(list, result);
	}
	
	@Test
	public void testFindByCurrentStatusInactive(){
		StudentData studentData = new StudentData();
		studentData.setName("student name");
		
		List<StudentData> list = new ArrayList<StudentData>();
		list.add(studentData);
		
		when(studentDAO.findByCurrentStatus(anyString())).thenReturn(list);
		String current_status = "inactive";
		List<StudentData> result = studentService.findByCurrentStatus(current_status);
		
		assertEquals(list, result);
	}
	
	@Test
	public void testFindByCurrentStatusNotCorrect(){
		StudentData studentData = new StudentData();
		studentData.setName("student name");
		
		List<StudentData> list = new ArrayList<StudentData>();
		list.add(studentData);
		String current_status = "in";
		List<StudentData> result = studentService.findByCurrentStatus(current_status);
		
		assertEquals(null, result);
	}
	
	@Test
	public void testCurrentStatusCountSuccessByActive(){
		
		when(studentDAO.CurrentStatusCount(anyString())).thenReturn(2);
		String current_status = "active";
		int result = studentService.CurrentStatusCount(current_status);
		
		assertEquals(2, result);
	}
	
	@Test
	public void testCurrentStatusCountSuccessByInactive(){
		
		when(studentDAO.CurrentStatusCount(anyString())).thenReturn(2);
		String current_status = "inactive";
		int result = studentService.CurrentStatusCount(current_status);
		
		assertEquals(2, result);
	}
	
	@Test
	public void testCurrentStatusCountIsNotFound(){
		
		
		String current_status = "inact";
		int result = studentService.CurrentStatusCount(current_status);
		
		assertEquals(0, result);
	}
	
	@Test
	public void testDeletebyidSuccess(){
		
		
		when(studentDAO.deleteByID(anyInt())).thenReturn(1);
		ResponseData<String> result = studentService.deletebyid(anyInt());
		
		assertEquals("Deleted Successfully", result.getMsg());
		assertEquals("Success", result.getStatus());
	}
	
	@Test
	public void testDeletebyidDataNotDeleted(){
		
		
		when(studentDAO.deleteByID(anyInt())).thenReturn(0);
		ResponseData<String> result = studentService.deletebyid(anyInt());
		
		assertEquals("ID Not Found", result.getMsg());
		assertEquals("Failure", result.getStatus());
	}
	
	@Test
	public void testUpdateSuccess(){
		
		
		when(studentDAO.checkRegisterNo(anyInt())).thenReturn(true);
		when(studentDAO.update(any(), anyInt())).thenReturn(1);
		StudentData studentData = new StudentData();
		studentData.setName("student name");
		ResponseData<String> result = studentService.update(studentData, anyInt());
		
		assertEquals("Updated successfully", result.getMsg());
		assertEquals("Success", result.getStatus());
	}
	
	@Test
	public void testUpdateRegisterNumberExists(){
		
		
		when(studentDAO.checkRegisterNo(anyInt())).thenReturn(false);
		
		StudentData studentData = new StudentData();
		studentData.setName("student name");
		ResponseData<String> result = studentService.update(studentData, anyInt());
		
		assertEquals("Register Number already exists", result.getMsg());
		assertEquals("Failure", result.getStatus());
	}
	
	@Test
	public void testUpdateIDNotFound(){
		
		
		when(studentDAO.checkRegisterNo(anyInt())).thenReturn(true);
		when(studentDAO.update(any(), anyInt())).thenReturn(0);
		StudentData studentData = new StudentData();
		studentData.setName("student name");
		ResponseData<String> result = studentService.update(studentData, anyInt());
		
		assertEquals("ID not Found", result.getMsg());
		assertEquals("Failure", result.getStatus());
	}
	
	@Test
	public void testFindbyidSucess(){
		
		StudentData studentData = new StudentData();
		studentData.setName("student name");
		when(studentDAO.findbyid(anyInt())).thenReturn(studentData);
		StudentData result = studentService.findbyid(anyInt());
		
		assertEquals(studentData, result);
		
	}
	
	@Test
	public void testValidateSuccess(){
		
		StudentData studentData = new StudentData();
		studentData.setName("student name");
		studentData.setAge(19);
		studentData.setGender("Male");
		studentData.setPhone_number("1010101010");
		
		when(studentDAO.checkRegisterNo(anyInt())).thenReturn(true);
		when(studentDAO.save(any())).thenReturn(1);
		ResponseData<String> result = studentService.validate(studentData);
		
		assertEquals("Value inserted Successfully", result.getMsg());
		assertEquals("Success", result.getStatus());
		
	}
	
	@Test
	public void testValidateSuccessAndGenderIsFemale(){
		
		StudentData studentData = new StudentData();
		studentData.setName("student name");
		studentData.setAge(19);
		studentData.setGender("feMale");
		studentData.setPhone_number("1010101010");
		
		when(studentDAO.checkRegisterNo(anyInt())).thenReturn(true);
		when(studentDAO.save(any())).thenReturn(1);
		ResponseData<String> result = studentService.validate(studentData);
		
		assertEquals("Value inserted Successfully", result.getMsg());
		assertEquals("Success", result.getStatus());
		
	}
	
	@Test
	public void testValidateGenderIsInvalid(){
		
		StudentData studentData = new StudentData();
		studentData.setName("student name");
		studentData.setAge(19);
		studentData.setGender("any string");
		studentData.setPhone_number("1010101010");
		
		when(studentDAO.checkRegisterNo(anyInt())).thenReturn(true);
		when(studentDAO.save(any())).thenReturn(1);
		ResponseData<String> result = studentService.validate(studentData);
		
		assertEquals("Gender must be male or female", result.getMsg());
		assertEquals("Failure", result.getStatus());
		
	}
	
	@Test
	public void testValidatePhoneNumberIsInvalid(){
		
		StudentData studentData = new StudentData();
		studentData.setName("student name");
		studentData.setAge(19);
		studentData.setGender("male");
		studentData.setPhone_number("10101010");
		
		when(studentDAO.checkRegisterNo(anyInt())).thenReturn(true);
		when(studentDAO.save(any())).thenReturn(1);
		ResponseData<String> result = studentService.validate(studentData);
		
		assertEquals("Phone no must be 10", result.getMsg());
		assertEquals("Failure", result.getStatus());
		
	}
	
	@Test
	public void testValidateRegisterNumberExists(){
		
		StudentData studentData = new StudentData();
		studentData.setName("student name");
		studentData.setAge(19);
		studentData.setGender("male");
		studentData.setPhone_number("1010101010");
		
		when(studentDAO.checkRegisterNo(anyInt())).thenReturn(false);
		when(studentDAO.save(any())).thenReturn(1);
		ResponseData<String> result = studentService.validate(studentData);
		
		assertEquals("Register number already exist", result.getMsg());
		assertEquals("Failure", result.getStatus());
		
	}
	
	@Test
	public void testValidateQueryNotExcecuted(){
		
		StudentData studentData = new StudentData();
		studentData.setName("student name");
		studentData.setAge(19);
		studentData.setGender("male");
		studentData.setPhone_number("1010101010");
		
		when(studentDAO.checkRegisterNo(anyInt())).thenReturn(true);
		when(studentDAO.save(any())).thenReturn(0);
		ResponseData<String> result = studentService.validate(studentData);
		
		assertEquals("Some of the value is missing please check", result.getMsg());
		assertEquals("Failure", result.getStatus());
		
	}
	
}
