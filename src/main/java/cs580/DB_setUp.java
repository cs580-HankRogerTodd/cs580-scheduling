package cs580;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.ImageIcon;

public class DB_setUp {

	private JFrame frame;

	
	public DB_setUp() {
		initialize();
		frame.setVisible(true);
		
		
		// five second is for database setup
		try {
			Thread.sleep(3000);
			LoginPage login = new LoginPage();
			frame.dispose();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	
	private void initialize() {
		String currentDirectory = System.getProperty("user.dir");
		
		frame = new JFrame();
		frame.setBounds(100, 100, 330, 150);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblProgram = new JLabel(" TH e SCHEDULing");
		lblProgram.setBounds(6, 31, 318, 64);
		lblProgram.setForeground(Color.WHITE);
		lblProgram.setFont(new Font("Dialog", Font.BOLD, 33));
		frame.getContentPane().add(lblProgram);
		
		JLabel lblNewLabel = new JLabel("New label");
		lblNewLabel.setIcon(new ImageIcon(currentDirectory+"/image/calendarB2.jpg"));
		lblNewLabel.setBounds(0, -130, 404, 258);
		frame.getContentPane().add(lblNewLabel);
		frame.setLocationRelativeTo(null);
		
		
		
	}

}
