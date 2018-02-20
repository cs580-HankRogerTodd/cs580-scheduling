package cs580;

import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;
import javax.swing.JTextPane;
import javax.swing.UIManager;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import javax.swing.JPanel;
import java.awt.GridLayout;
import java.awt.Font;

public class ProfilePage {

	private JFrame frame;
	private String LoginUsername;
	private boolean NewMeeting = false;

//////Database Setup /////////////////////////////////////////////////////////////////////////
	String uri = "mongodb://rhalf001:admin@580scheduledb-shard-00-00-w3srb.mongodb.net:27017,580scheduledb-shard-00-01-w3srb.mongodb.net:27017,580scheduledb-shard-00-02-w3srb.mongodb.net:27017/test?ssl=true&replicaSet=580scheduleDB-shard-0&authSource=admin";
	MongoClientURI clientUri = new MongoClientURI(uri);
	MongoClient mongoClient = new MongoClient(clientUri);
	MongoDatabase mongoDatabase = mongoClient.getDatabase("580Schedule");
	MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("Users");
	MongoCollection<Document> mongoCollectionRooms = mongoDatabase.getCollection("Rooms");
	MongoCollection<Document> mongoCollectionMeeting = mongoDatabase.getCollection("Meeting");
///////////////////////////////////////////////////////////////////////////////////////////////

	
	public ProfilePage(String username){
		LoginUsername = username;
		initialize();
		MeetingNotification();
		frame.setVisible(true);
	}


	private void initialize() {
		
		frame = new JFrame();
		frame.setBounds(100, 100, 500, 350);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setTitle("Home Page");
		
		String currentDirectory = System.getProperty("user.dir");
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(currentDirectory + "/image/"+ LoginUsername + ".jpg"));
		lblNewLabel.setBounds(80, 29, 102, 143);
		frame.getContentPane().add(lblNewLabel);
		
		JTextPane txtpnCaliforniaState = new JTextPane();
		txtpnCaliforniaState.setText("Hank, Tsou\nCal Poly Pomona\nMaster student\nComputer Science\n1992\n");
		txtpnCaliforniaState.setBounds(60, 184, 137, 109);
		frame.getContentPane().add(txtpnCaliforniaState);
		
		JPanel panel = new JPanel();
		panel.setBounds(331, 37, 131, 261);
		frame.getContentPane().add(panel);
		panel.setLayout(new GridLayout(5, 1, 0, 10));
		
//////// Button ////////////////////////////////////////////////////////
		JButton btnCalendar = new JButton("Calendar");
		btnCalendar.setFont(new Font("Tahoma", Font.BOLD, 11));
		panel.add(btnCalendar);
		btnCalendar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PersonalCalendar perCal = new PersonalCalendar(LoginUsername);
				frame.dispose();
			}
		});
//////////////////////////////////
		JButton btnMeeting = new JButton("My Meeting");
		btnMeeting.setFont(new Font("Tahoma", Font.BOLD, 11));
		panel.add(btnMeeting);
		btnMeeting.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Show Meeting List");
				//MyMeeting Mymet = new MyMeeting();
				//frame.dispose();
			}
		});
//////////////////////////////////		
		JButton btnNotification = new JButton("Notification Center");
		btnNotification.setFont(new Font("Tahoma", Font.BOLD, 11));
		panel.add(btnNotification);
		btnNotification.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				Document myStatus = mongoCollection.find(Filters.eq("Name", LoginUsername )).first();
				List<Document> meetingRes = (List<Document>) myStatus.get("Meeting");
				
				for(int i=0; i<meetingRes.size(); i++)
				{
					Document MeetingElement = meetingRes.get(i);
					String StringRespond = MeetingElement.getString("Respond");
					if(StringRespond.equals("P")){
						NewMeeting = true;
					}
				}
				
				if(NewMeeting == true){
					Notification Notice = new Notification(LoginUsername);
					frame.dispose();
				}
				
				else{
					JOptionPane.showMessageDialog(frame, "No New Meeting!");
				}
				
			}
		});
//////////////////////////////////
		JButton btnCreateMeeting = new JButton();
		btnCreateMeeting.setText("<html><center>Create Meeting</center></html>");
		btnCreateMeeting.setFont(new Font("Tahoma", Font.BOLD, 11));
		panel.add(btnCreateMeeting);
		btnCreateMeeting.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MemberSelection memslct = new MemberSelection(LoginUsername);
				frame.dispose();
			}
		});
//////////////////////////////////
		JButton btnLogout = new JButton("Logout");
		btnLogout.setFont(new Font("Tahoma", Font.BOLD, 11));
		panel.add(btnLogout);
		btnLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LoginPage login = new LoginPage();
				frame.dispose();
			}
		});
//////////////////////////////////	
	}
	
	
	private void MeetingNotification()
	{
		Document myDoc = mongoCollection.find(Filters.eq("Name", LoginUsername )).first();    //get member
		List<Document> MeetingLists = (List<Document>) myDoc.get("Meeting"); 					//get meeting list
		int MeetingListSize = MeetingLists.size(); 											//get meeting list size
		
		// Count meeting list
		for(int j=0; j<MeetingListSize; j++)
		{
			Document MeetingElement = MeetingLists.get(j);
			String StringRespond = MeetingElement.getString("Respond");

			if(StringRespond.equals("P")){
				JOptionPane.showMessageDialog(frame, "You have a New Meeting, Please go to Notification center!");
			}
		}
	}
	
	

}
