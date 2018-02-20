package cs580;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

import org.bson.Document;

import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class MemberSelection extends JFrame
	implements ActionListener, ItemListener, ListSelectionListener
{

	private static final long serialVersionUID = 1L;
	//boarder
	private Border borderCenter = BorderFactory.createEmptyBorder(10, 10, 10,10); //(top, left, bottom, right) 
	private Border borderContents = BorderFactory.createEmptyBorder(0, 0, 10, 0);
	private Border borderList = BorderFactory.createLineBorder(Color.BLUE, 1);
	
	//container
	private Box boxButtons = Box.createVerticalBox();
	private Box boxListEmployee = Box.createVerticalBox();
	private Box boxListInvitee = Box.createVerticalBox();
	private Box boxListSearch = Box.createVerticalBox();
	
	private JPanel contents;
	private JPanel panelCenter;
	private JPanel panelSouth;
	
	//components
	private JButton btnAdd;
	private JButton btnAddAll;
	private JButton btnRemove;
	private JButton btnRemoveAll;
	private JButton btnSearch;
	private JButton btnNext;
	private JButton btnBack;
	private JButton btnClear;
	
	private JLabel lblListEmployee;
	private JLabel lblListInvitee;
	private JLabel lblListSearch;
	private JLabel lblImage;
	//private JLabel lblSelectHidden;
	//private JLabel lblSelectHiddenLabel;
	//private JLabel lblSelectVisible;
	//private JLabel lblSelectVisibleLabel;
	//private JLabel lblSelectBorder;
	//private JLabel lblSelectBorderLabel;
	
	private JList<String> listEmployee;
	private JList<String> listInvitee;
	private JTextField searchName;
	
	private ArrayList<String> employees = new ArrayList<String>();
	
	private DefaultListModel<String> listModelEmployee = new DefaultListModel<String>();
	private DefaultListModel<String> listModelInvitee = new DefaultListModel<String>();
	private DefaultListModel<String> listModelTemp = new DefaultListModel<String>();
	
	private boolean pressSearch = false; 
	private String ForClearReset;
	private String LoginUsername;
	
//////Database Setup ////////////////////////////////////////////////////////////////	
	String uri = "mongodb://rhalf001:admin@580scheduledb-shard-00-00-w3srb.mongodb.net:27017,580scheduledb-shard-00-01-w3srb.mongodb.net:27017,580scheduledb-shard-00-02-w3srb.mongodb.net:27017/test?ssl=true&replicaSet=580scheduleDB-shard-0&authSource=admin";
	MongoClientURI clientUri = new MongoClientURI(uri);
	MongoClient mongoClient = new MongoClient(clientUri);
	MongoDatabase mongoDatabase = mongoClient.getDatabase("580Schedule");
	MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("Users");
	MongoCursor<Document> cursor = mongoCollection.find().iterator();
	private JSeparator separator;
///////////////////////////////////////////////////////////////////////////////////////	

	public MemberSelection(String username)
	{
		super("Member Selection");
		App.setFonts();
		LoginUsername = username;
		
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
		
		/////// main container //////
		contents = new JPanel();
		contents.setBorder(borderContents); // biggest border around Frame
		contents.setLayout(new BorderLayout()); // NORTH, SOUTH, EAST, WEST, CENTER
		setContentPane(contents);
		
		/////// Add Components ///////
		
		// North Part
		JLabel lblTitle = new JLabel ("Member Selection", SwingConstants.CENTER);
		contents.add(lblTitle, BorderLayout.NORTH);
		
		/////// Center Part --> new panel --> set border --> set label + list + scroll --> add into "BOX" --> add into Center
		panelCenter = new JPanel();
		panelCenter.setBorder(borderCenter);
		
		// Employee List
		lblListEmployee = new JLabel("Employee");
		lblListEmployee.setAlignmentX(LEFT_ALIGNMENT);
		
		initEmployeeModel();
		listEmployee = new JList<String>(listModelEmployee); // already set default
		listEmployee.setAlignmentX(LEFT_ALIGNMENT);
		listEmployee.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listEmployee.setBorder(borderList); // already set before
		
		JScrollPane scrollListEmployee = new JScrollPane(listEmployee);
		scrollListEmployee.setAlignmentX(LEFT_ALIGNMENT);
		scrollListEmployee.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		setSpecificSize(scrollListEmployee, new Dimension(200, 300));
		
		boxListEmployee.add(lblListEmployee);
		boxListEmployee.add(scrollListEmployee);
		panelCenter.add(boxListEmployee);
		 
/////// Space //////////////////////////////////////////////////////
		panelCenter.add(Box.createRigidArea(new Dimension(10, 1)));
/////// Space //////////////////////////////////////////////////////		

		/////// Add and Remove button ///////
		btnAdd = new JButton("Add >>");
		btnAddAll = new JButton("Add All >>");
		btnRemove = new JButton("<< Remove");
		btnRemoveAll = new JButton("<< Remove All");
		
		
		Dimension dimRemoveAll = btnRemoveAll.getPreferredSize();
		setSpecificSize(btnAdd, dimRemoveAll);
		setSpecificSize(btnAddAll, dimRemoveAll);
		setSpecificSize(btnRemove, dimRemoveAll);
		
		boxButtons.add(btnAdd);
		boxButtons.add(Box.createRigidArea(new Dimension (1, 5)));
		boxButtons.add(btnAddAll);
		boxButtons.add(Box.createRigidArea(new Dimension (1, 20)));
		boxButtons.add(btnRemove);
		boxButtons.add(Box.createRigidArea(new Dimension (1, 5)));
		boxButtons.add(btnRemoveAll);
		
		panelCenter.add(boxButtons);
		
/////// Space //////////////////////////////////////////////////////	
		panelCenter.add(Box.createRigidArea(new Dimension(10, 1)));
/////// Space //////////////////////////////////////////////////////			
		
		// Invited List
		lblListInvitee = new JLabel("Member");
		lblListInvitee.setAlignmentX(LEFT_ALIGNMENT);
		
		listInvitee = new JList<String>(listModelInvitee);
		listInvitee.setAlignmentX(LEFT_ALIGNMENT);
		listInvitee.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listInvitee.setBorder(borderList);

		JScrollPane scrollListInvitee = new JScrollPane(listInvitee);
		scrollListInvitee.setAlignmentX(LEFT_ALIGNMENT);
		scrollListInvitee.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		setSpecificSize(scrollListInvitee, new Dimension(200, 300));
		
		boxListInvitee.add(lblListInvitee);
		boxListInvitee.add(scrollListInvitee);
		panelCenter.add(boxListInvitee);
		
/////// Space /////////
		panelCenter.add(Box.createRigidArea(new Dimension(10, 1)));
/////// Space /////////
		
		// Image Part
		lblListSearch = new JLabel("Personal Information"); // space is for looking
		lblListSearch.setAlignmentX(LEFT_ALIGNMENT);
		
		// image position
		lblImage = new JLabel(new ImageIcon());
		lblImage.setAlignmentX(LEFT_ALIGNMENT);
		setSpecificSize(lblImage, new Dimension(200, 150));
		
		boxListSearch.add(lblListSearch);
		boxListSearch.add(Box.createRigidArea(new Dimension(1, 20)));
		boxListSearch.add(lblImage);
		panelCenter.add(boxListSearch);
		
		contents.add(panelCenter, BorderLayout.CENTER);
		
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		// South Part
		panelSouth = new JPanel();
		
		searchName = new JTextField();
		searchName.setAlignmentX(LEFT_ALIGNMENT);
		
		Dimension dimSearchView = searchName.getPreferredSize();
		setSpecificSize(searchName, new Dimension(100, dimSearchView.height));
		
		// search button
		btnSearch = new JButton("Search");
		setSpecificSize(btnSearch, new Dimension(80, 20));
		
		// Next button
		btnNext = new JButton("Next >>");
		setSpecificSize(btnNext, new Dimension(80, 20));
		
		// Back button
		btnBack = new JButton("<< Back");
		setSpecificSize(btnBack, new Dimension(80, 20));
		
		//Clear button
		btnClear = new JButton("Clear");
		setSpecificSize(btnClear, new Dimension(80, 20));
		
		panelSouth.add(searchName);
		panelSouth.add(btnSearch);
		panelSouth.add(btnClear);
		
		separator = new JSeparator();
		panelSouth.add(separator);
		Component rigidArea = Box.createRigidArea(new Dimension(350, 50));
		panelSouth.add(rigidArea);
		panelSouth.add(btnBack);
		panelSouth.add(Box.createRigidArea(new Dimension(10, 1)));
		panelSouth.add(btnNext);
		//panelSouth.add(Box.createRigidArea(new Dimension(10, 50)));
		contents.add(panelSouth, BorderLayout.SOUTH);

		
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////		
		
		/////// event listener ////////
		btnAdd.addActionListener(this);
		btnAddAll.addActionListener(this);
		btnRemove.addActionListener(this);
		btnRemoveAll.addActionListener(this);
		btnNext.addActionListener(this);
		btnBack.addActionListener(this);
		btnClear.addActionListener(this);
		btnSearch.addActionListener(this);
		listEmployee.addListSelectionListener(this);
		listInvitee.addListSelectionListener(this);
		//searchName.getDocument().addDocumentListener(new MyDocumentListener());

		/////// set size /////////
		setSize(850, 460);
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
	
	 }

	private void initEmployeeModel()
	{
		for(String s : employees)
		{
			listModelEmployee.addElement(s);
		}
	}

	public void actionPerformed(ActionEvent e)
	{
		Object source = e.getSource();
		if(source == btnAdd)
		{
			addItem();
			return;
		}
		if(source == btnAddAll)
		{
			addAllItems();
			return;
		}
		if(source == btnRemove)
		{
			removeItem();
			return;
		}
		if(source == btnRemoveAll)
		{
			removeAllItems();
			return;
		}
		if(source == btnNext)
		{
			goToScheduleCalender();
			//return;
		}
		if(source == btnBack)
		{
			backToProfile(LoginUsername);
			return;
		}
		if(source == btnSearch)
		{
			searchMember();
			return;
		}
		if(source == btnClear)
		{
			showRestEmployee();
			return;
		}
		
	}
	

	private void addItem()
	{
		int isSelected = listEmployee.getSelectedIndex();
		if(isSelected == -1)
		{
			return;
		}
		
		String addedItem = listEmployee.getSelectedValue();
		//ForClearReset = listEmployee.getSelectedValue();
		// remove from left list
		listModelEmployee.remove(isSelected);
		if (pressSearch == true)
		{
			listModelTemp.removeElement(addedItem);
			System.out.print(listModelTemp);
		}
		//displaySelectedItems(); ///////////////////////////////////////////////////////////
		
		// add to right list
		int size = listModelInvitee.getSize();
		if (size == 0)
		{
			listModelInvitee.addElement(addedItem);
			return;
		}
		
		// find a larger item to put in to right order
		for(int i = 0; i<size; i++)
		{
			String item = listModelInvitee.elementAt(i);
			int compare = addedItem.compareToIgnoreCase(item);
			if(compare < 0)
			{
				listModelInvitee.add(i, addedItem);
				return;
			}
		}
		
		// else add item to the end
		listModelInvitee.addElement(addedItem);
	}

	private void addAllItems()
	{
		listModelInvitee.clear();
		for (String s : employees)
		{
			listModelInvitee.addElement(s);
		}
		listModelEmployee.clear();
	}
	
	private void removeItem()
	{
		int iSelected = listInvitee.getSelectedIndex();
		if(iSelected == -1)
		{
			return;
		}
		
		String removedItem = listInvitee.getSelectedValue();
		
		// remove from right list
		listModelInvitee.remove(iSelected);
		//displaySelectedItems();
		
		// add to left list
		int size = listModelEmployee.getSize();
		if(size == 0)
		{
			listModelEmployee.addElement(removedItem);
			return;
		}
		
		// find a larger item to put into the right order
		for(int i = 0; i<size; i++)
		{
			String item = listModelEmployee.elementAt(i);
			int compare = removedItem.compareToIgnoreCase(item);
			if(compare < 0)
			{
				listModelEmployee.add(i, removedItem);
				return;
			}
		}
		
		// else add item to the end
		listModelEmployee.addElement(removedItem);
	}

	private void removeAllItems()
	{
		listModelEmployee.clear();
		initEmployeeModel();
		listModelInvitee.clear();
	}

	private void goToScheduleCalender()
	{
		ScheduleCalendar timeslct = new ScheduleCalendar(listModelInvitee, LoginUsername);
		dispose();
	}
	
	private void backToProfile(String LoginUsername)
	{
		new ProfilePage(LoginUsername);
		dispose();
	}
	
	private void changeImage()
	{
		int employeeIsSelected = listEmployee.getSelectedIndex();
		int inviteeIsSelected = listInvitee.getSelectedIndex();
		
		String currentDirectory = System.getProperty("user.dir");
		
		if(employeeIsSelected == -1 && inviteeIsSelected == -1)
		{
			lblImage.setIcon(null);
			return;
		}
		else if(employeeIsSelected != -1 ){
		String imageName = listEmployee.getSelectedValue();
		imageName = currentDirectory + "/image/" + imageName + ".jpg";
		lblImage.setIcon(new ImageIcon(imageName));
		}
		else if(inviteeIsSelected != -1 ) {
		String imageName2 = listInvitee.getSelectedValue();
		imageName2 = currentDirectory + "/image/" + imageName2 + ".jpg";
		lblImage.setIcon(new ImageIcon(imageName2));
		}
		
	}
	
	private void setTempModel()
	{
		int size = listModelEmployee.getSize();
		
		for(int i=0; i<size; i++)
		{
			listModelTemp.addElement(listModelEmployee.elementAt(i));
			//System.out.print(listModelTemp);
		}
	}

	private void searchMember()
	{
		setTempModel();
		pressSearch = true;
		int size = listModelEmployee.getSize();
		String findMe = searchName.getText();
		
		int findMeLength = findMe.length();
		boolean foundIt = false;
		int i = 0;
		
		while(i < size)
		{
			String searchMe = listModelEmployee.elementAt(i);
			int searchMeLength = searchMe.length();
			
			for (int j = 0; j <= (searchMeLength - findMeLength); j++) 
			{
	           if (searchMe.regionMatches(j, findMe, 0, findMeLength)) 
	           {
	              foundIt = true;
	              i++;
	              break;
	            }
		     }
			
			if (!foundIt) {
				listModelEmployee.removeElement(searchMe);
				size = listModelEmployee.getSize();
				i = 0;
			}
			foundIt = false;

		   }
	}	
	private void showRestEmployee()
	{
		if(pressSearch == false)
		{
			searchName.setText(null);
		}
		else
		{
			searchName.setText(null);
			listModelEmployee.clear();
			int size = listModelTemp.getSize();
			for(int i=0; i<size; i++)
			{
				listModelEmployee.addElement(listModelTemp.elementAt(i));
			}
			
			//listModelEmployee.removeElement(ForClearReset);
			listModelTemp.clear();
			pressSearch =false;
		}
		
	}
	
	private void setSpecificSize(JComponent component, Dimension dimension)
	{
		component.setMinimumSize(dimension);
		component.setPreferredSize(dimension);
		component.setMaximumSize(dimension);
	}
	public void valueChanged(ListSelectionEvent e)
	{
		Object source = e.getSource();
		if(source == listEmployee)
		{
			changeImage();
			return;
		}
		else if(source == listInvitee)
		{
			changeImage();
			return;
		}
	}
	

	public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public void iterateDatabase()
	{
		while (cursor.hasNext()) {
	        Document doc = cursor.next();
	      }
	}
	

}
	
	
	
	
	
	