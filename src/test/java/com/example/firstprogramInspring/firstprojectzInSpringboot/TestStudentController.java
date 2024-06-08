package com.example.firstprogramInspring.firstprojectzInSpringboot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.firstprogramInspring.Model.ResponseData;
import com.example.firstprogramInspring.Model.StudentData;
import com.example.firstprogramInspring.controller.StudentController;
import com.example.firstprogramInspring.service.StudentService;
import com.google.gson.Gson;

@SpringBootTest
public class TestStudentController {
	
	@Mock
	StudentService studentService;
	
//	@Mock
//	Gson gson;
	
	@InjectMocks
	StudentController studentController;
	
	@Test
	public void testFindAllSuccess() {
		List<StudentData> list= new ArrayList<StudentData>();
		StudentData studentData = new StudentData(2, "akahsya", 123, "male", 23, "course", "active");
		list.add(studentData);
		when(studentService.findall()).thenReturn(list);
		ResponseData<List<StudentData>> responseData = studentController.findAll();
		assertEquals("Data Retrieved", responseData.getMsg());
		assertEquals("Success", responseData.getStatus());
		assertEquals(list, responseData.getData());
		assertEquals(list.size(), responseData.getCount());
	}
	@Test
	public void testFindAllDataAccessException() {
		List<StudentData> list= new ArrayList<StudentData>();
		StudentData studentData = new StudentData(2, "akahsya", 123, "male", 23, "course", "active");
		list.add(studentData);
		when(studentService.findall()).thenThrow(new DataAccessException("DataBase Error") {

			private static final long serialVersionUID = 1L;
		});
		ResponseData<List<StudentData>> responseData = studentController.findAll();
		assertEquals("DataBase Error", responseData.getMsg());
		assertEquals("Error Occured", responseData.getStatus());
		assertEquals(null, responseData.getData());
	}
	
	@Test
	public void testFindAllException() {
		List<StudentData> list= new ArrayList<StudentData>();
		StudentData studentData = new StudentData(2, "akahsya", 123, "male", 23, "course", "active");
		list.add(studentData);
		when(studentService.findall()).thenThrow(new RuntimeException("runtime error"));
		ResponseData<List<StudentData>> responseData = studentController.findAll();
		assertEquals("Data not retrieved", responseData.getMsg());
		assertEquals("Error Occured", responseData.getStatus());
		assertEquals(null, responseData.getData());
	}
	
	@Test
	public void testFindByCurrentStatusSuccess() {
		List<StudentData> list= new ArrayList<StudentData>();
		StudentData studentData = new StudentData(2, "akahsya", 123, "male", 23, "course", "active");
		list.add(studentData);
		Map<String, String> requestMap = new HashMap<String, String>();
		requestMap.put("current_status", "active");
		when(studentService.findByCurrentStatus(anyString())).thenReturn(list);
//		when(studentService.CurrentStatusCount(anyString())).thenReturn(1);	
		ResponseEntity<ResponseData<List<StudentData>>> response = studentController.findByCurrentStatus(requestMap);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("Data Retrieved", response.getBody().getMsg());
		assertEquals("Success", response.getBody().getStatus());
		assertEquals(list, response.getBody().getData());
		assertEquals(list.size(), response.getBody().getCount());
		}
	@Test
	public void testFindByCurrentStatusIsNull() {
		List<StudentData> list= new ArrayList<StudentData>();
//		StudentData studentData = new StudentData(2, "akahsya", 123, "male", 23, "course", "active");
//		list.add(studentData);
		Map<String, String> requestMap = new HashMap<String, String>();
		requestMap.put("current_status", null);
		when(studentService.findByCurrentStatus(anyString())).thenReturn(list);
//		when(studentService.CurrentStatusCount(anyString())).thenReturn(1);	
		ResponseEntity<ResponseData<List<StudentData>>> response = studentController.findByCurrentStatus(requestMap);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("Provide the correct current status", response.getBody().getMsg());
		assertEquals("Error", response.getBody().getStatus());
		
		}
	
	@Test
	public void testFindByCurrentStatusIsEmpty() {
		List<StudentData> list= new ArrayList<StudentData>();
//		StudentData studentData = new StudentData(2, "akahsya", 123, "male", 23, "course", "active");
//		list.add(studentData);
		Map<String, String> requestMap = new HashMap<String, String>();
		requestMap.put("current_status", "");
		when(studentService.findByCurrentStatus(anyString())).thenReturn(list);
//		when(studentService.CurrentStatusCount(anyString())).thenReturn(1);	
		ResponseEntity<ResponseData<List<StudentData>>> response = studentController.findByCurrentStatus(requestMap);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("Provide the correct current status", response.getBody().getMsg());
		assertEquals("Error", response.getBody().getStatus());
		
		}
	
	@Test
	public void testFindByCurrentStatusIsNotinactiveOrActive() {
		List<StudentData> list= new ArrayList<StudentData>();
//		StudentData studentData = new StudentData(2, "akahsya", 123, "male", 23, "course", "active");
//		list.add(studentData);
		Map<String, String> requestMap = new HashMap<String, String>();
		requestMap.put("current_status", "any string");
		when(studentService.findByCurrentStatus(anyString())).thenReturn(list);
//		when(studentService.CurrentStatusCount(anyString())).thenReturn(1);	
		ResponseEntity<ResponseData<List<StudentData>>> response = studentController.findByCurrentStatus(requestMap);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("Provide the correct current status", response.getBody().getMsg());
		assertEquals("Error", response.getBody().getStatus());
		
		}
	@Test
	public void testFindByCurrentStatusSuccessWithInactive() {
		List<StudentData> list= new ArrayList<StudentData>();
		StudentData studentData = new StudentData(2, "akahsya", 123, "male", 23, "course", "active");
		list.add(studentData);
		Map<String, String> requestMap = new HashMap<String, String>();
		requestMap.put("current_status", "inactive");
		when(studentService.findByCurrentStatus(anyString())).thenReturn(list);
//		when(studentService.CurrentStatusCount(anyString())).thenReturn(1);	
		ResponseEntity<ResponseData<List<StudentData>>> response = studentController.findByCurrentStatus(requestMap);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("Data Retrieved", response.getBody().getMsg());
		assertEquals("Success", response.getBody().getStatus());
		assertEquals(list, response.getBody().getData());
		assertEquals(list.size(), response.getBody().getCount());
		}
	@Test
	public void testFindByCurrentStatusSuccessWithThrowDataAccessException() {
		Map<String, String> requestMap = new HashMap<String, String>();
		requestMap.put("current_status", "inactive");
		when(studentService.findByCurrentStatus(anyString())).thenThrow(new DataAccessException("this is the database error") {

			private static final long serialVersionUID = 1L;
			
		});
//		when(studentService.CurrentStatusCount(anyString())).thenReturn(1);	
		ResponseEntity<ResponseData<List<StudentData>>> response = studentController.findByCurrentStatus(requestMap);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("this is the database error", response.getBody().getMsg());
		assertEquals("Error Occured", response.getBody().getStatus());
		
		}
	@Test
	public void testFindByCurrentStatusSuccessWithThrowException() {
		Map<String, String> requestMap = new HashMap<String, String>();
		requestMap.put("current_status", "inactive");
		when(studentService.findByCurrentStatus(anyString())).thenThrow(new RuntimeException("this is run time error"));
//		when(studentService.CurrentStatusCount(anyString())).thenReturn(1);	
		ResponseEntity<ResponseData<List<StudentData>>> response = studentController.findByCurrentStatus(requestMap);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("this is run time error", response.getBody().getMsg());
		assertEquals("Error Occured", response.getBody().getStatus());
		
		}
	
	@Test
	public void testFindByIdSuccess() {
		StudentData studentData = new StudentData(2, "akahsya", 123, "male", 23, "course", "active");
		when(studentService.findbyid(anyInt())).thenReturn(studentData);
		
		ResponseEntity<ResponseData<StudentData>> entity = studentController.findById(anyInt());
		
		assertEquals("success", entity.getBody().getData().getStatus());
		assertEquals("success", entity.getBody().getStatus());
		assertEquals(studentData, entity.getBody().getData());
	}
	
	@Test
	public void testFindByIdThrowsEmptyDataAccessException() {
		
		when(studentService.findbyid(anyInt())).thenThrow(new EmptyResultDataAccessException("Empty Data access", 0) {

			private static final long serialVersionUID = 1L;});
		
		ResponseEntity<ResponseData<StudentData>> entity = studentController.findById(anyInt());
		
		assertEquals("failure", entity.getBody().getData().getStatus());
		assertEquals("failure", entity.getBody().getStatus());
		assertEquals("invalid user", entity.getBody().getData().getMsg());
	}
	
	@Test
	public void testFindByIdThrowsDataAccessException() {
		
		when(studentService.findbyid(anyInt())).thenThrow(new DataAccessException("Empty Data access") {

			private static final long serialVersionUID = 1L;});
		
		ResponseEntity<ResponseData<StudentData>> entity = studentController.findById(anyInt());
		
		assertEquals("Empty Data access", entity.getBody().getMsg());
		assertEquals("Error Occured", entity.getBody().getStatus());
		assertEquals(null, entity.getBody().getData());
	}
	
	@Test
	public void testFindByIdThrowsException() {
		
		when(studentService.findbyid(anyInt())).thenThrow(new RuntimeException("run time error"));
		
		ResponseEntity<ResponseData<StudentData>> entity = studentController.findById(anyInt());
		
		assertEquals("run time error", entity.getBody().getMsg());
		assertEquals("Error Occured", entity.getBody().getStatus());
		assertEquals(null, entity.getBody().getData());
	}
	
	@Test
	public void testDeleteByIdSuccess() {
		ResponseData<String> response = new ResponseData<String>("msg", "status");
		when(studentService.deletebyid(anyInt())).thenReturn(response);
		ResponseEntity<ResponseData<String>>  entity = studentController.deleteById(anyInt());
		
		assertEquals("msg", entity.getBody().getMsg());
		assertEquals("status", entity.getBody().getStatus());
	}
	
	@Test
	public void testDeleteByIdThrowsDataAccessException() {
		
		when(studentService.deletebyid(anyInt())).thenThrow(new DataAccessException("database error") {

			private static final long serialVersionUID = 1L;});
		ResponseEntity<ResponseData<String>>  entity = studentController.deleteById(anyInt());
		
		assertEquals("database error", entity.getBody().getMsg());
		assertEquals("Error Occured", entity.getBody().getStatus());
	}
	
	@Test
	public void testDeleteByIdThrowsException() {
		
		when(studentService.deletebyid(anyInt())).thenThrow(new RuntimeException("runtime error"));
		ResponseEntity<ResponseData<String>>  entity = studentController.deleteById(anyInt());
		
		assertEquals("Data Not Retreived", entity.getBody().getMsg());
		assertEquals("Error Occured", entity.getBody().getStatus());
	}
	
	@Test
	public void testInsertStudentSuccess() {
		StudentData student = new StudentData();
		student.setName("ajsaaya");
		ResponseData<String> response = new ResponseData<String>("msg", "status");
		when(studentService.validate(student)).thenReturn(response);
		ResponseEntity<ResponseData<String>>  entity = studentController.save(student);
		
		assertEquals("msg", entity.getBody().getMsg());
		assertEquals("status", entity.getBody().getStatus());
	}
	
	@Test
	public void testInsertStudentThrowsDataAccessException() {
		StudentData student = new StudentData();
		student.setName("ajsaaya");
		when(studentService.validate(student)).thenThrow(new DataAccessException("Data Base Error") {

			private static final long serialVersionUID = 1L;
			
		});
		ResponseEntity<ResponseData<String>>  entity = studentController.save(student);
		
		assertEquals("Data Base Error", entity.getBody().getMsg());
		assertEquals("Error Occured", entity.getBody().getStatus());
	}
	
	@Test
	public void testInsertStudentThrowsException() {
		StudentData student = new StudentData();
		student.setName("ajsaaya");
		when(studentService.validate(student)).thenThrow(new RuntimeException("runtime error"));
		ResponseEntity<ResponseData<String>>  entity = studentController.save(student);
		
		assertEquals("Data Not Retreived", entity.getBody().getMsg());
		assertEquals("Error Occured", entity.getBody().getStatus());
	}
	
	@Test
	public void testUpdateSuccess() {
		StudentData student = new StudentData();
		student.setName("ajsaaya");
		ResponseData<String> response = new ResponseData<String>("msg", "status");
		when(studentService.update(any(), anyInt())).thenReturn(response);
		ResponseEntity<ResponseData<String>>  entity = studentController.update(any(), anyInt());
		
		assertEquals("msg", entity.getBody().getMsg());
		assertEquals("status", entity.getBody().getStatus());
	}
	
	@Test
	public void testUpdateThrowsDataAccessException() {
		when(studentService.update(any(), anyInt())).thenThrow(new DataAccessException("Data Base Error") {

			private static final long serialVersionUID = 1L;
			
		});
		ResponseEntity<ResponseData<String>>  entity = studentController.update(any(), anyInt());
		
		assertEquals("Data Base Error", entity.getBody().getMsg());
		assertEquals("Error Occured", entity.getBody().getStatus());
	}
	
	@Test
	public void testUpdateThrowsException() {
		when(studentService.update(any(), anyInt())).thenThrow(new RuntimeException("runtime error"));
		ResponseEntity<ResponseData<String>>  entity = studentController.update(any(), anyInt());
		
		assertEquals("Data Not Retreived", entity.getBody().getMsg());
		assertEquals("Error Occured", entity.getBody().getStatus());
	}
}
