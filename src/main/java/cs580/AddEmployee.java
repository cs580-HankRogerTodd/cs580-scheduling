package cs580;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.bson.Document;

import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;

public class AddEmployee {

	private JFrame frame;
	private JTextField txtEployeeName;
	private JTextField txtUserName;
	private JTextField txtPassword;

	private String EmployeeName;
	private String EmployeePassword;
	private String EmployeeUserName;
	
	String uri = "mongodb://rhalf001:admin@580scheduledb-shard-00-00-w3srb.mongodb.net:27017,580scheduledb-shard-00-01-w3srb.mongodb.net:27017,580scheduledb-shard-00-02-w3srb.mongodb.net:27017/test?ssl=true&replicaSet=580scheduleDB-shard-0&authSource=admin";
	MongoClientURI clientUri = new MongoClientURI(uri);
	MongoClient mongoClient = new MongoClient(clientUri);
	MongoDatabase mongoDatabase = mongoClient.getDatabase("580Schedule");
	MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("Users");
	
	public AddEmployee() {
		initialize();
		frame.setVisible(true);
	}

	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 320, 260);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblName = new JLabel("Name: ");
		lblName.setBounds(72, 40, 49, 16);
		frame.getContentPane().add(lblName);
		
		JLabel lblUserName = new JLabel("User Name: ");
		lblUserName.setBounds(41, 90, 80, 16);
		frame.getContentPane().add(lblUserName);
		
		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setBounds(51, 141, 80, 16);
		frame.getContentPane().add(lblPassword);
		
		txtEployeeName = new JTextField();
		txtEployeeName.setBounds(123, 35, 130, 26);
		frame.getContentPane().add(txtEployeeName);
		txtEployeeName.setColumns(10);
		
		txtUserName = new JTextField();
		txtUserName.setBounds(123, 85, 130, 26);
		frame.getContentPane().add(txtUserName);
		txtUserName.setColumns(10);
		
		txtPassword = new JTextField();
		txtPassword.setBounds(123, 136, 130, 26);
		frame.getContentPane().add(txtPassword);
		txtPassword.setColumns(10);
		
		JButton btnComfirm = new JButton("Comfirm");
		btnComfirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				EmployeeUserName = txtUserName.getText();
				EmployeePassword = txtPassword.getText();
				EmployeeName = txtEployeeName.getText();
				
				long EmployeeNum = mongoCollection.count();
				String EID = String.valueOf(EmployeeNum+1);
				
				ArrayList< DBObject > array = new ArrayList< DBObject >();
				Document document = new Document("EID", EID);
				document.append("Name", EmployeeName);
				document.append("Availability", "Available");
				document.append("Username", EmployeeUserName);
				document.append("Password", EmployeePassword);
				document.append("Meeting", array);
				mongoCollection.insertOne(document);
				
				JOptionPane.showMessageDialog(frame, "SUCCESSFUL ADDED!");
				AdminPage mytest = new AdminPage();
				frame.dispose();
			}
		});
		btnComfirm.setBounds(173, 195, 80, 29);
		frame.getContentPane().add(btnComfirm);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setBounds(248, 195, 66, 29);
		frame.getContentPane().add(btnCancel);
	}
}
