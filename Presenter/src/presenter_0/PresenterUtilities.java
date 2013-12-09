package presenter_0;
import java.util.Calendar;


public class PresenterUtilities {
	public static void formatDate(Calendar formattedDate,String unformattedDate)
	{
		String[] date = unformattedDate.split(" ")[0].split("/");
		int year = Integer.parseInt(date[2]);
		int day = Integer.parseInt(date[0]);
		int month = Integer.parseInt(date[1]);
		String[] time = unformattedDate.split(" ")[1].split(":");
		int hours = Integer.parseInt(time[0]);
		int minutes = Integer.parseInt(time[1]);
		formattedDate.set(year,month,day,hours,minutes);
	}
	public static String arrayToCSV(String[] arrayIn)
	{
		String temp = arrayIn[0];
		for(int i=1;i<arrayIn.length;i++)
		{
			temp += "," + arrayIn[i];
		}
		return temp;
	}
	public static String prettyDate(Calendar timeStamp) {
		String temp = String.format("%02d/%02d/%04d",timeStamp.get(Calendar.DATE),timeStamp.get(Calendar.MONTH)+1,timeStamp.get(Calendar.YEAR));
		int hour = timeStamp.get(Calendar.HOUR);
		int minute = timeStamp.get(Calendar.MINUTE);
		if(timeStamp.get(Calendar.AM_PM)==Calendar.PM) {
			hour += 12;
		}
		temp += " " + String.format("%02d:%02d",hour,minute);
		return temp;
	}
}
