package presenter_0;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
public class Presenter {
	private JComboBox clinicianChoice;
	public Presenter() {
		String[] clinLines = loadClinNames("clinicians.txt");
		String[] clinNames = new String[clinLines.length];
		String[] clinPasses = new String[clinLines.length];
		for(int i=0;i<clinLines.length;i++) {
			String[] temp = clinLines[i].split("\t");
			clinNames[i] = temp[0];
			clinPasses[i] = temp[1];
		}
		String thisClinician = (String)JOptionPane.showInputDialog(
                null,
                "Choose clinician:",
                "Customized Dialog",
                JOptionPane.PLAIN_MESSAGE,
                null,
                clinNames,
                clinNames[0]);
		if(thisClinician==null) {
			System.exit(0);
		}
		String s2 = (String)JOptionPane.showInputDialog(null,""+thisClinician+", please enter your password");
		if(s2==null) {
			System.exit(0);
		}
		boolean passed = false;
		for(int i=0;i<clinLines.length;i++) {
			if(clinNames[i].equals(thisClinician)) {
				if(clinPasses[i].equals(s2)) {
					passed = true;
				}
			}
		}
		if(passed) {
			System.out.println("Verification passed");
			String patientFile = getNextFile(thisClinician);
			Visualiser v = new Visualiser(patientFile,thisClinician);
			System.out.println(patientFile);
		}else {
			System.out.println("Verification failed");
			System.exit(0);
		}
	}
	private String getNextFile(String clinName) {
		String clinFileName = clinName + ".tasks";
		String nextFile = null;
		try{
			BufferedReader reader = new BufferedReader(new FileReader(clinFileName));
			String temp;
			while((temp = reader.readLine())!=null) {
				String[] split = temp.split("\t");
				if(split.length==1) {
					nextFile = split[0];					
					reader.close();
					return nextFile;
				}
			}
			reader.close();
		}catch(IOException e) {
			System.out.println(e);
			System.out.println("Unable to load "+clinFileName);
			System.exit(0);
		}
		return nextFile;
	}
	private String[] loadClinNames(String fName) {
		String[] a;
		ArrayList<String> clins = new ArrayList<String>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(fName));
			String temp;
			while((temp=reader.readLine())!=null) {
				clins.add(temp);
			}
			reader.close();
		}catch(IOException e) {
			System.out.println("Unable to load clinician list");
			System.exit(0);
		}
		return clins.toArray(new String[clins.size()]);
	}
	public static void main(String[] args) {
		Presenter p = new Presenter();
	}
}
