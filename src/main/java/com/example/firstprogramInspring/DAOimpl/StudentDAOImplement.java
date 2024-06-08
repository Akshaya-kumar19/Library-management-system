package com.example.firstprogramInspring.DAOimpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.example.firstprogramInspring.DAO.StudentDAO;
import com.example.firstprogramInspring.Model.StudentData;


@Repository
public class StudentDAOImplement implements StudentDAO 
{
	@Autowired
	JdbcTemplate jdbc;

	@Override
	public List<StudentData> findall() 
	{
		System.out.println("DAO");
		String sql="SELECT * FROM student WHERE deleted_flag = 'N'";
		RowMapper<StudentData> rowmapper=new BeanPropertyRowMapper<StudentData>(StudentData.class);
		List<StudentData> query_output=jdbc.query(sql,rowmapper);
		return query_output;
	}

	@Override
	public StudentData findbyid(int id) 
	{
		StudentData query_output = new StudentData();
		
			String sql="select * from student WHERE student_id=? and deleted_flag = 'N'";
			RowMapper<StudentData> rowmapper=new BeanPropertyRowMapper<>(StudentData.class);
			query_output=jdbc.queryForObject(sql,rowmapper,id);
		
		return query_output;
	}

	@Override
	public List<StudentData> findByCurrentStatus(String currentStatus) {
		// TODO Auto-generated method stub
		String sql = "SELECT *  FROM student where current_status = ? and deleted_flag = 'N'";
		RowMapper<StudentData> rowmapper=new BeanPropertyRowMapper<StudentData>(StudentData.class);
		List<StudentData> query_output=jdbc.query(sql,rowmapper, new Object[]{currentStatus});
		return query_output;
	}
	@Override
	public int deleteByID(int id) 
	{
		int count=0;
		String sql="UPDATE student SET deleted_flag = 'Y'  WHERE student_id=? and deleted_flag = 'N'";
		count=jdbc.update(sql,id);
		return count;
	}

	@Override
	public int save(StudentData sp) 
	{
		int query_output = 0;
		//		sp.setCreatedDate(LocalDateTime.now());
		if((sp.getName() != null &&  !sp.getName().isEmpty())  && (sp.getRegister_no() != 0) && (sp.getGender() != null &&  !sp.getGender().isEmpty()) && (sp.getAge() != 0) && (sp.getPhone_number() != null &&  !sp.getPhone_number().isEmpty()) && (sp.getCurrent_status() != null &&  !sp.getCurrent_status().isEmpty()) && (sp.getEmail_address() != null &&  !sp.getEmail_address().isEmpty()) && (sp.getCourse() != null &&  !sp.getCourse().isEmpty()) && (sp.getBatch() != 0 ) && (sp.getFees() != 0 )) {
			String sql="INSERT INTO student (Name, register_no, gender,age,phone_number,current_status,email_address,course,batch,fees,createdDate,deleted_flag"
					+ ") VALUES (?,?,?,?,?,?,?,?,?,?,NOW(),'N')";
			query_output=jdbc.update(sql,sp.getName(),sp.getRegister_no(),sp.getGender(),sp.getAge(),sp.getPhone_number(),sp.getCurrent_status().toLowerCase(), sp.getEmail_address(), sp.getCourse(), sp.getBatch(), sp.getFees());
			System.out.println("Value stored successfully by ------------>>" + sp.getCreatedDate());

		}


		return query_output;
	}

	@Override
	public int saveStudentCheckIn(String student_name, int register_no) {
		int query_output=0;


		String sql = "insert into student (Name, register_no, createdDate, deleted_flag) values(?,?,NOW(),'N')";
		query_output = jdbc.update(sql, student_name, register_no);

		return query_output;
	}
	@Override
	public int update(StudentData sp, int id) 
	{
		int query_output=0;
		StringBuilder sqlBuilder = new StringBuilder("UPDATE student SET ");
		List<Object> params = new ArrayList<>();
		if (sp.getName() != null) {
			sqlBuilder.append("Name=?, ");
			params.add(sp.getName());
		}

		if (sp.getRegister_no() != 0) {
			sqlBuilder.append("register_no=?, ");
			params.add(sp.getRegister_no());
		}

		if(sp.getGender() != null) {
			sqlBuilder.append("gender = ?, ");
			params.add(sp.getGender());
		}

		if(sp.getAge() !=0) {
			sqlBuilder.append("age = ?, ");
			params.add(sp.getAge());
		}
		if(sp.getPhone_number() != null) {
			sqlBuilder.append("phone_number = ?, ");
			params.add(sp.getPhone_number());
		}
		if(sp.getCurrent_status() != null) {
			sqlBuilder.append("current_status = ?, ");
			params.add(sp.getCurrent_status());
		}

		if(sp.getEmail_address() != null) {
			sqlBuilder.append("email_address = ?, ");
			params.add(sp.getEmail_address());
		}

		if(sp.getCourse() != null) {
			sqlBuilder.append("course = ?, ");
			params.add(sp.getCourse());
		}
		if(sp.getBatch() != 0) {
			sqlBuilder.append("batch = ?, ");
			params.add(sp.getBatch());
		}
		if(sp.getFees() != 0) {
			sqlBuilder.append("fees = ?, ");
			params.add(sp.getFees());
		}
		sqlBuilder.append("updatedDate = NOW(), ");


		sqlBuilder.delete(sqlBuilder.length() - 2, sqlBuilder.length());
		sqlBuilder.append(" WHERE student_id=? and deleted_flag = 'N'");
		params.add(id);

		String sql = sqlBuilder.toString();
		query_output=jdbc.update(sql, params.toArray());
		return query_output;
	}

	@Override
	public String getStudentNameByRegisterID(int register_no) {
		String sql = "select Name from student where register_no = ?";
		String student_name = jdbc.queryForObject(sql, String.class, register_no);
		return student_name;

	}

	@Override
	public boolean checkRegisterNo(int register_no) 
	{
		String sql="select count(register_no) from student where register_no=?";
		int query_output=jdbc.queryForObject(sql,Integer.class,register_no);

		if(query_output==0)
		{
			return true;
		}
		else
		{
			return false;
		}

	}

	@Override
	public int CurrentStatusCount(String currentStatus) {

		String sql = "SELECT COUNT(*) FROM student WHERE current_status = ?";


		Integer query_output = jdbc.queryForObject(sql, Integer.class, currentStatus);


		return query_output != null ? query_output : 0; 
	}

	@Override
	public String getStudentNameByStudentID(int student_id) {
		String sql = "select Name from student where student_id =?";
		String query_output = jdbc.queryForObject(sql, String.class, student_id);
		return query_output;
	}

}