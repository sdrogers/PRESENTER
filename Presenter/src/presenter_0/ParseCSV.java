package presenter_0;
import java.io.*;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.ArrayList;
public class ParseCSV {
	private String fileName = null;
	private String[] headers = null;
	private String defHeadName = "head.csv";
	private ArrayList<Record> recordList = new ArrayList<Record>();
	private ArrayList<String> headList;
	public ParseCSV(String name)
	{
		fileName = name;
		readFile();
	}
	public ParseCSV(String[] headsin) {
		// Constructor when list isn't created from a CSV
		headers = headsin;
	}
	public void writeFile(String fName) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(fName));
			//Write the headers
			String line = "";
			for(int i=0;i<headers.length;i++) {
				line += headers[i];
				if(i<headers.length-1) {
					line += ",";
				}
			}
			writer.write(line + "\n");
			
			// Write the records
			for(int i=0;i<recordList.size();i++) {
				Record thisRecord = recordList.get(i);
				//line = thisRecord.getDateTime();
				line = "";
				for(int j=0;j<headers.length;j++) {
					String a = (String)thisRecord.getValue(headers[j]);
					line += ",";
					if(a!=null) {
						line += a;
					}
				}
				writer.write(line + "\n");
			}
			
			writer.close();
		}catch(IOException e) {
			System.out.println("Unable to open " + fName);
		}
	}
	public void addRecord(Record in) {
		recordList.add(in);
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
			headList = new ArrayList<String>(Arrays.asList(headers));
			while((line = in.readLine())!=null)
			{
				System.out.println(line);

				String[] items = line.split(",");
				Calendar thisDate = new GregorianCalendar();
				// Find the index of the 'time' entry
				int timeIndex = headList.indexOf("Time");
				PresenterUtilities.formatDate(thisDate,items[timeIndex]);
				recordList.add(new Record(thisDate));
				Record currentRecord = recordList.get(recordList.size()-1);
				//System.out.println("Items: " + items.length);
				for(int i=0;i<items.length;i++)
				{
					if(headers[i].equals("Time"))
					{
						//Nothing
					}
					else
					{
						if(items[i].length()>0)
						{
							currentRecord.setValue(headers[i],items[i]);
						}
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
