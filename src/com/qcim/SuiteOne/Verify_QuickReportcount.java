package com.qcim.SuiteOne;

import org.apache.commons.lang3.ArrayUtils;
import org.openqa.selenium.WebElement;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.qcim.utility.ExcelUtils;
import com.qcim.utility.Log;
import com.qcim.utility.Utils;
import com.qcim.appModules.BaseClass;
import com.qcim.appModules.Functions;

public class Verify_QuickReportcount extends Functions{

	ExcelUtils FilePath = null;
	String SheetName=null;
	String sTestCaseName=null;
	String ToRunColumnNameTestCase = null;
	String ToRunColumnNameTestData = null;
	String TestDataToRun[]=null;
	String TestCaseNameSheetName = null;
	static int DataSet=-1;	
	static boolean Testskip=false;
	static boolean TestCasePass=true;
	public static boolean Testfail=false;
	public static String FunctionResult="Pass";
	WebElement element = null; 
	public static String[] FunResult ;

	//*************************************
	@BeforeTest
	public void checkCaseToRun() throws Exception{
		//Called init() function from SuiteBase class to Initialize .xls Files
		init();			
		//To set SuiteOne.xls file's path In FilePath Variable.
		FilePath = TestCaseListExcelOne;		
		// Getting the Test Case name, as it will going to use in so many places
		sTestCaseName = this.getClass().getSimpleName();
		TestCaseNameSheetName = "TestCases";
		//Name of column In TestCasesList Excel sheet.
		ToRunColumnNameTestCase = "CaseToRun";
		//Name of column In Test Case Data sheets.
		ToRunColumnNameTestData = "DataToRun";
		//Bellow given syntax will Insert log In applog.log file.
		Log.info(sTestCaseName+" : Execution started.");

		//To check test case's CaseToRun = Y or N In related excel sheet.
		//If CaseToRun = N or blank, Test case will skip execution. Else It will be executed.
		if(!Utils.checkToRunUtility(FilePath, TestCaseNameSheetName,ToRunColumnNameTestCase,sTestCaseName)){
			Log.info(sTestCaseName+" : CaseToRun = N for So Skipping Execution.");
			//To report result as skip for test cases In TestCasesList sheet.
			Utils.WriteResultUtility(FilePath, TestCaseNameSheetName, "Result", sTestCaseName, "SKIP");
			//ExcelUtils.setCellData("SKIP", User, Constant.Col_Result);
			//To throw skip exception for this test case.
			throw new SkipException(sTestCaseName+"'s CaseToRun Flag Is 'N' Or Blank. So Skipping Execution Of "+sTestCaseName);
		}	
		//To retrieve DataToRun flags of all data set lines from related test data sheet.
		TestDataToRun = Utils.checkToRunUtilityOfData(FilePath, sTestCaseName, ToRunColumnNameTestData);
	}


	// This is the starting of the Main Test Case
	@Test(dataProvider = "TestCase_003")

	public void TestCase_003(String User,String Pwd,String Role) {

		// Every exception thrown from any class or method, will be catch here and will be taken care off
		DataSet++;
		//s_assert1 = new SoftAssert();

		// Here we are calling the SignIN Action and passing argument (iTestCaseRow)
		if(!TestDataToRun[DataSet].equalsIgnoreCase("Y"))
		{	
			Log.info(sTestCaseName+" : DataToRun = N for data set line "+(DataSet+1)+" So skipping Its execution.");
			//If DataToRun = "N", Set Testskip=true.
			Testskip=true;
			throw new SkipException("DataToRun for row number "+DataSet+" Is No Or Blank. So Skipping Its Execution.");
		}

		try
		{
			loadWebBrowser();
			ExcecuteFunction("NavigateURL",User,"","","","");
			ExcecuteFunction("SignIn",User,Pwd,"","","");
			ExcecuteFunction("I_AgreeClick",User,"","",Object.getProperty("btn_IAgree"),"");
			ExcecuteFunction("ClickbyXpath",User,"","",Object.getProperty("lnk_LM"),"");
			//To click on the Loans Tab

			//To verify the count for Pending File Requests 
			ExcecuteFunction("VerifyCount",User,Object.getProperty("lable_Pending"),Object.getProperty("lnk_QuickPendingFile"),Object.getProperty("lnk_PendingFile"),sTestCaseName);
			ExcecuteFunction("ClickbyXpath",User,"","",Object.getProperty("btn_QuickReport_Close"),"");
			//To verify the count for Incomplete File Requests 
			ExcecuteFunction("VerifyCount",User,Object.getProperty("lable_IncompleteFile"),Object.getProperty("lnk_QuickIncompleteFile"),Object.getProperty("lnk_IncompleteFile"),sTestCaseName);
			ExcecuteFunction("ClickbyXpath",User,"","",Object.getProperty("btn_QuickReport_Close"),"");
			//To verify the count for Incomplete Docs Requests 
			ExcecuteFunction("VerifyCount",User,Object.getProperty("lable_IncompleteDoc"),Object.getProperty("lnk_QuickIncompleteDoc"),Object.getProperty("lnk_IncompleteDoc"),sTestCaseName);
			ExcecuteFunction("ClickbyXpath",User,"","",Object.getProperty("btn_QuickReport_Close"),"");
			//To verify the count for Missing Docs Requests 
			ExcecuteFunction("VerifyCount",User,Object.getProperty("lable_MissingDoc"),Object.getProperty("lnk_QuickMissingDoc"),Object.getProperty("lnk_MissingDoc"),sTestCaseName);
			ExcecuteFunction("ClickbyXpath",User,"","",Object.getProperty("btn_QuickReport_Close"),"");
			//To verify the count for Appeals Pending 
			ExcecuteFunction("VerifyCount",User,Object.getProperty("lable_AppealsPending"),Object.getProperty("lnk_QuickAppealsPending"),Object.getProperty("lnk_AppealsPending"),sTestCaseName);
			ExcecuteFunction("ClickbyXpath",User,"","",Object.getProperty("btn_QuickReport_Close"),"");
			//To logout from the application
			ExcecuteFunction("ClickbyXpath",User,"","",Object.getProperty("btn_Logout"),"");


			//If any of the above function fails ,then flag will be set as true.
			String []FunResult = new String[Functions.AlFunResult.size()];
			AlFunResult.toArray(FunResult);
			if(ArrayUtils.contains(FunResult, "Fail"))   //To check array contains Fail
			{
				Testfail=true;
			}
			else
			{
				Testfail=false;	
			}


		}catch (Exception e){
			Testfail=true;
			System.out.println(e);
		}
		if(Testfail)
		{
			s_assert.assertAll();	
		}
	}	

	@DataProvider
	public Object[][] TestCase_003() throws Exception{

		return Utils.GetTestDataUtility(FilePath, sTestCaseName);
	}

	@AfterMethod
	public void reporterDataResults(){		

		if(Testskip){
			Log.info(sTestCaseName+" : Reporting test data set line "+(DataSet+1)+" as SKIP In excel.");
			//If found Testskip = true, Result will be reported as SKIP against data set line In excel sheet.
			Utils.WriteResultUtility(FilePath, sTestCaseName, "Result", DataSet+1, "SKIP");
		}
		else if(Testfail){
			Log.info(sTestCaseName +" : Reporting test data set line "+(DataSet+1)+" as FAIL In excel.");
			//To make object reference null after reporting In report.
			s_assert = null;
			Functions.AlFunResult.clear();
			//Set TestCasePass = false to report test case as fail In excel sheet.
			TestCasePass=false;	
			//If found Testfail = true, Result will be reported as FAIL against data set line In excel sheet.
			Utils.WriteResultUtility(FilePath, sTestCaseName, "Result", DataSet+1, "FAIL");			
		}else{
			Log.info(sTestCaseName+" : Reporting test data set line "+(DataSet+1)+" as PASS In excel.");
			Functions.AlFunResult.clear();
			//If found Testskip = false and Testfail = false, Result will be reported as PASS against data set line In excel sheet.
			Utils.WriteResultUtility(FilePath, sTestCaseName, "Result", DataSet+1, "PASS");
		}
		//At last make both flags as false for next data set.
		Testskip=false;
		Testfail=false;

	}
	@AfterTest
	public void afterMethod() throws Exception {
		// Printing beautiful logs to end the test case
		Log.endTestCase(sTestCaseName);
		// Closing the opened driver
		BaseClass.closeWebBrowser();
		if(TestCasePass){
			Log.info(sTestCaseName+" : Reporting test case as PASS In excel.");
			Utils.WriteResultUtility(FilePath, TestCaseNameSheetName, "Result", sTestCaseName, "PASS");
		}
		else{
			Log.info(sTestCaseName+" : Reporting test case as FAIL In excel.");
			Utils.WriteResultUtility(FilePath, TestCaseNameSheetName, "Result", sTestCaseName, "FAIL");			
		}
	}
}
