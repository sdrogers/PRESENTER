package presenter_0;
import java.util.Calendar;
import java.util.ArrayList;
public class Record {
	private Calendar timeStamp;
	private ArrayList<String> annotations = new ArrayList<String>();
	private ArrayList<Object> values = new ArrayList<Object>(); 
	private ArrayList<String> valueID = new ArrayList<String>();
	
	public Record (Calendar d)
	{
		timeStamp = d;
	}
	
	
	public Calendar getTimeStamp()
	{
		return timeStamp;
	}
	private int getHour()
	{
		return timeStamp.get(Calendar.HOUR);
	}
	public char getPSS() {
		String temp = (String)getValue("PSS");
		return temp.charAt(0);
	}
	public int getMinute()
	{
		return timeStamp.get(Calendar.MINUTE);
	}
	
	public String getDateTime()
	{
		String temp = String.format("%02d/%02d/%04d",timeStamp.get(Calendar.DATE),timeStamp.get(Calendar.MONTH)+1,timeStamp.get(Calendar.YEAR));
		temp += " " + get24Time();
		return temp;
	}
	public String get24Time()
	{
		int hour = getHour();
		int minute = getMinute();
		if(timeStamp.get(Calendar.AM_PM)==Calendar.PM)
		{
			hour += 12;
		}
		return String.format("%02d:%02d",hour,minute);
	}
	public String getTime()
	{
		String temp = String.format("%02d:%02d",getHour(),getMinute());
		if(timeStamp.get(Calendar.AM_PM)==Calendar.AM)
		{
			temp += "am";
		}
		else
		{
			temp += "pm";
		}
		return temp;
	}
	public void setValue(String valID,Object val)
	{
		int a = valueID.indexOf(valID);
		if(a==-1)
		{
			values.add(val);
			valueID.add(valID);
		}
		else
		{
			values.set(a,val);
		}
	}
	public int getSize()
	{
		return values.size();
	}
	public Object getValue(String valID)
	{
		int a = valueID.indexOf(valID);
		if(a==-1)
		{
			return null;
		}
		else
		{		
			return values.get(a);
		}
	}
	public String getString(String[] heads) {
		String temp = getDateTime();
		for(int i=1;i<heads.length;i++) { //Assumes first entry is charttime
			String a = (String)getValue(heads[i]);
			temp += ",";
			if(a!=null) {
				temp += a;
			}
		}
		return temp;
	}
	
}
