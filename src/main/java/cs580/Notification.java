package cs580;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.DefaultListModel;
import javax.swing.JButton;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import javax.swing.JLabel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Notification {

	private JFrame frame;
	private JList Meetinglist ;
	private DefaultListModel<String> MeetinglistModel;
	private String userName; 
	private int IntSelectMeeting = 0;

/////Database Setup ////////////////////////////////////////////////////////////////	
	String uri = "mongodb://rhalf001:admin@580scheduledb-shard-00-00-w3srb.mongodb.net:27017,580scheduledb-shard-00-01-w3srb.mongodb.net:27017,580scheduledb-shard-00-02-w3srb.mongodb.net:27017/test?ssl=true&replicaSet=580scheduleDB-shard-0&authSource=admin";
	MongoClientURI clientUri = new MongoClientURI(uri);
	MongoClient mongoClient = new MongoClient(clientUri);
	MongoDatabase mongoDatabase = mongoClient.getDatabase("580Schedule");
	MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("Users");
	MongoCollection<Document> mongoCollectionRooms = mongoDatabase.getCollection("Rooms");
	MongoCollection<Document> mongoCollectionMeeting = mongoDatabase.getCollection("Meeting");
///////////////////////////////////////////////////////////////////////////////////////

	public Notification(String username) {
		userName = username;
		initialize();
		showPendingMeeting();
		frame.setVisible(true);
	}

	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		final JTextArea MeetingDetail = new JTextArea();
		MeetingDetail.setBounds(251, 46, 161, 149);
		frame.getContentPane().add(MeetingDetail);

/// Meeting list table //////////////////////////////////////////////////////// 
		MeetinglistModel = new DefaultListModel();
		Meetinglist = new JList(MeetinglistModel);
		Meetinglist.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				if(Meetinglist.getSelectedValue() != null)
				{
					MeetingDetail.setText(null);
					IntSelectMeeting = Integer.valueOf((String) Meetinglist.getSelectedValue());
					
					
					Document myMeeting = mongoCollectionMeeting.find(Filters.eq("MeetingID", IntSelectMeeting )).first();
					System.out.print(myMeeting);
					
					MeetingDetail.append("Host: " + myMeeting.getString("Host") + "\n" +
										 "Room: " + myMeeting.getString("Room") + "\n" +
										 "Date: " + myMeeting.getString("Date") + "\n" +
										 "Start time: " + myMeeting.getString("StartTime") + "\n" +
										 "End Time: " + myMeeting.getString("EndTime")  
										);
				}
			}
		});
		Meetinglist.setBounds(42, 46, 153, 177);
		frame.getContentPane().add(Meetinglist);
		
// Button ////////////////////////////////////////////////////////		
		JButton btnAccept = new JButton("Accept");
		btnAccept.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(IntSelectMeeting == 0)
				{
					JOptionPane.showMessageDialog(frame, "Please select a meeting!");
				}
				else
				{
					mongoCollection.updateOne(  
			                new Document ("Name",userName),  
			                new Document( "$pull", new Document("Meeting" ,  
			                        new Document( "MeetingID", IntSelectMeeting))))  
			                .wasAcknowledged ();  
					
					BasicDBObject match = new BasicDBObject();
			        match.put( "Name", userName );

			        BasicDBObject addressSpec = new BasicDBObject();
			        addressSpec.put("MeetingID", IntSelectMeeting);
			        addressSpec.put("Respond", "A");

			        BasicDBObject update = new BasicDBObject();
			        update.put( "$push", new BasicDBObject( "Meeting", addressSpec ) );
			        mongoCollection.updateMany( match, update );
			        
			        int isSelected = Meetinglist.getSelectedIndex();
			        MeetinglistModel.remove(isSelected);
			        MeetingDetail.setText(null);
			        
					JOptionPane.showMessageDialog(frame, "Meeting ACCEPT!");
				}
			}
		});
		btnAccept.setBounds(29, 235, 91, 29);
		frame.getContentPane().add(btnAccept);
		
/////////////////////////////////////////////////
		JButton btnDecline = new JButton("Decline");
		btnDecline.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(IntSelectMeeting == 0)
				{
					JOptionPane.showMessageDialog(frame, "Please select a meeting!");
				}
				else
				{
					mongoCollection.updateOne(  
			                new Document ("Name",userName),  
			                new Document( "$pull", new Document("Meeting" ,  
			                        new Document( "MeetingID", IntSelectMeeting))))  
			                .wasAcknowledged ();  
					
					BasicDBObject match = new BasicDBObject();
			        match.put( "Name", userName );

			        BasicDBObject addressSpec = new BasicDBObject();
			        addressSpec.put("MeetingID", IntSelectMeeting);
			        addressSpec.put("Respond", "D");

			        BasicDBObject update = new BasicDBObject();
			        update.put( "$push", new BasicDBObject( "Meeting", addressSpec ) );
			        mongoCollection.updateMany( match, update );
			        
			        int isSelected = Meetinglist.getSelectedIndex();
			        MeetinglistModel.remove(isSelected);
			        MeetingDetail.setText(null);
			        
					JOptionPane.showMessageDialog(frame, "Meeting DECLINE!");
				}
			}
		});
		btnDecline.setBounds(116, 235, 91, 29);
		frame.getContentPane().add(btnDecline);
		
/////////////////////////////////////////////////
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ProfilePage profile = new ProfilePage(userName);
				frame.dispose();
			}
		});
		btnCancel.setBounds(315, 235, 97, 29);
		frame.getContentPane().add(btnCancel);

/////////////////////////////////////////////////
		JLabel lblNewMeeting = new JLabel("New Meeting");
		lblNewMeeting.setBounds(79, 18, 91, 16);
		frame.getContentPane().add(lblNewMeeting);
		
		JLabel lblMeetingDetail = new JLabel("Meeting Detail");
		lblMeetingDetail.setBounds(282, 18, 108, 16);
		frame.getContentPane().add(lblMeetingDetail);
	}
	
//////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////

	private void showPendingMeeting()
	{
		Document myDoc = mongoCollection.find(Filters.eq("Name", userName )).first(); //get member
		List<Document> MeetingLists = (List<Document>) myDoc.get("Meeting"); 			//get meeting list
		int MeetingListSize = MeetingLists.size(); 									//get meeting list size
		
		// Count meeting list
		for(int j=0; j<MeetingListSize; j++)
		{
			Document MeetingElement = MeetingLists.get(j);
			String StringRespond = MeetingElement.getString("Respond");

			if(StringRespond.equals("P")){
				String StringeetingID = String.valueOf(MeetingElement.get("MeetingID"));
				MeetinglistModel.addElement(StringeetingID);
			}
		}		
	}
}

