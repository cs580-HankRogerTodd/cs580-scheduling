package cs580;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;


import customComponents.ResizableButton;

import javax.swing.ListSelectionModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import java.awt.FlowLayout;
import java.awt.BorderLayout;

public class ScheduleCalendar {

	private final Font ARIAL_FONT = new Font("Arial", Font.PLAIN, 11);
	
	private JFrame frame;
	private JTable table;
	
	private int currentMonth = LocalDate.now().getMonthValue();
	private int currentYear = LocalDate.now().getYear();
	
	private final String DAYS_OF_WEEK[] = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
	private final String MONTHS[] = {"January", "February", "March", "April", "May", "June", "July",
			"August", "September", "October", "November", "December"};
	
	
	private DefaultListModel<String> Invitee;
	private JLabel lblNewLabel;
	private String LoginUsername;

	
	public ScheduleCalendar(DefaultListModel<String> listModelInvitee, String Username) {
		Invitee = listModelInvitee;
		LoginUsername = Username;
		initialize();
		frame.setVisible(true);
	}


	private void initialize() {
		frame = new JFrame();
		frame.setTitle("CS580 Scheduling - Group 7");
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		table = new JTable();
		table.setShowGrid(false);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setRowSelectionAllowed(false);
		
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				Object selectedValue = null;
				int row = table.rowAtPoint(arg0.getPoint()); // get row of current mouse cursor
				int col = table.columnAtPoint(arg0.getPoint()); // get column of current mouse cursor
				
				if (row >= 0 && col >= 0) {
					selectedValue = table.getValueAt(row, col); // get the value at the cursor
				}
				
				// Making sure the selected date is not null
				if (selectedValue != null) {
					JOptionPane.showMessageDialog(frame, "You selected " + Integer.toString(currentYear) + "/" + 
						Integer.toString(currentMonth) + "/" + selectedValue);
					
						TimeRoomSelection TimeRoomSelect = new TimeRoomSelection(Invitee, LoginUsername, currentMonth, selectedValue) ;
				}
				else {
					JOptionPane.showMessageDialog(frame, "You selected an invalid date. Please select another date.");
				}
			}
		});
		
		DefaultTableModel model = new DefaultTableModel(generateDaysInMonth(currentYear, currentMonth),
			DAYS_OF_WEEK) {
				private static final long serialVersionUID = 1L;
	
				public boolean isCellEditable(int row, int column) {
					return false;
				}
		};
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(36, 65, 386, 172);
		frame.getContentPane().add(scrollPane);
		
		table.setModel(model);
		table.setRowHeight(50);
		table.setFillsViewportHeight(true);
		table.setBounds(65, 54, 324, 168);
		scrollPane.setViewportView(table);
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment( JLabel.CENTER );
		table.setDefaultRenderer(Object.class, centerRenderer);
		
		ResizableButton btnCancel = new ResizableButton("Cancel");
		btnCancel.setFont(new Font("Arial", Font.BOLD, 11));
		btnCancel.setBounds(272, 249, 89, 23);
		frame.getContentPane().add(btnCancel);
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new ProfilePage(LoginUsername);
				frame.dispose();
			}
		});
		
		JPanel panel = new JPanel();
		panel.setBounds(138, 25, 190, 23);
		frame.getContentPane().add(panel);
		panel.setLayout(new BorderLayout(10, 0));
		
		ResizableButton button_1 = new ResizableButton("<");
		panel.add(button_1, BorderLayout.WEST);
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				currentMonth--;
				if (currentMonth <= 0) {
					currentMonth = MONTHS.length;
					currentYear--;
				}
				
				lblNewLabel.setText(MONTHS[currentMonth - 1] + " " + currentYear);
				Object[][] tempData = generateDaysInMonth(currentYear, currentMonth);
				table.setModel(new DefaultTableModel(tempData, DAYS_OF_WEEK));
				((AbstractTableModel)table.getModel()).fireTableDataChanged();
			}
		});
		button_1.setFont(ARIAL_FONT);
		
		ResizableButton button = new ResizableButton(">");
		panel.add(button, BorderLayout.EAST);
		button.setFont(ARIAL_FONT);
		
		lblNewLabel = new JLabel();
		panel.add(lblNewLabel, BorderLayout.CENTER);
		lblNewLabel.setFont(new Font("Arial", Font.BOLD, 11));
		lblNewLabel.setText(MONTHS[currentMonth - 1] + " " + currentYear);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultTableModel tempModel = (DefaultTableModel) table.getModel();
				tempModel.setRowCount(0);
				
				currentMonth++;
				if (currentMonth > MONTHS.length) {
					currentMonth = 1;
					currentYear++;
				}
				
				lblNewLabel.setText(MONTHS[currentMonth - 1] + " " + currentYear);
				Object[][] tempData = generateDaysInMonth(currentYear, currentMonth);
				table.setModel(new DefaultTableModel(tempData, DAYS_OF_WEEK));
				((AbstractTableModel)table.getModel()).fireTableDataChanged();
			}
		});
	}
	
	
	
	
	private Object[][] generateDaysInMonth(int year, int month) {
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
