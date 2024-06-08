package com.example.firstprogramInspring.DAOimpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.example.firstprogramInspring.DAO.DataTableDAO;
import com.example.firstprogramInspring.Model.BookData;
import com.example.firstprogramInspring.Model.PageRequest;
import com.example.firstprogramInspring.Model.PageRequest2;
import com.example.firstprogramInspring.Model.StudentData;

@Repository
public class DataTableDAOImplement implements DataTableDAO{

	@Autowired
	private JdbcTemplate jdbcTemplate;

	Logger logger = LoggerFactory.getLogger(DataTableDAOImplement.class);
	@Override
	public List<BookData> BookDataTable(PageRequest request) {
		// TODO Auto-generated method stub
		int offset = (request.getPageLimit() * request.getPageNo()) - request.getPageLimit();


		StringBuilder sql = new StringBuilder("SELECT * FROM book_list");


		List<Object> queryParams = new ArrayList<>();


		if (request.getSearchelement() != null && !request.getSearchelement().isEmpty()) {
			System.out.println(request.getSearchUsingColumn());
			if((request.getSearchUsingColumn() == null) || (request.getSearchUsingColumn().isEmpty()) || (request.getSearchUsingColumn().equalsIgnoreCase("book_name"))) {

				sql.append(" WHERE book_name LIKE ?");
			}

			else if(request.getSearchUsingColumn().equalsIgnoreCase("author_name")) {

				sql.append(" WHERE author_name LIKE ?");
			}
			else if(request.getSearchUsingColumn().equalsIgnoreCase("book_id")) {

				sql.append(" WHERE book_id LIKE ?");
			}

			queryParams.add("%" + request.getSearchelement() + "%");
		}

		sql.append(" ORDER BY book_id");
		if ("DESC".equalsIgnoreCase(request.getSortingOrder())) {
			sql.append(" DESC");
		} else {
			sql.append(" ASC");
		}


		sql.append(" LIMIT ? OFFSET ?");
		queryParams.add(request.getPageLimit());
		queryParams.add(offset);


		Object[] paramsArray = queryParams.toArray();


		RowMapper<BookData> rowMapper = new BeanPropertyRowMapper<>(BookData.class);


		List<BookData> list = jdbcTemplate.query(sql.toString(), rowMapper, paramsArray);
		System.out.println(sql.toString());
		return list;
	}

	public List<String> getColumnNames(String tableName){
		String sql = "SELECT COLUMN_NAME\n"
				+ "FROM INFORMATION_SCHEMA.COLUMNS\n"
				+ "WHERE TABLE_SCHEMA = 'LibraryManagementSystem' AND TABLE_NAME = ?;";
		List<String> list = jdbcTemplate.queryForList(sql, String.class, tableName);
		return list;
	}  

	//methods with the page request are the methods for the data table that was first created using me.
	@Override
	public List<StudentData> studentDataTable(PageRequest request) {
		int offset = (request.getPageLimit() * request.getPageNo()) - request.getPageLimit();

		StringBuilder sql = new StringBuilder("SELECT * FROM student");
		List<Object> queryParams = new ArrayList<>();

		if (request.getSearchelement() != null && !request.getSearchelement().isEmpty()) {

			sql.append(" WHERE ");
			if(request.getSearchUsingColumn() == null || request.getSearchUsingColumn().isEmpty()) {
				sql.append(" name ");

			}
			else {
				sql.append(request.getSearchUsingColumn());
			}

			sql.append(" LIKE ? ");
			queryParams.add("%" + request.getSearchelement() + "%");
		}


		sql.append(" ORDER BY student_id");
		if ("DESC".equalsIgnoreCase(request.getSortingOrder())) {
			sql.append(" DESC");
		} else {
			sql.append(" ASC");
		}


		sql.append(" LIMIT ? OFFSET ?");
		queryParams.add(request.getPageLimit());
		queryParams.add(offset);


		Object[] paramsArray = queryParams.toArray();


		RowMapper<StudentData> rowMapper = new BeanPropertyRowMapper<>(StudentData.class);


		List<StudentData> list = jdbcTemplate.query(sql.toString(), rowMapper, paramsArray);
		logger.info("sql query from studentDataTable  "+sql.toString());
		return list;
	}

	@Override
	public int getTotalStudentRecords() {
		String sql = "SELECT count(*) FROM student where deleted_flag = 'N'";
		int TotalEntry = jdbcTemplate.queryForObject(sql, Integer.class);
		return TotalEntry;
	}

	@Override
	public int getCountFilteredRecords(PageRequest request) {
		StringBuilder sql = new StringBuilder("SELECT count(*) FROM student  WHERE  deleted_flag = 'N'");
		List<Object> queryParams = new ArrayList<>();

		if (request.getSearchelement() != null && !request.getSearchelement().isEmpty()) {

			sql.append("and");
			if(request.getSearchUsingColumn() == null || request.getSearchUsingColumn().isEmpty()) {
				sql.append(" name ");

			}
			else {
				sql.append(request.getSearchUsingColumn());
			}

			sql.append(" LIKE ? ");
			queryParams.add("%" + request.getSearchelement() + "%");
		}

		sql.append(" ORDER BY student_id");
		if ("DESC".equalsIgnoreCase(request.getSortingOrder())) {
			sql.append(" DESC");
		} else {
			sql.append(" ASC");
		}


		Object[] paramsArray = queryParams.toArray();


		int list = jdbcTemplate.queryForObject(sql.toString(), Integer.class, paramsArray);
		System.out.println(sql.toString());
		logger.info("query : "+sql.toString()+" is done");
		return list;
	}

	@Override
	public int getTotalPages(PageRequest request) {
		int totalFiltered = getCountFilteredRecords(request);

		return (int) Math.ceil((double) totalFiltered / request.getPageLimit());
	}


	//these are the methods that are having the request with the specified by mentor


	@Override
	public List<StudentData> studentDataTableWithSearchElement(PageRequest2 request) {
		Integer ordercolumn = (Integer) request.getOrder().get(0).get("column");
		String columnName = (String) request.getColumns().get(ordercolumn).get("data");
		String searchElement = "%" + request.getSearch().get("value") + "%";
		List<HashMap<String, Object>> listOfColumns = request.getColumns();
		List<String> individualSearchElements = new ArrayList<String>();

		StringBuilder sql = new StringBuilder();
		sql.append("select * from student where deleted_flag = 'N' and ( "); // Use single quotes for specific value

		List<String> columnNames = getColumnNames("student");
		List<String> searchParameters = new ArrayList<>();
		columnNames.remove("deleted_flag");
		columnNames.remove("createdDate");
		columnNames.remove("updatedDate");
		for (String string : columnNames) {
			sql.append(string + " like ? or ");

			searchParameters.add(searchElement);
		}

		for (HashMap<String, Object> string : listOfColumns) {
			String individualSearchElement = (String) ((Map<String, Object>) string.get("search")).get("value");

			String column_name = (String) (string.get("data"));
			System.out.println("individual search element : "+ individualSearchElement + "----------"+ column_name);
			if (columnNames.contains(column_name)) {
				if (individualSearchElement != null && !individualSearchElement.isEmpty()) {
					sql.append(column_name + " like ? or ");
					searchParameters.add("%" + individualSearchElement + "%"); // Add individual search term (if not empty)
				} 
			}
		}

		// Remove the last "or "
		int lastOrIndex = sql.lastIndexOf("or ");
		if (lastOrIndex != -1) {
			sql.delete(lastOrIndex, lastOrIndex + 3);
		}

		sql.append(") ");


		if (request.getOrder() != null && !request.getOrder().isEmpty()) {
			sql.append("ORDER BY ");
			for (int i = 0; i < request.getOrder().size(); i++) {
				int sortOrderColumn;
				if((int)request.getOrder().get(i).get("column") > request.getColumns().size()-1) {
					sortOrderColumn = (int) request.getOrder().get(i).get("column");
				}
				else {
					sortOrderColumn = 0;
				}
				String sortOrderColumnName = (String) request.getColumns().get(sortOrderColumn).get("data");
				String sortOrderDirection = (String) request.getOrder().get(i).get("dir");
				if (columnNames.contains(sortOrderColumnName)) {
					sql.append(sortOrderColumnName).append(" ").append(sortOrderDirection.isEmpty() || sortOrderDirection == null ? "ASC ": sortOrderDirection.equalsIgnoreCase("DESC ") ? "DESC " : "ASC ");
					if (i < request.getOrder().size() - 1) {
						sql.append(", ");
					}
				}
			}
		} else {
			sql.append("ORDER BY student_id asc ");
		}

		sql.append("limit ? offset ? ");
		logger.info(sql.toString());
		RowMapper<StudentData> rowMapper = new BeanPropertyRowMapper<StudentData>(StudentData.class);
		//	    System.out.println("stream ----"+ Stream.concat(searchParameters.stream(), Stream.of(request.getLength(), request.getStart())).toArray().toString());
		List<StudentData> result = jdbcTemplate.query(sql.toString(), rowMapper, 
				Stream.concat(searchParameters.stream(), Stream.of(request.getLength(), request.getStart())).toArray());
		return result;
	}

	@Override
	public int getCountFilteredRecords(PageRequest2 request) {
		Integer ordercolumn = (Integer) request.getOrder().get(0).get("column");
		String columnName = (String) request.getColumns().get(ordercolumn).get("data");
		String searchElement = "%" + request.getSearch().get("value") + "%";
		List<HashMap<String, Object>> listOfColumns = request.getColumns();
		List<String> individualSearchElements = new ArrayList<String>();

		StringBuilder sql = new StringBuilder();
		sql.append("select count(*) from student where deleted_flag = 'N' and ( "); // Use single quotes for specific value

		List<String> columnNames = getColumnNames("student");
		List<String> searchParameters = new ArrayList<>();
		columnNames.remove("deleted_flag");
		columnNames.remove("createdDate");
		columnNames.remove("updatedDate");
		for (String string : columnNames) {
			sql.append(string + " like ? or ");

			searchParameters.add(searchElement);
		}

		for (HashMap<String, Object> string : listOfColumns) {
			String individualSearchElement = (String) ((Map<String, Object>) string.get("search")).get("value");

			String column_name = (String) (string.get("data"));
			System.out.println("individual search element : "+ individualSearchElement + "----------"+ column_name);
			if (columnNames.contains(column_name)) {
				if (individualSearchElement != null && !individualSearchElement.isEmpty()) {
					sql.append(column_name + " like ? or ");
					searchParameters.add("%" + individualSearchElement + "%"); // Add individual search term (if not empty)
				} 
			}
		}

		// Remove the last "or "
		int lastOrIndex = sql.lastIndexOf("or ");
		if (lastOrIndex != -1) {
			sql.delete(lastOrIndex, lastOrIndex + 3);
		}

		sql.append(") ");


		if (ordercolumn != null) {
			String orderDirection = (String) request.getOrder().get(0).get("dir");
			sql.append("order by " + (columnNames.contains(columnName) ? columnName : "student_id") + " " + (orderDirection != null ? orderDirection : "asc") + " ");

		}


		logger.info(sql.toString());
		//	    System.out.println("stream ----"+ Stream.concat(searchParameters.stream(), Stream.of(request.getLength(), request.getStart())).toArray().toString());
		int result = jdbcTemplate.queryForObject(sql.toString(), Integer.class, 
				searchParameters.toArray());
		return result;

	}



	@Override
	public List<StudentData> studentDataTableWithoutSearchElement(PageRequest2 request) {

		StringBuilder sql = new StringBuilder();
		sql.append("select count(*) from student where deleted_flag = 'N' ");
		RowMapper<StudentData> rowMapper = new BeanPropertyRowMapper<StudentData>(StudentData.class);

		List<String> columnNames = getColumnNames("student");
		if (request.getOrder() != null && !request.getOrder().isEmpty()) {
			sql.append("ORDER BY ");
			for (int i = 0; i < request.getOrder().size(); i++) {
				int sortOrderColumn;
				if((int)request.getOrder().get(i).get("column") > request.getColumns().size()-1) {
					sortOrderColumn = (int) request.getOrder().get(i).get("column");
				}
				else {
					sortOrderColumn = 0;
				}
				String sortOrderColumnName = (String) request.getColumns().get(sortOrderColumn).get("data");
				String sortOrderDirection = (String) request.getOrder().get(i).get("dir");
				if (columnNames.contains(sortOrderColumnName)) {
					sql.append(sortOrderColumnName).append(" ").append(sortOrderDirection.isEmpty() || sortOrderDirection == null ? "ASC ": sortOrderDirection.equalsIgnoreCase("DESC ") ? "DESC " : "ASC ");
					if (i < request.getOrder().size() - 1) {
						sql.append(", ");
					}
				}
			}
		} else {
			sql.append("ORDER BY student_id asc ");
		}

		sql.append("limit ? offset ? ");
		logger.info("From studentDataTableWithoutSearchElement ---"+sql.toString());
		List<StudentData> result = jdbcTemplate.query(sql.toString(), rowMapper , request.getLength(), request.getStart());
		return result;

	}

}




