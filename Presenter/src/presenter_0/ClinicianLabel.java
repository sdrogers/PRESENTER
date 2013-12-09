package presenter_0;

import java.util.Calendar;

public class ClinicianLabel {
	private Calendar timeStamp; // The ID
	private char originalPSS;
	private char revisedPSS;
	private String annotation;
	
	public ClinicianLabel(Calendar timeID,char oP) {
		timeStamp = timeID;
		originalPSS = oP;
		revisedPSS = oP;
		annotation = "";
	}
	public void setRevisedPSS(char a) {
		revisedPSS = a;
	}
	public void setAnnotation(String a) {
		annotation = a;
	}
	public String prettyString() {
		String temp = PresenterUtilities.prettyDate(timeStamp);
		temp += "," + originalPSS + "," + revisedPSS + "," + annotation;
		return temp;
	}
}
