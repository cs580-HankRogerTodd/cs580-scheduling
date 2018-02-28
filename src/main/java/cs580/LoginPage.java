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
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.Font;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.FlowLayout;

public class LoginPage {
	
////// Database Setup ////////////////////////////////////////////////////////////////
	String uri = "mongodb://rhalf001:admin@580scheduledb-shard-00-00-w3srb.mongodb.net:27017,580scheduledb-shard-00-01-w3srb.mongodb.net:27017,580scheduledb-shard-00-02-w3srb.mongodb.net:27017/test?ssl=true&replicaSet=580scheduleDB-shard-0&authSource=admin";
	MongoClientURI clientUri = new MongoClientURI(uri);
	MongoClient mongoClient = new MongoClient(clientUri);
	MongoDatabase mongoDatabase = mongoClient.getDatabase("580Schedule");
	MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("Users");
	MongoCollection<Document> mongoCollectionRooms = mongoDatabase.getCollection("Rooms");
	MongoCollection<Document> mongoCollectionMeeting = mongoDatabase.getCollection("Meeting");
///////////////////////////////////////////////////////////////////////////////////////
	
	private JFrame frame;
	private JFrame frmLoginPage;
	private JTextField txtUsername;
	private JPasswordField txtPassword;
	private String password;
	private String username;
	
	private int MeetingDay;
	private int currentDay = LocalDate.now().getDayOfMonth();
	
	String currentDirectory = System.getProperty("user.dir");

	
	public LoginPage() {
		App.setFonts();
		initialize();
		frmLoginPage.setLocationRelativeTo(null);
		frmLoginPage.setVisible(true);
	}

	private void initialize() {
		
		String currentDirectory = System.getProperty("user.dir");
		
		frmLoginPage = new JFrame();
		//frmLoginPage.getContentPane().setBackground( Color.cyan );
		frmLoginPage.setTitle("Login Page");
		frmLoginPage.setBounds(200, 200, 425, 300);
		frmLoginPage.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmLoginPage.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Login");
		lblNewLabel.setForeground(Color.WHITE);
		lblNewLabel.setFont(new Font("Dialog", Font.BOLD, 30));
		lblNewLabel.setBounds(169, 21, 96, 37);
		frmLoginPage.getContentPane().add(lblNewLabel);
		
		JLabel lblUsername = new JLabel("User Name");
		lblUsername.setForeground(Color.WHITE);
		lblUsername.setFont(new Font("Dialog", Font.BOLD, 16));
		lblUsername.setBounds(97, 86, 88, 21);
		frmLoginPage.getContentPane().add(lblUsername);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setForeground(Color.WHITE);
		lblPassword.setFont(new Font("Dialog", Font.BOLD, 16));
		lblPassword.setBounds(97, 136, 88, 32);
		frmLoginPage.getContentPane().add(lblPassword);
		
		txtUsername = new JTextField();
		txtUsername.setBounds(197, 84, 130, 26);
		frmLoginPage.getContentPane().add(txtUsername);
		txtUsername.setColumns(10);
		
		txtPassword = new JPasswordField();
		txtPassword.setBounds(197, 140, 130, 26);
		frmLoginPage.getContentPane().add(txtPassword);
		
		JPanel panel = new JPanel();
		panel.setBounds(75, 197, 288, 45);
		panel.setBackground(new Color(0,0,0,0));
		frmLoginPage.getContentPane().add(panel);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));
		
////////Button ////////////////////////////////////////////////////////		
		JButton btnLogin = new JButton("Login");
		panel.add(btnLogin);
		frmLoginPage.getRootPane().setDefaultButton(btnLogin); // Make enter key press login by default
		
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				password = txtPassword.getText();
				username = txtUsername.getText();
				
				try
				{
					if(username.equals("Admin"))
					{
						if(password.equals("0000"))
						{
							frmLoginPage.dispose();
							AdminPage Admin = new AdminPage();
						}
					}
					else
					{
						Document myDoc = mongoCollection.find(Filters.eq("Name", username )).first();
						if(myDoc.get("Password").equals(password))
						{
							frmLoginPage.dispose();
							MeetingNotification();
							ExpireNotification();
							ProfilePage profile = new ProfilePage(username);
							
						}
						else
						{
							JOptionPane.showMessageDialog(null, "Login Error");
							txtPassword.setText(null);
							txtUsername.setText(null);
						}
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
		
		JLabel lblNewLabel_1 = new JLabel("New label");
		lblNewLabel_1.setIcon(new ImageIcon(currentDirectory+"/image/calendarB2.jpg"));
		lblNewLabel_1.setBounds(-29, 0, 459, 278);
		frmLoginPage.getContentPane().add(lblNewLabel_1);
		btnForgotPassword.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Pleace Contact Admin to Reset Your Password");
			}
		});
//////////////////////////////////
		
	}
	
	

	private void MeetingNotification()
	{
		Document myDoc = mongoCollection.find(Filters.eq("Name", username )).first();    //get member
		List<Document> MeetingLists = (List<Document>) myDoc.get("Meeting"); 					//get meeting list
		int MeetingListSize = MeetingLists.size(); 											//get meeting list size
		
		// Count meeting list
		for(int j=0; j<MeetingListSize; j++)
		{
			Document MeetingElement = MeetingLists.get(j);
			String StringRespond = MeetingElement.getString("Respond");
			String StringUpdate = MeetingElement.getString("Update");

			if(StringRespond.equals("P") || StringUpdate.equals("1")){
				JOptionPane.showMessageDialog(frame, "You have a Notification, Please go to Notification center!");
				break;
			}
		}
	}
	
	private void ExpireNotification()
	{
		Document myDoc = mongoCollection.find(Filters.eq("Name", username )).first();    //get member
		List<Document> MeetingLists = (List<Document>) myDoc.get("Meeting"); 					//get meeting list
		int MeetingListSize = MeetingLists.size(); 											//get meeting list size
		
		// Count meeting list
		for(int j=0; j<MeetingListSize; j++)
		{
			Document MeetingElement = MeetingLists.get(j);
			String StringMeetingID = String.valueOf(MeetingElement.get("MeetingID"));  
			Integer IntMeetingID = Integer.valueOf(StringMeetingID);
			
			Document myMeeting = mongoCollectionMeeting.find(Filters.eq("MeetingID", IntMeetingID )).first(); 
			
			if(myMeeting != null)
			{
				int Date = Integer.valueOf((String) myMeeting.get("Date"));
				MeetingDay = Date % 100;
				
				if(MeetingDay - currentDay >0 && MeetingDay - currentDay < 3){
					JOptionPane.showMessageDialog(frame, "You have a Meeting Expire in 3 days!");
					break;
				}
				if(MeetingDay - currentDay >0 && MeetingDay - currentDay < 2){
					JOptionPane.showMessageDialog(frame, "You have a Meeting Expire in 2 days!");
					break;
				}
				if(MeetingDay - currentDay >0 && MeetingDay - currentDay < 1){
					JOptionPane.showMessageDialog(frame, "You have a Meeting Expire in 1 days!");
					break;
				}
			}
		}
	}
	
	
}
