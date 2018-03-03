package cs580;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;

public class AdminPage {

	private JFrame frame;
	private JList EmployeeList;
	private JList RoomList;
	private JLabel lblListEmployee;
	private ArrayList<String> employees = new ArrayList<String>();
	private ArrayList<String> rooms = new ArrayList<String>();
	
	private String SelectedEmployee;
	private int EmployeeSelected;
	
	private JList<String> listEmployee;
	private JList<String> listRoom;
	private DefaultListModel<String> listModelEmployee = new DefaultListModel<String>();
	private DefaultListModel<String> listModelRoom = new DefaultListModel<String>();
	
//////Database Setup ////////////////////////////////////////////////////////////////	
	String uri = "mongodb://rhalf001:admin@580scheduledb-shard-00-00-w3srb.mongodb.net:27017,580scheduledb-shard-00-01-w3srb.mongodb.net:27017,580scheduledb-shard-00-02-w3srb.mongodb.net:27017/test?ssl=true&replicaSet=580scheduleDB-shard-0&authSource=admin";
	MongoClientURI clientUri = new MongoClientURI(uri);
	MongoClient mongoClient = new MongoClient(clientUri);
	MongoDatabase mongoDatabase = mongoClient.getDatabase("580Schedule");
	MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("Users");
	MongoCollection<Document> mongoCollectionRooms = mongoDatabase.getCollection("Rooms");
	MongoCollection<Document> mongoCollectionAdmin = mongoDatabase.getCollection("AdminUse");
///////////////////////////////////////////////////////////////////////////////////////

	public AdminPage() {
		initModel();
		initialize();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	private void initialize() {

		EmployeeList = new JList<String>(listModelEmployee);
		RoomList = new JList<String>(listModelRoom);
		
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 320);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JScrollPane scrollPane_Emp = new JScrollPane(EmployeeList);
		scrollPane_Emp.setBounds(45, 45, 164, 165);
		frame.getContentPane().add(scrollPane_Emp);
		
		JButton btnAddEmployee = new JButton("Add");
		btnAddEmployee.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					AddEmployee AddEmp = new AddEmployee();
					frame.dispose();
			}
		});
		btnAddEmployee.setBounds(55, 215, 75, 29);
		frame.getContentPane().add(btnAddEmployee);
		
		JButton btnDeleteEmployee = new JButton("Delete");
		btnDeleteEmployee.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(EmployeeList.getSelectedValue()!=null)
				{
					Bson filter = new Document("Name", String.valueOf(EmployeeList.getSelectedValue()));
					mongoCollection.deleteOne(filter);
			
					listModelEmployee.remove(EmployeeList.getSelectedIndex());
					
					JOptionPane.showMessageDialog(frame, "Employee Delete!");
				}
				else
				{
					JOptionPane.showMessageDialog(frame, "Please select an Employee!");
				}
				
				
			}
		});
		btnDeleteEmployee.setBounds(127, 215, 75, 29);
		frame.getContentPane().add(btnDeleteEmployee);
		
		JScrollPane scrollPane_Room = new JScrollPane(RoomList);
		scrollPane_Room.setBounds(243, 45, 160, 165);
		frame.getContentPane().add(scrollPane_Room);
		
		JButton btnAddRoom = new JButton("Add");
		btnAddRoom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				long TotalRoom = mongoCollectionRooms.count();
				String RoomNo = String.valueOf(1000+TotalRoom+1);
				
				ArrayList< DBObject > array = new ArrayList< DBObject >();
				Document document = new Document("RoomNo", RoomNo);
				document.append("TimeBooked", array);
				mongoCollectionRooms.insertOne(document);
				
				listModelRoom.addElement(RoomNo);
				
				JOptionPane.showMessageDialog(frame, "Room Add");
			}
		});
		btnAddRoom.setBounds(253, 215, 70, 29);
		frame.getContentPane().add(btnAddRoom);
		
		JButton btnDeleteRoom = new JButton("Delete");
		btnDeleteRoom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(RoomList.getSelectedValue()!=null)
				{
					System.out.print(String.valueOf(RoomList.getSelectedValue()));
					Bson filter = new Document("RoomNo", String.valueOf(RoomList.getSelectedValue()));
					mongoCollectionRooms.deleteOne(filter);
			
					listModelRoom.remove(RoomList.getSelectedIndex());
					
					JOptionPane.showMessageDialog(frame, "Room Delete!");
				}
				else
				{
					JOptionPane.showMessageDialog(frame, "Please select a Room!");
				}
			}
		});
		btnDeleteRoom.setBounds(324, 215, 75, 29);
		frame.getContentPane().add(btnDeleteRoom);
		
		JLabel lblEmployee = new JLabel("Employee");
		lblEmployee.setBounds(85, 17, 86, 16);
		lblEmployee.setForeground(Color.WHITE);
		lblEmployee.setFont(new Font("Dialog", Font.BOLD, 16));
		frame.getContentPane().add(lblEmployee);
		
		JLabel lblRoom = new JLabel("Room");
		lblRoom.setBounds(298, 17, 61, 16);
		lblRoom.setForeground(Color.WHITE);
		lblRoom.setFont(new Font("Dialog", Font.BOLD, 16));
		frame.getContentPane().add(lblRoom);
		
		JButton btnCancel = new JButton("Logout");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LoginPage login = new LoginPage();
				frame.dispose();
			}
		});
		btnCancel.setBounds(352, 263, 92, 29);
		frame.getContentPane().add(btnCancel);
		
		JButton btnApplicationForm = new JButton("Application Form");
		btnApplicationForm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(mongoCollectionAdmin.count()==0)
				{
					JOptionPane.showMessageDialog(frame, "No Application!");
				}
				else
				{
					AdminAppForm mytest = new AdminAppForm();
					frame.dispose();
				}
				
			}
		});
		btnApplicationForm.setBounds(207, 263, 152, 29);
		frame.getContentPane().add(btnApplicationForm);
		
		String currentDirectory = System.getProperty("user.dir");
		
		JLabel lblNewLabel = new JLabel("New label");
		lblNewLabel.setIcon(new ImageIcon(currentDirectory+"/image/calendarB2.jpg"));
		lblNewLabel.setBounds(0, 0, 450, 298);
		frame.getContentPane().add(lblNewLabel);

	}
	
	private void initModel()
	{
		MongoCursor<Document> cursor = mongoCollection.find().iterator();
		try {
		      while (cursor.hasNext()) {
		        Document doc = cursor.next();
		        if(doc.get("Availability").equals("Available"))
		        {
		        	employees.add(doc.get("Name").toString());
		        }
		      }
		    } finally {
		      cursor.close();
		    }
		
		for(String s : employees)
		{
			listModelEmployee.addElement(s);
		}
		
		MongoCursor<Document> roomcursor = mongoCollectionRooms.find().iterator();
		try {
		      while (roomcursor.hasNext()) {
		        Document doc = roomcursor.next();
		        	rooms.add(doc.get("RoomNo").toString());
		      }
		    } finally {
		    	roomcursor.close();
		    }
		
		for(String r : rooms)
		{
			listModelRoom.addElement(r);
		}
	}
}
