package cs580;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;

public class AddEmployee {

	private JFrame frame;
	private JTextField txtEployeeName;
	private JTextField txtUserName;
	private JTextField txtPassword;
	
	private JComboBox CBoxDepartment ;
	private JComboBox CBoxDegree;

	private String EmployeeName="";
	private String EmployeePassword="";
	private String EmployeeUserName="";
	private String BDay="";
	private String UniversityName="";
	private String DepartmentVal = "";
	private String DegreeVal = "";
	
	String[] Department={"Biological Sciences","Chemistry","Computer Science", "Geological Sciences", "Mathematics & Statistics", "Physics & Astronomy", "Health Promotion "};
	String[] Degree={"Bachelar Student", "Master Student", "PHD"};
			
	
	String uri = "mongodb://rhalf001:admin@580scheduledb-shard-00-00-w3srb.mongodb.net:27017,580scheduledb-shard-00-01-w3srb.mongodb.net:27017,580scheduledb-shard-00-02-w3srb.mongodb.net:27017/test?ssl=true&replicaSet=580scheduleDB-shard-0&authSource=admin";
	MongoClientURI clientUri = new MongoClientURI(uri);
	MongoClient mongoClient = new MongoClient(clientUri);
	MongoDatabase mongoDatabase = mongoClient.getDatabase("580Schedule");
	MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("Users");
	private JTextField textUniversity;
	private JTextField txtBDay;
	
	public AddEmployee() {
		initialize();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 320, 500);
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
				BDay = txtBDay.getText();
				UniversityName = textUniversity.getText();
				DepartmentVal = (String)CBoxDepartment.getSelectedItem();
				DegreeVal = (String)CBoxDegree.getSelectedItem();
				
				if(EmployeeUserName.equals("")||EmployeePassword.equals("")||EmployeeName.equals("")||
						BDay.equals("")||UniversityName.equals("")||DepartmentVal==null||DegreeVal==null)
				{
					JOptionPane.showMessageDialog(frame, "Please input all the information!");
				}
				else
				{
					Document myDoc = (Document)mongoCollection.find().sort(new BasicDBObject("EID",-1)).first();
					int EID = myDoc.getInteger("EID")+1;

					ArrayList< DBObject > array = new ArrayList< DBObject >();
					Document document = new Document("EID", EID);
					document.append("Name", EmployeeName);
					document.append("Availability", "Available");
					document.append("Username", EmployeeUserName);
					document.append("Password", EmployeePassword);
					
					document.append("University", UniversityName);
					document.append("Department", DepartmentVal);
					document.append("Degree", DegreeVal);
					document.append("Byear", BDay);
					document.append("Meeting", array);
					mongoCollection.insertOne(document);
					
					JOptionPane.showMessageDialog(frame, "SUCCESSFUL ADDED!");
					frame.dispose();
				}
				
				
			}
		});
		btnComfirm.setBounds(174, 406, 80, 29);
		frame.getContentPane().add(btnComfirm);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AdminPage mytest = new AdminPage();
				frame.dispose();
			}
		});
		btnCancel.setBounds(248, 406, 66, 29);
		frame.getContentPane().add(btnCancel);
		
		textUniversity = new JTextField();
		textUniversity.setText("Cal Poly Pomona");
		textUniversity.setBounds(123, 191, 130, 26);
		frame.getContentPane().add(textUniversity);
		textUniversity.setColumns(10);
		
		JLabel lblUniversity = new JLabel("University: ");
		lblUniversity.setBounds(40, 196, 71, 16);
		frame.getContentPane().add(lblUniversity);
		
		JLabel lblDepartment = new JLabel("Department:");
		lblDepartment.setBounds(31, 248, 80, 16);
		frame.getContentPane().add(lblDepartment);
		
		JLabel lblDegree = new JLabel("Degree:");
		lblDegree.setBounds(62, 293, 49, 16);
		frame.getContentPane().add(lblDegree);
		
		txtBDay = new JTextField();
		txtBDay.setBounds(123, 335, 130, 26);
		frame.getContentPane().add(txtBDay);
		txtBDay.setColumns(10);
		
		JLabel lblBday = new JLabel("B Day:");
		lblBday.setBounds(72, 340, 39, 16);
		frame.getContentPane().add(lblBday);
		
		CBoxDepartment = new JComboBox(Department);
		CBoxDepartment.setBounds(123, 244, 168, 27);
		CBoxDepartment.setSelectedItem(null);
		frame.getContentPane().add(CBoxDepartment);
		
		CBoxDegree = new JComboBox(Degree);
		CBoxDegree.setBounds(123, 289, 170, 27);
		CBoxDegree.setSelectedItem(null);
		frame.getContentPane().add(CBoxDegree);
		
		JLabel lblApplicationForm = new JLabel("APPLICATION FORM");
		lblApplicationForm.setBounds(109, 6, 130, 16);
		frame.getContentPane().add(lblApplicationForm);
	}
}
