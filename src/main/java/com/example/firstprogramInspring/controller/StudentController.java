package com.example.firstprogramInspring.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.google.gson.Gson;
import com.itextpdf.text.DocumentException;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.firstprogramInspring.Model.PageRequest2;
import com.example.firstprogramInspring.Model.ResponseData;
import com.example.firstprogramInspring.Model.StudentData;
import com.example.firstprogramInspring.service.DownloadService;
import com.example.firstprogramInspring.service.ImportService;
import com.example.firstprogramInspring.service.StudentService;

@RestController
@Tag(description = "Controller for student management", name = "Student")
public class StudentController {
	@Autowired
	private StudentService student_service;

	@Autowired
	Gson gson;

	@Autowired
	private DownloadService downloadService;
	
	@Autowired
	private ImportService importService;
	
	Logger logger = LoggerFactory.getLogger(StudentController.class);

	@GetMapping("/get-all-student-details")
	public ResponseData<List<StudentData>> findAll() {
		try {
			List<StudentData> pojo = student_service.findall();
			ResponseData<List<StudentData>> response = new ResponseData<List<StudentData>>("Data Retrieved", "Success", pojo, pojo.size());
			logger.info("Data retrived from database");
			return response;
		}catch (DataAccessException e) {
			logger.error("Error Occured --> " + e.getCause() + "\n Error Message: " + e.getMessage());
			return new ResponseData<List<StudentData>>(e.getMessage(), "Error Occured");
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("Error Occured --> " + e.getCause() + "\n Error Message: " + e.getMessage());
			return new ResponseData<List<StudentData>>("Data not retrieved", "Error Occured");
		}
	}

	@PostMapping("/get-student-by-current-status")
	public ResponseEntity<ResponseData<List<StudentData>>>findByCurrentStatus(@RequestBody Map<String, String> requestBody){
		String current_status = requestBody.get("current_status");
		ResponseData<List<StudentData>> response;
		if(current_status != null && !current_status.isEmpty() && (current_status.equalsIgnoreCase("active") || current_status.equalsIgnoreCase("inactive"))) {
			try {
				List<StudentData> pojo = student_service.findByCurrentStatus(current_status);
				//				int count = student_service.CurrentStatusCount(current_status);
				//				String data = "Count :"+ count + ", " + gson.toJson(pojo);
				response = new ResponseData<List<StudentData>>("Data Retrieved", "Success", pojo,pojo.size());
				//				System.out.println(gson.toJson(pojo));
				return new ResponseEntity<ResponseData<List<StudentData>>>(response, HttpStatus.OK);
			}catch (DataAccessException e) {
				return new ResponseEntity<ResponseData<List<StudentData>>>(new ResponseData<List<StudentData>>(e.getMessage(), "Error Occured"), HttpStatus.OK);
			} 
			catch (Exception e) {
				return new ResponseEntity<ResponseData<List<StudentData>>>(new ResponseData<List<StudentData>>(e.getMessage(), "Error Occured"), HttpStatus.OK);
			}

		}
		response = new ResponseData<List<StudentData>>( "Provide the correct current status","Error");
		return new ResponseEntity<ResponseData<List<StudentData>>>(response, HttpStatus.BAD_REQUEST);
	}

	@GetMapping("/get-student/{id}")
	public ResponseEntity<ResponseData<StudentData>>findById(@PathVariable int id) 
	{
		StudentData pojo = null;
		ResponseData<StudentData> response;
		try
		{
			pojo=student_service.findbyid(id);
			logger.info("Data retrived from database");
			pojo.setMsg("valid user");
			pojo.setStatus("success");
			response = new ResponseData<StudentData>("Data Retreived", pojo.getStatus(),pojo,1);// need to add the data
		}
		catch (EmptyResultDataAccessException e) 
		{
			StudentData p=new StudentData();
			logger.error("Error Occured --> " + e.getCause() + "\n Error Message: " + e.getMessage());
			p.setMsg("invalid user");
			p.setStatus("failure");
			response = new ResponseData<StudentData>("Data not retrieved", p.getStatus(),p,0);// need to add the data
			return new ResponseEntity<ResponseData<StudentData>>(response,HttpStatus.BAD_REQUEST);
		}
		catch (DataAccessException e) {
			logger.error("Error Occured --> " + e.getCause() + "\n Error Message: " + e.getMessage());
			return new ResponseEntity<ResponseData<StudentData>>(new ResponseData<StudentData>(e.getMessage(), "Error Occured"),HttpStatus.BAD_REQUEST);
		}
		catch (Exception e) {
			logger.error("Error Occured --> " + e.getCause() + "\n Error Message: " + e.getMessage());
			return new ResponseEntity<ResponseData<StudentData>>(new ResponseData<StudentData>(e.getMessage(), "Error Occured"),HttpStatus.BAD_REQUEST);

		}
		return new ResponseEntity<ResponseData<StudentData>>(response,HttpStatus.OK);
	}

	@DeleteMapping("/delete-student/{id}")
	public ResponseEntity<ResponseData<String>> deleteById(@PathVariable int id) 
	{
		try {
			ResponseData<String> msg = student_service.deletebyid(id);
			logger.info("Data retrived from database");
			//			Gson gson = new Gson();
			//			String value = gson.toJson(msg);
			//			System.out.println(value);
			return new ResponseEntity<ResponseData<String>>(msg,HttpStatus.OK);
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			logger.error("Error Occured --> " + e.getCause() + "\n Error Message: " + e.getMessage());
			return new ResponseEntity<ResponseData<String>>(new ResponseData<String>(e.getMessage(),"Error Occured"),HttpStatus.OK);
		}
		catch (Exception e) {
			logger.error("Error Occured --> " + e.getCause() + "\n Error Message: " + e.getMessage());
			return new ResponseEntity<ResponseData<String>>(new ResponseData<String>("Data Not Retreived","Error Occured"),HttpStatus.OK);
		}
	}

	@PostMapping("/insert-student")
	public ResponseEntity<ResponseData<String>> save(@RequestBody StudentData sp)
	{
		try {
			ResponseData<String> msg;
			//			System.out.println(sp.getCreatedDate());

			msg=student_service.validate(sp);
			logger.info("Data retrived from database");
			System.out.println(msg);
			System.out.println(sp.getCreatedDate());
			return new ResponseEntity<ResponseData<String>>(msg,HttpStatus.OK);
		} catch (DataAccessException e) {
			logger.error("Error Occured --> " + e.getCause() + "\n Error Message: " + e.getMessage());
			return new ResponseEntity<ResponseData<String>>(new ResponseData<String>(e.getMessage(),"Error Occured"),HttpStatus.OK);

		}catch (Exception e) {
			logger.error("Error Occured --> " + e.getCause() + "\n Error Message: " + e.getMessage());
			return new ResponseEntity<ResponseData<String>>(new ResponseData<String>("Data Not Retreived","Error Occured"),HttpStatus.OK);

		}
	}

	@PutMapping("/update-student/{id}")
	public ResponseEntity<ResponseData<String>> update(@RequestBody StudentData sp, @PathVariable int id) {


		try {
			ResponseData<String> msg = student_service.update(sp,id);
			logger.info("Data retrived from database");
			return new ResponseEntity<ResponseData<String>>(msg,HttpStatus.OK);
		} catch (DataAccessException e) {
			logger.error("Error Occured --> " + e.getCause() + "\n Error Message: " + e.getMessage());
			return new ResponseEntity<ResponseData<String>>(new ResponseData<String>(e.getMessage(),"Error Occured"),HttpStatus.OK);

		}catch (Exception e) {
			logger.error("Error Occured --> " + e.getCause() + "\n Error Message: " + e.getMessage());
			return new ResponseEntity<ResponseData<String>>(new ResponseData<String>("Data Not Retreived","Error Occured"),HttpStatus.OK);

		}
	}


	@PostMapping("/student-data-table")
	public ResponseEntity<Map<String, Object>> studentDataTable(@RequestBody PageRequest2 request){
//		logger.info("The API student-data-table is started");
//		Map<String, Object> response = student_service.studentDataTable(request);
//
//		logger.info("The response from teh studentDataTable : "+response.toString());
//		return new ResponseEntity<Map<String,Object>>(response, HttpStatus.OK);
		logger.info("The API student-data-table is started");
		Map<String, Object> response = student_service.dataTableNew(request);
		logger.info("The response from teh studentDataTable : "+response.toString());
		return new ResponseEntity<Map<String,Object>>(response, HttpStatus.OK);
	}


	@GetMapping("/excel-download")
	public ResponseEntity<?> downloadExcel(@RequestParam(required = false) Integer studentID){
		return downloadService.downloadExcelFile(studentID);
	}
	
	@GetMapping("/csv-download")
	public ResponseEntity<?> downloadCsvFile(@RequestParam(required = false) Integer studentId){
		return downloadService.csvFileDowload(studentId);
	}

	@GetMapping("/pdf-download")
	public void downloadPDFFile(@RequestParam(required = false) Integer studentId, HttpServletResponse response) throws DocumentException, IOException{
		downloadService.pdfFileDownload(studentId, response);
	}
	
	@PostMapping("/save-student-from-excel")
	public String saveStudentFromExcel(@RequestPart("file") MultipartFile file, HttpServletResponse response) {
		try {
		return importService.saveStudentFromExcelError(file,response);
		}
		catch (Exception e) {
			return e.getMessage();
		}
	}
}