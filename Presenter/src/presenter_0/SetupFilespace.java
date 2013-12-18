package presenter_0;
import java.io.*;
import java.util.ArrayList;

public class SetupFilespace {
	
	public SetupFilespace() {
		ArrayList<String> clins = loadClinicians();
		// Get a list of patient files
		File[] fileList = new File("./patientfiles/").listFiles();
		for(int i=0;i<fileList.length;i++) {
			System.out.println(fileList[i].getPath());
		}
		
		for(int i=0;i<clins.size();i++) {
			String thisName = clins.get(i) + ".tasks";
			try {
				BufferedWriter writer = new BufferedWriter(new FileWriter(thisName));
				for(int j=0;j<fileList.length;j++) {
					writer.write(""+fileList[j].getPath() + "\n");
				}
				writer.close();
			}catch(IOException e) {
				System.out.println("Unable to open " + thisName);
			}
		}
		
	}
	
	private ArrayList<String> loadClinicians() {
		ArrayList<String> clins = new ArrayList<String>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader("clinicians.txt"));
			String temp;
			while((temp=reader.readLine())!=null) {
				String[] split = temp.split("\t");
				clins.add(split[0]);
			}
			reader.close();
		}catch(IOException e) {
			System.out.println("Unable to load clinician file");
			System.exit(0);
		}
		return clins;
	}
	
	public static void main(String[] args) {
		SetupFilespace s = new SetupFilespace();
	}
}
