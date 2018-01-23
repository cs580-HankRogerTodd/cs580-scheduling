package cs580;

import java.awt.EventQueue;
import java.time.LocalDate;
import java.time.YearMonth;

import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.ListSelectionModel;

public class ScheduleCalendar2 {

	private JFrame frame;
	private JTable table;
	
	private static String DAYS_OF_WEEK[] = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};

	/**
	 * Launch the application.
	 */
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
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		table = new JTable();
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setRowSelectionAllowed(false);
		
		DefaultTableModel model = new DefaultTableModel(generateDaysInMonth(),
				new String[] {
						"New column", "New column", "New column", "New column",
						"New column", "New column", "New column"
					}) {
						private static final long serialVersionUID = 1L;

						public boolean isCellEditable(int row, int column) {
							return false;
						}
		};
		
		table.setModel(model);
		table.setFillsViewportHeight(true);
		table.setBounds(65, 54, 324, 168);
		frame.getContentPane().add(table);
	}
	
	private Object[][] generateDaysInMonth() {
		int rowCounter = 5; // 5 rows to display month
		int colCounter = 7;
				
		// Adding January
		int lengthOfMonth = YearMonth.of(2018, 1).lengthOfMonth();
		LocalDate temp = LocalDate.of(2018, 1, 1);
		int dayOfFirstDayOfMonth = temp.getDayOfWeek().getValue() % 7;
		
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
}
