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
			Visualiser v = new Visualiser(thisClinician);
		}else {
			System.out.println("Verification failed");
			System.exit(0);
		}
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
