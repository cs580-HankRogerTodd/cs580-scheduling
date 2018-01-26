package cs580;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JSeparator;
import java.awt.Font;
import javax.swing.JPanel;
import java.awt.GridLayout;
import java.awt.FlowLayout;

public class LoginPage {

	public static final Font ARIAL_FONT = new Font("Arial", Font.PLAIN, 11);
	
	private JFrame frame;
	private JTextField txtUsername;
	private JPasswordField txtPassword;

	/**
	 * Launch the application.
	 
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginPage window = new LoginPage();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	*/
	/**
	 * Create the application.
	 */
	public LoginPage() {
		initialize();
		frame.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(200, 200, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Login");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel.setBounds(193, 35, 46, 26);
		frame.getContentPane().add(lblNewLabel);
		
		JLabel lblUsername = new JLabel("User Name");
		lblUsername.setFont(new Font("Arial", Font.PLAIN, 11));
		lblUsername.setBounds(109, 89, 74, 16);
		frame.getContentPane().add(lblUsername);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setFont(new Font("Arial", Font.PLAIN, 11));
		lblPassword.setBounds(109, 145, 61, 16);
		frame.getContentPane().add(lblPassword);
		
		txtUsername = new JTextField();
		txtUsername.setBounds(193, 84, 130, 26);
		frame.getContentPane().add(txtUsername);
		txtUsername.setColumns(10);
		
		txtPassword = new JPasswordField();
		txtPassword.setBounds(193, 140, 130, 26);
		frame.getContentPane().add(txtPassword);
		
		JPanel panel = new JPanel();
		panel.setBounds(0, 207, 434, 30);
		frame.getContentPane().add(panel);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));
		
		JButton btnLogin = new JButton("Login");
		panel.add(btnLogin);
		btnLogin.setFont(new Font("Arial", Font.BOLD, 11));
		
		JButton btnReset = new JButton("Reset");
		panel.add(btnReset);
		btnReset.setFont(new Font("Arial", Font.BOLD, 11));
		
		JButton btnForgotPassword = new JButton("Forgot");
		panel.add(btnForgotPassword);
		btnForgotPassword.setFont(new Font("Arial", Font.BOLD, 11));
		btnForgotPassword.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtUsername.setText(null);
				txtPassword.setText(null);
			}
		});
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String password = txtPassword.getText();
				String username = txtUsername.getText();
				
				if(username.contains("hank") && password.contains("1234")) {
					//txtPassword.setText(null);
					//txtUsername.setText(null);
					ProfilePage profile = new ProfilePage();
					frame.dispose();
				}
				else {
					JOptionPane.showMessageDialog(null, "Login Error");
					txtPassword.setText(null);
					txtUsername.setText(null);
				}
			}
		});
	}
}
