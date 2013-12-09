package presenter_0;
import java.util.ArrayList;
import java.io.*;
public class FileMerger {
	private ParseCSV pc;
	private String[] headers;
	private ArrayList<Record> recordList;
	public FileMerger(String fname)
	{
		pc = new ParseCSV(fname);
		headers = pc.getHeaders();
		
		String mergeName = "";
		String[] temp = fname.split("\\.");
		mergeName = temp[0] + ".merge." + temp[1];
		try
		{
			BufferedWriter outFile = new BufferedWriter(new FileWriter(mergeName));
			outFile.write(PresenterUtilities.arrayToCSV(headers)+"\n");
			String[] tempString = new String[headers.length];
			for(int i=0;i<headers.length;i++)
			{
				tempString[i] = "";
			}
			
			for(int i=0;i<pc.getSize();i++)
			{
				Record current = pc.getRecord(i);
				tempString[0] = current.getDateTime();
				for(int j=1;j<headers.length;j++)
				{
					Object tempObject = current.getValue(headers[j]);
					if(tempObject!=null)
					{
						tempString[j] = tempObject.toString(); // Update to the most recent value
					}
				}
				// Only write if it is an nice hour
				if(current.getMinute() == 0)
				{
					outFile.write(PresenterUtilities.arrayToCSV(tempString)+"\n");
					// Reset the string array
					for(int j=0;j<headers.length;j++)
					{
						tempString[j] = "";
					}
				}
			}
			outFile.close();
		}
		catch(IOException e)
		{
			System.out.println("Couldn't open outfile");
		}
	 
	}
	
	
	public static void main(String[] args)
	{
		FileMerger fm = new FileMerger("test.csv");
	}
}
