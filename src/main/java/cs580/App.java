/*
 * Group 7.
 * */

package cs580;

import java.time.format.DateTimeFormatter;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

public class App {	
	public static void main( String[] args ) {
		
		LoginPage login = new LoginPage();
		//dbManage member = new dbManage();
		
		
		//this connects to the database
//		String uri = "mongodb://rhalf001:admin@580scheduledb-shard-00-00-w3srb.mongodb.net:27017,580scheduledb-shard-00-01-w3srb.mongodb.net:27017,580scheduledb-shard-00-02-w3srb.mongodb.net:27017/test?ssl=true&replicaSet=580scheduleDB-shard-0&authSource=admin";
//		MongoClientURI clientUri = new MongoClientURI(uri);
//		MongoClient mongoClient = new MongoClient(clientUri);
//		MongoDatabase mongoDatabase = mongoClient.getDatabase("580Schedule");
//		MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("Users");
//		MongoCollection<Document> mongoCollectionMessages = mongoDatabase.getCollection("Messages");
//		MongoCollection<Document> mongoCollectionRooms = mongoDatabase.getCollection("Rooms");
//		
//		used to test the connection
//		Document document = new Document("EID", "11");
//		document.append("Name", "");
//		document.append("Availability", "Available");
//		document.append("Username", "");
//		document.append("Password", "1234");
//		
//		mongoCollection.insertOne(document);
		
//		Document document = new Document("MID", "1");
//		document.append("From", "Roger");
//		document.append("To", "Hank");
//		document.append("Message", "Hello! Meeting at 9 in Rhodes");
//		document.append("Response", "");
//		
//		mongoCollectionMessages.insertOne(document);
		
		//Used to update only one document by a filter ("Name") and field("Availability")
//		Bson filter = new Document("Name", "Roger");
//		Bson newValue = new Document("Availability", "Unavailable");
//		Bson updateOperationDocument = new Document("$set", newValue);
//		mongoCollection.updateOne(filter, updateOperationDocument);
		
		//updating a sub document
//		Bson filter = new Document("Name", "Roger");
//		Bson newValue = new Document("TimeAvailable.Monday.Start1", "9");
//		Bson updateOperationDocument = new Document("$set", newValue);
//		mongoCollection.updateOne(filter, updateOperationDocument);
		
		//updated everyone with the new documents
//		Document document = new Document("Start1", "");
//		document.append("End1", "");
//		document.append("Start2", "");
//		document.append("End2", "");
//		document.append("Start3", "");
//		document.append("End3", "");
//		
//		BasicDBObject Available = new BasicDBObject();
//		Available.put("Monday",  document);
//		Available.put("Tuesday",document );
//		Available.put("Wednesday",document );
//		Available.put("Thursday", document);
//		Available.put("Friday", document);
//		Available.put("Saturday", document);
//		Available.put("Sunday", document);
//		Bson update = new Document("TimeAvailable", Available);
//		Bson updateOperationDocument = new Document("$set", update);
//		mongoCollection.updateMany(Filters.exists("Name"), updateOperationDocument);
		
		//used to read from the database by a specific Filter
		//filter can be "EID", "Name", "Availability", "Username", "Password"
		
		//how read sub documents
//		Document subDoc = (Document) myDoc.get("TimeAvailable");
//		Document subSubDoc = (Document) subDoc.get("Monday");
//		System.out.println(subSubDoc.get("Start1"));
		
//		try{
//			Document myDoc = mongoCollectionMessages.find(Filters.eq("From", "Roger")).first();
//			System.out.println(myDoc.get("Message"));
//		}
//		catch(Exception e) 
//		{
//			System.out.println("Failed to find doc");
//		}
		
		
		
	}
}
