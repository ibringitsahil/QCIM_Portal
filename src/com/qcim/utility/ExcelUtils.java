package com.qcim.utility;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.qcim.appModules.BaseClass;

public class ExcelUtils extends BaseClass {
	public  String filelocation;
	public  FileInputStream ipstr = null;
	public  FileOutputStream opstr =null;
	private  XSSFSheet ExcelWSheet=null;
	private  XSSFWorkbook ExcelWBook=null;


	//Constructor 
	public ExcelUtils(String filelocation) {		
		this.filelocation=filelocation;
		try {
			ipstr = new FileInputStream(filelocation);
			ExcelWBook = new XSSFWorkbook(ipstr);
			ExcelWSheet = ExcelWBook.getSheetAt(0);
			ipstr.close();
		} catch (Exception e) {			
			e.printStackTrace();
		} 

	}

	//To retrieve No Of Rows from .xls file's sheets.
	public  int retrieveNoOfRows(String wsName){		
		int sheetIndex=ExcelWBook.getSheetIndex(wsName);
		if(sheetIndex==-1)
			return 0;
		else{
			ExcelWSheet = ExcelWBook.getSheetAt(sheetIndex);
			int rowCount=ExcelWSheet.getLastRowNum()+1;		
			return rowCount;		
		}
	}

	//To retrieve No Of Columns from .cls file's sheets.
	public int retrieveNoOfCols(String wsName){
		int sheetIndex=ExcelWBook.getSheetIndex(wsName);
		if(sheetIndex==-1)
			return 0;
		else{
			ExcelWSheet = ExcelWBook.getSheetAt(sheetIndex);
			int colCount=ExcelWSheet.getRow(0).getLastCellNum();			
			return colCount;
		}
	}



	//To retrieve test data from test case data sheets.
	public Object[][] retrieveTestData(String wsName){
		int sheetIndex=ExcelWBook.getSheetIndex(wsName);
		if(sheetIndex==-1)
			return null;
		else{
			int rowNum = retrieveNoOfRows(wsName);
			int colNum = retrieveNoOfCols(wsName);

			Object data[][] = new Object[rowNum-1][colNum-2];

			for (int i=0; i<rowNum-1; i++){
				XSSFRow row = ExcelWSheet.getRow(i+1);
				for(int j=0; j< colNum-2; j++){					
					if(row==null){
						data[i][j] = "";
					}
					else{
						XSSFCell cell = row.getCell(j);	

						if(cell==null){
							data[i][j] = "";							
						}
						else{
							cell.setCellType(Cell.CELL_TYPE_STRING);
							String value = cellToString(cell);
							data[i][j] = value;						
						}
					}
				}				
			}			
			return data;		
		}

	}		


	//To write result In test data and test case list sheet.
	public boolean writeResult(String wsName, String colName, int rowNumber, String Result){
		try{
			int sheetIndex=ExcelWBook.getSheetIndex(wsName);
			if(sheetIndex==-1)
				return false;			
			int colNum = retrieveNoOfCols(wsName);
			int colNumber=-1;


			XSSFRow Suiterow = ExcelWSheet.getRow(0);			
			for(int i=0; i<colNum; i++){				
				if(Suiterow.getCell(i).getStringCellValue().equals(colName.trim())){
					colNumber=i;					
				}					
			}

			if(colNumber==-1){
				return false;				
			}

			XSSFRow Row = ExcelWSheet.getRow(rowNumber);
			XSSFCell cell = Row.getCell(colNumber);
			if (cell == null)
				cell = Row.createCell(colNumber);			

			cell.setCellValue(Result);

			opstr = new FileOutputStream(filelocation);
			ExcelWBook.write(opstr);
			opstr.close();


		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}

	//To write result In test suite list sheet.
	public boolean writeResult(String wsName, String colName, String rowName, String Result){
		try{
			int rowNum = retrieveNoOfRows(wsName);
			int rowNumber=-1;
			int sheetIndex=ExcelWBook.getSheetIndex(wsName);
			if(sheetIndex==-1)
				return false;			
			int colNum = retrieveNoOfCols(wsName);
			int colNumber=-1;


			XSSFRow Suiterow = ExcelWSheet.getRow(0);			
			for(int i=0; i<colNum; i++){				
				if(Suiterow.getCell(i).getStringCellValue().equals(colName.trim())){
					colNumber=i;					
				}					
			}

			if(colNumber==-1){
				return false;				
			}

			for (int i=0; i<rowNum-1; i++){
				XSSFRow row = ExcelWSheet.getRow(i+1);				
				XSSFCell cell = row.getCell(0);	
				cell.setCellType(org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING);
				String value = cellToString(cell);	
				if(value.equals(rowName)){
					rowNumber=i+1;
					break;
				}
			}		

			XSSFRow Row = ExcelWSheet.getRow(rowNumber);
			XSSFCell cell = Row.getCell(colNumber);
			if (cell == null)
				cell = Row.createCell(colNumber);			

			//-------------Setup the font---------

			XSSFFont redFont = ExcelWBook.createFont();
			redFont.setColor(HSSFColor.RED.index);

			XSSFFont greenFont = ExcelWBook.createFont();
			greenFont.setColor(HSSFColor.GREEN.index);

			// create a cell style and assign the first font to it
			XSSFCellStyle style = ExcelWBook.createCellStyle();



			cell.setCellValue(Result);
			if (Result.equalsIgnoreCase("PASS"))
			{
				cell = Row.getCell(colNumber+1);
				if (cell == null)
					cell = Row.createCell(colNumber+1);
				style.setFont(greenFont);
			}
			else if (Result.equalsIgnoreCase("FAIL"))
			{
				cell = Row.getCell(colNumber+1);
				if (cell == null)
					cell = Row.createCell(colNumber+1);

				style.setFont(redFont);
			}

			// assign the style to the cell
			cell.setCellStyle(style);

			opstr = new FileOutputStream(filelocation);
			ExcelWBook.write(opstr);
			opstr.close();


		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}


	//To get the ColumnNames from the excel sheet
	public Object[] retrieveColumnNameData(String wsName){
		int colNum=0;
		int sheetIndex=ExcelWBook.getSheetIndex(wsName);
		if(sheetIndex==-1)
			return null;
		else{
			int rowNum = retrieveNoOfRows(wsName);
			//	int colNum = retrieveNoOfCols(wsName);

			Object data[] = new Object[rowNum-1];

			for (int i=0; i<rowNum-1; i++){
				XSSFRow row = ExcelWSheet.getRow(i+1);
				//for(int j=0; j< colNum; j++){					
				if(row==null){
					data[i] = "";
				}
				else{
					XSSFCell cell = row.getCell(colNum);	

					if(cell==null){
						data[i] = "";							
					}
					else{
						cell.setCellType(Cell.CELL_TYPE_STRING);
						String value = cellToString(cell);
						data[i] = value;						
					}
				}
				//}				
			}			
			return data;		
		}

	}		

	//To retrieve  CaseToRun flag of test case.
	public String retrieveToRunFlag(String wsName, String colName, String rowName){

		int sheetIndex=ExcelWBook.getSheetIndex(wsName);
		if(sheetIndex==-1)
			return null;
		else{
			int rowNum = retrieveNoOfRows(wsName);
			int colNum = retrieveNoOfCols(wsName);
			int colNumber=-1;
			int rowNumber=-1;			

			XSSFRow Suiterow = ExcelWSheet.getRow(0);				

			for(int i=0; i<colNum; i++){
				if(Suiterow.getCell(i).getStringCellValue().equals(colName.trim())){
					colNumber=i;					
				}					
			}

			if(colNumber==-1){
				return "";				
			}


			for(int j=0; j<rowNum; j++){
				XSSFRow Suitecol = ExcelWSheet.getRow(j);				
				if(Suitecol.getCell(0).getStringCellValue().equals(rowName.trim())){
					rowNumber=j;	
				}					
			}

			if(rowNumber==-1){
				return "";				
			}

			XSSFRow row = ExcelWSheet.getRow(rowNumber);
			XSSFCell cell = row.getCell(colNumber);
			if(cell==null){
				return "";
			}
			String value = cellToString(cell);
			return value;			
		}			
	}

	//To convert the cell value to String
	private String cellToString(XSSFCell cell2) {
		int type;
		Object result;
		type = cell2.getCellType();			
		switch (type){
		case 0 :
			result = cell2.getNumericCellValue();
			break;

		case 1 : 
			result = cell2.getStringCellValue();
			break;

		default :
			throw new RuntimeException("Unsupportd cell.");			
		}
		return result.toString();
	}

	//To retrieve DataToRun flag of test data.
	public String[] retrieveToRunFlagTestData(String wsName, String colName){

		int sheetIndex=ExcelWBook.getSheetIndex(wsName);
		if(sheetIndex==-1)
			return null;
		else{
			int rowNum = retrieveNoOfRows(wsName);
			int colNum = retrieveNoOfCols(wsName);
			int colNumber=-1;


			XSSFRow Suiterow = ExcelWSheet.getRow(0);				
			String data[] = new String[rowNum-1];
			for(int i=0; i<colNum; i++){
				if(Suiterow.getCell(i).getStringCellValue().equals(colName.trim())){
					colNumber=i;					
				}					
			}

			if(colNumber==-1){
				return null;				
			}

			for(int j=0; j<rowNum-1; j++){
				XSSFRow Row = ExcelWSheet.getRow(j+1);
				if(Row==null){
					data[j] = "";
				}
				else{
					XSSFCell cell = Row.getCell(colNumber);
					if(cell==null){
						data[j] = "";
					}
					else{
						String value = cellToString(cell);
						data[j] = value;	
					}	
				}
			}

			return data;			
		}			
	}

}