package com.example.firstprogramInspring.service;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.firstprogramInspring.DAO.StudentDAO;
import com.example.firstprogramInspring.Model.StudentData;

@Service
public class ImportService {

	@Autowired
	StudentDAO studentDAO;

	public String saveStudentFromExcel(MultipartFile file) {
		String message = "";
		try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
			Sheet sheet = workbook.getSheetAt(0);

			for (Row row : sheet) {
				if (row.getRowNum() == 0) {
					continue;
				}
				StudentData student = new StudentData();
				student.setName(row.getCell(0).getStringCellValue());
				student.setRegister_no((int) row.getCell(1).getNumericCellValue());
				student.setGender(row.getCell(2).getStringCellValue());
				student.setAge((int) row.getCell(3).getNumericCellValue());
				student.setPhone_number(row.getCell(4).getStringCellValue());
				student.setCurrent_status(row.getCell(5).getStringCellValue());
				student.setEmail_address(row.getCell(6).getStringCellValue());
				student.setCourse(row.getCell(7).getStringCellValue());
				student.setBatch((int) row.getCell(8).getNumericCellValue());
				student.setFees((int) row.getCell(9).getNumericCellValue());

				if (studentDAO.checkRegisterNo((int) row.getCell(1).getNumericCellValue())) {

					if (row.getCell(2).getStringCellValue().equalsIgnoreCase("male")
							|| row.getCell(2).getStringCellValue().equalsIgnoreCase("female")) {

						if (row.getCell(4).getStringCellValue().length() == 10) {

							if (row.getCell(5).getStringCellValue().equalsIgnoreCase("active")
									|| row.getCell(5).getStringCellValue().equalsIgnoreCase("inactive")) {

								int query_result = studentDAO.save(student);
								if (query_result != 0) {
									message += "The student inserted successfully from the row " + (row.getRowNum() + 1)
											+ "\n";

								} else {
									message += "Some values are missing from the row " + (row.getRowNum() + 1) + "\n";
								}
							} else {
								message += "current status must be active or inactive in row " + (row.getRowNum() + 1)
										+ "\n";
							}
						} else {
							message += "phone number must contains 10 digits in row " + (row.getRowNum() + 1) + "\n";
						}
					} else {
						message += "gender must be male or female in row " + (row.getRowNum() + 1) + "\n";
					}

				} else {
					message += "The register number already exists that you entered in row " + (row.getRowNum() + 1)
							+ "\n";
				}
			}
		} catch (Exception e) {
			return e.getMessage();
		}
		return message;
	}

	public String saveStudentFromExcelError(MultipartFile file, HttpServletResponse response) {
		String message = "";
		try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
			Sheet sheet = workbook.getSheetAt(0);
			int errorColumnIndex = 10; // Column K (index 10, as columns are 0-indexed)

			// Create a header for the error message column if it doesn't exist
			Row headerRow = sheet.getRow(0);
			Cell errorHeaderCell = headerRow.createCell(errorColumnIndex);
			errorHeaderCell.setCellValue("Error Message");

			// Iterate over the rows using a for loop
			for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
				Row row = sheet.getRow(rowIndex);
				if (row == null) {
					continue; // Skip empty rows
				}

				StringBuilder errorMessage = new StringBuilder();
				StudentData student = new StudentData();

				try {
					student.setName(row.getCell(0).getStringCellValue());
					student.setRegister_no((int) row.getCell(1).getNumericCellValue());
					student.setGender(row.getCell(2).getStringCellValue());
					student.setAge((int) row.getCell(3).getNumericCellValue());
					student.setPhone_number(row.getCell(4).getStringCellValue());
					student.setCurrent_status(row.getCell(5).getStringCellValue());
					student.setEmail_address(row.getCell(6).getStringCellValue());
					student.setCourse(row.getCell(7).getStringCellValue());
					student.setBatch((int) row.getCell(8).getNumericCellValue());
					student.setFees((int) row.getCell(9).getNumericCellValue());
				} catch (Exception e) {
					errorMessage.append("Error reading data; ");
				}

				if (studentDAO.checkRegisterNo((int) row.getCell(1).getNumericCellValue())) {
					if (row.getCell(2).getStringCellValue().equalsIgnoreCase("male")
							|| row.getCell(2).getStringCellValue().equalsIgnoreCase("female")) {
						if (row.getCell(4).getStringCellValue().length() == 10) {
							if (row.getCell(5).getStringCellValue().equalsIgnoreCase("active")
									|| row.getCell(5).getStringCellValue().equalsIgnoreCase("inactive")) {
								int query_result = studentDAO.save(student);
								if (query_result != 0) {
									message += "The student inserted successfully from the row " + (rowIndex + 1)
											+ "\n";
								} else {
									errorMessage.append("Some values are missing; ");
								}
							} else {
								errorMessage.append("Current status must be active or inactive; ");
							}
						} else {
							errorMessage.append("Phone number must contain 10 digits; ");
						}
					} else {
						errorMessage.append("Gender must be male or female; ");
					}
				} else {
					errorMessage.append("The register number already exists; ");
				}

				// Write the error message to the new column (K)
				Cell errorCell = row.createCell(errorColumnIndex);
				errorCell.setCellValue(errorMessage.toString());
			}

			// Set the response headers
			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			response.setHeader("Content-Disposition",
					"attachment; filename=modified_" + file.getOriginalFilename().replace(".xlsx", "") + ".xlsx");

			// Write the workbook to the response output stream
			try (OutputStream out = response.getOutputStream()) {
				workbook.write(out);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return message;
	}
	
	
}
