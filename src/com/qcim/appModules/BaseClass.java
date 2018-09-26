package com.qcim.appModules;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.xml.DOMConfigurator;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.asserts.SoftAssert;

import com.qcim.utility.ErrorUtil;
import com.qcim.utility.ExcelUtils;
import com.qcim.utility.Log;


public class BaseClass {

	public static Properties Param = null;
	public static Properties Object = null;
	public static ExcelUtils TestCaseListExcelOne=null;
	public static ExcelUtils TestDataListExcelTwo=null;
	public static WebDriver driver = null;
	public static WebDriver ExistingchromeBrowser;
	public static WebDriver ExistingmozillaBrowser;
	public static WebDriver ExistingIEBrowser;
	public static SoftAssert s_assert =null;
	protected static Map<String, String> holidays = null;

	public void init() throws IOException{

		DOMConfigurator.configure("log4j.xml");
		//Initiated soft assert
		s_assert = new SoftAssert();

		//Initializing Test Data1(TestData1.xlsx) File Path Using Constructor Of ExcelUtils Utility Class.
		TestDataListExcelTwo = new ExcelUtils(System.getProperty("user.dir")+"//src//com//qcim//TestData//TestData1.xlsx");
		///Bellow given syntax will Insert log In applog.log file.
		Log.info("All Excel Files Initialized successfully.");


		//Initializing Test Data(TestData.xlsx) File Path Using Constructor Of ExcelUtils Utility Class.
		TestCaseListExcelOne = new ExcelUtils(System.getProperty("user.dir")+"//src//com//qcim//TestData//TestData.xlsx");
		///Bellow given syntax will Insert log In applog.log file.
		Log.info("All Excel Files Initialized successfully.");


		//Initialize Param.properties file.
		Param = new Properties();
		FileInputStream fip = new FileInputStream(System.getProperty("user.dir")+"//src//com//qcim//properties//Param.properties");
		Param.load(fip);
		Log.info("Param.properties file loaded successfully.");		


		//Initialize Objects.properties file.
		Object = new Properties();
		fip = new FileInputStream(System.getProperty("user.dir")+"//src//com//qcim//properties//Object.properties");
		Object.load(fip);
		Log.info("Objects.properties file loaded successfully.");


		//Created Holidays Hash map
		holidays = new HashMap<String, String>();
		holidays.put("1,1","New Year's day");
		holidays.put("1,19","Martin Luther King Day");
		holidays.put("2,16","Washington's B'Day");
		holidays.put("4,3","Good Friday");
		holidays.put("5,25","Memorial Day");
		holidays.put("7,4","Independence Day");
		holidays.put("9,7","Labor Day");
		holidays.put("11,26"," Thanksgiving Day");
		holidays.put("7,20", "Christmas");

	}

	public static void loadWebBrowser() 
	{
		try
		{

			//Check If any previous webdriver browser Instance Is exist then run new test In that existing webdriver browser Instance.
			if(Param.getProperty("testBrowser").equalsIgnoreCase("Mozilla") && ExistingmozillaBrowser!=null){
				driver = ExistingmozillaBrowser;
				return;
			}else if(Param.getProperty("testBrowser").equalsIgnoreCase("chrome") && ExistingchromeBrowser!=null){
				driver = ExistingchromeBrowser;
				return;
			}else if(Param.getProperty("testBrowser").equalsIgnoreCase("IE") && ExistingIEBrowser!=null){
				driver = ExistingIEBrowser;
				return;
			}		

			if(Param.getProperty("testBrowser").equalsIgnoreCase("Mozilla")){
				//To Load Firefox driver Instance. 
				//ProfilesIni profile = new ProfilesIni();
				//FirefoxProfile myprofile = profile.getProfile("ProfileQA");
				//driver = new FirefoxDriver(myprofile);
				driver = new FirefoxDriver();
				ExistingmozillaBrowser=driver;
				Log.info("Firefox Driver Instance loaded successfully.");

			}else if(Param.getProperty("testBrowser").equalsIgnoreCase("Chrome")){
				//To Load Chrome driver Instance.
				System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+"//BrowserDrivers//chromedriver.exe");
				driver = new ChromeDriver();
				ExistingchromeBrowser=driver;
				Log.info("Chrome Driver Instance loaded successfully.");

			}else if(Param.getProperty("testBrowser").equalsIgnoreCase("IE")){
				//To Load IE driver Instance.
				//System.setProperty("webdriver.ie.driver", System.getProperty("user.dir")+"//BrowserDrivers//IEDriverServer.exe");
				System.setProperty("webdriver.ie.driver", "C://TestFramwork//QCIMFramework//BrowserDrivers//IEDriverServer.exe");
				driver = new InternetExplorerDriver();
				ExistingIEBrowser=driver;
				Log.info("IE Driver Instance loaded successfully.");

			}	
			else
				driver.manage().deleteAllCookies();
			System.out.println("Cookies deleted...");
			Log.info("Cookies deleted...");
			driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
			driver.manage().window().maximize();	
			//return "Pass";
		}catch(Exception e)
		{
			ErrorUtil.addVerificationFailure(e);            //??
			Log.info(e.getMessage());          //Learn Log
			//return "Fail";
		}

	}


	public static String closeWebBrowser(){
		try
		{

			driver.close();
			//null browser Instance when close.
			ExistingchromeBrowser=null;
			ExistingmozillaBrowser=null;
			ExistingIEBrowser=null;
			Log.info("Browser closed successfully");
			return "Pass";
		}
		catch(Exception e){
			Log.error("Not able to close the browser" + e.getMessage());;
			return "Fail";
		}
	}

	public void waitForElement(WebElement element){

		WebDriverWait wait = new WebDriverWait(driver,40);
		wait.until(ExpectedConditions.visibilityOf(element));
	}

}





