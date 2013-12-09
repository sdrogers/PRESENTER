package presenter_0;
import java.util.ArrayList;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
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
	private ParseCSV pc;
	private JPanel myPanel,buttonPanel,checkBoxPanel;
	private DefaultTableModel tableModel;
	private JScrollPane myScrollPane;
	private JTable myTable;
	private String[] headers;
	private JButton addButton,removeButton,selectAllButton,deselectAllButton;
	XTableColumnModel xTM;
	private int nStep = 5;
	private JCheckBox[] cbArray;
	private ClinicianOutput clinician;
	
	public Visualiser(String fname)
	{
		
		//Display file selector dialog
		if(fname == null)
		{
			final JFileChooser fc = new JFileChooser();
			int returnVal = fc.showOpenDialog(null);
			if(returnVal == JFileChooser.APPROVE_OPTION)
			{
				File f = fc.getSelectedFile();
				fname = f.getPath();
			}
			else
			{
				System.exit(0);
			}
		}
		
		pc = new ParseCSV(fname);
		clinician = new ClinicianOutput();
		
		
		myFrame = new JFrame();
		myFrame.setSize(1000,1000);
		myFrame.setTitle(fname);
		
		myPanel = new JPanel(new BorderLayout());
		myFrame.add(myPanel);
		
		//Create the table things
		headers = pc.getHeaders();
		tableModel = new DefaultTableModel(null,headers);
		myTable = new JTable(tableModel);
		myScrollPane = new JScrollPane(myTable);
		myTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		myPanel.add(myScrollPane,BorderLayout.CENTER);

		//Set the special tablecolumnmodel that allows for adding and removing columns
		xTM = new XTableColumnModel();
		Enumeration<TableColumn> en = myTable.getColumnModel().getColumns();
		while(en.hasMoreElements())
		{
			xTM.addColumn(en.nextElement());
		}
		myTable.setColumnModel(xTM);
		//Set the cell renderer
		en = myTable.getColumnModel().getColumns();
		while (en.hasMoreElements())
		{
			TableColumn tc = en.nextElement();
			tc.setCellRenderer(new myCellRenderer());
		}

		//Create the add and remove buttons
		addButton = new JButton("Add");
		removeButton = new JButton("Remove");
		buttonPanel = new JPanel(new GridLayout(0,2));
		buttonPanel.add(addButton);
		buttonPanel.add(removeButton);
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
						addRowToClinician();
					}
					addRows();
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
		removeButton.addActionListener(ml);
		selectAllButton.addActionListener(ml);
		deselectAllButton.addActionListener(ml);

		myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		myFrame.setVisible(true);	

	}
	private void addRowToClinician() {
		int nRows = tableModel.getRowCount();
		Record last = pc.getRecord(nRows-1);
		Calendar timeStamp = last.getTimeStamp();
		ClinicianLabel l = new ClinicianLabel(last.getTimeStamp(),last.getPSS());
		l.setRevisedPSS('B');
		l.setAnnotation("blah");
		clinician.addLabel(l);
		System.out.println("" + clinician.size() + ":" + clinician.getString(clinician.size()-1));
	}
	private void removeLastRow()
	{
		int nRows = tableModel.getRowCount();
		if(nRows>0)
		{
			tableModel.removeRow(nRows-1);
		}
	}
	private void addRows()
	{
		// adds nStep rows
		for(int i=0;i<nStep;i++)
		{
			addNextRow();
		}
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
	
	public static void main(String[] args)
	{
		Visualiser v = new Visualiser(null);
	}
}
