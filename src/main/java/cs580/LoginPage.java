package cs580;



import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.Font;
import javax.swing.JPanel;
import java.awt.FlowLayout;

public class LoginPage {

	public static final Font ARIAL_FONT = new Font("Arial", Font.PLAIN, 11);
	
////// Database Setup ////////////////////////////////////////////////////////////////
	String uri = "mongodb://rhalf001:admin@580scheduledb-shard-00-00-w3srb.mongodb.net:27017,580scheduledb-shard-00-01-w3srb.mongodb.net:27017,580scheduledb-shard-00-02-w3srb.mongodb.net:27017/test?ssl=true&replicaSet=580scheduleDB-shard-0&authSource=admin";
	MongoClientURI clientUri = new MongoClientURI(uri);
	MongoClient mongoClient = new MongoClient(clientUri);
	MongoDatabase mongoDatabase = mongoClient.getDatabase("580Schedule");
	MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("Users");
///////////////////////////////////////////////////////////////////////////////////////
	
	private JFrame frame;
	private JTextField txtUsername;
	private JPasswordField txtPassword;

	
	public LoginPage() {
		initialize();
		frame.setVisible(true);
	}

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
		
////////Button ////////////////////////////////////////////////////////		
		JButton btnLogin = new JButton("Login");
		panel.add(btnLogin);
		btnLogin.setFont(new Font("Arial", Font.BOLD, 11));
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String password = txtPassword.getText();
				String username = txtUsername.getText();
				
				try
				{
					Document myDoc = mongoCollection.find(Filters.eq("Name", username )).first();
					if(myDoc.get("Password").equals(password))
					{
						frame.dispose();
						ProfilePage profile = new ProfilePage(username);
						
					}
					else
					{
						JOptionPane.showMessageDialog(null, "Login Error");
						txtPassword.setText(null);
						txtUsername.setText(null);
					}
					
				}
				catch(Exception l) 
				{
					JOptionPane.showMessageDialog(null, "Login Error");
					txtPassword.setText(null);
					txtUsername.setText(null);
				}
				
			}
		});
//////////////////////////////////		
		JButton btnReset = new JButton("Reset");
		panel.add(btnReset);
		btnReset.setFont(new Font("Arial", Font.BOLD, 11));
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtUsername.setText(null);
				txtPassword.setText(null);
			}
		});
//////////////////////////////////		
		JButton btnForgotPassword = new JButton("Forgot");
		panel.add(btnForgotPassword);
		btnForgotPassword.setFont(new Font("Arial", Font.BOLD, 11));
		btnForgotPassword.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Pleace Contact Admin to Reset Your Password");
			}
		});
//////////////////////////////////
		
	}
	
	
}
