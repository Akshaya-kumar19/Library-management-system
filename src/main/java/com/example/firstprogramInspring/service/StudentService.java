package com.example.firstprogramInspring.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.firstprogramInspring.DAO.DataTableDAO;
import com.example.firstprogramInspring.DAO.StudentDAO;
import com.example.firstprogramInspring.Model.PageRequest;
import com.example.firstprogramInspring.Model.PageRequest2;
import com.example.firstprogramInspring.Model.ResponseData;
import com.example.firstprogramInspring.Model.StudentData;

@Service
public class StudentService 
{
	@Autowired
	StudentDAO sDAO;

	@Autowired
	DataTableDAO dataTableDAO;
	
	Logger logger = LoggerFactory.getLogger(StudentService.class);

	public List<StudentData> findall(){
		List<StudentData> result = sDAO.findall();
		logger.info("Success in Service");
		return result;
	}

	public List<StudentData> findByCurrentStatus(String currentStatus) {
		if(currentStatus.equalsIgnoreCase("active") || currentStatus.equalsIgnoreCase("inactive")){
			List<StudentData> result = sDAO.findByCurrentStatus(currentStatus);
			logger.info("Success in Service");
			return result;
		}

		return null;
	}

	public int CurrentStatusCount(String CurrentStatus) {
		if(CurrentStatus.equalsIgnoreCase("active") || CurrentStatus.equalsIgnoreCase("inactive")){
			Integer result =sDAO.CurrentStatusCount(CurrentStatus);
			logger.info("Success in Service");
			return (int)result;
		}
		return 0;
	}
	public ResponseData<String> deletebyid(int id) 
	{

		int result=sDAO.deleteByID(id);
		ResponseData<String> data;
		if(result==0)
		{
			data=new ResponseData<String>("ID Not Found", "Failure");
			logger.info("ID not  found");
			return data;
		}
		else
		{
			data =new ResponseData<String>("Deleted Successfully", "Success");
			logger.info("Success in Service");

			return data;
		}
	}

	public ResponseData<String> update(StudentData sp,int id)
	{
		ResponseData<String> response;
		if(sDAO.checkRegisterNo(sp.getRegister_no()))
		{

			JSONObject json = new JSONObject(sp);
			System.out.println("*******************  "+json.toString()+" *******************************");
			int result=sDAO.update(sp, id);

			if(result==0)
			{
				response = new ResponseData<String>("ID not Found", "Failure");
				logger.warn("student ID not found");
				return response;
			}else{
				response = new ResponseData<String>("Updated successfully", "Success");
				logger.info("Updating successfully");
				return response;
			}
		}else {

			response = new ResponseData<String>("Register Number already exists", "Failure");
			logger.warn("Register Number already exists");
			return response;
		}
	}



	public StudentData findbyid(int id) 
	{
		StudentData result = sDAO.findbyid(id);
		logger.info("Success in service");
		return result; 
	}


	/*
	 * Doing some validation process of data's before inserting value into DB 
	 */
	public ResponseData<String> validate(StudentData sp) 
	{

		ResponseData<String> response;
		if(sp.getGender().equalsIgnoreCase("female")||sp.getGender().equalsIgnoreCase("male"))
		{


			if(sp.getPhone_number().length()==10)
			{
				//				System.out.println("Checking register_no is unique \n User Input for Register_no"+sp.getRegister_no());
				logger.info("Checking register_no is unique \n User Input for Register_no"+sp.getRegister_no());
				if(sDAO.checkRegisterNo(sp.getRegister_no()))
				{
					JSONObject json = new JSONObject(sp);
					//					System.out.println("*******************  "+json.toString()+" *******************************");
					int result=sDAO.save(sp);


					if(result ==0) {
						response = new ResponseData<String>("Some of the value is missing please check", "Failure");
						logger.info("Some of the value is missing please check");
						return response;
					}


				}
				else
				{
					//					System.out.println("Duplicate of Register_no" );
					logger.info("Duplicate of Register_no");
					response = new ResponseData<String>("Register number already exist", "Failure");
					return response;
				}
			}
			else
			{
				//				System.out.println("Phone number cannot be less than 10");
				logger.info("Phone number cannot be less than 10");
				response = new ResponseData<String>("Phone no must be 10", "Failure");
				return response;
			}

		}
		else
		{
			//			System.out.println("Gender either male or female");
			logger.info("Gender either male or female");
			response = new ResponseData<String>("Gender must be male or female", "Failure");

			return response;
		}
		response = new ResponseData<String>("Value inserted Successfully", "Success");
		logger.info("Value inserted Successfully");
		return response;
	}


	public Map<String, Object> studentDataTable(PageRequest request){
		
		Map<String, Object> response = new HashMap<String, Object>();
		logger.info("checked the page no given is more than 0 or not /n ");
		if(request.getPageNo()>0) {
			logger.info("page no is more than Zero");
			List<StudentData> students=dataTableDAO.studentDataTable(request);
			int filteredrecord = dataTableDAO.getCountFilteredRecords(request);
			int totalrecord = dataTableDAO.getTotalStudentRecords();
			int totalPages = dataTableDAO.getTotalPages(request);
			int howmanyshown = students.size();
			logger.info("Checking total page number greater than the page no and page limit is greater than 0");
		if(request.getPageNo() <= totalPages && request.getPageLimit() > 0) {
			logger.info("Condition satisfied");
			response.put("TotalRecords", totalrecord);
			response.put("FilteredRecord", filteredrecord);
			response.put("Data", students);
			response.put("Records Shown", howmanyshown+ " out of "+ filteredrecord);
			response.put("Pages", request.getPageNo() + " out of "+ totalPages);
		}
		else {
			String message = request.getPageLimit() <= 0 ? "The page limit has to be more than 0":(totalPages > 0) ?"The Maximum page number is : "+ totalPages : "No record found";
			logger.warn("Condition not satisfied with a message : "+ message);
			response.put("Message", message);
		}
		
		}
		else {
			logger.warn("Condition not statisfied page number is less than 0");
			response.put("Message", "Page number has to be more than 0");
		}
		return response;
	}
	
	public Map<String, Object> dataTableNew(PageRequest2 request){
		List<StudentData> data;
		Map<String, Object> response = new HashMap<String, Object>();
		if(request.getSearch().get("value") != null) {
			data = dataTableDAO.studentDataTableWithSearchElement(request);
		}
		else {
			data = dataTableDAO.studentDataTableWithoutSearchElement(request);
		}
		
		int filteredRecordCount = dataTableDAO.getCountFilteredRecords(request);
		int totalRecordsCount = dataTableDAO.getTotalStudentRecords();
		
		int totalPages = (int) Math.ceil((double) filteredRecordCount / request.getLength());
		
		int currentPage = (request.getStart()/ request.getLength()) + 1;
		response.put("draw", request.getDraw());
		response.put("data", data);
		response.put("total_records", totalRecordsCount);
		response.put("filtered_records", filteredRecordCount);
		response.put("total_pages", totalPages);
		response.put("current_page", currentPage);
		
		return response;
		
		
	}
}