package cs580;

import java.awt.EventQueue;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
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

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;



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
	
	private String UserStartTime;
	private String UserEndTime;
	private String UserSelectedRoom;
	private String LoginUsername;
	private String Date;
	private JList<String> ListRoom;
	
	private JTextField StartTimeText;
	private JTextField EndTimeText;
	
	JTextArea AvailableRoomText;
	JTextArea AvailableTimeText;
    

    
//////Database Setup ////////////////////////////////////////////////////////////////	
	String uri = "mongodb://rhalf001:admin@580scheduledb-shard-00-00-w3srb.mongodb.net:27017,580scheduledb-shard-00-01-w3srb.mongodb.net:27017,580scheduledb-shard-00-02-w3srb.mongodb.net:27017/test?ssl=true&replicaSet=580scheduleDB-shard-0&authSource=admin";
	MongoClientURI clientUri = new MongoClientURI(uri);
	MongoClient mongoClient = new MongoClient(clientUri);
	MongoDatabase mongoDatabase = mongoClient.getDatabase("580Schedule");
	MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("Users");
	MongoCollection<Document> mongoCollectionRooms = mongoDatabase.getCollection("Rooms");
	MongoCollection<Document> mongoCollectionMeeting = mongoDatabase.getCollection("Meeting");
///////////////////////////////////////////////////////////////////////////////////////

	

	public TimeRoomSelection(DefaultListModel<String> listModelInvitee, String Username, int InputcurrentMonth, Object inputSelectedValue) {
		Invitee = listModelInvitee;
		LoginUsername = Username;
		currentMonth = InputcurrentMonth;
		selectedValue = inputSelectedValue;
		
		CalculateDate(currentMonth, selectedValue);
		findAvailableTime();
		initialize();
		
		frame.setVisible(true);
	}


	private void initialize() 
	{		
		frame = new JFrame();
		
		frame.setBounds(100, 100, 500, 350);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		JLabel lblNewLabel = new JLabel("Date: 2018 / "+ currentMonth + "/" + selectedValue);
		lblNewLabel.setBounds(6, 6, 203, 31);
		frame.getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Available Time");
		lblNewLabel_1.setBounds(89, 37, 120, 16);
		frame.getContentPane().add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("Available Room");
		lblNewLabel_2.setBounds(317, 37, 120, 16);
		frame.getContentPane().add(lblNewLabel_2);
		
		JLabel lblNewLabel_3 = new JLabel("Start Time: ");
		lblNewLabel_3.setBounds(55, 239, 95, 16);
		frame.getContentPane().add(lblNewLabel_3);
		
		JLabel lblEndTime = new JLabel("End Time: ");
		lblEndTime.setBounds(55, 267, 79, 16);
		frame.getContentPane().add(lblEndTime);
		
		StartTimeText = new JTextField();
		StartTimeText.setBounds(152, 234, 79, 26);
		frame.getContentPane().add(StartTimeText);
		StartTimeText.setColumns(10);
		
		EndTimeText = new JTextField();
		EndTimeText.setBounds(152, 262, 79, 26);
		frame.getContentPane().add(EndTimeText);
		EndTimeText.setColumns(10);
		
/////// Button //////////////////////////////////////////////////////
		JButton btnFinsh = new JButton("Finish");
		btnFinsh.setBounds(386, 272, 95, 29);
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
		btnNewButton_1.setBounds(284, 272, 102, 29);
		frame.getContentPane().add(btnNewButton_1);
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ProfilePage profile = new ProfilePage(LoginUsername);
				frame.dispose();
			}
		});
//////////////////////////////////	
		JButton btnFindRoom = new JButton("Find Room");
		btnFindRoom.setBounds(141, 293, 102, 29);
		frame.getContentPane().add(btnFindRoom);
		btnFindRoom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UserStartTime = StartTimeText.getText();
				UserEndTime = EndTimeText.getText();
				
				IntUserStartTime = Integer.valueOf((String) UserStartTime);
				IntUserEndTime = Integer.valueOf((String) UserEndTime);
				
				for(int i=IntUserStartTime; i<IntUserEndTime; i++)
				{
					if(AvailableTimeArray[i] == 1)
					{
						JOptionPane.showMessageDialog(null, "Pleace Input Available Time");
						StartTimeText.setText(null);
						EndTimeText.setText(null);
						UserStartTime = null;
						UserEndTime = null;
						break;
					}
				}
				
				if(UserStartTime != null && UserEndTime != null)
				{
					if(IntUserStartTime > IntUserEndTime)
					{
						JOptionPane.showMessageDialog(null, "Pleace Input Available Time");
						StartTimeText.setText(null);
						EndTimeText.setText(null);
						UserStartTime = null;
						UserEndTime = null;
					}
					else
					{
						findAvailableRoom();
					}
				}
			}
		});

////// Available Time List ////////////////////////////////////////////
		AvailableTimeText = new JTextArea();
		AvailableTimeText.setBounds(55, 65, 175, 162);
		frame.getContentPane().add(AvailableTimeText);
		for(int i=0; i<17; i++) 
		{
			if(AvailableTimeArray[i] == 0)
			{
				int endtime = i+1;
				AvailableTimeText.append(i + ":00 - " + endtime + ":00" + "\n");
			}
		}
/////// Available Room List ////////////////////////////////////////////////////
		RoomlistModel = new DefaultListModel();
		ListRoom = new JList(RoomlistModel);
		ListRoom.setBounds(284, 65, 175, 82);
        frame.getContentPane().add(ListRoom);
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
		AvailableTimeArray = new int[] {1,1,1,1,1,1,1,0,0,0,0,0,0,0,0,0,0};
		
		String Four_Digit_Date = '0' + Integer.toString(currentMonth) + '0' + selectedValue;

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
				//System.out.println(MeetingElement); // get meeting ID to find the meeting date, start time and end time
				String StringMeetingID = String.valueOf(MeetingElement.get("MeetingID"));
				int IntMeetingID = Integer.parseInt(StringMeetingID);
				// use meeting ID to get meeting detail
				Document myMeeting = mongoCollectionMeeting.find(Filters.eq("MeetingID", IntMeetingID )).first();
				
				String convertedToString = myMeeting.get("Date").toString();
				
				if (convertedToString.equals(Four_Digit_Date)==true)
				{
					int IntStartTime = Integer.valueOf((String) myMeeting.get("StartTime"));
					int IntEndTime = Integer.valueOf((String) myMeeting.get("EndTime"));
					
					for (int Mtime=IntStartTime; Mtime<IntEndTime; Mtime++)
					{
						AvailableTimeArray[Mtime] = 1;
					}
				}
				else 
				{
					continue;
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
			ScheduleCalendar timeslct = new ScheduleCalendar(Invitee, LoginUsername);
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
        			BookedStartTime = Integer.valueOf((String) RoomBookedTime.get(i).get("StartTime"));
        			BookedEndTime = Integer.valueOf((String) RoomBookedTime.get(i).get("EndTime"));

        			if ((IntUserStartTime >= BookedStartTime && IntUserStartTime < BookedEndTime) || (IntUserEndTime > BookedStartTime && IntUserEndTime <= BookedEndTime))
        			{
            			RoomAvailable = false;	
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

        BasicDBObject update = new BasicDBObject();
        update.put( "$push", new BasicDBObject( "TimeBooked", addressSpec ) );
        mongoCollectionRooms.updateMany( match, update );
        //*/

/////// Add new meeting schedule into Employee database
		
        for (int i= 0; i< Invitee.size(); i++)
        {
        		BasicDBObject matchEmployee = new BasicDBObject();
        		matchEmployee.put( "Name", Invitee.elementAt(i) );

            BasicDBObject Employee_addressSpec = new BasicDBObject();
            Employee_addressSpec.put("MeetingID", TotalMeeting+1);
            Employee_addressSpec.put("Respond", "P");

            BasicDBObject updateEmployee = new BasicDBObject();
            updateEmployee.put( "$push", new BasicDBObject( "Meeting", Employee_addressSpec ) );
            mongoCollection.updateMany( matchEmployee, updateEmployee );
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
