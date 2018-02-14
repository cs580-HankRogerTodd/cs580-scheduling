package cs580;



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

import java.util.ArrayList;
import java.util.List;


public class dbManage
{
	String uri = "mongodb://rhalf001:admin@580scheduledb-shard-00-00-w3srb.mongodb.net:27017,580scheduledb-shard-00-01-w3srb.mongodb.net:27017,580scheduledb-shard-00-02-w3srb.mongodb.net:27017/test?ssl=true&replicaSet=580scheduleDB-shard-0&authSource=admin";
	MongoClientURI clientUri = new MongoClientURI(uri);
	MongoClient mongoClient = new MongoClient(clientUri);
	MongoDatabase mongoDatabase = mongoClient.getDatabase("580Schedule");
	MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("Users");
	MongoCollection<Document> mongoCollectionRooms = mongoDatabase.getCollection("Rooms");
	MongoCollection<Document> mongoCollectionMeeting = mongoDatabase.getCollection("Meeting");

	public dbManage()
	{
		//System.out.print("success");
		DBtest();
	}
	
	private void DBtest()
	{
		String username = "LazyChou";
		
		//delete member document
		/* 
        Bson filter = new Document("Name", username);
		mongoCollection.deleteOne(filter);
		*/
		
		//new member document
		/* 
		ArrayList< DBObject > array = new ArrayList< DBObject >();
		Document document = new Document("EID", "7");
		document.append("Name", username);
		document.append("Availability", "Available");
		document.append("Username", username);
		document.append("Password", "1234");
		document.append("Meeting", array);
		mongoCollection.insertOne(document);
		//*/
        
		//(RECORD FOR USED)create meeting Array
        /* 
        Bson filter = new Document("Name", username);
        ArrayList< DBObject > array = new ArrayList< DBObject >();
		Bson new_document = new Document("Meeting", array);
		Bson updateOperationDocument2 = new Document("$set", new_document);
		mongoCollection.updateOne(filter, updateOperationDocument2);
		*/
		
		//add meeting detail in array
		/* 
		BasicDBObject match = new BasicDBObject();
        match.put( "Name", username );

        BasicDBObject addressSpec = new BasicDBObject();
        //addressSpec.put("Date", "0101");
       // addressSpec.put("StartTime", "13");
       // addressSpec.put("EndTime", "15");
       // addressSpec.put("Host", "Todd");
       // addressSpec.put("Room", "1001");
        addressSpec.put("MeetingID", "6");
        addressSpec.put("Respond", "A");

        BasicDBObject update = new BasicDBObject();
        update.put( "$push", new BasicDBObject( "Meeting", addressSpec ) );
        mongoCollection.updateMany( match, update );
		//*/
		
///////// NOT USE just for record (add detail in element) ///////////////////////
        /* 
        Document document = new Document("Date", "0213");
		document.append("StartTime", "9");
		document.append("EndTime", "18");
		document.append("Host", "Amy");
		document.append("Room", "1001");
		document.append("Respond", "W");
		
		BasicDBObject MeetingObj = new BasicDBObject();
		MeetingObj.put("Meeting2",  document);
		
		Bson update = new Document("Meeting", MeetingObj);
		Bson updateOperationDocument = new Document("$set", update);
		
		mongoCollection.updateMany(filter, updateOperationDocument);
		*/
///////// NOT USE just for record (add detail in element) ///////////////////////
		
///////// I FORTGET WHAT IS THIS save for record ////////////////////////////////
		/*
		Bson filter = new Document("Name", "Hank");
        MongoCursor<Document> cursor = mongoCollection.find(filter).iterator();
        
        //print all the array element
        
		try {
        	while (cursor.hasNext()) {
        	    Document str = cursor.next();
        	    List<Document> list = (List<Document>)str.get("Meeting");
        	    System.out.println(str.get(list.get(2))); // display specific field
        	}
        	} finally {
        	cursor.close();
        	}
       */
///////// I FORTGET WHAT IS THIS save for record ////////////////////////////////
		
        // get array element
        /*
        Document myDoc = mongoCollection.find(Filters.eq("Name", "Joe" )).first();
		List<Document> courses = (List<Document>) myDoc.get("Meeting");
		Document course = courses.get(0);
		
		System.out.println(course.getString("Date"));
		System.out.println(course.getString("Host"));
		System.out.println(course.getString("Room"));
         */
        
		//Document myDoc = mongoCollection.find(Filters.eq("Name", username )).first();
		//System.out.print(mongoCollectionRooms);
        //System.out.println(courses);
        //System.out.println(courses.size());
		
//ROOM///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////		
		
		/*
		FindIterable<Document> findIterable = mongoCollection.find();  
        MongoCursor<Document> mongoCursor = findIterable.iterator();  
        while(mongoCursor.hasNext()){  
           System.out.println(mongoCursor.next());  
        }  
       */
		
       
        String RoomNo = "1003";
        
		/*
        Bson filter = new Document("RoomNo", RoomNo);
        mongoCollectionRooms.deleteOne(filter);
        */
        /*
		ArrayList< DBObject > array = new ArrayList< DBObject >();
		Document document = new Document("RID", "3");
		document.append("RoomNo", RoomNo);
		document.append("TimeBooked", array);
		mongoCollectionRooms.insertOne(document);
		*/
        
        /*
        BasicDBObject match = new BasicDBObject();
        match.put( "RoomNo", RoomNo );

        BasicDBObject addressSpec = new BasicDBObject();
        addressSpec.put("Date", "0101");
        addressSpec.put("StartTime", "7");
        addressSpec.put("EndTime", "17");
        addressSpec.put("Host", "JackChen");

        BasicDBObject update = new BasicDBObject();
        update.put( "$push", new BasicDBObject( "TimeBooked", addressSpec ) );
        mongoCollectionRooms.updateMany( match, update );
        //*/
        
//Meeting////////////////////////////////////////////////////////////////////////////////////////////////////
		 String MeetingID = "6";
		 
		//Bson filter = new Document("MeetingID", MeetingID);
	    //mongoCollectionMeeting.deleteOne(filter);
	        
       /*
        ArrayList< DBObject > array = new ArrayList< DBObject >();
		Document document = new Document("MeetingID", MeetingID);
		document.append("Host", "JackyChen");
		document.append("Date", "0101");
		document.append("StartTime", "7");
		document.append("EndTime", "17");
		document.append("Room", "1001");
		document.append("Member", array);
		mongoCollectionMeeting.insertOne(document);
		//*/

        
		FindIterable<Document> findIterable = mongoCollection.find();  
        MongoCursor<Document> mongoCursor = findIterable.iterator();  
        while(mongoCursor.hasNext()){  
           System.out.println(mongoCursor.next());  
        } 
        
	}


	
	




}