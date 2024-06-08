package com.example.firstprogramInspring.DAOimpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.stereotype.Repository;
import com.example.firstprogramInspring.DAO.LibraryDAO;
import com.example.firstprogramInspring.Model.BookData;
import com.example.firstprogramInspring.Model.DateRequestCheckin;
import com.example.firstprogramInspring.Model.StudentBookRegister;
import com.example.firstprogramInspring.Model.WaitingList;
import com.google.gson.Gson;

@Repository
public class LibraryDAOImplement implements LibraryDAO{
	@Autowired
	JdbcTemplate jdbc;
	@Autowired
	Gson gson;
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public int insertBook(BookData book) {
		
		int result = 0;
		if((book.getBook_name() != null || book.getBook_name().isEmpty()) && 
				(book.getQuantity() != 0) && (book.getAuthor_name() != null || 
				book.getAuthor_name().isEmpty())) {
			String sql = "INSERT INTO book_list(book_name, author_name, quantity, createdDate) VALUES (?,?,?,NOW())";
			result = jdbc.update(sql, book.getBook_name(), book.getAuthor_name(), book.getQuantity());
			if(result == 0) {
				logger.info("Book is not inserted for the data : "+  book.toString());
			}else {
				logger.info("Book inserted for the data : "+ book.toString());
			}
			
		}
		System.out.println(result);
		return result;
	}
	
	@Override
	public List<BookData> getBooksByAuthor(String author_name) {
		
		String sql = "select book_id, book_name, author_name, quantity from book_list where author_name = ?";
		RowMapper<BookData> rowmapper = new BeanPropertyRowMapper<BookData>(BookData.class);
		List<BookData> query = jdbc.query(sql, rowmapper, author_name);
		logger.info("The books of author : "+ author_name + " are :" + query.toString());
		return query;
	}

	@Override
	public List<BookData> getAllBooks() {
		String sql = "select book_id,book_name, author_name, quantity,read_time from book_list";
		RowMapper<BookData> rowmapper=new BeanPropertyRowMapper<BookData>(BookData.class);
		List<BookData> query = jdbc.query(sql, rowmapper);
		logger.info("Got the books from the book list: "+ query.toString());
		return query;
	}
	
	@Override
	public BookData getBookByName(String book_name) {
		String sql="select book_id,book_name, author_name, quantity from book_list WHERE book_name=? ";
		RowMapper<BookData> rowmapper=new BeanPropertyRowMapper<>(BookData.class);
		BookData query=jdbc.queryForObject(sql,rowmapper,book_name);
		logger.info("Finding Book with " + book_name + " is "+ query.toString());
		return query;
	}

	@Override
	public int upgradeQuantity(int quantity, int book_id) {
		int result = 0;
		
			String sql = "UPDATE book_list SET quantity=?, updatedDate = NOW() where book_id = ?";
			result = jdbc.update(sql, quantity, book_id);
			if(result == 0) {
				logger.info("Update query is not worked for the book id : "+ book_id);
			}else {
				logger.info("Update query is worked for the book id : "+ book_id);
			}
		return result;
	}
	
//	@Override
//	public int studentCheckingIn(int student_id, int book_id) {
//		
//		String sql = "insert into student_book_register (student_id,book_id,check_in_time)"
//				+ " values(?,?,NOW())";
//		int a = jdbc.update(sql, student_id, book_id);
//		return a;
//	}
	
	//checking for the book name whether it is present or not
	@Override
	public boolean checkBookName(String bookname) {
		String sql="select count(book_name) from book_list where book_name=?";
		int query_output=jdbc.queryForObject(sql,Integer.class,bookname);
		
		if(query_output==0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	//checking for the whether student is there or not 
	@Override
	public boolean checkStduentIDUsingNameRegisterNO(String student_name, int register_no) {
		String sql = "select count(student_id) from student where student_name = ? and register_no = ?";
		int c = jdbc.queryForObject(sql,Integer.class,student_name, register_no);
		if (c ==0) {
			return false;
		}else {
			return true;
		}
	}

	//checking whether the book is there or not
	@Override
	public boolean checkBookIDUsingBookName(String book_name) {
		String sql = "select count(book_id) from book_list where book_name=?";
		int query_output = jdbc.queryForObject(sql,Integer.class,book_name);
		if (query_output ==0) {
			return false;
		}else {
			return true;
		}
	}

	
	//getting student id to store in the student_book_register
	@Override
	public int getStudentIDUsingNameRegisterNO(String student_name, int register_no) {
		
		String sql = "select student_id from student where Name = ? and register_no = ?";
		int query_output = jdbc.queryForObject(sql, Integer.class, student_name, register_no);
		return query_output;
	}

	@Override
	public int getBookIDUsingBookName(String book_name) {
		
		String sql = "select book_id from book_list where book_name=?";
		int query_output = jdbc.queryForObject(sql, Integer.class, book_name);
		return query_output;
	}
	
	@Override
	public int insertStudentInWaitingList(WaitingList waitingStudent) {
		int result = 0;
		if(waitingStudent.getBook_id() !=0 && waitingStudent.getStudent_id() !=0 && (waitingStudent.getWaiting_flag() != null || waitingStudent.getWaiting_flag().isEmpty())) {
			String sql = "insert into waiting_list (book_id,student_id,waiting_flag, createdDate) values(?,?,?,NOW())";
			result = jdbc.update(sql, waitingStudent.getBook_id(), waitingStudent.getStudent_id(), waitingStudent.getWaiting_flag());
			if(result == 0) {
				logger.info("query not executed.");
			}else {
				logger.info("query executed");
			}
		}
		return result;
	}

	@Override
	public int getQuantity(int book_id) {
		
		String sql = "select quantity from book_list where book_id = ?";
		
		int query_output = jdbc.queryForObject(sql, Integer.class, book_id);
		if(query_output == 0) {
			logger.info("Getting quantity query is not worked");
		}else {
			logger.info("Getting quantity is worked and quantity is : "+ query_output);
		}
		
		return query_output;
	}

	@Override
	public int insertDataStudentBookRegister(StudentBookRegister registerStudent) {
		
		int query_output = 0;
		if(registerStudent.getBook_id() != 0 && registerStudent.getStudent_id() != 0) {
			
			String sql = "insert into student_book_register (student_id, book_id, check_in_time, time_left) values(?,?,NOW(),?)";
			KeyHolder keyHolder = new GeneratedKeyHolder();
			query_output = jdbc.update(new PreparedStatementCreator() {
	            @Override
	            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
	                PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
	                preparedStatement.setInt(1, registerStudent.getStudent_id());
	                preparedStatement.setInt(2, registerStudent.getBook_id());
	                preparedStatement.setInt(3, registerStudent.getTime_left());
	                return preparedStatement;
	            }
	        }, keyHolder);
			Number autoGeneratedKey = keyHolder.getKey();

	        
	        if (autoGeneratedKey != null) {
	            return autoGeneratedKey.intValue();
	        } else {
	            
	            return 0;
	        }
		}
		if(query_output == 0) {
			logger.error("insert data student book register query is not excecuted");
		}else {
			logger.info("insert data student book register query is excecuted");
		}
    // Return 0 if the book_id or student_id is 0
    return 0;
	}

	@Override
	public boolean checkWaitingStudentEntry(int student_id, int book_id) {
		
		String sql = "select count(student_id) from waiting_list where book_id =? and student_id = ?";
		int c = jdbc.queryForObject(sql,Integer.class, book_id, student_id);
		
		if(c==0) {
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public boolean checkForStudentCheckInRegister(int student_id, int book_id) {
		
		String sql = "select count(student_id) from student_book_register where book_id =? and student_id = ?";
		int query_output = jdbc.queryForObject(sql, Integer.class, book_id, student_id);
		if(query_output==0) {
			return true;
		}
		else {
			return false;
		}
	}

	
	@Override
	public String getBookNameUsingBookID(int book_id) {
		
		String sql = "select book_name from book_list where book_id = ?";
		String query_output = jdbc.queryForObject(sql, String.class, book_id);
		
		return query_output;
	}

	@Override
	public List<StudentBookRegister> getStudentBookRegister() {
		
		String sql = "select * from student_book_register;";
		RowMapper<StudentBookRegister> rowmapper = new BeanPropertyRowMapper<StudentBookRegister>(StudentBookRegister.class);
		
		List<StudentBookRegister> query_output = jdbc.query(sql, rowmapper);
		logger.info("Query is excuted for getting student_book_register :"+ query_output.toString());
		return query_output;
	}

	@Override
	public boolean checkForEntryInStudentBookRegister(int student_id, int book_id) {
		
		String sql = "select count(book_id) from student_book_register where student_id = ? and book_id = ?";
		int query_output = jdbc.queryForObject(sql, Integer.class, student_id, book_id);
		if(query_output==0) {
			return false;
		}
		else {
			return true;
		}
	}

	@Override
	public int setCheckoutTime(int student_id, int book_id) {
		
		String sql = "UPDATE student_book_register SET check_out_time = NOW() WHERE student_id=? and book_id = ?";
		int query_output = jdbc.update(sql, student_id, book_id);
		return query_output;
	}

	@Override
	public boolean checkForBookReturn(int student_id, int book_id) {
		
		String sql = "select count(student_id) from student_book_register where student_id = ? and book_id = ? and check_out_time IS NULL";
		int query_output = jdbc.queryForObject(sql,Integer.class, student_id, book_id);
		if(query_output == 0) {
			return false;
		}
		else {
			return true;
		}
	}

	@Override
	public List<WaitingList> getListOfWaitingForBook(int book_id) {
		
		String sql = "select * from waiting_list where book_id = ? and waiting_flag = 'WAITING'";
		RowMapper<WaitingList> rowmapper = new BeanPropertyRowMapper<WaitingList>(WaitingList.class);
		List<WaitingList> list = jdbc.query(sql, rowmapper, book_id);
		logger.info("the query finished in getting list of the waiting for book : "+ list.toString());
		return list;
	}

	@Override
	public int changeWaitingFlag(int student_id, int book_id){
		
		String sql = "update waiting_list set waiting_flag = 'GIVEN', updatedDate=NOW() where book_id = ? and student_id = ? and waiting_flag = 'WAITING' order by createdDate limit 1";
		
		int result = jdbc.update(sql, book_id,student_id);
		if(result == 0) {
			logger.error("The query to change the waiting flag is not excecuted");
		}
		else {
			logger.info("The query to change the waiting flag is excecuted");
		}
		return result;
	}

	@Override
	public List<Map<String, Integer>> getBookCountByBookID(DateRequestCheckin request) {
		// TODO Auto-generated method stub
		 String sql = "SELECT book_id, COUNT(book_id) AS book_count "
	               + "FROM student_book_register "
	               + "WHERE date(check_in_time) >= ? AND date(check_in_time) <= ? "
	               + "GROUP BY book_id "
	               + "ORDER BY book_count DESC "
	               + "LIMIT 1";		  
		 List<Map<String, Integer>> result = jdbc.query(sql, (rs,rowNum) -> {
		        Map<String, Integer> row = new HashMap<>();
		        row.put("book_id", rs.getInt("book_id"));
		        row.put("book_count", rs.getInt("book_count"));
		        return row;
		    }, request.getMin_date(), request.getMax_date());
		    
//		System.out.println(request.toString());
		 
		return result;
	}

	@Override
	public List<BookData> getMaxAvailableBook(){
		System.out.println("DaO");
		String sql = "SELECT * FROM book_list where quantity = (SELECT max(quantity) FROM book_list);";
		RowMapper<BookData> rowMapper = new BeanPropertyRowMapper<BookData>(BookData.class);
		System.out.println("dao test");
		List<BookData> result = jdbc.query(sql,rowMapper);
		System.out.println("reslut :   "+ result);
		return result;
	}

	@Override
	public List<BookData> getMinimumAvailableBook(){
		String sql =  "SELECT * FROM book_list where quantity = (SELECT min(quantity) FROM book_list WHERE quantity > 0);";
		RowMapper<BookData> rowMapper = new BeanPropertyRowMapper<BookData>(BookData.class);
		List<BookData> list = jdbc.query(sql, rowMapper);
		return list;
	}
	
	@Override
	public int getBookIDOfMaxWaiting() {
		String sql = "SELECT book_id FROM waiting_list GROUP BY book_id ORDER BY COUNT(book_id) DESC LIMIT 1;";
		int book_id = jdbc.queryForObject(sql, Integer.class);
		return book_id;
	}
	
	@Override
	public int getBookIdOfMinimumWaiting() {
		String sql = "SELECT book_id FROM waiting_list GROUP BY book_id ORDER BY COUNT(book_id) ASC LIMIT 1;";
		int book_id = jdbc.queryForObject(sql, Integer.class);
		return book_id;
	}
	
	@Override
	public BookData getBookDataByBookID(int book_id) {
		String sql = "SELECT * FROM book_list where book_id = ?";
		RowMapper<BookData> rowMapper = new BeanPropertyRowMapper<BookData>(BookData.class);
		BookData book = jdbc.queryForObject(sql, rowMapper, book_id);
		return book;
	}
	
	@Override
	public List<BookData> getAvailableBooks(){
		String sql = "SELECT * FROM book_list WHERE quantity > 0;";
		RowMapper<BookData> rowMapper = new BeanPropertyRowMapper<BookData>(BookData.class);
		List<BookData> list = jdbc.query(sql, rowMapper);
		return list;
	}
	
	//queries for scheduling 
	@Override
	public Integer getReadTime(int book_id) {
		String sql = "SELECT read_time from book_list where book_id = ?";
		Integer query_output = jdbc.queryForObject(sql, Integer.class, book_id);
		return query_output==null ? -1:query_output;
	}
	
	@Override
	public int setCheckoutTime(int book_issue_no) {
		String sql = "UPDATE student_book_register SET check_out_time = NOW() WHERE book_issue_id = ? and check_out_time is null order by check_in_time limit 1";
		int query_output = jdbc.update(sql, book_issue_no);
		if(query_output == 0) {
			logger.info("query not executed.");
		}else {
			logger.info("query executed");
		}
		return query_output;
		
	}
	@Override
	public int updateTimeLeft(int timeleft, int book_issue_id) {
		String sql = "UPDATE student_book_register SET time_left = ? where book_issue_id = ?";
		int query_output = jdbc.update(sql, timeleft, book_issue_id);
		return query_output;
	}
	@Override
	public List<StudentBookRegister> getListOfOngoingReading(){
		String sql = "select * from student_book_register where check_out_time is null and time_left > 0";
		RowMapper<StudentBookRegister> rowMapper = new BeanPropertyRowMapper<StudentBookRegister>(StudentBookRegister.class);
		List<StudentBookRegister> list = jdbc.query(sql, rowMapper);
		return list;
	}

	@Override
	public StudentBookRegister findCheckoutById(int bookIssueId) {
		// TODO Auto-generated method stub
		String sql = "select * from student_book_register where book_issue_id = ? ";
		RowMapper<StudentBookRegister> rowMapper = new BeanPropertyRowMapper<StudentBookRegister>(StudentBookRegister.class);
		StudentBookRegister result = jdbc.queryForObject(sql, rowMapper, bookIssueId);
		return result;
	}
	
	
	
}
