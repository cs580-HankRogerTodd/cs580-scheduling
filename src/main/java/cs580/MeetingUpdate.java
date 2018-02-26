package cs580;

import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JTextArea;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import javax.swing.JLabel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MeetingUpdate {

	private DefaultListModel<String> listModelInvitee = new DefaultListModel<String>();
	
	private JFrame frmMeetingUpdate;
	private String LoginUsername;
	private int MeetingID;
	private Document myMeeting;
	
	private JTextArea Datetxt;
	private JTextArea Timetxt;
	private JTextArea Roomtxt;
	private JTextArea Memtxt;
	
	private Boolean ExistMeeting = true;

	String uri = "mongodb://rhalf001:admin@580scheduledb-shard-00-00-w3srb.mongodb.net:27017,580scheduledb-shard-00-01-w3srb.mongodb.net:27017,580scheduledb-shard-00-02-w3srb.mongodb.net:27017/test?ssl=true&replicaSet=580scheduleDB-shard-0&authSource=admin";
	MongoClientURI clientUri = new MongoClientURI(uri);
	MongoClient mongoClient = new MongoClient(clientUri);
	MongoDatabase mongoDatabase = mongoClient.getDatabase("580Schedule");
	MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("Users");
	MongoCollection<Document> mongoCollectionRooms = mongoDatabase.getCollection("Rooms");
	MongoCollection<Document> mongoCollectionMeeting = mongoDatabase.getCollection("Meeting");

	
	public MeetingUpdate(String username, int meetingid) {
		LoginUsername = username;
		MeetingID = meetingid;
		initialize();
		setUpMeetingDetail();
		frmMeetingUpdate.setVisible(true);
	}


	private void initialize() {
		frmMeetingUpdate = new JFrame();
		frmMeetingUpdate.setTitle("Meeting Update");
		frmMeetingUpdate.setBounds(100, 100, 475, 337);
		frmMeetingUpdate.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmMeetingUpdate.getContentPane().setLayout(null);
		
		Datetxt = new JTextArea();
		Datetxt.setBounds(159, 31, 125, 32);
		frmMeetingUpdate.getContentPane().add(Datetxt);
		
		Timetxt = new JTextArea();
		Timetxt.setBounds(159, 75, 125, 32);
		frmMeetingUpdate.getContentPane().add(Timetxt);
		
		Roomtxt = new JTextArea();
		Roomtxt.setBounds(159, 123, 125, 32);
		frmMeetingUpdate.getContentPane().add(Roomtxt);
		
		Memtxt = new JTextArea();
		Memtxt.setBounds(159, 167, 125, 105);
		frmMeetingUpdate.getContentPane().add(Memtxt);
		
		JLabel lblDate = new JLabel("Date");
		lblDate.setBounds(86, 42, 61, 16);
		frmMeetingUpdate.getContentPane().add(lblDate);
		
		JLabel lblTime = new JLabel("Time");
		lblTime.setBounds(86, 85, 61, 16);
		frmMeetingUpdate.getContentPane().add(lblTime);
		
		JLabel lblRoom = new JLabel("Room");
		lblRoom.setBounds(86, 134, 61, 16);
		frmMeetingUpdate.getContentPane().add(lblRoom);
		
		JLabel lblMember = new JLabel("Member");
		lblMember.setBounds(86, 207, 61, 16);
		frmMeetingUpdate.getContentPane().add(lblMember);
		
		JButton btnDateEdit = new JButton("Edit");
		btnDateEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new ScheduleCalendar(listModelInvitee, LoginUsername, ExistMeeting, MeetingID);
				frmMeetingUpdate.dispose();
			}
		});
		btnDateEdit.setBounds(296, 75, 75, 32);
		frmMeetingUpdate.getContentPane().add(btnDateEdit);
		
		JButton btnAdd = new JButton("Add");
		btnAdd.setBounds(296, 202, 75, 29);
		frmMeetingUpdate.getContentPane().add(btnAdd);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new MyMeeting(LoginUsername);
			}
		});
		btnCancel.setBounds(358, 263, 86, 29);
		frmMeetingUpdate.getContentPane().add(btnCancel);
	}
	
	private void setUpMeetingDetail()
	{
		myMeeting = mongoCollectionMeeting.find(Filters.eq("MeetingID", MeetingID )).first();    //get member
		
		Datetxt.append(myMeeting.getString("Date"));
		Timetxt.append(myMeeting.getString("StartTime") + " - " + myMeeting.getString("EndTime") );
		Roomtxt.append(myMeeting.getString("Room"));
		
		ArrayList MemberLists = (ArrayList) myMeeting.get("Member");
		
		for(int i=0; i<MemberLists.size(); i++)
		{
			String StringMember = String.valueOf(MemberLists.get(i)); 
			Memtxt.append(StringMember+"\n");
			listModelInvitee.addElement(StringMember);
		}
		
		//System.out.print(listModelInvitee);
	}
}
