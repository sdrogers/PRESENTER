package presenter_0;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.io.*;

public class ClinicianOutput {
	
	private ArrayList<ClinicianLabel> labels;
	private String metaData;
	private Calendar creationStamp;

	public ClinicianOutput() {
		labels = new ArrayList<ClinicianLabel>();
		creationStamp = new GregorianCalendar();
		metaData = "";
	}
	public void setMetaData(String a) {
		metaData = a;
	}
	public void addLabel(ClinicianLabel l) {
		labels.add(l);
	}
	public String getString(int i) {
		return labels.get(i).prettyString();
	}
	public int size() {
		return labels.size();
	}
	public String getMetaData() {
		return metaData;
	}
	public String getCreationStamp() {
		return PresenterUtilities.prettyDate(creationStamp);
	}
	public void writeFile(String fname) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(fname));
			for(int i=0;i<labels.size();i++) {
				writer.write(labels.get(i).prettyString() + "\n");
			}
			writer.close();
			
		} catch(IOException e) {
			System.out.println("Unable to open file");
		}
	}
	
}
