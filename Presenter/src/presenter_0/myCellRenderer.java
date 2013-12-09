package presenter_0;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.JTable;
import java.awt.Color;
import java.awt.Component;

public class myCellRenderer extends DefaultTableCellRenderer implements TableCellRenderer
{
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
	{
		setBackground(null);
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if(value!=null)
        {
        	setText(String.valueOf(value));
        }
        boolean interestingRow = row % 2 == 0;
        if(isSelected)
        {
        	setBackground(Color.GRAY);
        }else
        {
	        if (interestingRow) {
	            setBackground(Color.YELLOW);
	        } else { 
	        	setBackground(Color.WHITE);
	        }
        }
        return this;
	}
}
