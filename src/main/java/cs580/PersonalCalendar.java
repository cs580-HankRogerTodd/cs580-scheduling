package cs580;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.ListSelectionModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import customComponents.ResizableButton;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JList;
import javax.swing.JTextArea;
import javax.swing.JButton;

public class PersonalCalendar {

	private final Font ARIAL_FONT = new Font("Arial", Font.PLAIN, 11);
	
	private JFrame frame;
	private JTable table;
	
	private int currentMonth = LocalDate.now().getMonthValue();
	private int currentYear = LocalDate.now().getYear();
	
	private final String DAYS_OF_WEEK[] = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
	private final String MONTHS[] = {"January", "February", "March", "April", "May", "June", "July",
			"August", "September", "October", "November", "December"};
	
	private ArrayList<String> myMeetingList = new ArrayList<String>();
	private JList<String> Meetinglist;
	private DefaultListModel<String> MeetinglistModel;
	
	private JLabel lblNewLabel;
	private Object selectedValue;
	private Boolean NewMeeting = false;
	private JTextArea MeetingDetail;
	
	private String Date;
	private String StringDate;
	private String LoginUsername;

	private DefaultTableCellRenderer tcr;
	private DefaultTableModel tablemodel;
	
/////Database Setup /////////////////////////////////////////////////////////////////////////
	String uri = "mongodb://rhalf001:admin@580scheduledb-shard-00-00-w3srb.mongodb.net:27017,580scheduledb-shard-00-01-w3srb.mongodb.net:27017,580scheduledb-shard-00-02-w3srb.mongodb.net:27017/test?ssl=true&replicaSet=580scheduleDB-shard-0&authSource=admin";
	MongoClientURI clientUri = new MongoClientURI(uri);
	MongoClient mongoClient = new MongoClient(clientUri);
	MongoDatabase mongoDatabase = mongoClient.getDatabase("580Schedule");
	MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("Users");
	MongoCollection<Document> mongoCollectionRooms = mongoDatabase.getCollection("Rooms");
	MongoCollection<Document> mongoCollectionMeeting = mongoDatabase.getCollection("Meeting");
///////////////////////////////////////////////////////////////////////////////////////////////
	
	public PersonalCalendar(String username) {
		LoginUsername = username;
		initialize();
		setUpMeetingList();
		frame.setVisible(true);
	}


	private void initialize() {
		frame = new JFrame();
		frame.setTitle("CS580 Scheduling - Group 7");
		frame.setBounds(100, 100, 580, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(16, 55, 314, 275);
		frame.getContentPane().add(scrollPane);
		
///++++++++++++++++++++++++++++++++++++++++		
		tablemodel = new DefaultTableModel(generateDaysInMonth(currentYear, currentMonth), DAYS_OF_WEEK) 
		{
				private static final long serialVersionUID = 1L;
				public boolean isCellEditable(int row, int column) 
				{
					return false;
				}
		};
///++++++++++++++++++++++++++++++++++++++++

		table = new JTable(tablemodel);
		table.setShowGrid(false);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setRowSelectionAllowed(false);
		table.setRowHeight(50);
		table.setFillsViewportHeight(true);
		table.setBounds(65, 54, 324, 168);
		scrollPane.setViewportView(table);
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
			
				selectedValue = null;
				int row = table.rowAtPoint(arg0.getPoint()); // get row of current mouse cursor
				int col = table.columnAtPoint(arg0.getPoint()); // get column of current mouse cursor
				
				if (row >= 0 && col >= 0) {
					selectedValue = table.getValueAt(row, col); // get the value at the cursor
				}			
				// Making sure the selected date is not null
				if (selectedValue != null) {
					CalculateDate();
					MeetinglistModel.clear();
					MeetingDetail.setText(null);
					listMeetingSchedule();
				}
				else {
					JOptionPane.showMessageDialog(frame, "You selected an invalid date. Please select another date.");
				}
			}
		});

///////////////////////////////////////////////////////////////////////////////////////// cancel button
/////////////////////////////////////////////////////////////////////////////////////////
		ResizableButton btnCancel = new ResizableButton("Cancel");
		btnCancel.setFont(new Font("Arial", Font.BOLD, 11));
		btnCancel.setBounds(485, 342, 89, 23);
		frame.getContentPane().add(btnCancel);
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ProfilePage profile = new ProfilePage(LoginUsername);
				frame.dispose();
			}
		});	
		
/////////////////////////////////////////////////////////////////////////////////////////  <<  month  >>
/////////////////////////////////////////////////////////////////////////////////////////
		JPanel panel = new JPanel();
		panel.setBounds(48, 20, 244, 23);
		frame.getContentPane().add(panel);
		panel.setLayout(new BorderLayout(10, 0));
		
// Button << ///////////////////		
		ResizableButton button_1 = new ResizableButton("<");
		panel.add(button_1, BorderLayout.WEST);
		button_1.setFont(ARIAL_FONT);
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				currentMonth--;
				if (currentMonth <= 0) {
					currentMonth = MONTHS.length;
					currentYear--;
				}
///++++++++++++++++++++++++++++++++++++++++				
				lblNewLabel.setText(MONTHS[currentMonth - 1] + " " + currentYear);
				Object[][] tempData = generateDaysInMonth(currentYear, currentMonth);
				table.setModel(new DefaultTableModel(tempData, DAYS_OF_WEEK));
///++++++++++++++++++++++++++++++++++++++++				
			}
		});		
		
// Button >> ///////////////////		
		ResizableButton button = new ResizableButton(">");
		panel.add(button, BorderLayout.EAST);
		button.setFont(ARIAL_FONT);
		
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultTableModel tempModel = (DefaultTableModel) table.getModel();
				tempModel.setRowCount(0);
				
				currentMonth++;
				if (currentMonth > MONTHS.length) {
					currentMonth = 1;
					currentYear++;
				}
///+++++++++++++++++++++++++++++++++++++++				
				lblNewLabel.setText(MONTHS[currentMonth - 1] + " " + currentYear);
				Object[][] tempData = generateDaysInMonth(currentYear, currentMonth);
				table.setModel(new DefaultTableModel(tempData, DAYS_OF_WEEK));
///+++++++++++++++++++++++++++++++++++++++
			}
		});
		
/////////////////////
		lblNewLabel = new JLabel();
		panel.add(lblNewLabel, BorderLayout.CENTER);
		lblNewLabel.setFont(new Font("Arial", Font.BOLD, 11));
		lblNewLabel.setText(MONTHS[currentMonth - 1] + " " + currentYear);
		
///////////////////////////////////////////////////////////////////////////////////////// Left side detail information
/////////////////////////////////////////////////////////////////////////////////////////
		MeetingDetail = new JTextArea();
		MeetingDetail.setBounds(353, 182, 212, 148);
		frame.getContentPane().add(MeetingDetail);
		
		MeetinglistModel = new DefaultListModel();
		Meetinglist = new JList(MeetinglistModel);
		Meetinglist.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e) 
			{
				String UserSelectedMeeting = Meetinglist.getSelectedValue();
				String getID = UserSelectedMeeting.substring(0, UserSelectedMeeting.indexOf(' ')); 
				int IntID = Integer.parseInt(getID);
				MeetingDetail.setText(null);
				
				Document myMeeting = mongoCollectionMeeting.find(Filters.eq("MeetingID", IntID )).first();
				Document myStatus = mongoCollection.find(Filters.eq("Name", LoginUsername )).first();
				List<Document> meetingRes = (List<Document>) myStatus.get("Meeting");
				
				MeetingDetail.append("Host: " + myMeeting.getString("Host") + "\n" +
									 "Room: " + myMeeting.getString("Room") + "\n" +
									 "Start time: " + myMeeting.getString("StartTime") + "\n" +
									 "End Time: " + myMeeting.getString("EndTime") + "\n" +
									 "Respond: " +meetingRes.get(0).getString("Respond")
									);
			}
		});
		Meetinglist.setBounds(353, 56, 212, 118);
		frame.getContentPane().add(Meetinglist);
		
		JButton btnNotificationCenter = new JButton("Notification Center");
		btnNotificationCenter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Document myStatus = mongoCollection.find(Filters.eq("Name", LoginUsername )).first();
				List<Document> meetingRes = (List<Document>) myStatus.get("Meeting");
				
				for(int i=0; i<meetingRes.size(); i++)
				{
					Document MeetingElement = meetingRes.get(i);					
					String StringRespond = MeetingElement.getString("Respond");
					if(StringRespond.equals("P")){
						NewMeeting = true;
					}
				}
				
				if(NewMeeting == true){
					Notification Notice = new Notification(LoginUsername);
					frame.dispose();
				}
				
				else{
					JOptionPane.showMessageDialog(frame, "No New Meeting!");
				
				}
			}
		});
		btnNotificationCenter.setBounds(339, 338, 150, 29);
		frame.getContentPane().add(btnNotificationCenter);
	}
	
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private void setUpMeetingList()
	{
		Document myDoc = mongoCollection.find(Filters.eq("Name", LoginUsername )).first();
		List<Document> meetinglsit = (List<Document>) myDoc.get("Meeting");
		int size = meetinglsit.size();
		
		for (int i=0; i<size; i++)
		{
			Document meeting = meetinglsit.get(i);
			String StringMeetingID = String.valueOf(meeting.get("MeetingID"));
			int IntMeetingID = Integer.parseInt(StringMeetingID);
	
			Document myMeeting = mongoCollectionMeeting.find(Filters.eq("MeetingID", IntMeetingID )).first();
	
			StringDate = String.valueOf( myMeeting.get("Date"));
			myMeetingList.add(StringDate); 
		}
	}

	private void CalculateDate()
	{
		int intSelectedValue = (Integer) selectedValue;
		String stringSelectedValue = Integer.toString(intSelectedValue);
		String stringCurrentMonth = Integer.toString(currentMonth);
		
		if (currentMonth/10 == 0){
			stringCurrentMonth = "0" + stringCurrentMonth;
		}
		
		if (intSelectedValue/10 == 0){
			stringSelectedValue = "0" + stringSelectedValue;
		}
		
		Date = stringCurrentMonth + stringSelectedValue;
	}
	
	private void listMeetingSchedule()
	{
		Document myDoc = mongoCollection.find(Filters.eq("Name", LoginUsername )).first();
		List<Document> meetinglsit = (List<Document>) myDoc.get("Meeting");
		int size = meetinglsit.size();
		
		for (int i=0; i<size; i++)
		{
			Document meeting = meetinglsit.get(i);
			String StringMeetingID = String.valueOf(meeting.get("MeetingID"));
			int IntMeetingID = Integer.parseInt(StringMeetingID);
			
			Document myMeeting = mongoCollectionMeeting.find(Filters.eq("MeetingID", IntMeetingID )).first();
			
			if(myMeeting.getString("Date").equals(Date)){
				String outputInList = StringMeetingID + " " + myMeeting.getString("Host") + " " + myMeeting.getString("StartTime") + " - " +myMeeting.getString("EndTime");
				MeetinglistModel.addElement(outputInList);
			}
		}		
	}
	
	private Object[][] generateDaysInMonth(int year, int month) 
	{
		int colCounter = 7;
		int rowCounter;
				
		int lengthOfMonth = YearMonth.of(year, month).lengthOfMonth();
		LocalDate tempLocalDate = LocalDate.of(year, month, 1);
		int dayOfFirstDayOfMonth = tempLocalDate.getDayOfWeek().getValue() % 7;
		
		// If the month is February and the first day of month is Monday
		if (dayOfFirstDayOfMonth == 0 && YearMonth.of(year, month).getMonthValue() == 2) {
			rowCounter = 4;
		}
		// If first day of month is a Friday and the month's length is 31
		else if (dayOfFirstDayOfMonth == 5 && lengthOfMonth == 31) {
			rowCounter = 6;
		} 
		// If first day of month is a Saturday
		else if (dayOfFirstDayOfMonth == 6) {
			rowCounter = 6;
		}
		// Else, there will be 5 weeks
		else {
			rowCounter = 5;
		}
		
		Object[][] days = new Object[rowCounter][colCounter];
		
		boolean isFirstWeek = true;
		int curRow = 0;
		int curCol = 0;
		
		for (int i = 1; i <= lengthOfMonth; i++) {
			if (isFirstWeek)
				curCol = dayOfFirstDayOfMonth;
					
			days[curRow][curCol] = i;
			curCol++;
			
			if (curCol >= 7 || i == lengthOfMonth) {			
				curCol = 0;
				curRow++;
			}
			
			// Toggle isFirstWeek off
			if (isFirstWeek)
				isFirstWeek = false;
		}
		
		return days;
	}
	
	//*****************************
	// Getter and setters
	//*****************************
	public int getCurrentMonth() {return currentMonth;}
	
	public void print(Object o) {
		System.out.println(o);
	}
}




////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Add color on the calendar  //////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


///++++++++++++++++++++++++++++++++++++++++	
/*{
	public Component prepareRenderer(TableCellRenderer renderer, int row, int col) 
	{
		int i = 0;
		Component comp = super.prepareRenderer(renderer, row, col);
		System.out.print(getColumnCount());
		//Object value = getModel().getValueAt(row, col);
		System.out.print(getModel().getValueAt(5, 5));
		System.out.print("\n");
       
       return comp;
    }
};

*
*Object HasValue= model.getValueAt(row, column);
				if(HasValue != null)
				{
					System.out.print(HasValue); 
				}
				Object temp= model.getColumnCount();				
				*/

/*
tcr = new DefaultTableCellRenderer() {
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) 
			{
				Component cell = super.getTableCellRendererComponent (table, value, isSelected, hasFocus, row, column);

				selectedValue = tablemodel.getValueAt(row, column);

				if(selectedValue != null)
				{
					CalculateDate();
					System.out.print(Date+"\n");
					for (String meeting : myMeetingList )
					{
						//System.out.print(meeting);
						if(meeting.equals(Date))
						{
							//System.out.print(Date+"\n");
							cell.setBackground(Color.GRAY);
						}
						else
							cell.setBackground(Color.WHITE);
				    }

				}

				return cell;
			
			}
			
		};
///++++++++++++++++++++++++++++++++++++++++		
		for(int i = 0; i < DAYS_OF_WEEK.length; i++) 
		{
			table.getColumn(DAYS_OF_WEEK[i]).setCellRenderer(tcr);
		}
///++++++++++++++++++++++++++++++++++++++++
*/


