package gui;

import java.util.LinkedList;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

public class CustomTableModel extends AbstractTableModel {

	private String[] colummNames;
	private Object[][] rowData;
	
	public CustomTableModel(String[] colummNames, Object[][] rowData){
		System.out.println("CustomModel inicilizálása");
		this.colummNames = new String[colummNames.length];
		for(int i = 0; i < colummNames.length; ++i){
			this.colummNames[i] = colummNames[i];
		}
		System.out.println("Átadott rowData mérete: " + rowData.length);
		for (int i = 0; i < rowData.length; i++) {
			System.out.println("Dolgozok");
			System.out.println("rowData inicializálása");
			this.rowData[i] = new Object[rowData[i].length];
			for (int j = 0; j < rowData[i].length; j++) {
				this.rowData[i][j] = rowData[i][j];
			}
		}
	}


	@Override
	public int getColumnCount(){
		if(colummNames.length > 0){
			return colummNames.length;
		}
		return 0;
	}
	
	public String getColummName(int col){
		return colummNames[col];
	}
	
	public void setValueAt(Object value, int col, int row){
		rowData[row][col] = value;
		fireTableCellUpdated(row, col);
	}
	
	@Override
	public int getRowCount() {
		/*if(rowData.length > 0){
			return rowData.length;
		}*/
		return 0;
	}
	

	@Override
	public Object getValueAt(int columm, int row) {
		return rowData[columm][row];
	}
	
	public boolean isCellEditable(int row, int col){
		return false;
	}

}
