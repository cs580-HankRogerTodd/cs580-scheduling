package cs580;

import java.awt.EventQueue;
import java.util.Vector;

import javax.swing.JFrame;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import javax.swing.DefaultListModel;
import javax.swing.JButton;

public class TimeSelection {

	private JFrame frame;
	private String memberName;
	
	Vector<Double> collectRecordTime=new Vector<Double>();
	
	String uri = "mongodb://rhalf001:admin@580scheduledb-shard-00-00-w3srb.mongodb.net:27017,580scheduledb-shard-00-01-w3srb.mongodb.net:27017,580scheduledb-shard-00-02-w3srb.mongodb.net:27017/test?ssl=true&replicaSet=580scheduleDB-shard-0&authSource=admin";
	MongoClientURI clientUri = new MongoClientURI(uri);
	MongoClient mongoClient = new MongoClient(clientUri);
	MongoDatabase mongoDatabase = mongoClient.getDatabase("580Schedule");
	MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("Users");
	MongoCursor<Document> cursor = mongoCollection.find().iterator();


	public TimeSelection(DefaultListModel<String> listModelInvitee) {
		initialize();
		dateDecision(listModelInvitee);
		frame.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton btnPp = new JButton("pp");
		btnPp.setBounds(63, 91, 117, 29);
		frame.getContentPane().add(btnPp);
	}
	
	private void dateDecision(DefaultListModel<String> listModelInvitee)
	{
		Vector DateAndTime = new Vector();
		Vector outter = new Vector();
		
		//FindIterable<Document> findIterable = mongoCollection.find();  
        //MongoCursor<Document> mongoCursor = findIterable.iterator();
        
        String username = "Hank";
        Document myDoc = mongoCollection.find(Filters.eq("Name", username )).first();
        
        /*
        while(mongoCursor.hasNext()){  
        System.out.println(mongoCursor.next());  
        }
        
		int size = listModelInvitee.getSize();
		for(int i=0; i<size; i++)
		{
			memberName = listModelInvitee.elementAt(i);
			Document myDoc = mongoCollection.find(Filters.eq("Name", memberName )).first();
			Document subDoc = (Document) myDoc.get("TimeAvailable");
		}*/
		
	}
}
