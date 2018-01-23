package cs580;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.LineListener;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

/**
 * The class that will display a calendar for users.
 * @author Todd Nguyen
 *
 */
public class ScheduleCalendar {
	private static String MONTHS[] = {"January", "February", "March", "April", "May", "June", "July",
			"August", "September", "October", "November", "December"};
	
	private static String DAYS_OF_WEEK[] = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
	
	private static int NUM_OF_MONTHS = 12;
	
	private List<Object> selectedItems = new ArrayList<Object>();
	
    private JPanel jPanel;
	private JTable tableCalendar;
	private JScrollPane scrollPane;
	private JFrame jFrame;
	private DefaultTableModel model;
	
	public ScheduleCalendar() {
		model = new DefaultTableModel();
		tableCalendar = new JTable(model) {
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column) {
				return false;
			};
		};
		
		tableCalendar.setPreferredScrollableViewportSize(new Dimension(500, 400));
		tableCalendar.setFillsViewportHeight(true);
		tableCalendar.setShowGrid(false);
		tableCalendar.setIntercellSpacing(new Dimension(0, 0));
		tableCalendar.getTableHeader().setReorderingAllowed(false);
		tableCalendar.getTableHeader().setResizingAllowed(false);
		tableCalendar.setRowSelectionAllowed(false);
		tableCalendar.addMouseListener(tableMouseListener);
		
		addDataToTable(model);
		
		tableCalendar.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		resizeColumnWidth(tableCalendar);
		
		scrollPane = new JScrollPane(tableCalendar);
		jPanel = new JPanel(new GridLayout(1, 0));
		jPanel.add(scrollPane);;
		jFrame = new JFrame("Calendar");
		centerTable(tableCalendar);
	}
	
	public void showFrames() {
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jPanel.setOpaque(true);
		
		jFrame.setContentPane(jPanel);
		jFrame.pack();
		jFrame.setVisible(true);
		//printDebugData(tableCalendar);
	}
	
	private void addDataToTable(DefaultTableModel model) {
		for (int i = 0; i < DAYS_OF_WEEK.length; i++) {
			model.addColumn(DAYS_OF_WEEK[i]);
		}
				
		// Adding January
		Object[] daysInWeek = new Object[7];
		int lengthOfMonth = YearMonth.of(2018, 1).lengthOfMonth();
		
		LocalDate temp = LocalDate.of(2018, 1, 1);
		int dayOfFirstDayOfMonth = temp.getDayOfWeek().getValue() % 7;
		boolean isFirstWeek = true;
		int counter = 0;
		
		for (int i = 1; i <= lengthOfMonth; i++) {
			if (isFirstWeek)
				counter = dayOfFirstDayOfMonth;
			
			daysInWeek[counter] = i;
			counter++;
			
			if (counter >= 7 || i == lengthOfMonth) {			
				counter = 0;
				model.addRow(daysInWeek);
				
				// Clear array
				for (int index = 0; index < daysInWeek.length; index++) {
					daysInWeek[index] = null;
				}
			}
			
			// Toggle isFirstWeek off
			if (isFirstWeek)
				isFirstWeek = false;
		}
		
		/*for (int i = 1; i < NUM_OF_MONTHS + 1; i++) {
			int lengthOfMonth = YearMonth.of(2018, i).lengthOfMonth();
			
			LocalDate temp = LocalDate.of(2018, i, 1);
			int dayOfFirstDayOfMonth = temp.getDayOfWeek().getValue() % 7;
			boolean isFirstWeek = true;
			int counter = 0;
			
			for (int j = 1; j <= lengthOfMonth; j++) {
				if (isFirstWeek)
					counter = dayOfFirstDayOfMonth;
				
				daysInWeek[counter] = j;
				counter++;
				
				if (counter >= 7) {
					counter = 0;
					model.addRow(daysInWeek);
				}
				
				// Toggle isFirstWeek off
				if (isFirstWeek)
					isFirstWeek = false;
			}
		}*/
	}
	
	MouseListener tableMouseListener = new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {
			int row = tableCalendar.rowAtPoint(e.getPoint()); // get row of selected cell
			int col = tableCalendar.columnAtPoint(e.getPoint()); // get column of selected cell
			System.out.println(tableCalendar.getValueAt(row, col));
			/*Object[] curEntry = new Object[] {row, col};
			
			// If the items are already selected
			if (selectedItems.contains(curEntry)) {
				selectedItems.remove(curEntry);
			}
			// Item is not selected
			else {
				selectedItems.add(curEntry);
				for (Object o : curEntry)
					System.out.print(o + " ");
				System.out.println("");
			}*/
		}
	};
	
	private void centerTable(JTable table) {
		DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
		cellRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		table.setDefaultRenderer(Object.class, cellRenderer);
	}
	
	private void resizeColumnWidth(JTable table) {
		TableColumnModel columnModel = table.getColumnModel();
		for (int col = 0; col < table.getColumnCount(); col++) {
			int width = 40; // Minimum width
			for (int row = 0; row < table.getRowCount(); row++) {
				TableCellRenderer renderer = table.getCellRenderer(row, col);
				Component comp = table.prepareRenderer(renderer, row, col);
				width = Math.max(comp.getPreferredSize().width + 1, width);				
			}
			columnModel.getColumn(col).setPreferredWidth(width);
		}
	}
	
	private void printDebugData(JTable table) {
		int numRows = table.getRowCount();
		int numCols = table.getColumnCount();
		javax.swing.table.TableModel model = table.getModel();
		
		System.out.println("Value of data: ");
		for (int i = 0; i < numRows; i++) {
			System.out.println("    row " + i + ":");
			for (int j = 0; j < numCols; j++) {
				System.out.println("    " + model.getValueAt(i, j));
			}
			System.out.println();
		}
		System.out.println("--------------------");
	}
}
