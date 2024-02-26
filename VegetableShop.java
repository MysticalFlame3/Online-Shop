import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class VegetableShop extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	private DefaultTableModel model;
    private JTextField txtVegetableId, txtQuantity;
    private JButton btnPurchase;
    private Connection connection;

    public VegetableShop() {
        setTitle("Vegetable Shop");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        model = new DefaultTableModel();
        model.addColumn("Vegetable ID");
        model.addColumn("Vegetable Name");
        model.addColumn("Price");
        model.addColumn("Available Quantity");

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(10, 10, 560, 200);
        add(scrollPane);

        JPanel inputPanel = new JPanel(new GridLayout(1, 6));
        inputPanel.add(new JLabel("Vegetable ID:"));
        txtVegetableId = new JTextField();
        inputPanel.add(txtVegetableId);
        inputPanel.add(new JLabel("Quantity:"));
        txtQuantity = new JTextField();
        inputPanel.add(txtQuantity);
        btnPurchase = new JButton("Purchase");
        btnPurchase.addActionListener(this);
        inputPanel.add(btnPurchase);
        add(inputPanel);

        setVisible(true);


        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/vegetable", "root", "0523");
            loadTableData(); 
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error connecting to the database: " + e.getMessage());
            System.exit(1);
        }

        setVisible(true);
    }

    private void loadTableData() {
        try {
            model.setRowCount(0); 
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM sell");
            while (resultSet.next()) {
                int vegetableId = resultSet.getInt("vegetable_id");
                String vegetableName = resultSet.getString("Vegetable_name");
                int price = resultSet.getInt("price");
                int quantity = resultSet.getInt("quantity");
                model.addRow(new Object[]{vegetableId, vegetableName, price, quantity});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading table data: " + e.getMessage());
        }
    }

    private void updateTableData(int vegetableId, int purchasedQuantity) {
        try {
            PreparedStatement updateStatement = connection.prepareStatement(
                    "UPDATE sell SET quantity = quantity - ? WHERE vegetable_id = ?");
            updateStatement.setInt(1, purchasedQuantity);
            updateStatement.setInt(2, vegetableId);
            updateStatement.executeUpdate();

            loadTableData(); 
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error updating table data: " + e.getMessage());
        }
    }

    private void purchaseVegetable() {
        try {
            int vegetableId = Integer.parseInt(txtVegetableId.getText());
            int purchasedQuantity = Integer.parseInt(txtQuantity.getText());
            int availableQuantity = getAvailableQuantity(vegetableId);
            if (purchasedQuantity > availableQuantity) {
                JOptionPane.showMessageDialog(null, "Not enough quantity available for purchase.");
                return;
            }
            int price = getPrice(vegetableId);
            int totalBill = price * purchasedQuantity;

            JOptionPane.showMessageDialog(null, "Purchase Successful! Total Bill: " + totalBill);

            updateTableData(vegetableId, purchasedQuantity);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Please enter valid numeric values for Vegetable ID and Quantity.");
        }
    }

    private int getAvailableQuantity(int vegetableId) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT quantity FROM sell WHERE vegetable_id = ?");
            statement.setInt(1, vegetableId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("quantity");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private int getPrice(int vegetableId) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT price FROM sell WHERE vegetable_id = ?");
            statement.setInt(1, vegetableId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("price");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    	if (e.getSource() == btnPurchase) {
            purchaseVegetable();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VegetableShop());
    }
    }
