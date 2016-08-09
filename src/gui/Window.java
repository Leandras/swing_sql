package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import database.EstablishConnection;
import logic.CreateTable;

import javax.swing.JComboBox;

public class Window extends JFrame implements ActionListener, MouseListener {
	
	private JButton okButton, sendButton, establishConnectionButton, changeTable;
	private JTextField username;
	private JPasswordField password;
	private JPanel signIn, results, buttons;
	private DefaultComboBoxModel databaseTypes, tablesList;
	private JComboBox dataComboBox, tablesComboBox;
	private JOptionPane errorMessage;
	private List<String> errors;
	private JTable table;
	private JScrollPane scrollePane;
	
	private EstablishConnection connection;
	
	private ActionListener buttonPressed;
	
	public Window(){}
	
	public Window(int WIDTH, int HEIGHT){
		initWindow();
		setTitle("JDBC");
		setSize(WIDTH, HEIGHT);
		setLocationRelativeTo(null);
		setLayout(null);
		setVisible(true);
		setDefaultCloseOperation(this.DO_NOTHING_ON_CLOSE);
		setLocationRelativeTo(null);
		connection = null;
		errors = new LinkedList<String>();
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e){
				try {
					//Boolean serverStatus = connection.isAlive();
					if(connection != null && connection.isAlive()){
						connection.terminateConnection();
						dispose();
						System.exit(0);
					}else{
						dispose();
						System.exit(0);
					}
				} catch (SQLException e1) {
					dispose();
					System.exit(0);
				}
				dispose();
				System.exit(0);
			}
		});
		
	}
	
	private void initWindow(){
		okButton = new JButton("Ok");
		okButton.setSize(60, 20);
		okButton.setLocation(600, 30);
		
		establishConnectionButton = new JButton("Connect to Server");
		
		username = new JTextField("Leandras"); //debug miatt
		username.setSize(100, 20);
		username.setLocation(0, 0);
		username.addActionListener(this);
		username.addMouseListener(this);
		
		password = new JPasswordField("ow9pY7QR"); //debug miatt
		password.setSize(100, 20);
		password.setLocation(110, 0);
		password.addActionListener(this);
		password.addMouseListener(this);
		
		databaseTypes = new DefaultComboBoxModel();
		databaseTypes.addElement("MySQL");
		databaseTypes.addElement("Oracle");
		databaseTypes.addElement("MS SQL");
		dataComboBox = new JComboBox(databaseTypes);
		dataComboBox.setSelectedItem(0);
		dataComboBox.setSize(100, 20);
		dataComboBox.setLocation(220,  0);
		dataComboBox.addActionListener(this);
		
		sendButton = new JButton("Send");
		sendButton.setSize(80, 20);
		sendButton.setLocation(330, 0);
		sendButton.addActionListener(this);
		
		tablesList = new DefaultComboBoxModel();
		
	
		
		//this.add(okButton);
		this.add(sendButton);
		this.add(username);
		this.add(password);
		this.add(dataComboBox);
		

	}
	
	private void createTable(){
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String usernameInput = username.getText();
		char[] passwordInput = password.getPassword();
		String serverType = dataComboBox.getSelectedItem().toString();
		String convertedPassword = new String(passwordInput);
		String messages = "";
		errors.clear();
		if(usernameInput.equals("Username") || usernameInput.equals("")){
			errors.add("Nem adta meg a bejelentkezési nevet.");
		}
		if(convertedPassword.equals("Password") || convertedPassword.equals("")){
			errors.add("Nem adta mega  jelszót");
		}
		if(e.getActionCommand() == "Send"){
			messages = "";
			if(errors.size() == 0){
				connection = new EstablishConnection(usernameInput, convertedPassword, serverType, this);
				connection.setData();
				initTableComboBox();
			}else{
				for(int i = 0; i < errors.size(); ++i){
					messages += errors.get(i) + "\n";
				}
				message(messages);
			}
			if(table != null){
				scrollePane = new JScrollPane(table);
				scrollePane.setSize(500, 500);
				scrollePane.setLocation(0, 30);
				this.add(scrollePane);
				this.revalidate();
			}else{
				message("Tábla iniclaizálása sikertelen.");
			}
		}
		
		if(e.getActionCommand() == "Change table"){
			//connection.changeTable(tablesComboBox.getSelectedItem().toString());
			connection.setTable(tablesComboBox.getSelectedItem().toString());
			this.table.revalidate();
			this.table.repaint();
		}
	}
	
	public void setTable(JTable table){
		this.table = table;
	}
	
	public void initTableComboBox(){
		tablesComboBox = new JComboBox(tablesList);
		tablesComboBox.setSize(100, 20);
		tablesComboBox.setLocation(420, 0);
		tablesComboBox.setSelectedItem(0);
		this.add(tablesComboBox);
		
		changeTable = new JButton("Change table");
		changeTable.setSize(100, 20);
		changeTable.setLocation(530, 0);
		changeTable.addActionListener(this);
		this.add(changeTable);
		
		this.revalidate();
		this.repaint();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getComponent() instanceof JTextField){
			JTextField field = (JTextField)e.getComponent();
			field.setText("");
		}
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public void message(String msg){
		JOptionPane.showMessageDialog(this, msg);
	}
	
	public Window getWindow(){
		return this;
	}
	
	public void setTables(String table){
		tablesList.addElement(table);
	}
	
	public void resetTableList(){
		for(int i = 0; i < tablesList.getSize(); ++i){
			tablesList.removeElementAt(i);
		}
	}
}
