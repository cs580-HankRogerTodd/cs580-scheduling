package cs580;

import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

public class test {

	private JFrame frame;
	private int MeetingMonth;
	private int MeetingDay;
	private ArrayList<Integer> MeetingDateList=new ArrayList<Integer>(); 
	
/////Database Setup /////////////////////////////////////////////////////////////////////////
		String uri = "mongodb://rhalf001:admin@580scheduledb-shard-00-00-w3srb.mongodb.net:27017,580scheduledb-shard-00-01-w3srb.mongodb.net:27017,580scheduledb-shard-00-02-w3srb.mongodb.net:27017/test?ssl=true&replicaSet=580scheduleDB-shard-0&authSource=admin";
		MongoClientURI clientUri = new MongoClientURI(uri);
		MongoClient mongoClient = new MongoClient(clientUri);
		MongoDatabase mongoDatabase = mongoClient.getDatabase("580Schedule");
		MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("Users");
		MongoCollection<Document> mongoCollectionRooms = mongoDatabase.getCollection("Rooms");
		MongoCollection<Document> mongoCollectionMeeting = mongoDatabase.getCollection("Meeting");
	///////////////////////////////////////////////////////////////////////////////////////////////
		
		
		

	public test() {
		initialize();
		frame.setVisible(true);
	}

	private void initialize() {
		frame = new JFrame();
		
	
		
		SeperateDate();
		
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblTest = new JLabel("TEST");
		lblTest.setBounds(162, 132, 61, 16);
		frame.getContentPane().add(lblTest);
	}
	
	private void SeperateDate()
	{
		Document myDoc = mongoCollection.find(Filters.eq("Name", "Hank" )).first(); //get member
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
			
			//String convertedToString = myMeeting.get("Date").toString();
			int MeetingDate = Integer.valueOf((String) myMeeting.get("Date"));
			
			MeetingMonth = MeetingDate/100;
			MeetingDay = MeetingDate%100;
			MeetingDateList.add(MeetingMonth);
			MeetingDateList.add(MeetingDay);
			
		}
		System.out.print(MeetingDateList);
	}
		
}

