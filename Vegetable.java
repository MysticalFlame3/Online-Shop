import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Vegetable implements ActionListener  {
	JButton button, btn;
	JFrame frame;
	JLabel label;
	JOptionPane optpne;
    public Vegetable(){
        frame = new JFrame("SuperMart");
        label = new JLabel("Welcome");
        button = new JButton("Sell");
        btn = new JButton("Shop");
        
        optpne = new JOptionPane();
        label.setBounds(230, 30, 130, 30);
        button.setBounds(180,60,160,60);
        btn.setBounds(180,140,160,60);
        frame.add(label);
        

        frame.setLayout(null);
        frame.setSize(500,500);
        button.setVisible(true);
        btn.setVisible(true);
        frame.add(button);
        frame.add(btn);
        button.addActionListener(this);
        btn.addActionListener(this);



        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        
        
        
        
    }
    
    public static void main(String args[]) {
        new Vegetable();

    }
    
    public void actionPerformed(ActionEvent ae) {
    	if(ae.getSource()==button){
    		frame.dispose();
    		Sell selling = new Sell();
    		
    		}
    	if (ae.getSource()==btn) {
    		frame.dispose();
    		VegetableShop shop = new VegetableShop();
    	}
    	}
    
}
