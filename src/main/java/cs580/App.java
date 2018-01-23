package cs580;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class App {
	public static void main( String[] args ) {
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		LocalDateTime localDate = LocalDateTime.now();
		YearMonth yearMonth = YearMonth.of(localDate.getYear(), localDate.getMonth());
		DayOfWeek dayOfWeek = localDate.getDayOfWeek();
		
		/*System.out.println(localDate.getYear());
		System.out.println(localDate.getMonthValue());
		System.out.println(localDate.getDayOfMonth());
		System.out.println(yearMonth.lengthOfMonth());
		System.out.println(dayOfWeek.getValue());*/
		
		LoginPage login = new LoginPage();
		
		//ScheduleCalendar scal = new ScheduleCalendar();
		//scal.showFrames();
		
		//this connects to the database
		String uri = "mongodb://rhalf001:admin@580scheduledb-shard-00-00-w3srb.mongodb.net:27017,580scheduledb-shard-00-01-w3srb.mongodb.net:27017,580scheduledb-shard-00-02-w3srb.mongodb.net:27017/test?ssl=true&replicaSet=580scheduleDB-shard-0&authSource=admin";
		MongoClientURI clientUri = new MongoClientURI(uri);
		MongoClient mongoClient = new MongoClient(clientUri);
		MongoDatabase mongoDatabase = mongoClient.getDatabase("580Schedule");
		MongoCollection mongoCollection = mongoDatabase.getCollection("Users");
		
		//used to test the connection
//		Document document = new Document("Name", "Roger");
//		document.append("Position", "Employee");
//		document.append("Availability", "Available");
//		document.append("age", "21");
//		
//		mongoCollection.insertOne(document);
		
	}
}
