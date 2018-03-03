package cs580;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextArea;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import javax.swing.ImageIcon;



public class TimeRoomSelection {

	private JFrame frame;

	private DefaultListModel<String> Invitee;
	private DefaultListModel<String> RoomlistModel;
	
	private int[] AvailableTimeArray;
	private int currentMonth;
	private int IntUserStartTime;
	private int IntUserEndTime;
	private int BookedStartTime;
	private int BookedEndTime;
	
	private Object selectedValue;
	
	private String UserStartTime = "";
	private String UserEndTime = "";
	private String UserSelectedRoom;
	private String LoginUsername;
	private String Date;
	private JList<String> ListRoom;
	
	private JTextField StartTimeText;
	private JTextField EndTimeText;
	
	JTextArea AvailableRoomText;
	JTextArea AvailableTimeText;
	
	private Boolean ExistMeeting;
	private int ExistMeetingID;
    

    
//////Database Setup ////////////////////////////////////////////////////////////////	
	String uri = "mongodb://rhalf001:admin@580scheduledb-shard-00-00-w3srb.mongodb.net:27017,580scheduledb-shard-00-01-w3srb.mongodb.net:27017,580scheduledb-shard-00-02-w3srb.mongodb.net:27017/test?ssl=true&replicaSet=580scheduleDB-shard-0&authSource=admin";
	MongoClientURI clientUri = new MongoClientURI(uri);
	MongoClient mongoClient = new MongoClient(clientUri);
	MongoDatabase mongoDatabase = mongoClient.getDatabase("580Schedule");
	MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("Users");
	MongoCollection<Document> mongoCollectionRooms = mongoDatabase.getCollection("Rooms");
	MongoCollection<Document> mongoCollectionMeeting = mongoDatabase.getCollection("Meeting");
///////////////////////////////////////////////////////////////////////////////////////

	

	public TimeRoomSelection(DefaultListModel<String> listModelInvitee, String Username, int InputcurrentMonth, Object inputSelectedValue, Boolean existmeeting, int existmeetingid) {
		Invitee = listModelInvitee;
		LoginUsername = Username;
		currentMonth = InputcurrentMonth;
		selectedValue = inputSelectedValue;
		ExistMeeting = existmeeting;
		ExistMeetingID = existmeetingid;
		
		CalculateDate(currentMonth, selectedValue);
		findAvailableTime();
		initialize();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}


	private void initialize() 
	{		
		frame = new JFrame();
		
		frame.setBounds(100, 100, 493, 380);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		JLabel lblNewLabel = new JLabel("2018 / "+ currentMonth + " / " + selectedValue);
		lblNewLabel.setBounds(158, 6, 210, 31);
		lblNewLabel.setForeground(Color.WHITE);
		lblNewLabel.setFont(new Font("Dialog", Font.BOLD, 25));
		frame.getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Available Time");
		lblNewLabel_1.setBounds(78, 51, 129, 26);
		lblNewLabel_1.setForeground(Color.WHITE);
		lblNewLabel_1.setFont(new Font("Dialog", Font.BOLD, 16));
		frame.getContentPane().add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("Available Room");
		lblNewLabel_2.setBounds(289, 49, 130, 26);
		lblNewLabel_2.setForeground(Color.WHITE);
		lblNewLabel_2.setFont(new Font("Dialog", Font.BOLD, 16));
		frame.getContentPane().add(lblNewLabel_2);
		
		JLabel lblNewLabel_3 = new JLabel("Start Time: ");
		lblNewLabel_3.setBounds(53, 271, 92, 16);
		lblNewLabel_3.setForeground(Color.WHITE);
		lblNewLabel_3.setFont(new Font("Dialog", Font.BOLD, 16));
		frame.getContentPane().add(lblNewLabel_3);
		
		JLabel lblEndTime = new JLabel(" End Time: ");
		lblEndTime.setBounds(53, 299, 95, 16);
		lblEndTime.setForeground(Color.WHITE);
		lblEndTime.setFont(new Font("Dialog", Font.BOLD, 16));
		frame.getContentPane().add(lblEndTime);
		
		StartTimeText = new JTextField();
		StartTimeText.setBounds(146, 267, 82, 26);
		frame.getContentPane().add(StartTimeText);
		StartTimeText.setColumns(10);
		
		EndTimeText = new JTextField();
		EndTimeText.setBounds(146, 295, 82, 26);
		frame.getContentPane().add(EndTimeText);
		EndTimeText.setColumns(10);
		
/////// Button //////////////////////////////////////////////////////
		JButton btnFinsh = new JButton("Finish");
		btnFinsh.setBounds(404, 323, 83, 29);
		frame.getContentPane().add(btnFinsh);
		btnFinsh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(UserSelectedRoom == null || UserStartTime == null || UserEndTime == null)
				{
					JOptionPane.showMessageDialog(null, "Please input the time and select a room");
				}
				else
				{
					UpdateDB_NewMeeting();
					JOptionPane.showMessageDialog(null, "Request Send");
					ProfilePage profile = new ProfilePage(LoginUsername);
					frame.dispose();
				}

			}
		});	
//////////////////////////////////		
		JButton btnNewButton_1 = new JButton("Cancel");
		btnNewButton_1.setBounds(317, 323, 92, 29);
		frame.getContentPane().add(btnNewButton_1);
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ProfilePage profile = new ProfilePage(LoginUsername);
				frame.dispose();
			}
		});
//////////////////////////////////	
		JButton btnFindRoom = new JButton("Refresh Room");
		btnFindRoom.setBounds(289, 222, 120, 37);
		frame.getContentPane().add(btnFindRoom);
		btnFindRoom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UserStartTime = StartTimeText.getText();
				UserEndTime = EndTimeText.getText();
				
				if(UserStartTime.equals("") || UserEndTime.equals(""))
				{
					JOptionPane.showMessageDialog(null, "Pleace Input Available Time");
					StartTimeText.setText(null);
					EndTimeText.setText(null);
					UserStartTime = null;
					UserEndTime = null;
				}	
				
				else//(UserStartTime != null && UserEndTime != null)
				{

					IntUserStartTime = Integer.valueOf((String) UserStartTime);
					IntUserEndTime = Integer.valueOf((String) UserEndTime);
				
					if(IntUserStartTime > IntUserEndTime || IntUserStartTime == IntUserEndTime)
					{
						JOptionPane.showMessageDialog(null, "Pleace Input Available Time");
						RoomlistModel.clear();
						StartTimeText.setText(null);
						EndTimeText.setText(null);
						UserStartTime = null;
						UserEndTime = null;
					}
					else
					{
						for(int i=IntUserStartTime; i<IntUserEndTime; i++)
						{
							if(AvailableTimeArray[i] == 1)
							{
								JOptionPane.showMessageDialog(null, "Pleace Input Available Time");
								RoomlistModel.clear();
								StartTimeText.setText(null);
								EndTimeText.setText(null);
								UserStartTime = null;
								UserEndTime = null;
								break;
							}
							else if(i == IntUserEndTime-1)
							{
								findAvailableRoom();
							}
						}	
					}
				}
			}
		});

////// Available Time List ////////////////////////////////////////////
		AvailableTimeText = new JTextArea();
		AvailableTimeText.setBounds(53, 79, 175, 173);
		frame.getContentPane().add(AvailableTimeText);
		for(int i=0; i<17; i++) 
		{
			if(AvailableTimeArray[i] == 0)
			{
				int endtime = i+1;
				AvailableTimeText.append(" "+ i + ":00 - " + endtime + ":00" + "\n");
			}
		}
/////// Available Room List ////////////////////////////////////////////////////
		RoomlistModel = new DefaultListModel();
		ListRoom = new JList(RoomlistModel);
		ListRoom.setBounds(262, 77, 175, 133);
        frame.getContentPane().add(ListRoom);
        
        JLabel lblNewLabel_4 = new JLabel("New label");
        lblNewLabel_4.setIcon(new ImageIcon("/Users/hanktsou/Documents/GitHub/cs580-scheduling/image/calendarB2.jpg"));
        lblNewLabel_4.setBounds(0, 0, 493, 358);
        frame.getContentPane().add(lblNewLabel_4);
        ListRoom.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) 
        	{
        		UserSelectedRoom = ListRoom.getSelectedValue();
        	}
        });
	}
	
	private void CalculateDate(int currentMonth, Object SelectedValue)
	{
		int intSelectedValue = (Integer) SelectedValue;
		String stringSelectedValue = Integer.toString(intSelectedValue);
		String stringCurrentMonth = Integer.toString(currentMonth);
		
		if (currentMonth/10 == 0)
		{
			stringCurrentMonth = "0" + stringCurrentMonth;
		}
		if (intSelectedValue/10 == 0)
		{
			stringSelectedValue = "0" + stringSelectedValue;
		}
		
		Date = stringCurrentMonth + stringSelectedValue;
	}
	
	private void findAvailableTime()
	{
		boolean HaveAvailableTime = false;
		int size = Invitee.getSize();
		String Four_Digit_Date="0";
		AvailableTimeArray = new int[] {1,1,1,1,1,1,1,0,0,0,0,0,0,0,0,0,0};
		
		String StringSelectedValue = String.valueOf(selectedValue); 
		int IntSelectedValue = Integer.parseInt(StringSelectedValue);
		//System.out.println(convertedToString2);
		if(IntSelectedValue < 10)
		{
			Four_Digit_Date = '0' + Integer.toString(currentMonth) + '0' + selectedValue;
		}
		else
		{
			Four_Digit_Date = '0' + Integer.toString(currentMonth) + selectedValue;
		}
		

		// Count member
		for(int i=0; i<size; i++)
		{
			//System.out.print(Invitee.elementAt(i));
			Document myDoc = mongoCollection.find(Filters.eq("Name", Invitee.elementAt(i) )).first(); //get member
			List<Document> MeetingLists = (List<Document>) myDoc.get("Meeting"); 						//get meeting list
			int MeetingListSize = MeetingLists.size(); 												//get meeting list size
			
			// Count meeting list
			for(int j=0; j<MeetingListSize; j++)
			{
				Document MeetingElement = MeetingLists.get(j);
				String testString = String.valueOf(MeetingElement.get("MeetingID"));
				Integer testint = Integer.valueOf(testString);

				//System.out.println(MeetingElement); // get meeting ID to find the meeting date, start time and end time
				
				if( ExistMeeting == true && testint == ExistMeetingID)
				{
					continue;	
				}
				
				else
				{
					 if(MeetingElement.getString("Respond").equals("A"))
						{
				
							String StringMeetingID = String.valueOf(MeetingElement.get("MeetingID"));
							int IntMeetingID = Integer.parseInt(StringMeetingID);
							// use meeting ID to get meeting detail
							Document myMeeting = mongoCollectionMeeting.find(Filters.eq("MeetingID", IntMeetingID )).first();
							
							String convertedToString = myMeeting.get("Date").toString();
							
							//System.out.print(convertedToString+"\n");
							//System.out.print(Four_Digit_Date+"\n");
							
							if (convertedToString.equals(Four_Digit_Date)==true)
							{
								int IntStartTime = Integer.valueOf((String) myMeeting.get("StartTime"));
								int IntEndTime = Integer.valueOf((String) myMeeting.get("EndTime"));
								
								for (int Mtime=IntStartTime; Mtime<IntEndTime; Mtime++)
								{
									AvailableTimeArray[Mtime] = 1;
								}
								//System.out.println(Arrays.toString(AvailableTimeArray));
								//System.out.print(AvailableTimeArray);
							}
							else 
							{
								continue;	
							}
						}
				}
			}
		}
		
		// remove the time has no available room
		RemoveNoRoomTime();

		// Check has Available Time ////////////////////////////////////////////////////////////////
		for (int i=0; i<17; i++)
		{
			if(AvailableTimeArray[i] == 0)
			{
				HaveAvailableTime = true;
			}
		}
		
		if(HaveAvailableTime == false)
		{
			JOptionPane.showMessageDialog(null, "No Available Time");
			ScheduleCalendar timeslct = new ScheduleCalendar(Invitee, LoginUsername, ExistMeeting, ExistMeetingID);
			frame.dispose();
		}
	}
	
	
	private void RemoveNoRoomTime()
	{
		for(int i=0; i<17; i++)
		{
			if(AvailableTimeArray[i] == 0)
			{
				int CheckStartTime = i;
				int CheckEndTime = i+1;
				CheckTimeHasRoom(CheckStartTime, CheckEndTime);
			}
		}
	}
	
	
	private void CheckTimeHasRoom(int IntUserStartTime, int IntUserEndTime)
	{
		Boolean RoomAvailable = true;
		Boolean RoomAvailableChange = true;
		
		FindIterable<Document> findIterable = mongoCollectionRooms.find();  
        MongoCursor<Document> mongoCursor = findIterable.iterator();  
        while(mongoCursor.hasNext())
        {  
        		Document Room = (Document) mongoCursor.next(); 
        		List<Document> RoomBookedTime = (List<Document>) Room.get("TimeBooked"); 
        		for(int i=0 ; i<RoomBookedTime.size(); i++)
        		{
        			String StringRoomBookedTime = RoomBookedTime.get(i).get("Date").toString();
        			
        			String testString = String.valueOf(RoomBookedTime.get(i).get("MeetingID"));
    				Integer testint = Integer.valueOf(testString);
    				
        			if( ExistMeeting == true && testint == ExistMeetingID)
    				{
    					continue;	
    				}
    				
    				else
    				{
    					if(StringRoomBookedTime.equals(Date))
            			{
            				BookedStartTime = Integer.valueOf((String) RoomBookedTime.get(i).get("StartTime"));
                			BookedEndTime = Integer.valueOf((String) RoomBookedTime.get(i).get("EndTime"));
                			//int UpdateMeetingID = Integer.valueOf((String) RoomBookedTime.get(i).get("MeetingID"));
                			
                			if ((IntUserStartTime >= BookedStartTime && IntUserStartTime < BookedEndTime) || (IntUserEndTime > BookedStartTime && IntUserEndTime <= BookedEndTime))
                			{
                    			RoomAvailable = false;	
                			}
            			}
    				}
        		}
        		
        		if (RoomAvailable == true)
        		{
        			RoomAvailableChange = false;
        			break;
        		}
        		else 
        		{
        			RoomAvailable = true;
        		}
        } 
		
        if(RoomAvailableChange == true)
        {
        		AvailableTimeArray[IntUserStartTime] = 1;
        }
	}
	

	private void findAvailableRoom()
	{
		RoomlistModel.clear();
		Boolean RoomAvailable = true;
		
		FindIterable<Document> findIterable = mongoCollectionRooms.find();  
        MongoCursor<Document> mongoCursor = findIterable.iterator();  
        
        while(mongoCursor.hasNext())
        {  
        		Document Room = (Document) mongoCursor.next(); 
        		List<Document> RoomBookedTime = (List<Document>) Room.get("TimeBooked"); 
       
        		for(int i=0 ; i<RoomBookedTime.size(); i++)
        		{
        			String StringRoomBookedTime = RoomBookedTime.get(i).get("Date").toString();
        			String testString = String.valueOf(RoomBookedTime.get(i).get("MeetingID"));
    				Integer testint = Integer.valueOf(testString);
    				
        			if( ExistMeeting == true && testint == ExistMeetingID)
    				{
    					continue;	
    				}
    				
    				else
    				{
    					if(StringRoomBookedTime .equals(Date))
            			{
            				BookedStartTime = Integer.valueOf((String) RoomBookedTime.get(i).get("StartTime"));
                			BookedEndTime = Integer.valueOf((String) RoomBookedTime.get(i).get("EndTime"));

                			if ((IntUserStartTime >= BookedStartTime && IntUserStartTime < BookedEndTime) || (IntUserEndTime > BookedStartTime && IntUserEndTime <= BookedEndTime))
                			{
                    			RoomAvailable = false;	
                			}
            			}
    				}
        			
        		}
        		
        		if (RoomAvailable == true)
        		{
        			RoomlistModel.addElement(Room.getString("RoomNo"));
        		}
        		else 
        		{
        			RoomAvailable = true;
        		}
        } 
	}

	private void UpdateDB_NewMeeting()
	{
		/* Result Information
		 DefaultListModel<String> Invitee;
		 int currentMonth / Object selectedValue;
		 String UserStartTime / String UserEndTime;
		 String UserSelectedRoom;
		 String LoginUsername
		*/
		
/////// Add new meeting into Meeting database
		if(ExistMeeting == false)
		{
			long TotalMeeting = mongoCollectionMeeting.count();
			///*
			
			ArrayList< DBObject > array = new ArrayList< DBObject >();
			Document document = new Document("MeetingID", TotalMeeting+1);
			document.append("Host", LoginUsername);
			document.append("Date", Date);
			document.append("StartTime", UserStartTime);
			document.append("EndTime", UserEndTime);
			document.append("Room", UserSelectedRoom);
			document.append("Member", array);
			mongoCollectionMeeting.insertOne(document);
			
			
			for (int i=0 ; i < Invitee.size(); i++)
			{
				mongoCollectionMeeting. updateOne( Filters.eq( "MeetingID", TotalMeeting+1),  
		                new Document( "$addToSet", new Document( "Member", Invitee.getElementAt(i))))  
		                .wasAcknowledged ();
			}
			
			//*/
			
	/////// Add new booked time into Room database
			///*
	        BasicDBObject match = new BasicDBObject();
	        match.put( "RoomNo", UserSelectedRoom );

	        BasicDBObject addressSpec = new BasicDBObject();
	        addressSpec.put("Date", Date);
	        addressSpec.put("StartTime", UserStartTime);
	        addressSpec.put("EndTime", UserEndTime);
	        addressSpec.put("Host", LoginUsername);
	        addressSpec.put("MeetingID", TotalMeeting+1);

	        BasicDBObject update = new BasicDBObject();
	        update.put( "$push", new BasicDBObject( "TimeBooked", addressSpec ) );
	        mongoCollectionRooms.updateMany( match, update );
	        //*/

	/////// Add new meeting schedule into Employee database
			
	        for (int i= 0; i< Invitee.size(); i++)
	        {
	        		if(Invitee.elementAt(i).equals(LoginUsername))
	        		{
	        			BasicDBObject matchEmployee = new BasicDBObject();
		        		matchEmployee.put( "Name", LoginUsername);

		            BasicDBObject Employee_addressSpec = new BasicDBObject();
		            Employee_addressSpec.put("MeetingID", TotalMeeting+1);
		            Employee_addressSpec.put("Respond", "A");
		            Employee_addressSpec.put("Update", "0");

		            BasicDBObject updateEmployee = new BasicDBObject();
		            updateEmployee.put( "$push", new BasicDBObject( "Meeting", Employee_addressSpec ) );
		            mongoCollection.updateMany( matchEmployee, updateEmployee );
	        		}
	        		else
	        		{
	        			BasicDBObject matchEmployee = new BasicDBObject();
		        		matchEmployee.put( "Name", Invitee.elementAt(i) );

		            BasicDBObject Employee_addressSpec = new BasicDBObject();
		            Employee_addressSpec.put("MeetingID", TotalMeeting+1);
		            Employee_addressSpec.put("Respond", "P");
		            Employee_addressSpec.put("Update", "0");

		            BasicDBObject updateEmployee = new BasicDBObject();
		            updateEmployee.put( "$push", new BasicDBObject( "Meeting", Employee_addressSpec ) );
		            mongoCollection.updateMany( matchEmployee, updateEmployee );
	        		}
	        		
	        }
	        
		}
		
		else
		{
			Document myMeeting = mongoCollectionMeeting.find(Filters.eq("MeetingID", ExistMeetingID )).first();
			String previousRoom = myMeeting.getString("Room");
			
			for (int i= 0; i< Invitee.size(); i++)
	        {
				if(Invitee.elementAt(i).equals(LoginUsername))
	        		{
					mongoCollection.updateOne(  
			                new Document ("Name",Invitee.elementAt(i)),  
			                new Document( "$pull", new Document("Meeting" ,  
			                        new Document( "MeetingID", ExistMeetingID))))  
			                .wasAcknowledged ();  
					
		        		BasicDBObject matchEmployee = new BasicDBObject();
		        		matchEmployee.put( "Name", Invitee.elementAt(i) );

		            BasicDBObject Employee_addressSpec = new BasicDBObject();
		            Employee_addressSpec.put("MeetingID", ExistMeetingID);
		            Employee_addressSpec.put("Respond", "A");
		            Employee_addressSpec.put("Update", "0");

		            BasicDBObject updateEmployee = new BasicDBObject();
		            updateEmployee.put( "$push", new BasicDBObject( "Meeting", Employee_addressSpec ) );
		            mongoCollection.updateMany( matchEmployee, updateEmployee );
	        		}
				else
				{
					mongoCollection.updateOne(  
			                new Document ("Name",Invitee.elementAt(i)),  
			                new Document( "$pull", new Document("Meeting" ,  
			                        new Document( "MeetingID", ExistMeetingID))))  
			                .wasAcknowledged ();  
					
		        		BasicDBObject matchEmployee = new BasicDBObject();
		        		matchEmployee.put( "Name", Invitee.elementAt(i) );

		            BasicDBObject Employee_addressSpec = new BasicDBObject();
		            Employee_addressSpec.put("MeetingID", ExistMeetingID);
		            Employee_addressSpec.put("Respond", "P");
		            Employee_addressSpec.put("Update", "1");

		            BasicDBObject updateEmployee = new BasicDBObject();
		            updateEmployee.put( "$push", new BasicDBObject( "Meeting", Employee_addressSpec ) );
		            mongoCollection.updateMany( matchEmployee, updateEmployee );
				}
				
	        }
			
			mongoCollectionRooms.updateOne(  
	                new Document ("RoomNo",previousRoom),  
	                new Document( "$pull", new Document("TimeBooked" ,  
	                        new Document( "MeetingID", ExistMeetingID))))  
	                .wasAcknowledged ();  
			
			 BasicDBObject match = new BasicDBObject();
		        match.put( "RoomNo", UserSelectedRoom );

		        BasicDBObject addressSpec = new BasicDBObject();
		        addressSpec.put("Date", Date);
		        addressSpec.put("StartTime", UserStartTime);
		        addressSpec.put("EndTime", UserEndTime);
		        addressSpec.put("Host", LoginUsername);
		        addressSpec.put("MeetingID", ExistMeetingID);

		        BasicDBObject update = new BasicDBObject();
		        update.put( "$push", new BasicDBObject( "TimeBooked", addressSpec ) );
		        mongoCollectionRooms.updateMany( match, update );
			
			//private Boolean ExistMeeting;
			//Used to update only one document by a filter ("Name") and field("Availability")
			//document.append("Update", "0");
			Bson filter = new Document("MeetingID", ExistMeetingID);
			Document document = new Document("Date", Date);
			document.append("StartTime", UserStartTime);
			document.append("EndTime", UserEndTime);
			document.append("Room", UserSelectedRoom);
			Bson updateOperationDocument = new Document("$set", document);
			mongoCollectionMeeting.updateOne(filter, updateOperationDocument);
		}
		
		//System.out.print(Invitee.size());
		//System.out.print(Invitee.elementAt(0));
		
		/*
		System.out.print("Host: " + LoginUsername + "\n");
		System.out.print("Member: " + Invitee + "\n");
		System.out.print("Date: " + Date + "\n");
		System.out.print("Start time: " + UserStartTime + "\n");
		System.out.print("End time: " + UserEndTime + "\n");
		System.out.print("Room: " + UserSelectedRoom + "\n");
		//*/
		
		FindIterable<Document> findIterable = mongoCollection.find();
        MongoCursor<Document> mongoCursor = findIterable.iterator();  
        while(mongoCursor.hasNext()){  
           System.out.println(mongoCursor.next());  
        } 
        System.out.print("\n");
        FindIterable<Document> findIterableMeeting = mongoCollectionMeeting.find();
        MongoCursor<Document> mongoCursorMeeting = findIterableMeeting.iterator();  
        while(mongoCursorMeeting.hasNext()){  
           System.out.println(mongoCursorMeeting.next());  
        } 
        System.out.print("\n");
        FindIterable<Document> findIterableRoom = mongoCollectionRooms.find();
        MongoCursor<Document> mongoCursorRoom = findIterableRoom.iterator();  
        while(mongoCursorRoom.hasNext()){  
           System.out.println(mongoCursorRoom.next());  
        } 
	}
}
