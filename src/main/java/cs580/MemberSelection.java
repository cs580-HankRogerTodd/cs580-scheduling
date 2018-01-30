package cs580;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

public class MemberSelection extends JFrame
	implements ActionListener, ItemListener, ListSelectionListener
{
	
	/**
	 * 
	 */
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
	
	//Fonts
	private final Font fontBold = new Font(Font.DIALOG, Font.BOLD, 14);
	private final Font fontPlain = new Font(Font.DIALOG, Font.PLAIN, 14);
	
	//List_datd
	private String [] employees = {"apple", "banana", "acat", "dog", "elephant", "fish", 
			"girlgeneration", "ahah", "iamhandsome", "juice", "ak47", "Lion", "Momo", 
			"Nima", "Octupus", "Python", "queen", "ruby"};
	
	private DefaultListModel<String> listModelEmployee = new DefaultListModel<String>();
	private DefaultListModel<String> listModelInvitee = new DefaultListModel<String>();
	
	public MemberSelection()
	{
		super("Member Selection");
		setFonts();
		
		/////// main container //////
		contents = new JPanel();
		contents.setBorder(borderContents); // biggest border around Frame
		contents.setLayout(new BorderLayout()); // NORTH, SOUTH, EAST, WEST, CENTER
		setContentPane(contents);
		
		/////// Add Components ///////
		
		// North Part
		JLabel lblTitle = new JLabel ("Member Selection", SwingConstants.CENTER);
		lblTitle.setFont(new Font(Font.DIALOG, Font.BOLD, 18));
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
		lblListSearch = new JLabel("    personal information"); // space is for looking
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
		btnNext = new JButton(" NEXT >>");
		setSpecificSize(btnNext, new Dimension(80, 50));
		
		// Back button
		btnBack = new JButton("<< BACK ");
		setSpecificSize(btnBack, new Dimension(90, 50));
		
		panelSouth.add(searchName);
		panelSouth.add(btnSearch);
		panelSouth.add(Box.createRigidArea(new Dimension(420, 50)));
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
		listEmployee.addListSelectionListener(this);
		listInvitee.addListSelectionListener(this);

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
			return;
		}
		if(source == btnBack)
		{
			backToProfile();
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
		
		// remove from left list
		listModelEmployee.remove(isSelected);
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
		System.out.println("IN goToScheduleCalender");
	}
	
	private void backToProfile()
	{
		new ProfilePage();
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
		
	private void setFonts()
	{
		UIManager.put("Button.font", fontBold);
		UIManager.put("ComboBox.font", fontBold);
		UIManager.put("Label.font", fontBold);
		UIManager.put("List.font", fontPlain);
		
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
/*
	public static void main(String[] args)
	{
		MemberSelection gui = new MemberSelection();
		gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}*/

	public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub
		
	}
	

}
	
	
	
	
	
	