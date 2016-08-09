package logic;

import java.util.ArrayList;
import java.util.LinkedList;
import javax.swing.JTable;
import javax.swing.SortingFocusTraversalPolicy;

import database.EstablishConnection;
import gui.CustomTableModel;
import gui.Window;

public class CreateTable {
	private LinkedList<String[]> resultList;
	private JTable table;
	private String[] collummNames;
	private Object[][] rowData;
	private Window window;
	private CustomTableModel tableModel;
	
	
	public CreateTable(Window window, ArrayList<String> collumArrayNames, EstablishConnection connection, LinkedList<String[]> resultList){
		this.window = window;
		//this.resultList = new LinkedList<String[]>();
		this.collummNames = new String[collumArrayNames.size()];
		for(int i = 0; i < collumArrayNames.size(); ++i){
			collummNames[i] = collumArrayNames.get(i);
			System.out.println(collummNames[i]);
		}
		System.out.println("ResultList mérete a createTable classban: " + resultList.size());
		rowData = new Object[resultList.size()][];
		for(int i = 0; i < resultList.size(); ++i){
			rowData[i] = new Object[resultList.get(i).length];
			for(int j = 0; j < resultList.get(i).length; ++j){
				rowData[i][j] = resultList.get(i)[j];
				System.out.println(rowData[i][j]);
			}
		}
		initTable();
	}
	
	private void initTable(){
		table = new JTable(new CustomTableModel(collummNames, rowData));
		window.setTable(table);
	}
	
	public JTable getTable(){
		return table;
	}
}
