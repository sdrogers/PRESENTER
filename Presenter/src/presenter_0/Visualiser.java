package presenter_0;
import java.util.ArrayList;
import java.io.*;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;

import java.util.Enumeration;
import java.util.Calendar;

import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JCheckBox;
public class Visualiser {
	private JFrame myFrame;
	private ParseCSV pc,clinician;
	private JPanel myPanel,buttonPanel,checkBoxPanel,PSSPanel;
	private DefaultTableModel tableModel;
	private JScrollPane myScrollPane;
	private JTable myTable;
	private JButton addButton,removeButton,selectAllButton,deselectAllButton;
	XTableColumnModel xTM;
	private int nStep = 5;
	private JCheckBox[] cbArray;
	private JLabel currentPSSLabel;
	private JTextField newPSSannotation;
	private JComboBox<String> newPSSCombo;
	private String[] headers = {"ChartTime","PSS","Revised PSS","FiO2","SpO2","Adrenaline","Noradrenaline","MAP","NIBPMean",
			"HR","Urine","Propofol","Alfentanil","Morphine","VentilatorMode","Airway","H","Lactate",
			"SVO2","Creatinine","CRP","WBC","PT","Platelets","Glucose","Troponin","Notes"};
	private String thisClinician,currentFile;
	private int notesCol,revisedPSScol;
	public Visualiser(String thisC) {
		for(int i=0;i<headers.length;i++) {
			if(headers[i].equals("Notes")) {
				notesCol = i;
			}
			if(headers[i].equals("Revised PSS")) {
				revisedPSScol = i;
			}
		}
		thisClinician = thisC;
		setupComponents();	
		loadFile();
	}
	private void loadFile() {
		currentFile = getNextFile(thisClinician);
		if(currentFile == null) {
			JOptionPane.showMessageDialog(myFrame, "Task file completed");
			System.exit(0);
		}
		myFrame.setTitle(currentFile);
		pc = new ParseCSV(currentFile);
		clinician = new ParseCSV(headers);
		tableModel.setNumRows(0);
		addRows();
	}
	private void setupComponents() {
		myFrame = new JFrame();
		myFrame.setSize(500,500);
		myPanel = new JPanel(new BorderLayout());
		myFrame.add(myPanel);
		
		//Create the table things
		//headers = pc.getHeaders();
		tableModel = new DefaultTableModel(null,headers);
		myTable = new JTable(tableModel);
		myScrollPane = new JScrollPane(myTable);
		myTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		myPanel.add(myScrollPane,BorderLayout.CENTER);

		//Set the special tablecolumnmodel that allows for adding and removing columns
		xTM = new XTableColumnModel();
		Enumeration<TableColumn> en = myTable.getColumnModel().getColumns();
		while(en.hasMoreElements()) {
			xTM.addColumn(en.nextElement());
		}
		myTable.setColumnModel(xTM);
		//Set the cell renderer
		en = myTable.getColumnModel().getColumns();
		while (en.hasMoreElements()) {
			TableColumn tc = en.nextElement();
			tc.setCellRenderer(new myCellRenderer());
		}

		//Create the add and remove buttons
		addButton = new JButton("Next...");
		removeButton = new JButton("Remove");
		buttonPanel = new JPanel(new GridLayout(0,2));
		buttonPanel.add(addButton);
		//buttonPanel.add(removeButton); No remove button
		currentPSSLabel = new JLabel("XXX");
		PSSPanel = new JPanel(new GridLayout(0,6));
		PSSPanel.add(new JLabel("Current PSS:"));
		PSSPanel.add(currentPSSLabel);
		String[] PSSChars = { "","A", "B", "C", "D", "E" };
		newPSSCombo = new JComboBox(PSSChars);
		PSSPanel.add(new JLabel("Choose new PSS:"));
		PSSPanel.add(newPSSCombo);
		newPSSannotation = new JTextField("");
		PSSPanel.add(new JLabel("Annotation (optional):"));
		PSSPanel.add(newPSSannotation);
		buttonPanel.add(PSSPanel);
		myPanel.add(buttonPanel,BorderLayout.SOUTH);
		
		//Create the checkboxpanel
		checkBoxPanel = new JPanel(new GridLayout(headers.length+1,1));
		myPanel.add(checkBoxPanel,BorderLayout.EAST);

		//Listener for the checkboxes
		final class MyCBListener implements ItemListener
		{
			public void itemStateChanged(ItemEvent e)
			{
				//Look through the checkboxes and see which is selected
				for(int i=0;i<headers.length-1;i++)
				{
					TableColumn tc = xTM.getColumnByModelIndex(i+1);
					xTM.setColumnVisible(tc,cbArray[i].isSelected());
				}
			}
		}
		//Listener for the buttons
		final class MyListener implements ActionListener
		{
			public void actionPerformed(ActionEvent e)
			{
				if(e.getSource() == addButton)
				{
					System.out.println("Add");
					//Check if this is the first
					if(tableModel.getRowCount()>0) {
						if(newPSSCombo.getSelectedIndex()==0) {
							// Haven't selected anything
							JOptionPane.showMessageDialog(myFrame, "You must enter a new PSS score before viewing more data");
							return;
						}
						if(newPSSannotation.getText().length()==0) {
							// Present a dialog asking for an (optional annotation)
							String s = (String)JOptionPane.showInputDialog(
				                    myFrame,
				                    "You haven't annotated your new PSS score. If you would like to, do so here, or press cancel to return",
				                    "Customized Dialog",
				                    JOptionPane.PLAIN_MESSAGE);
							if(s==null) {
								return;
							} else {
								newPSSannotation.setText(s);
							}
						}

						addRowToClinician();
					}
					if(addRows()==false) {
						System.out.println("Reached end of file");
						// Write out the clinician file
						String[] split = currentFile.split(File.separator);
						clinician.writeFile("." + File.separator + "outputfiles" + File.separator+thisClinician + "." + split[split.length-1]);
						// Update the task file
						int todoCount = updateTaskFile();
						Object[] options = {"Yes","No"};
						if(todoCount==0) {
							JOptionPane.showMessageDialog(myFrame, "All patient records examined");
							System.exit(0);
						} else {
							int s = JOptionPane.showOptionDialog(myFrame,"Would you like to do another patient?",null,JOptionPane.YES_NO_OPTION,
									JOptionPane.QUESTION_MESSAGE,null,options,options[0]);
							if(s==JOptionPane.OK_OPTION) {
								loadFile();
							} else {
								System.exit(0);
							}
						}
					}
				}
				else if(e.getSource() == removeButton)
				{
					System.out.println("Remove");
					removeRows();
				}
				if(e.getSource() == selectAllButton)
				{
					for(int i=0;i<cbArray.length;i++)
					{
						cbArray[i].setSelected(true);
					}
				}
				if(e.getSource() == deselectAllButton)
				{
					for(int i=0;i<cbArray.length;i++)
					{
						cbArray[i].setSelected(false);
					}
				}

			}
		}

		//Create the individual checkboxes
		cbArray = new JCheckBox[headers.length-1];
		MyCBListener cbListener = new MyCBListener();
		for(int i=0;i<headers.length-1;i++)
		{
			cbArray[i] = new JCheckBox(headers[i+1],true);
			cbArray[i].addItemListener(cbListener);
			checkBoxPanel.add(cbArray[i]);
		}
		
		//Add the shortcut buttons to the checkboxpanel
		selectAllButton = new JButton("Select all");
		checkBoxPanel.add(selectAllButton);
		deselectAllButton = new JButton("Remove all");
		checkBoxPanel.add(deselectAllButton);
		
		//Attach listeners to the buttons
		MyListener ml = new MyListener();
		addButton.addActionListener(ml);
		//removeButton.addActionListener(ml);
		selectAllButton.addActionListener(ml);
		deselectAllButton.addActionListener(ml);

		myFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		myFrame.setVisible(true);	
	}
	private int updateTaskFile() {
		int todoCount = 0;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(""+thisClinician+".tasks"));
			ArrayList<String> filelines = new ArrayList<String>();
			String temp;
			int pos = 0;
			while((temp = reader.readLine())!=null){
				filelines.add(temp);
			}
			reader.close();
			
			BufferedWriter writer = new BufferedWriter(new FileWriter(""+thisClinician+".tasks"));
			for(int i=0;i<filelines.size();i++) {
				String[] split = filelines.get(i).split("\t");
				if(split[0].equals(currentFile)) {
					writer.write(split[0]+"\t" + "Done\n"); 
				}else {
					writer.write(filelines.get(i) + "\n");
					if(split.length==1) {
						todoCount ++;
					}
				}
			}
			writer.close();
			
			
		}catch(IOException e) {
			System.out.println("Unable to open task file");
		}
		return todoCount;
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
	private void addRowToClinician() {
		int nRows = tableModel.getRowCount();
		Record last = clinician.getRecord(nRows-1);
		Calendar timeStamp = last.getTimeStamp();
		
//		ClinicianLabel l = new ClinicianLabel(last.getTimeStamp(),last.getPSS());
		String temp = (String)newPSSCombo.getSelectedItem();
		
		last.setValue("Revised PSS",temp);
		last.setValue("Notes",newPSSannotation.getText());
//		l.setRevisedPSS(temp.charAt(0));
//		l.setAnnotation(newPSSannotation.getText());
		
		tableModel.setValueAt(temp,tableModel.getRowCount()-1,revisedPSScol);
		tableModel.setValueAt(newPSSannotation.getText(),tableModel.getRowCount()-1,notesCol);
		
		//clinician.addRecord(last);
		System.out.println(last.getString(headers));
	}
	private void removeLastRow()
	{
		int nRows = tableModel.getRowCount();
		if(nRows>0)
		{
			tableModel.removeRow(nRows-1);
		}
	}
	private boolean addRows()
	{
		// adds nStep rows
		for(int i=0;i<nStep;i++)
		{
			// Need a check here to see if there are enough rows left to add
			// Question for DS: what happens when we get to the end?
			int nRows = tableModel.getRowCount();
			if(nRows<pc.getSize()){
				addNextRow();
			} else {
				return false;
			}
		}
		return true;
	}
	private void removeRows()
	{
		//Removes nStep rows
		for(int i=0;i<nStep;i++)
		{
			removeLastRow();
		}
	}
	private void addNextRow()
	{
		int nRows = tableModel.getRowCount();
		if(nRows<pc.getSize())
		{
			Record current = pc.getRecord(nRows);
			Object[] temp = new Object[headers.length];
			temp[0] = current.getDateTime();
			for(int j=1;j<headers.length;j++)
			{
				temp[j] = current.getValue(headers[j]);
			}
			tableModel.addRow(temp);
			// Extract the PSS information
			currentPSSLabel.setText((String)current.getValue("PSS"));
			newPSSCombo.setSelectedIndex(0);
			newPSSannotation.setText("");
			clinician.addRecord(current); //Add the row to the clinician object
		}
		myTable.scrollRectToVisible(myTable.getCellRect(nRows, 0, true));
	}
	private void clearTable()
	{
		tableModel.setRowCount(0);
	}
	private void addAllContent()
	{
		// Adds all of the content in the file parser to the table
		clearTable();
		for(int i=0;i<pc.getSize();i++)
		{
			addNextRow();
		}
	}
	
//	public static void main(String[] args)
//	{
//		Visualiser v = new Visualiser("Fred");
//	}
}
