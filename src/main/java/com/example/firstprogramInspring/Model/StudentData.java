package com.example.firstprogramInspring.Model;


public class StudentData {
	private int student_id;
	private String name;
	private Integer register_no;
	private String gender;
	private Integer age;
	private String phone_number;
	private String current_status;
	private String email_address;
	private String course;
	private Integer batch;
	private Integer fees;
	private String createdDate;
	private String updatedDate;
	private String msg;
	private String status;
	
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public StudentData() {
		super();
		// TODO Auto-generated constructor stub
	}
	public int getStudent_id() {
		return student_id;
	}
	public void setStudent_id(int student_id) {
		this.student_id = student_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getRegister_no() {
		return register_no != null ? register_no : 0;
	}
	public void setRegister_no(int register_no) {
		this.register_no = register_no;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public int getAge() {
		return age != null ? age : 0;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public String getPhone_number() {
		return phone_number;
	}
	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}
	public String getCurrent_status() {
		return current_status;
	}
	public void setCurrent_status(String current_status) {
		this.current_status = current_status;
	}
	public StudentData(int student_id, String name, int register_no, String gender, int age, String phone_number,
			String current_status) {
		super();
		this.student_id = student_id;
		this.name = name;
		this.register_no = register_no;
		this.gender = gender;
		this.age = age;
		this.phone_number = phone_number;
		this.current_status = current_status;
		
	}
	public String getEmail_address() {
		return email_address;
	}
	public void setEmail_address(String email_address) {
		this.email_address = email_address;
	}
	public String getCourse() {
		return course;
	}
	public void setCourse(String course) {
		this.course = course;
	}
	public int getBatch() {
		return batch != null ? batch : 0;
	}
	public void setBatch(Integer batch) {
		this.batch = batch;
	}
	public int getFees() {
		return fees != null ? fees : 0;
	}
	public void setFees(Integer fees) {
		this.fees = fees;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public String getUpdatedDate() {
		return updatedDate;
	}
	@Override
	public String toString() {
		return "StudentData [student_id=" + student_id + ", name=" + name + ", register_no=" + register_no + ", gender="
				+ gender + ", age=" + age + ", phone_number=" + phone_number + ", current_status=" + current_status
				+ ", email_address=" + email_address + ", course=" + course + ", batch=" + batch + ", fees=" + fees
				+ ", createdDate=" + createdDate + ", updatedDate=" + updatedDate + ", msg=" + msg + ", status="
				+ status + "]";
	}
	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

}
