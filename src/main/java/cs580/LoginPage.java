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
	
////// Database Setup ////////////////////////////////////////////////////////////////
	String uri = "mongodb://rhalf001:admin@580scheduledb-shard-00-00-w3srb.mongodb.net:27017,580scheduledb-shard-00-01-w3srb.mongodb.net:27017,580scheduledb-shard-00-02-w3srb.mongodb.net:27017/test?ssl=true&replicaSet=580scheduleDB-shard-0&authSource=admin";
	MongoClientURI clientUri = new MongoClientURI(uri);
	MongoClient mongoClient = new MongoClient(clientUri);
	MongoDatabase mongoDatabase = mongoClient.getDatabase("580Schedule");
	MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("Users");
///////////////////////////////////////////////////////////////////////////////////////
	
	private JFrame frmLoginPage;
	private JTextField txtUsername;
	private JPasswordField txtPassword;

	
	public LoginPage() {
		App.setFonts();
		initialize();
		frmLoginPage.setVisible(true);
	}

	private void initialize() {
		
		frmLoginPage = new JFrame();
		frmLoginPage.setTitle("Login Page");
		frmLoginPage.setBounds(200, 200, 450, 300);
		frmLoginPage.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmLoginPage.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Login");
		lblNewLabel.setBounds(193, 35, 46, 26);
		frmLoginPage.getContentPane().add(lblNewLabel);
		
		JLabel lblUsername = new JLabel("User Name");
		lblUsername.setBounds(109, 89, 74, 16);
		frmLoginPage.getContentPane().add(lblUsername);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setBounds(109, 145, 61, 16);
		frmLoginPage.getContentPane().add(lblPassword);
		
		txtUsername = new JTextField();
		txtUsername.setBounds(193, 84, 130, 26);
		frmLoginPage.getContentPane().add(txtUsername);
		txtUsername.setColumns(10);
		
		txtPassword = new JPasswordField();
		txtPassword.setBounds(193, 140, 130, 26);
		frmLoginPage.getContentPane().add(txtPassword);
		
		JPanel panel = new JPanel();
		panel.setBounds(0, 207, 434, 41);
		frmLoginPage.getContentPane().add(panel);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));
		
////////Button ////////////////////////////////////////////////////////		
		JButton btnLogin = new JButton("Login");
		panel.add(btnLogin);
		frmLoginPage.getRootPane().setDefaultButton(btnLogin); // Make enter key press login by default
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String password = txtPassword.getText();
				String username = txtUsername.getText();
				
				try
				{
					Document myDoc = mongoCollection.find(Filters.eq("Name", username )).first();
					if(myDoc.get("Password").equals(password))
					{
						frmLoginPage.dispose();
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
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtUsername.setText(null);
				txtPassword.setText(null);
			}
		});
//////////////////////////////////		
		JButton btnForgotPassword = new JButton("Forgot");
		panel.add(btnForgotPassword);
		btnForgotPassword.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Pleace Contact Admin to Reset Your Password");
			}
		});
//////////////////////////////////
		
	}
	
	
}
