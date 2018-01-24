package cs580;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MemberSelection {

	private JFrame frame;

	/**
	 * Launch the application.
	 
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MemberSelection window = new MemberSelection();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	*/
	/**
	 * Create the application.
	 */
	public MemberSelection() {
		initialize();
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
		
		JButton btnNextMeetingDate = new JButton("Next");
		btnNextMeetingDate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ScheduleCalendar2 scal2 = new ScheduleCalendar2();
				frame.dispose();
			}
		});
		btnNextMeetingDate.setBounds(327, 227, 117, 29);
		frame.getContentPane().add(btnNextMeetingDate);
		
		JButton btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ProfilePage profile = new ProfilePage();
				frame.dispose();
			}
		});
		btnBack.setBounds(6, 227, 117, 29);
		frame.getContentPane().add(btnBack);
	}

}
