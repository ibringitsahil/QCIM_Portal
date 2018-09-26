package com.qcim.SuiteThree;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class GetPreviousBusinessDay {

    private static Map<String, String> holidays = null;
    private static GetPreviousBusinessDay myCalendar = null;
    private static final int WEEKEND_1 = Calendar.SATURDAY;
    private static final int WEEKEND_2 = Calendar.SUNDAY;

    private GetPreviousBusinessDay() {
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

        //holidays.putAll(DBUtils.readAnyDynamicHolidaysFromDB());
    }

    public static Date getPreviousWorkingDay(Date date) {

        Date previousWorkingDate = null;
        try {
            if (myCalendar == null) {
                myCalendar = new GetPreviousBusinessDay();
            }

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




public static void main(String[] args) throws ParseException {
	String d1="07/21/2015";
	 
	  

		DateFormat outputFormat = new SimpleDateFormat("MM/dd/yyyy");
		//DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");

		
		Date date2 = outputFormat.parse(d1);
		String outputText1 = outputFormat.format(date2);
		Date date3 = outputFormat.parse(outputText1);
		
        System.out.println(GetPreviousBusinessDay.getPreviousWorkingDay(date3)); //July 5, 2011 which returns July 1 as the working day because July 4th 2011 is Monday
}
}