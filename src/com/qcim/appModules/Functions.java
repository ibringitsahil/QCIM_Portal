package com.qcim.appModules;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.ArrayUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Reporter;
import com.qcim.utility.ErrorUtil;
import com.qcim.utility.ExcelUtils;
import com.qcim.utility.Log;
import com.qcim.utility.Utils;

public class Functions extends BaseClass{

	//Variable Declaration
	String result="";
	String Loggedinuser=" ";
	boolean signinResult=false;;
	WebElement element = null; 
	ExcelUtils FilePath = null;
	String DataSheetName = null;
	// to format the date
	DateFormat outputFormat = new SimpleDateFormat("MM/dd/yyyy");
	DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
	public static String[] FunRes ;
	public static String[] FunRes1 ;
	public static ArrayList<String> AlFunResult1 = new ArrayList<String>();
	public static ArrayList<String> AlFunResult = new ArrayList<String>();
	public static ArrayList<String> AlFunResult2 = new ArrayList<String>();
	private static final int WEEKEND_1 = Calendar.SATURDAY;
	private static final int WEEKEND_2 = Calendar.SUNDAY;

	String datePattern = "\\d{4}-\\d{1,2}-\\d{1,2}";

	/*This Function will get the user name and password from the properties file
and	sign in in the application*/
	public String SignIn(String UserName,String Password,String testcasename )
	{
		try
		{
			input(Object.getProperty("txt_UserName"),UserName);
			Log.info(UserName+" is entered in UserName text box" );
			input(Object.getProperty("txt_Password"),Password);
			ClickbyXpath(Object.getProperty("btn_SignIn"));
			signinResult=verifylogin();
			if (signinResult==true)
			{				
				return "Pass";
			}
			else
			{

				closeWebBrowser();
				return "Fail";
			}

		}catch(Exception e)
		{
			ErrorUtil.addVerificationFailure(e);
			Log.error("Not able sign in in the application --- " + e.getMessage());
			return "Fail";
		}
	}

	//To verify if login is successful or not
	public static boolean verifylogin()
	{
		try
		{
			driver.manage().timeouts().implicitlyWait(90, TimeUnit.SECONDS);
			if (driver.findElement(By.xpath(".//*[@id='mxui_widget_Window_0']/div[1]/div[1]")).isDisplayed())
				//	Reporter.log("Site is available and Successfully logged in");

				return true;
		}catch(Exception e)
		{

			ErrorUtil.addVerificationFailure(e);
			//Reporter.log("Site is not available/Invalid Username OR Password");
			Log.error("Invalid Username OR Password");

		}
		return false;
	}

	//To input the data in the text fields
	public static void input(String xpath, String data){
		try{

			driver.findElement(By.xpath((xpath))).sendKeys(data);
			Log.info("Value entered in the input box");
		}catch(Exception e){
			ErrorUtil.addVerificationFailure(e);
			Log.error("Not able to Enter the value --- " + e.getMessage());


		}
	}

	//To navigate to the url provided in the properties file
	public String NavigateURL()
	{
		try{
			Log.info("Navigating to URL");
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			//Constant Variable is used in place of URL
			driver.get(Param.getProperty("siteURL"));
			return "Pass";
		}catch(Exception e){
			ErrorUtil.addVerificationFailure(e);
			Log.error("Not able to navigate --- " + e.getMessage());
			return "Fail";
		}

	}

	//This function is to click on the 'I Agree' button.
	public String I_AgreeClick(String xpathstring)
	{
		try
		{
			driver.manage().timeouts().implicitlyWait(90, TimeUnit.SECONDS);
			element=driver.findElement(By.xpath(xpathstring));
			waitForElement(element);
			element.click();
			Log.info("Clicked on I Agree button");
			return "Pass";
		}catch(Exception e)
		{
			ErrorUtil.addVerificationFailure(e);
			Log.error("Exception occured while clicking on the I Agree"+ e.getMessage());
			return "Fail";
		}

	}

	//To click on the element
	public String ClickbyXpath(String xpathstring)
	{
		String Label;
		try
		{

			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			element=driver.findElement(By.xpath(xpathstring));
			Label=element.getText();
			waitForElement(element);
			element.click();
			Log.info("Clicked on " +Label+" the Webelement");

			return "Pass";
		}catch(Exception e)
		{
			ErrorUtil.addVerificationFailure(e);
			Log.error("Webelement not found or clicked" +e.getMessage() );
			return "Fail";
		}

	}

	//To verify the count on two different screens is same 
	public String VerifyCount(String xpath1,String xpath2,String xpath3,String testcasename)

	{
		try{
			WebElement element1 = null;
			String temp[]=null;
			String LinkName=driver.findElement(By.xpath(xpath1)).getText();
			driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
			element= driver.findElement(By.xpath(xpath2));
			waitForElement(element);
			String Quickreportcount =element.getText();
			element.click();
			Thread.sleep(7000);
			element1=driver.findElement(By.xpath(xpath3));
			waitForElement(element1);
			String Screenreportcount=element1.getText();
			temp=Screenreportcount.split("of");
			Screenreportcount=temp[1].trim();
			//Converting String to Integer
			int ActualResultInt=Integer.parseInt(Quickreportcount);
			int ExpectedResultInt =  Integer.parseInt(Screenreportcount);
			if(!(ActualResultInt==ExpectedResultInt)){

				s_assert.assertEquals(ActualResultInt, ExpectedResultInt, "QuickReport Count for "+ LinkName + "=" + ActualResultInt + " doesnot matches with " + LinkName + " Screen count " + "=" + ExpectedResultInt + "\n");
				Log.info("QuickReport Count for "+ LinkName + "=" + ActualResultInt + " doesnot matches with " + LinkName + " Screen count " + "=" + ExpectedResultInt);
				AlFunResult1.add("fail");
				return "Fail";	

			}else
			{
				AlFunResult1.add("pass");
				Reporter.log("QuickReport Count for "+ LinkName + "=" + ActualResultInt + " matches with " + LinkName + " Screen count " + "=" + ExpectedResultInt + "\n");
				Log.info("QuickReport Count for "+ LinkName + "=" + ActualResultInt + " matches with " + LinkName + " Screen count " + "=" + ExpectedResultInt);

				return "Pass";
			}

		}

		catch(Exception e)
		{ 
			ErrorUtil.addVerificationFailure(e);
			Log.error("Exception occured ,Count is not verified" +e.getMessage());
			return "Fail";

		}
		finally
		{

			String []FunRes = new String[Functions.AlFunResult1.size()];
			AlFunResult1.toArray(FunRes);
			if(ArrayUtils.contains(FunRes, "Fail"))
			{
				Utils.WriteResultUtility(BaseClass.TestCaseListExcelOne, "TestCases", "Value" ,testcasename, "Count Not Match");
			}
			else
			{
				Utils.WriteResultUtility(BaseClass.TestCaseListExcelOne, "TestCases", "Value" ,testcasename, "Count Match");
			}
		}
	}

	//To get the current system date.
	public static Date getCurrentdate()
	{
		//get current date with Date()
		Date date = new Date();  
		return date;
	}

	public String GetDataAsOfDate(String xpathstring1, String Index)

	{
		try{
			/*String parts[]=Index.split(",");
			int part1=Integer.parseInt(parts[0]);
			int part2=Integer.parseInt(parts[1]);*/
			driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
			element= driver.findElement(By.xpath(xpathstring1));
			String actLabel =element.getText();
			//String ActdataasofDate=actLabel.substring(part1,part2);
			String Parts[]=actLabel.split("of");
			String Actdate=Parts[1].trim();
			
			return Actdate;
		}catch(Exception e)
		{ 
			ErrorUtil.addVerificationFailure(e);
			Log.error("Exception occured " +e.getMessage());
			return null;
		}


	}

	/*----To verify the Data as of Date on the main menu landing screen
	is previous business day's date----*/
	public String VerifyDate(String xpathstring1,String testcasename,String Index)
	{
		try{

			//Get the DataAsOfDate
			String ActDate= GetDataAsOfDate(xpathstring1,Index);

			Date Actdate2 = outputFormat.parse(ActDate);

			//Get the Current System date
			Date CurrentDate=getCurrentdate();
			Date ExpectedDate=getPreviousWorkingDay(CurrentDate);

			String outputText = outputFormat.format(ExpectedDate);
			Date ExpecDate = outputFormat.parse(outputText);


			if(!(ExpecDate.equals(Actdate2)))
			{
				s_assert.assertEquals(ActDate,ExpectedDate,"Actual Date=" + ActDate + " and " + "Expected Date=" + ExpectedDate +" is not matching "+ "\n");
				Log.info(ExpectedDate + " is not matching ");
				Utils.WriteResultUtility(BaseClass.TestCaseListExcelOne, "TestCases", "Value" ,testcasename, ActDate);
				return "Fail";
			}else
			{
				Reporter.log("Actual Date=" + ActDate + " and " + "Expected Date=" + ExpectedDate +" is matching "+ "\n");
				Log.info(ActDate+ " verified and is correct ");
				Utils.WriteResultUtility(BaseClass.TestCaseListExcelOne, "TestCases", "Value" ,testcasename, ActDate);
				return "Pass";
			}

		}catch(Exception e)
		{ 
			ErrorUtil.addVerificationFailure(e);
			Log.error("Exception occured ,Date is not verified" +e.getMessage());
			return "Fail";

		}

	}

	public static Date getPreviousWorkingDay(Date date) {

		Date previousWorkingDate = null;
		try 
		{

			if(date != null) {
				Calendar calInstance = Calendar.getInstance();
				calInstance.setTime(date);
				int weekDay = 0;

				do {
					calInstance.add(Calendar.DATE, -1);
					weekDay = calInstance.get(Calendar.DAY_OF_WEEK);
				} while(weekDay == WEEKEND_1 || weekDay == WEEKEND_2 ||
						holidays.get((calInstance.get(Calendar.MONTH) + 1)
								+ "," + calInstance.get(Calendar.DATE)) != null);

				previousWorkingDate = calInstance.getTime();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return previousWorkingDate;
	}

	//To verify the number of records on any tab.

	public String Verifyrecords(String xpathstring1, String Expectedrec,String testcasename,String xpathstring2)
	{
		try{
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			element= driver.findElement(By.xpath(xpathstring1));
			String actLabel =element.getText();
			//To get the tab Name
			element= driver.findElement(By.xpath(xpathstring2));
			String tabName =element.getText();
			String ActLoanRecords[]=actLabel.split("of");
			String ActLoanRec=ActLoanRecords[1].trim();
			Integer ActRec=Integer.parseInt(ActLoanRec);


			Integer ExpRec=Integer.parseInt(Expectedrec);

			if(!(ActRec > ExpRec) && element.isDisplayed())
			{
				s_assert.assertFalse(ActRec > ExpRec , "Number of records on " + tabName + " Tab =" + ActRec + " are less than " +  Expectedrec+ "\n");
				Log.info("Number of records on " + tabName + " Tab ="  + ActRec + " are less than " +  Expectedrec);
				Utils.WriteResultUtility(BaseClass.TestCaseListExcelOne, "TestCases", "Value" ,testcasename, ActLoanRec);
				return "Fail";
			}else
			{
				Reporter.log("Number of records on " + tabName + " Tab ="  + ActRec + " are greater than " +  Expectedrec+ "\n");
				Log.info("Number of records on" + tabName + " Tab ="  + ActRec + " are greater than " +  Expectedrec);
				Utils.WriteResultUtility(BaseClass.TestCaseListExcelOne, "TestCases", "Value" ,testcasename, ActLoanRec);
				return "Pass";
			}

		}catch(Exception e)
		{ 
			ErrorUtil.addVerificationFailure(e);
			Log.error("Exception occured ,Date is not verified" +e.getMessage());
			return "Fail";

		}
	}

	//To sort the Columns **Clicking three times on the column header

	public String SortColumn(String xpath1)
	{
		try{
			driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
			Actions action = new Actions(driver);
			WebElement element=driver.findElement(By.xpath(xpath1));
			String ColName=element.getText();
			action.moveToElement(element).doubleClick().build().perform();
			action.moveToElement(element).click().build().perform();
			Thread.sleep(2000);

			Log.info("Sorting performed on " + ColName + " Column ");
			return "Pass";
		}catch (Exception e){
			ErrorUtil.addVerificationFailure(e);
			Log.error("Exception occured while performing Sorting on the Selected Column " +e.getMessage());
			return "Fail";
		}
	}

	public String DoubleClick(String xpath1)
	{
		try
		{
			Actions action = new Actions(driver);
			WebElement ColName=driver.findElement(By.xpath(xpath1));
			//Sorting the column by clicking two times on the column header
			action.moveToElement(ColName).click().build().perform();
			Thread.sleep(2000);
			action.moveToElement(ColName).click().build().perform();
			Thread.sleep(2000);
			Log.info("Double Click performed on " + ColName + " Column ");
			return "Pass";
		}catch(Exception e)
		{
			ErrorUtil.addVerificationFailure(e);
			Log.error("Exception occured while performing Doubleclick on the Selected Column " +e.getMessage());
			return "Fail";
		}

	}

	//To Verify Blanks not available
	public String VerifyBlank(String xpath1,String xpath2,String testcasename)
	{
		try{
			driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
			WebElement element=driver.findElement(By.xpath(xpath1));
			String RowValue=element.getText().trim();
			System.out.println("Value is " +RowValue.length());
			WebElement element1=driver.findElement(By.xpath(xpath2));
			String ColName=element1.getText();
			if((RowValue.isEmpty()))
			{
				s_assert.assertFalse(RowValue.length()==0 , " Column " + ColName + " Contains Blank Value " + "\n");
				Log.info(" Column " + ColName + " Contains Blank Value " );
				Utils.WriteResultUtility(BaseClass.TestCaseListExcelOne, "TestCases", "Value" ,testcasename, "Blanks");
				return "Fail";
			}else
			{
				Reporter.log(" Column " + ColName + " Not Contains Blank Value " + "\n");
				Log.info(" Column " + ColName + " Not Contains Blank Value ");
				Utils.WriteResultUtility(BaseClass.TestCaseListExcelOne, "TestCases", "Value" ,testcasename, "OK");
				return "Pass";
			}

		}catch (Exception e){
			ErrorUtil.addVerificationFailure(e);
			Log.error("Exception occured while performing Sorting on the Selected Column " +e.getMessage());
			return "Fail";
		}
	}

	//To verify if difference between Data as of Date and column date is greater 
	//than 1 business day or date format incorrect
	public String VerifyBusinessDays(String xpath1,String xpath2,String testcasename,String xpath3)
	{
		try{
			driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
			WebElement element=driver.findElement(By.xpath(xpath2));
			String ColName=element.getText();
			if(ColName.equalsIgnoreCase(Param.getProperty("col_App")))
			{		
				SortColumn(xpath2);
			}
			else
			{
				DoubleClick(xpath2);
			}
			driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
			WebElement element1=driver.findElement(By.xpath(xpath1));
			//Get the the maximum FirstFileReqDate from the column
			String ColDate=element1.getText().trim();

			//Get the DataAsOfDate 
			String ActDate=GetDataAsOfDate(xpath3,Param.getProperty("Datadate"));

			//Format both the dates to same date format
			Date Firstdate = inputFormat.parse(ColDate);
			String outputText = outputFormat.format(Firstdate);
			Date date1 = outputFormat.parse(outputText);

			Date Actdate2 = outputFormat.parse(ActDate);
			String outputText1 = outputFormat.format(Actdate2);
			Date date3 = outputFormat.parse(outputText1);


			//To check if date is in correct format i.e. yyyy-MM-dd

			if( ColDate.matches(datePattern))
			{
				//Function call to calculate the working days between two dates 

				int Workdays=getWorkingDaysBetweenTwoDates(date1,date3);
				System.out.println(+Workdays);

				if((Workdays) <1 )
				{
					s_assert.assertTrue((Workdays) <1 , " Column " + ColName + "  difference is less than one business day and date format is correct " + "\n");
					Log.info(" Column " + ColName + " difference is less than one business day and date format is correct");
					Utils.WriteResultUtility(BaseClass.TestCaseListExcelOne, "TestCases", "Value" ,testcasename,ColDate );
					return "Pass";
				}

				else
				{
					Reporter.log(" Column " + ColName + " difference is greater than one business day " + "\n");
					Log.info(" Column " + ColName + " difference is greater than one business day " );
					Utils.WriteResultUtility(BaseClass.TestCaseListExcelOne, "TestCases", "Value" ,testcasename, ColDate);
					return "Fail";

				}
			}
			else
			{
				s_assert.assertFalse(!(ColDate.matches(datePattern)), " Column " + ColName + " contains incorrect date format  " + "\n");
				Log.info(" Column " + ColName + " contains incorrect date format " );
				Utils.WriteResultUtility(BaseClass.TestCaseListExcelOne, "TestCases", "Value" ,testcasename, ColDate);
				return "Fail";

			}



		}catch (Exception e){
			ErrorUtil.addVerificationFailure(e);
			Log.error("Exception occured while performing Sorting on the Selected Column " +e.getMessage());
			return "Fail";
		}

	}

	//Function to get the working days between two dates	
	public static int getWorkingDaysBetweenTwoDates(Date startDate, Date endDate) {
		Map<String,String> holidaysmap = new HashMap<String,String>();   
		holidaysmap.putAll(holidays);

		Calendar startCal;  
		Calendar endCal;  
		startCal = Calendar.getInstance();  
		startCal.setTime(startDate);  
		endCal = Calendar.getInstance();  
		endCal.setTime(endDate);  
		int workDays = 0;  

		//Return 0 if start and end are the same  
		if (startCal.getTimeInMillis() == endCal.getTimeInMillis()) {  
			return 0;  
		}  
		//If start date is greater than End date then assign end date as start date
		if (startCal.getTimeInMillis() > endCal.getTimeInMillis()) {  
			startCal.setTime(endDate);  
			endCal.setTime(startDate);  
		}  


		do {
			startCal.add(Calendar.DAY_OF_MONTH, 1);
			if (startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY
					&& startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY
					&& !holidays.containsKey((startCal.get(Calendar.MONTH) + 1)
							+ "," + startCal.get(Calendar.DATE))) {
				++workDays;
			}

		} //while (startCal.getTimeInMillis() < endCal.getTimeInMillis());  
		while (startCal.get(Calendar.YEAR) < endCal.get(Calendar.YEAR) ||
				(startCal.get(Calendar.YEAR) == endCal.get(Calendar.YEAR) &&
				startCal.get(Calendar.DAY_OF_YEAR) < endCal.get(Calendar.DAY_OF_YEAR)));

		return workDays;  
	}
	public String VerifyRMlinkaccess(String xpath1,String testcasename)

	{
		try
		{
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			WebElement Rmlink=driver.findElement(By.xpath(xpath1));
			String RmText=Rmlink.getText();
			String ExpRMText=Param.getProperty("Label_RemedyMgmt");

			if (!((Rmlink.isDisplayed()) && RmText.equals(ExpRMText)))
			{
				//s_assert.assertEquals(RmText, ExpRMText);
				s_assert.assertTrue((RmText.equals(ExpRMText)) ,"Remedy Management is not accessible");
				Log.info("Remedy Management is not accessible ");
				Utils.WriteResultUtility(BaseClass.TestCaseListExcelOne, "TestCases", "Value" ,testcasename, "No");
				return "Fail";
			}else
			{
				Reporter.log(" Remedy Management is accessible " + "\n");
				Log.info(" Remedy Management is  accessible ");
				Utils.WriteResultUtility(BaseClass.TestCaseListExcelOne, "TestCases", "Value" ,testcasename, "Yes");
				return "Pass";
			}

		}catch(Exception e)
		{
			ErrorUtil.addVerificationFailure(e);
			Log.error("Exception occured while accessing RM Link " +e.getMessage());
			return "Fail";
		}
	}

	//To verify the values in the Dashboard
	public String VerifyDashboard(String xpath1,String xpath2,String testcasename)

	{
		try{
			WebElement element1 = null;
			driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
			element1= driver.findElement(By.xpath(xpath1));
			waitForElement(element1);
			String Dashboardcount =element1.getText();
			String LinkName=driver.findElement(By.xpath(xpath2)).getText();
			String count=Dashboardcount.replaceAll(",","");
			//Converting String to Integer
			long ActualDashCount=(long)Double.parseDouble(count);
			System.out.println(LinkName);
			System.out.println(ActualDashCount);
			if(!(ActualDashCount > 160)){
				s_assert.assertTrue((ActualDashCount >160), " Dashboard count = " + ActualDashCount +" on "+ LinkName + " is less than or equal to zero(0) " + "\n");
				Log.info("Dashboard count on"+ LinkName + " is less than or equal to zero(0)");
				//Utils.WriteResultUtility(BaseClass.TestCaseListExcelOne, "TestCases", "Value" ,testcasename, "Not Validated");
				AlFunResult1.add("Fail");
				return "Fail";
				
			}else
			{
				Log.info("Dashboard count on"+ LinkName + " is greater than zero(0)");
				Reporter.log(" Dashboard count =" + ActualDashCount +" on = "+ LinkName + " is grater than zero(0) " + "\n");
				AlFunResult1.add("Pass");
				return "Pass";
			}
		}
		catch(Exception e)
		{ 
			ErrorUtil.addVerificationFailure(e);
			Log.error("Exception occured ,Count is not verified" +e.getMessage());
			return "Fail";

		}
	}

	public void ExcecuteFunction(String Para1,String Para2,String Para3,String Para4,String Para5,String Para6) throws Exception
	{
		switch(Para1)
		{
		case "NavigateURL":
		{
			result=NavigateURL();
			if (result=="Pass")
			{
				AlFunResult.add("Pass");
			}else
				AlFunResult.add("Fail");	
			break;
		}
		case "SignIn":
		{
			result=SignIn(Para2,Para3,Para4);
			if (result=="Pass")
			{
				Reporter.log("SignIn Action is successfully performed");

				AlFunResult.add("Pass");
			}else
				AlFunResult.add("Fail");	
			break;
		}
		case "I_AgreeClick":
		{
			result=I_AgreeClick(Para5);
			if (result=="Pass")
			{
				AlFunResult.add("Pass");
			}else
				AlFunResult.add("Fail");	
			break;
		}
		case "ClickbyXpath":
		{
			result=ClickbyXpath(Para5);
			if (result=="Pass")
			{
				AlFunResult.add("Pass");
			}
			else
				AlFunResult.add("Fail");	
			break;
		}

		case "VerifyCount":
		{
			result=VerifyCount(Para3,Para4,Para5,Para6);
			if (result=="Pass")
			{
				AlFunResult.add("Pass");
			}else
				AlFunResult.add("Fail");	
			break;
		}
		case "VerifyDate":
		{
			result=VerifyDate(Para3,Para4,Para5);
			if (result=="Pass")
			{
				AlFunResult.add("Pass");
			}else
				AlFunResult.add("Fail");	
			break;
		}


		case "Verifyrecords":
		{
			result=Verifyrecords(Para2,Para3,Para4,Para5);
			if (result=="Pass")
			{
				AlFunResult.add("Pass");
			}else
				AlFunResult.add("Fail");	
			break;
		}
		case "SortColumn":
		{
			result=SortColumn(Para5);
			if (result=="Pass")
			{
				AlFunResult.add("Pass");
			}else
				AlFunResult.add("Fail");	
			break;
		}

		case "VerifyBlank":
		{
			result=VerifyBlank(Para3,Para4,Para5);
			if (result=="Pass")
			{
				AlFunResult.add("Pass");
			}else
				AlFunResult.add("Fail");	
			break;
		}

		case "VerifyBusinessDays":
		{
			result=VerifyBusinessDays(Para3,Para4,Para5,Para6);
			if (result=="Pass")
			{
				AlFunResult.add("Pass");
			}else
				AlFunResult.add("Fail");	
			break;
		}
		case "DoubleClick":
		{
			result=DoubleClick(Para3);
			if (result=="Pass")
			{
				AlFunResult.add("Pass");
			}else
				AlFunResult.add("Fail");	
			break;
		}

		case "VerifyRMlinkaccess":
		{
			result=VerifyRMlinkaccess(Para5,Para6);
			if (result=="Pass")
			{
				AlFunResult.add("Pass");
			}else
				AlFunResult.add("Fail");	
			break;
		}
		case "VerifyDashboard":
		{
			result=VerifyDashboard(Para4,Para5,Para6);
			if (result=="Pass")
			{
				AlFunResult.add("Pass");
			}else
				AlFunResult.add("Fail");	
			break;
		}
		}

		/*String destDir = "";
		String passfailMethod = Para1;
		//To capture screenshot.
		File scrFile = ((TakesScreenshot) BaseClass.driver).getScreenshotAs(OutputType.FILE);
		DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy__hh_mm_ssaa");
		//If status = fail then set folder name "screenshots/Failures"
		if (!result.equalsIgnoreCase("Pass"))

		{
			destDir = "screenshots/" +this.getClass().getSimpleName()+"/"+ Para2 + "/Failures";
		}
		//If status = pass then set folder name "screenshots/Success"
		else if (result.equalsIgnoreCase("Pass")){
			destDir = "screenshots/" +this.getClass().getSimpleName()+"/"+ Para2 +"/Success";
		}

		//To create folder to store screenshots
		new File(destDir).mkdirs();
		//Set file name with combination of test class name + date time.
		String destFile =  Para2 +" - " + passfailMethod+" - "+dateFormat.format(new Date()) + ".png";

		try {
			//Store file at destination folder location
			FileUtils.copyFile(scrFile, new File(destDir + "/" + destFile));
		}
		catch (IOException e) {
			ErrorUtil.addVerificationFailure(e);
			e.printStackTrace();
		}
		 */

	}

}





