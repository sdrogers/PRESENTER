package presenter_0;
import java.io.*;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.ArrayList;
public class ParseCSV {
	private String fileName = null;
	private String[] headers = null;
	private String defHeadName = "head.csv";
	private ArrayList<Record> recordList = new ArrayList<Record>();
	public ParseCSV(String name)
	{
		fileName = name;
		readFile();
	}
	
	public ArrayList<Record> getRecordList()
	{
		return recordList;
	}
	public int getSize()
	{
		return recordList.size();
	}
	public Record getRecord(int recordNo)
	{
		return recordList.get(recordNo);
	}
	public String[] getHeaders()
	{
		return headers;
	}
	private void readFile()
	{
		try
		{
			BufferedReader in = new BufferedReader(new FileReader(fileName));
			System.out.println("File opened");
			String line = in.readLine(); // Read the headers
			headers = line.split(",");
			while((line = in.readLine())!=null)
			{
				//System.out.println(line);

				String[] items = line.split(",");
				Calendar thisDate = new GregorianCalendar();
				PresenterUtilities.formatDate(thisDate,items[0]);
				recordList.add(new Record(thisDate));
				Record currentRecord = recordList.get(recordList.size()-1);
				//System.out.println("Items: " + items.length);
				for(int i=1;i<items.length;i++)
				{
					if(items[i].length()>0)
					{
						currentRecord.setValue(headers[i],items[i]);
					}
				}
				//System.out.println(currentRecord.getTime());
				//System.out.println(currentRecord.getValue("FiO2"));
				//System.out.println(currentRecord.getValue("Creatinine"));
				//System.out.println(currentRecord.getValue("Platelets"));
			}
			in.close();
		}
		catch(IOException e)
		{
			System.err.println("File not found");
		}
	}
	private Object clean(String item)
	{
		//return item.split(" ")[0];
		return item;
	}
	public void writeHeaders(String headerName)
	{
		BufferedWriter out = null;
		try
		{
			out = new BufferedWriter(new FileWriter(headerName));
			for(int i=0;i<headers.length;i++)
			{
				out.write(headers[i]+"\n");
			}
			out.close();
		}
		catch(IOException e)
		{
			System.err.println("Couldn't write headers file");
		}
	}
	
	
//	public static void main(String[] args)
//	{
//		ParseCSV pc = new ParseCSV("test.csv");
//		//pc.writeHeaders("head.csv");
//	}
}
