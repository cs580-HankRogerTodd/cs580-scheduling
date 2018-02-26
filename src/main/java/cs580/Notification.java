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
import javax.swing.ListModel;
import javax.swing.SwingConstants;

public class Notification {

	private JFrame frmNotification;
	private JList Meetinglist ;
	private DefaultListModel<String> MeetinglistModel;
	private DefaultListModel<String> UpdateMeetinglistModel;
	private String userName; 
	private int IntSelectMeeting = 0;
	private int IntSelectUpdateMeeting = 0;
	private JList UpdateMeetingList;

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
		frmNotification.setVisible(true);
	}

	private void initialize() {
		frmNotification = new JFrame();
		frmNotification.setTitle("Notification");
		frmNotification.setBounds(100, 100, 500, 315);
		frmNotification.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmNotification.getContentPane().setLayout(null);
		
		final JTextArea MeetingDetail = new JTextArea();
		MeetingDetail.setBounds(298, 46, 150, 177);
		frmNotification.getContentPane().add(MeetingDetail);

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
					
					MeetingDetail.append("Host: " + myMeeting.getString("Host") + "\n" +
										 "Room: " + myMeeting.getString("Room") + "\n" +
										 "Date: " + myMeeting.getString("Date") + "\n" +
										 "Start time: " + myMeeting.getString("StartTime") + "\n" +
										 "End Time: " + myMeeting.getString("EndTime")  
										);
				}
			}
		});
		Meetinglist.setBounds(6, 46, 148, 177);
		frmNotification.getContentPane().add(Meetinglist);
		
// Button ////////////////////////////////////////////////////////		
		JButton btnAccept = new JButton("Accept");
		btnAccept.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(IntSelectMeeting == 0)
				{
					JOptionPane.showMessageDialog(frmNotification, "Please select a meeting!");
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
			        addressSpec.put("Update", "0");

			        BasicDBObject update = new BasicDBObject();
			        update.put( "$push", new BasicDBObject( "Meeting", addressSpec ) );
			        mongoCollection.updateMany( match, update );
			        
			        int isSelected = Meetinglist.getSelectedIndex();
			        MeetinglistModel.remove(isSelected);
			        MeetingDetail.setText(null);
			        
					JOptionPane.showMessageDialog(frmNotification, "Meeting ACCEPT!");
				}
			}
		});
		btnAccept.setBounds(6, 235, 75, 29);
		frmNotification.getContentPane().add(btnAccept);
		
/////////////////////////////////////////////////
		JButton btnDecline = new JButton("Decline");
		btnDecline.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(IntSelectMeeting == 0)
				{
					JOptionPane.showMessageDialog(frmNotification, "Please select a meeting!");
				}
				else
				{
					mongoCollection.updateOne(  
			                new Document ("Name",userName),  
			                new Document( "$pull", new Document("Meeting" ,  
			                        new Document( "MeetingID", IntSelectMeeting))))  
			                .wasAcknowledged ();  
					/*
					BasicDBObject match = new BasicDBObject();
			        match.put( "Name", userName );

			        BasicDBObject addressSpec = new BasicDBObject();
			        addressSpec.put("MeetingID", IntSelectMeeting);
			        addressSpec.put("Respond", "D");
			        addressSpec.put("Update", "0");

			        BasicDBObject update = new BasicDBObject();
			        update.put( "$push", new BasicDBObject( "Meeting", addressSpec ) );
			        mongoCollection.updateMany( match, update );
			         */
					
			        int isSelected = Meetinglist.getSelectedIndex();
			        MeetinglistModel.remove(isSelected);
			        MeetingDetail.setText(null);
			       
					JOptionPane.showMessageDialog(frmNotification, "Meeting DECLINE!");
				}
			}
		});
		btnDecline.setBounds(79, 235, 75, 29);
		frmNotification.getContentPane().add(btnDecline);
		
/////////////////////////////////////////////////
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ProfilePage profile = new ProfilePage(userName);
				frmNotification.dispose();
			}
		});
		btnCancel.setBounds(351, 235, 97, 29);
		frmNotification.getContentPane().add(btnCancel);

/////////////////////////////////////////////////
		JLabel lblNewMeeting = new JLabel("Pending");
		lblNewMeeting.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewMeeting.setBounds(6, 18, 148, 16);
		frmNotification.getContentPane().add(lblNewMeeting);
		
		JLabel lblMeetingDetail = new JLabel("Meeting Detail");
		lblMeetingDetail.setHorizontalAlignment(SwingConstants.CENTER);
		lblMeetingDetail.setBounds(298, 18, 150, 16);
		frmNotification.getContentPane().add(lblMeetingDetail);
		
		UpdateMeetinglistModel = new DefaultListModel();
		UpdateMeetingList = new JList(UpdateMeetinglistModel);
		UpdateMeetingList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(UpdateMeetingList.getSelectedValue() != null)
				{
					MeetingDetail.setText(null);
					IntSelectUpdateMeeting = Integer.valueOf((String) UpdateMeetingList.getSelectedValue());
					
					Document myMeeting = mongoCollectionMeeting.find(Filters.eq("MeetingID", IntSelectUpdateMeeting )).first();
					if(myMeeting != null)
					{
						MeetingDetail.append("Host: " + myMeeting.getString("Host") + "\n" +
								 "Room: " + myMeeting.getString("Room") + "\n" +
								 "Date: " + myMeeting.getString("Date") + "\n" +
								 "Start time: " + myMeeting.getString("StartTime") + "\n" +
								 "End Time: " + myMeeting.getString("EndTime")  
								);
		
						JOptionPane.showMessageDialog(frmNotification, "Host: " + myMeeting.getString("Host") + "\n" +
								 "Room: " + myMeeting.getString("Room") + "\n" +
								 "Date: " + myMeeting.getString("Date") + "\n" +
								 "Start time: " + myMeeting.getString("StartTime") + "\n" +
								 "End Time: " + myMeeting.getString("EndTime") );
						
						int isSelected = UpdateMeetingList.getSelectedIndex();
						UpdateMeetinglistModel.remove(isSelected);
				        MeetingDetail.setText(null);
				        
				        UserNoticed();
					}
					else
					{
						mongoCollection.updateOne(  
				                new Document ("Name",userName),  
				                new Document( "$pull", new Document("Meeting" ,  
				                        new Document( "MeetingID", IntSelectUpdateMeeting))))  
				                .wasAcknowledged ();  
						
						JOptionPane.showMessageDialog(frmNotification, "Meeting "+IntSelectUpdateMeeting+" has been cancel");
						int isSelected = UpdateMeetingList.getSelectedIndex();
						UpdateMeetinglistModel.remove(isSelected);
				        MeetingDetail.setText(null);
						
					}
					
				
				}
			}
		});
		UpdateMeetingList.setBounds(164, 46, 124, 177);
		frmNotification.getContentPane().add(UpdateMeetingList);
		
		JLabel lblUpdate = new JLabel("Meeting Update");
		lblUpdate.setHorizontalAlignment(SwingConstants.CENTER);
		lblUpdate.setBounds(164, 18, 124, 16);
		frmNotification.getContentPane().add(lblUpdate);
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
			String StringUpdate = MeetingElement.getString("Update");
			String StringMeetingID = String.valueOf(MeetingElement.get("MeetingID"));

			if(StringRespond.equals("P")){
				MeetinglistModel.addElement(StringMeetingID);
			}
			
			if(StringUpdate.equals("1"))
			{
				UpdateMeetinglistModel.addElement(StringMeetingID);
			}
		}	
		
		
	}
	
	private void UserNoticed()
	{
		 mongoCollection.updateOne(  
	                new Document ("Name",userName),  
	                new Document( "$pull", new Document("Meeting" ,  
	                        new Document( "MeetingID", IntSelectUpdateMeeting))))  
	                .wasAcknowledged (); 
		 
		 BasicDBObject matchEmployee = new BasicDBObject();
 		 matchEmployee.put( "Name", userName);

	     BasicDBObject Employee_addressSpec = new BasicDBObject();
	     Employee_addressSpec.put("MeetingID", IntSelectUpdateMeeting);
	     Employee_addressSpec.put("Respond", "P");
	     Employee_addressSpec.put("Update", "0");
	
	     BasicDBObject updateEmployee = new BasicDBObject();
	     updateEmployee.put( "$push", new BasicDBObject( "Meeting", Employee_addressSpec ) );
	     mongoCollection.updateMany( matchEmployee, updateEmployee );
	    
	}
}

