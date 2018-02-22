package cs580;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import javax.swing.JLabel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MyMeeting {

	private JFrame frame;
	private DefaultListModel<String> AcceptMeetinglistModel;
	private DefaultListModel<String> MyMeetinglistModel;
	private String LoginUsername = "Hank"; 
	private JList MyMeetinglist;
	private JTextArea MeetingDetail;
	private JList AcceptMeetinglist;
	private int SelectAcceptMeeting = 0;
	
	String uri = "mongodb://rhalf001:admin@580scheduledb-shard-00-00-w3srb.mongodb.net:27017,580scheduledb-shard-00-01-w3srb.mongodb.net:27017,580scheduledb-shard-00-02-w3srb.mongodb.net:27017/test?ssl=true&replicaSet=580scheduleDB-shard-0&authSource=admin";
	MongoClientURI clientUri = new MongoClientURI(uri);
	MongoClient mongoClient = new MongoClient(clientUri);
	MongoDatabase mongoDatabase = mongoClient.getDatabase("580Schedule");
	MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("Users");
	MongoCollection<Document> mongoCollectionRooms = mongoDatabase.getCollection("Rooms");
	MongoCollection<Document> mongoCollectionMeeting = mongoDatabase.getCollection("Meeting");


	public MyMeeting() {
		initialize();
		setAcceptMeetinglist();
		setMyMeetinglist();
		frame.setVisible(true);
	}


	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 500, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		AcceptMeetinglistModel = new DefaultListModel();
		AcceptMeetinglist = new JList(AcceptMeetinglistModel);
		AcceptMeetinglist.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(AcceptMeetinglist.getSelectedValue() != null)
				{
					MeetingDetail.setText(null);
					SelectAcceptMeeting = Integer.valueOf((String) AcceptMeetinglist.getSelectedValue());
					
					Document myMeeting = mongoCollectionMeeting.find(Filters.eq("MeetingID", SelectAcceptMeeting )).first();
					MeetingDetail.append("Host: " + myMeeting.getString("Host") + "\n" +
										 "Room: " + myMeeting.getString("Room") + "\n" +
										 "Date: " + myMeeting.getString("Date") + "\n" +
										 "Start time: " + myMeeting.getString("StartTime") + "\n" +
										 "End Time: " + myMeeting.getString("EndTime")  
										);
				}
			}
		});
		AcceptMeetinglist.setBounds(23, 45, 135, 166);
		frame.getContentPane().add(AcceptMeetinglist);
		
		MyMeetinglistModel = new DefaultListModel();
		MyMeetinglist = new JList(MyMeetinglistModel);
		MyMeetinglist.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(MyMeetinglist.getSelectedValue() != null)
				{
					MeetingDetail.setText(null);
					int IntSelectMeeting = Integer.valueOf((String) MyMeetinglist.getSelectedValue());
					
					Document myMeeting = mongoCollectionMeeting.find(Filters.eq("MeetingID", IntSelectMeeting )).first();
					MeetingDetail.append("Host: " + myMeeting.getString("Host") + "\n" +
										 "Room: " + myMeeting.getString("Room") + "\n" +
										 "Date: " + myMeeting.getString("Date") + "\n" +
										 "Start time: " + myMeeting.getString("StartTime") + "\n" +
										 "End Time: " + myMeeting.getString("EndTime")  
										);
				}
			}
		});
		MyMeetinglist.setBounds(180, 45, 135, 166);
		frame.getContentPane().add(MyMeetinglist);
		
		MeetingDetail = new JTextArea();
		MeetingDetail.setBounds(338, 45, 135, 166);
		frame.getContentPane().add(MeetingDetail);
		
		JLabel lblAcceptMeeting = new JLabel("Accept Meeting");
		lblAcceptMeeting.setBounds(41, 17, 106, 16);
		frame.getContentPane().add(lblAcceptMeeting);
		
		JLabel lblMyMeeting = new JLabel("My Meeting");
		lblMyMeeting.setBounds(208, 17, 98, 16);
		frame.getContentPane().add(lblMyMeeting);
		
		JLabel lblMeetingDetail = new JLabel("Meeting Detail");
		lblMeetingDetail.setBounds(358, 17, 98, 16);
		frame.getContentPane().add(lblMeetingDetail);
		
		JButton btnDecline = new JButton("Decline");
		btnDecline.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(SelectAcceptMeeting == 0)
				{
					JOptionPane.showMessageDialog(frame, "Please select a meeting!");
				}
				else
				{
					mongoCollection.updateOne(  
			                new Document ("Name",LoginUsername),  
			                new Document( "$pull", new Document("Meeting" ,  
			                        new Document( "MeetingID", SelectAcceptMeeting))))  
			                .wasAcknowledged ();  
					
					BasicDBObject match = new BasicDBObject();
			        match.put( "Name", LoginUsername );

			        BasicDBObject addressSpec = new BasicDBObject();
			        addressSpec.put("MeetingID", SelectAcceptMeeting);
			        addressSpec.put("Respond", "D");

			        BasicDBObject update = new BasicDBObject();
			        update.put( "$push", new BasicDBObject( "Meeting", addressSpec ) );
			        mongoCollection.updateMany( match, update );
			        
			        int isSelected = AcceptMeetinglist.getSelectedIndex();
			        AcceptMeetinglistModel.remove(isSelected);
			        MeetingDetail.setText(null);
			        
					JOptionPane.showMessageDialog(frame, "Meeting DECLINE!");
				}
			}
		});
		btnDecline.setBounds(30, 223, 117, 29);
		frame.getContentPane().add(btnDecline);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setBounds(377, 243, 117, 29);
		frame.getContentPane().add(btnCancel);
		
		JButton btnUpdate = new JButton("Update");
		btnUpdate.setBounds(175, 223, 79, 29);
		frame.getContentPane().add(btnUpdate);
		
		JButton btnCancel_1 = new JButton("Cancel");
		btnCancel_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnCancel_1.setBounds(248, 223, 79, 29);
		frame.getContentPane().add(btnCancel_1);
	}
	
	private void setAcceptMeetinglist()
	{
		Document myDoc = mongoCollection.find(Filters.eq("Name", LoginUsername )).first();    //get member
		List<Document> MeetingLists = (List<Document>) myDoc.get("Meeting"); 					//get meeting list
		int MeetingListSize = MeetingLists.size(); 											//get meeting list size
		
		// Count meeting list
		for(int j=0; j<MeetingListSize; j++)
		{
			Document MeetingElement = MeetingLists.get(j);
			String StringMeetingID = String.valueOf(MeetingElement.get("MeetingID"));  

			if(MeetingElement.get("Respond").equals("A"))
			{
				AcceptMeetinglistModel.addElement(StringMeetingID);
			}
			
			Integer IntMeetingID = Integer.valueOf(StringMeetingID);
			Document myMeeting = mongoCollectionMeeting.find(Filters.eq("MeetingID", IntMeetingID )).first(); 
			
			if(myMeeting.getString("Host").equals(LoginUsername))
			{
				MyMeetinglistModel.addElement(StringMeetingID);
			}

		}
		
	}
	
	
	private void setMyMeetinglist()
	{
		
	}

}
