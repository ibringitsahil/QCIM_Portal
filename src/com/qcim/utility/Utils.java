package com.qcim.utility;



public class Utils {
	public static String getTestCaseName(String sTestCase)throws Exception{
		String value = sTestCase;
		try{
			int posi = value.indexOf("@");
			value = value.substring(0, posi);
			posi = value.lastIndexOf(".");	
			value = value.substring(posi + 1);
			return value;
		}catch (Exception e){
			Log.error("Class Utils | Method getTestCaseName | Exception desc : "+e.getMessage());
			throw (e);
		}
	}


	public static String setUsername(String User)
	{
		return User;

	}

	public static boolean checkToRunUtility(ExcelUtils xls, String sheetName, String ToRun, String testSuite){

		boolean Flag = false;		
		if(xls.retrieveToRunFlag(sheetName,ToRun,testSuite).equalsIgnoreCase("y")){
			Flag = true;
		}
		else{
			Flag = false;
		}
		return Flag;		
	}

	public static String[] checkToRunUtilityOfData(ExcelUtils xls, String sheetName, String ColName){		
		return xls.retrieveToRunFlagTestData(sheetName,ColName);		 	
	}

	public static boolean WriteResultUtility(ExcelUtils xls, String sheetName, String ColName, int rowNum, String Result){			
		return xls.writeResult(sheetName, ColName, rowNum, Result);		 	
	}

	public static boolean WriteResultUtility(ExcelUtils xls, String sheetName, String ColName, String rowName, String Result){			
		return xls.writeResult(sheetName, ColName, rowName, Result);		 	
	}

	public static Object[][] GetTestDataUtility(ExcelUtils xls, String sheetName){
		return xls.retrieveTestData(sheetName);	
	}
	public static Object[] GetColumnDataUtility(ExcelUtils xls, String sheetName){
		return xls.retrieveColumnNameData(sheetName);	
	}
}



