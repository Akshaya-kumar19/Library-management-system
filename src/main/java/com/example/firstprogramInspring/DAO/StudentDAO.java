package com.example.firstprogramInspring.DAO;

import com.example.firstprogramInspring.Model.*;
import java.util.List;

public interface StudentDAO {
	
	
	public List<StudentData> findall();
	
	public StudentData findbyid(int id);
	public List<StudentData> findByCurrentStatus(String currentStatus);
	public int deleteByID(int id);
	
	public int save(StudentData sp);
	
	public int update(StudentData sp, int id);
	
	public boolean checkRegisterNo(int register_no);
	
	public int CurrentStatusCount(String CurrentStatus);

	int saveStudentCheckIn(String student_name, int register_no);
	
	public String getStudentNameByRegisterID(int register_no);

	public String getStudentNameByStudentID(int student_id);
	
}
