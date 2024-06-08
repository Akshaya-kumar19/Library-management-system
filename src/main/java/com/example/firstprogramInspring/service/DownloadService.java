package com.example.firstprogramInspring.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletResponse;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts.FontName;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.poi.openxml4j.opc.internal.ContentType;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.firstprogramInspring.DAO.LibraryDAO;
import com.example.firstprogramInspring.DAO.StudentDAO;
import com.example.firstprogramInspring.Model.CheckingInRequest;
import com.example.firstprogramInspring.Model.StudentBookRegister;
import com.example.firstprogramInspring.Model.StudentData;
import com.example.firstprogramInspring.Model.WaitingList;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

@Service
public class DownloadService {
	@Autowired
	private LibraryDAO libraryDAO;

	@Autowired
	private StudentDAO studentDAO;

	public ResponseEntity<?> downloadExcelFile(Integer studentId) {
		List<StudentData> list = new ArrayList<StudentData>();
		if (studentId == null) {
			list = studentDAO.findall();
		} else {
			list.add(studentDAO.findbyid(studentId));
		}
		try (Workbook workbook = new XSSFWorkbook()) {
			Sheet sheet = workbook.createSheet("students");

			Row headerRow = sheet.createRow(0);
			String[] columns = { "Student ID", "Name", "Register No", "Gender", "Age", "Phone Number",
			"Current Status" };
			for (int i = 0; i < columns.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(columns[i]);
				CellStyle style = workbook.createCellStyle();
				Font font = workbook.createFont();
				font.setBold(true);
				style.setFont(font);
				cell.setCellStyle(style);
			}

			int rowNum = 1;
			for (StudentData student : list) {
				Row row = sheet.createRow(rowNum++);
				row.createCell(0).setCellValue(student.getStudent_id());
				row.createCell(1).setCellValue(student.getName());
				row.createCell(2).setCellValue(student.getRegister_no());
				row.createCell(3).setCellValue(student.getGender());
				row.createCell(4).setCellValue(student.getAge());
				row.createCell(5).setCellValue(student.getPhone_number());
				row.createCell(6).setCellValue(student.getCurrent_status());
			}

			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			workbook.write(byteArrayOutputStream);

			ByteArrayInputStream in = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
			InputStreamResource resource = new InputStreamResource(in);

			return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=students.xlsx")
					.contentType(MediaType.MULTIPART_FORM_DATA).body(resource);

		} catch (Exception e) {
			return ResponseEntity.status(500).body(null);
		}

	}

	public ResponseEntity<?> csvFileDowload(Integer studentId) {
		List<StudentData> list = new ArrayList<StudentData>();
		if (studentId == null) {
			list = studentDAO.findall();
		} else {
			list.add(studentDAO.findbyid(studentId));
		}

		try {
			StringBuilder csvfile = new StringBuilder();
			csvfile.append("Student ID, Name, Register No, Gender, Age, Phone Number, Current Status\n");
			for (StudentData student : list) {
				csvfile.append(student.getStudent_id()).append(", ");
				csvfile.append(student.getName()).append(",");
				csvfile.append(student.getRegister_no()).append(", ");
				csvfile.append(student.getGender()).append(", ");
				csvfile.append(student.getAge()).append(", ");
				csvfile.append(student.getPhone_number()).append(", ");
				csvfile.append(student.getCurrent_status()).append("\n");
			}
			ByteArrayInputStream input = new ByteArrayInputStream(csvfile.toString().getBytes());
			InputStreamResource resource = new InputStreamResource(input);
			HttpHeaders header = new HttpHeaders();
			header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=students.csv");
			header.setContentType(MediaType.parseMediaType("text/csv"));

			return ResponseEntity.ok().headers(header).body(resource);
		} catch (Exception e) {

			return ResponseEntity.status(500).body(null);
		}
	}

	public void pdfFileDownload(Integer student_id, HttpServletResponse response) {
		try {
			response.setContentType("application/pdf");
			response.setHeader("Content-Disposition", "attachment; filename=student_details.pdf");
			List<StudentData> list = new ArrayList<StudentData>();
			if (student_id == null) {
				list = studentDAO.findall();
			} else {
				list.add(studentDAO.findbyid(student_id));
			}

			Document document = new Document(PageSize.A4.rotate());

			PdfWriter.getInstance(document, response.getOutputStream());
			document.open();

			Image logo = Image.getInstance("/home/akshayakumar/Downloads/studentdetailsImage.jpeg");
			logo.scaleToFit(100, 100);
			logo.setAlignment(Element.ALIGN_CENTER);
			document.add(logo);

			document.add(Chunk.NEWLINE);
			com.itextpdf.text.Font headingFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 24, BaseColor.BLACK);
			Paragraph heading = new Paragraph("Student Details...", headingFont);
			heading.setAlignment(Element.ALIGN_CENTER);
			document.add(heading);
			document.add(Chunk.NEWLINE);
			PdfPTable table = new PdfPTable(11);
			table.setWidthPercentage(100);
			table.setWidths(new int[] { 1, 2, 2, 2, 1, 2, 2, 2, 2, 2, 1 });
			document.newPage();
			com.itextpdf.text.Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLUE);

			addTableHeader(table, headerFont);

			int count = 0;
			com.itextpdf.text.Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLUE);

			for (StudentData studentData : list) {
				table.addCell(new PdfPCell(new Phrase(String.valueOf(studentData.getStudent_id()), cellFont)));
				table.addCell(new PdfPCell(new Phrase(studentData.getName(), cellFont)));
				table.addCell(new PdfPCell(new Phrase(String.valueOf(studentData.getRegister_no()), cellFont)));
				table.addCell(new PdfPCell(new Phrase(studentData.getGender(), cellFont)));
				table.addCell(new PdfPCell(new Phrase(String.valueOf(studentData.getAge()), cellFont)));
				table.addCell(new PdfPCell(new Phrase(studentData.getPhone_number(), cellFont)));
				table.addCell(new PdfPCell(new Phrase(studentData.getCurrent_status(), cellFont)));
				table.addCell(new PdfPCell(new Phrase(studentData.getEmail_address(), cellFont)));
				table.addCell(new PdfPCell(new Phrase(studentData.getCourse(), cellFont)));
				table.addCell(new PdfPCell(new Phrase(String.valueOf(studentData.getBatch()), cellFont)));
				table.addCell(new PdfPCell(new Phrase(String.valueOf(studentData.getFees()), cellFont)));
				count++;

				if (count % 10 == 0) {
					document.add(table);
					document.newPage();
					table = new PdfPTable(11);
					table.setWidthPercentage(100);
					table.setWidths(new int[] { 1, 2, 2, 2, 1, 2, 2, 2, 2, 2, 1 });
					addTableHeader(table, headerFont);
				}
			}

			if (count % 10 != 0) {
				document.add(table);
			}
			document.close();
		} catch (BadElementException e) {

		} catch (MalformedURLException e) {

			e.printStackTrace();
		} catch (DocumentException e) {
			response.setStatus(200);
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void addTableHeader(PdfPTable table, com.itextpdf.text.Font headerFont) {
		Stream.of("id", "name", "register no", "gender", "age", "phone number", "current status", "email", "course",
				"batch", "fees").forEach(columnTitle -> {
					PdfPCell header = new PdfPCell();
					header.setBackgroundColor(BaseColor.LIGHT_GRAY);
					header.setPhrase(new Phrase(columnTitle, headerFont));
					header.setHorizontalAlignment(Element.ALIGN_CENTER);
					table.addCell(header);
				});
	}

	public byte[] generatePdfForCheckIn(CheckingInRequest request, List<StudentBookRegister> booksTaken,
			List<WaitingList> waitingLists, List<String> booksNotInLibrary) {

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		Document document = new Document();
		try {

			PdfWriter pdfwriter = PdfWriter.getInstance(document, outputStream);

			document.open();

			//			logo
			String logoPath = "/home/akshayakumar/Downloads/librarylogo3.jpeg";
			Image logo = Image.getInstance(logoPath);
			logo.scaleToFit(50, 50);
			logo.setAlignment(Element.ALIGN_CENTER);
			document.add(logo);

			//Fonts
			com.itextpdf.text.Font titleFont = FontFactory.getFont(FontFactory.COURIER_BOLD, 30);
			titleFont.setColor(BaseColor.BLUE);
			com.itextpdf.text.Font tableTitleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLDOBLIQUE, 20);

			//title
			Paragraph title = new Paragraph("Library Check-in Report", titleFont);
			title.setAlignment(Element.ALIGN_CENTER);
			document.add(title);

			//horizontal line
			LineSeparator horizontalLine = new LineSeparator();
			horizontalLine.setLineWidth(1);

			document.add(new Chunk(horizontalLine));

			Paragraph paraline = new Paragraph("Register No: " + request.getRegister_id());
			paraline.setAlignment(Element.ALIGN_RIGHT);
			document.add(paraline);

			paraline = new Paragraph("Student Name: " + request.getStudent_name());
			paraline.setAlignment(Element.ALIGN_RIGHT);
			document.add(paraline);

			document.add(new Chunk(horizontalLine));

			// books taken
			if (booksTaken.size() > 0) {
				paraline = new Paragraph("Books Taken: ", tableTitleFont);
				paraline.setSpacingAfter(10);
				document.add(paraline);

				PdfPTable booksTakenTable = new PdfPTable(2);
				booksTakenTable.addCell("Book id");
				booksTakenTable.addCell("Book Name");

				for (StudentBookRegister book : booksTaken) {
					booksTakenTable.addCell(String.valueOf(book.getBook_id()));
					booksTakenTable.addCell(libraryDAO.getBookNameUsingBookID(book.getBook_id()));
				}
				booksTakenTable.setHorizontalAlignment(90);
				document.add(booksTakenTable);
			}
			// waiting list
			if (waitingLists.size() > 0) {
				paraline = new Paragraph("Waiting List: ", tableTitleFont);
				paraline.setSpacingAfter(10);
				document.add(paraline);

				PdfPTable waitingListTable = new PdfPTable(2);

				waitingListTable.addCell("Book id");
				waitingListTable.addCell("Book Name");

				for (WaitingList book : waitingLists) {
					waitingListTable.addCell(String.valueOf(book.getBook_id()));
					waitingListTable.addCell(libraryDAO.getBookNameUsingBookID(book.getBook_id()));
				}
				waitingListTable.setHorizontalAlignment(90);
				document.add(waitingListTable);
			}
			// books not in library
			if (booksNotInLibrary.size() > 0) {
				paraline = new Paragraph("Books Not In Library: ", tableTitleFont);
				paraline.setSpacingAfter(10);
				document.add(paraline);

				com.itextpdf.text.List bookNotInLibraryList = new com.itextpdf.text.List(true);

				for (String book : booksNotInLibrary) {
					bookNotInLibraryList.add(book);
				}
				bookNotInLibraryList.setIndentationRight(10);
				document.add(bookNotInLibraryList);
			}

			if (booksTaken.size() == 0) {
				com.itextpdf.text.Font warnFont = FontFactory.getFont(FontFactory.COURIER, 12);
				warnFont.setColor(BaseColor.RED);
				paraline = new Paragraph("No book is issued right now", warnFont);

				document.add(paraline);
			}

			document.close();
			pdfwriter.close();
			return outputStream.toByteArray();

		} catch (DocumentException e) {
			e.printStackTrace();
			return null;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}


}
