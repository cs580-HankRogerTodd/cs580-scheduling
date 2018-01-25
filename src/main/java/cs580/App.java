/*
 * Group 7.
 * */

package cs580;

import java.awt.Font;
import java.time.format.DateTimeFormatter;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

public class App {	
	public static void main( String[] args ) {
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		
		LoginPage login = new LoginPage();
		
		//this connects to the database
		String uri = "mongodb://rhalf001:admin@580scheduledb-shard-00-00-w3srb.mongodb.net:27017,580scheduledb-shard-00-01-w3srb.mongodb.net:27017,580scheduledb-shard-00-02-w3srb.mongodb.net:27017/test?ssl=true&replicaSet=580scheduleDB-shard-0&authSource=admin";
		MongoClientURI clientUri = new MongoClientURI(uri);
		MongoClient mongoClient = new MongoClient(clientUri);
		MongoDatabase mongoDatabase = mongoClient.getDatabase("580Schedule");
		MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("Users");
		
//		used to test the connection
//		Document document = new Document("EID", "11");
//		document.append("Name", "");
//		document.append("Availability", "Available");
//		document.append("Username", "");
//		document.append("Password", "1234");
//		
//		mongoCollection.insertOne(document);
		
		//used to read from the database by a specific Filter
//		try{
//			Document myDoc = mongoCollection.find(Filters.eq("Name", "Roger")).first();
//			System.out.println(myDoc.get("Availability"));
//		}
//		catch(Exception e) 
//		{
//			System.out.println("Failed to find doc");
//		}
		
		
		
	}
}
