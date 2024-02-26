import javax.swing.*;

import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class Sell implements ActionListener {
    JFrame frame;
    JLabel label, name, price, qty;
    static JTextField txtfld;
	static JTextField fldnm;
	static JTextField txtpr;
	static JTextField txtqty;
    JButton  update,del, add;
	JTable table; 
	DefaultTableModel model;
    public Sell(){
        frame = new JFrame();
        frame.setSize(500,500);
        label = new JLabel("Enter Vegetable ID");
        label.setBounds(130,90,120,120);
        txtfld = new JTextField("");
        txtfld.setBounds(240,130,30,30);
        name = new JLabel("Enter Vegetable Name");
        name.setBounds(130,100,160,160);
        fldnm = new JTextField();
        fldnm.setBounds(260, 170, 90, 30);
        price = new JLabel("Price");
        price.setBounds(130,200,30,30);
        txtpr = new JTextField();
        txtpr.setBounds(240,200,30,30);
        qty = new JLabel("Quantity");
        qty.setBounds(130,220,120,60);
        txtqty = new JTextField();
        txtqty.setBounds(260,230,60,30);
        
        update = new JButton("Update");
        update.setBounds(130,260,90,30);
        update.addActionListener(this);

        del = new JButton("Delete");
        del.setBounds(230,260,90,30);
        del.addActionListener(this);
        
        add = new JButton("Add");
        add.setBounds(330,260,90,30);
        add.addActionListener(this);




        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(label);
        frame.add(add);
        frame.add(del);
        frame.add(update);
        frame.add(txtfld);
        frame.add(fldnm);
        frame.add(name);
        frame.add(price);
        frame.add(txtpr);
        frame.add(txtqty);
        frame.add(qty);
        frame.setLayout(null);

        frame.setVisible(true);
        
        model = new DefaultTableModel();
        model.addColumn("Vegetable id");
        model.addColumn("Vegetable Name");
        model.addColumn("Price");
        model.addColumn("Quantity");

        table = new JTable(model);
        table.setAutoCreateRowSorter(true);
        table.setRowHeight(30);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(10, 300, 480, 150);
        frame.add(scrollPane);




    }


    public void actionPerformed(ActionEvent ae) {
        if(ae.getSource() == update){
        	updateRow();       }
        if(ae.getSource() == del){
        	deleteRow();        }
        if(ae.getSource() == add){
        	addRow();        }
    }
    
    private void updateRow() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            model.setValueAt(txtfld.getText(), selectedRow, 0);
            model.setValueAt(fldnm.getText(), selectedRow, 1);
            model.setValueAt(txtpr.getText(), selectedRow, 2);
            model.setValueAt(txtqty.getText(), selectedRow, 3);
        } else {
            JOptionPane.showMessageDialog(frame, "Select a row to update.");
        }
    }

    private void deleteRow() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            model.removeRow(selectedRow);
        } else {
            JOptionPane.showMessageDialog(frame, "Select a row to delete.");
        }
    }

    private void addRow() {
        String[] rowData = {txtfld.getText(), fldnm.getText(), txtpr.getText(), txtqty.getText()};
        model.addRow(rowData);
    }
    
    
    

    public static void main(String[] args) throws SQLException {
            new Sell();
            String url = "jdbc:mysql://localhost:3306/vegetable";
            String user = "root";
            String pass = "0523";
            
            try {
				Class.forName("com.mysql.jdbc.Driver");
				
				Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/vegetable","root","0523");
				
				Statement statement = con.createStatement();
				
				createselltable(statement);
				add();
				
				
				} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
    }
    public static void createselltable(Statement statement) throws SQLException{
    	String createTableQuery="create table if not exists sell("+
    			"vegetable_id int ,"+
    			"Vegetable_name varchar(30) not null,"+
    			"price int not null,"+
    			"quantity int)";
    	
    	statement.execute(createTableQuery);
    	
    }
    private static void add() {
        try {
            String sql = "INSERT INTO sell (Vegetable_id, vegetable_name, quantity, price) values(?, ?, ?, ?)";
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/vegetable", "root", "0523");

            

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                // Parse validated values
                statement.setString(1, txtfld.getText());
                statement.setInt(2, Integer.parseInt(fldnm.getText()));
                statement.setInt(3, Integer.parseInt(txtpr.getText()));
                statement.setInt(4, Integer.parseInt(txtqty.getText()));
                statement.execute();
            }

            

        } catch (SQLException | NumberFormatException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error adding record: " + ex.getMessage());
        }
    }
}
