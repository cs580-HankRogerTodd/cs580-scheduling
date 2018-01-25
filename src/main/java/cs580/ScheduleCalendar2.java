package cs580;

import java.awt.EventQueue;
import java.time.LocalDate;
import java.time.YearMonth;

import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

import customComponents.ResizableButton;

import javax.swing.ListSelectionModel;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ScheduleCalendar2 {

	private JFrame frame;
	private JTable table;
	
	private int currentMonth = LocalDate.now().getMonthValue();
	private int currentYear = LocalDate.now().getYear();
	
	private static String DAYS_OF_WEEK[] = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
	private static String MONTHS[] = {"January", "February", "March", "April", "May", "June", "July",
			"August", "September", "October", "November", "December"};
	
	private JLabel lblNewLabel;

	// Launch the application.	 
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ScheduleCalendar2 window = new ScheduleCalendar2();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * Create the application.
	 */
	public ScheduleCalendar2() {
		initialize();
		frame.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("CS580 Scheduling - Group 7");
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		table = new JTable();
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setRowSelectionAllowed(false);
		
		DefaultTableModel model = new DefaultTableModel(generateDaysInMonth(currentYear, currentMonth),
				DAYS_OF_WEEK) {
						private static final long serialVersionUID = 1L;

						public boolean isCellEditable(int row, int column) {
							return false;
						}
		};
		
		table.setModel(model);
		table.setFillsViewportHeight(true);
		table.setBounds(65, 54, 324, 168);
		frame.getContentPane().add(table);
		
		lblNewLabel = new JLabel();
		lblNewLabel.setBounds(199, 20, 65, 23);
		frame.getContentPane().add(lblNewLabel);
		lblNewLabel.setText(MONTHS[currentMonth - 1]);
		
		ResizableButton button = new ResizableButton(">");
		button.setFont(new Font("Tahoma", Font.PLAIN, 11));
		button.setBounds(274, 20, 46, 23);
		frame.getContentPane().add(button);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultTableModel tempModel = (DefaultTableModel) table.getModel();
				tempModel.setRowCount(0);
				
				currentMonth++;
				if (currentMonth > MONTHS.length) {
					currentMonth = 1;
				}
				
				lblNewLabel.setText(MONTHS[currentMonth - 1]);
				Object[][] tempData = generateDaysInMonth(2018, currentMonth);
				table.setModel(new DefaultTableModel(tempData, DAYS_OF_WEEK));
				((AbstractTableModel)table.getModel()).fireTableDataChanged();
			}
		});
		
		JButton button_1 = new JButton("<");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				currentMonth--;
				if (currentMonth <= 0) {
					currentMonth = MONTHS.length;
				}
				
				lblNewLabel.setText(MONTHS[currentMonth - 1]);
				Object[][] tempData = generateDaysInMonth(2018, currentMonth);
				table.setModel(new DefaultTableModel(tempData, DAYS_OF_WEEK));
				((AbstractTableModel)table.getModel()).fireTableDataChanged();
			}
		});
		button_1.setFont(new Font("Tahoma", Font.PLAIN, 11));
		button_1.setBounds(129, 20, 46, 23);
		frame.getContentPane().add(button_1);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setBounds(244, 233, 89, 23);
		frame.getContentPane().add(btnCancel);
	}
	
	private Object[][] generateDaysInMonth(int year, int month) {
		int rowCounter = 6; // 6 rows to display month
		int colCounter = 7;
				
		int lengthOfMonth = YearMonth.of(year, month).lengthOfMonth();
		LocalDate tempLocalDate = LocalDate.of(year, month, 1);
		int dayOfFirstDayOfMonth = tempLocalDate.getDayOfWeek().getValue() % 7;
		
		// If the month is February and the first day is Monday
		/*if (dayOfFirstDayOfMonth == 0)
			rowCounter = 4;*/
		
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
