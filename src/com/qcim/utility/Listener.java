package com.qcim.utility;

import java.util.Arrays;
import java.util.List;

import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.internal.Utils;

public class Listener implements ITestListener, IInvokedMethodListener {

	public void onFinish(ITestContext arg0) {
		Reporter.log("Completed executing test " + arg0.getName(), true);
	}

	public void onStart(ITestContext arg0) {
		Reporter.log("About to begin executing test " + arg0.getName(), true);
	}

	public void onTestFailedButWithinSuccessPercentage(ITestResult arg0) {
	}

	public void onTestFailure(ITestResult arg0) {
		printTestResults(arg0);
	}

	private void printTestResults(ITestResult result) {


		//Reporter.log("TestName = " + result.getTestName(), true);
		Reporter.log("Test Method resides in " + result.getTestClass().getName(), true);
		if (result.getParameters().length != 0) {
			String[] params1 = null;
			String params2 = null;
			String params3 = null;
			String params = null;
			String [] params4=null;
			for (Object parameter : result.getParameters()) {
				params +=  "," + parameter.toString() ;
			}
			params1=params.split(",");
			params2=params1[2];
			char[] ca = new char[params2.length()];
			Arrays.fill(ca, '*');
			params3 = new String(ca);
			params = params1[1].concat("&").concat(params3).concat("&").concat(params1[3]);

			Reporter.log("Test Method had the following parameters : " + params , true);
			params4=params.split("&");
			result.setParameters(params4);
		}
		String status = null;
		switch (result.getStatus()) {
		case ITestResult.SUCCESS:
			status = "Pass";
			break;
		case ITestResult.FAILURE:
			status = "Failed";
			break;
		case ITestResult.SKIP:
			status = "Skipped";
		}
		Reporter.log("Test Status: " + status, true);
	}

	public void onTestSkipped(ITestResult arg0) {
		printTestResults(arg0);
	}

	public void onTestStart(ITestResult arg0) {
	}

	public void onTestSuccess(ITestResult arg0) {
		printTestResults(arg0);
	}



	private String returnMethodName(ITestNGMethod method) {
		return method.getRealClass().getSimpleName() + "." + method.getMethodName();
	}




	public void afterInvocation(IInvokedMethod arg0, ITestResult arg1) {

		String textMsg = "Completed executing " + returnMethodName(arg0.getTestMethod());
		Reporter.log(textMsg, true);
		Reporter.setCurrentTestResult(arg1);

		if (arg0.isTestMethod()) {
			@SuppressWarnings("unchecked")
			List<Throwable> verificationFailures = ErrorUtil.getVerificationFailures();
			//if there are verification failures...
			if (verificationFailures.size() != 0) {
				//set the test to failed
				arg1.setStatus(ITestResult.FAILURE);

				//if there is an assertion failure add it to verificationFailures
				if (arg1.getThrowable() != null) {
					verificationFailures.add(arg1.getThrowable());
				}

				int size = verificationFailures.size();
				//if there's only one failure just set that
				if (size == 1) {
					arg1.setThrowable(verificationFailures.get(0));
				} else {
					//create a failure message with all failures and stack traces (except last failure)
					StringBuffer failureMessage = new StringBuffer("Multiple failures (").append(size).append("):>");
					for (int i = 0; i < size-1; i++) {
						failureMessage.append("Failure ").append(i+1).append(" of ").append(size).append(": -->>");
						Throwable t = verificationFailures.get(i);
						String fullStackTrace = Utils.stackTrace(t, false)[1];
						failureMessage.append(fullStackTrace).append("-->>");
					}

					//final failure
					Throwable last = verificationFailures.get(size-1);
					failureMessage.append("Failure ").append(size).append(" of ").append(size).append(": -->>");
					failureMessage.append(last.toString());

					//set merged throwable
					Throwable merged = new Throwable(failureMessage.toString());
					merged.setStackTrace(last.getStackTrace());

					arg1.setThrowable(merged);

				}
			}

		}

	}


	public void beforeInvocation(IInvokedMethod arg0, ITestResult arg1) {
		String textMsg = "About to begin executing " + returnMethodName(arg0.getTestMethod());
		Reporter.log(textMsg, true);
	}





}


