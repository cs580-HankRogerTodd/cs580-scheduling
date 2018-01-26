package cs580;

import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JTextPane;
import javax.swing.JPanel;
import java.awt.GridLayout;
import java.awt.Font;

public class ProfilePage {

	private JFrame frame;

	/**
	 * Create the application.
	 */
	public ProfilePage() {
		initialize();
		frame.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 500, 350);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		String currentDirectory = System.getProperty("user.dir");
		
		JLabel lblNewLabel = new JLabel("Image");
		lblNewLabel.setIcon(new ImageIcon(currentDirectory + "/image/hank.png"));
		lblNewLabel.setBounds(32, 6, 137, 143);
		frame.getContentPane().add(lblNewLabel);
		
		JTextPane txtpnCaliforniaState = new JTextPane();
		txtpnCaliforniaState.setText("Hank, Tsou\nCal Poly Pomona\nMaster student\nComputer Science\n1992\n");
		txtpnCaliforniaState.setBounds(32, 161, 137, 109);
		frame.getContentPane().add(txtpnCaliforniaState);
		
		JPanel panel = new JPanel();
		panel.setBounds(345, 50, 117, 190);
		frame.getContentPane().add(panel);
		panel.setLayout(new GridLayout(5, 1, 0, 10));
		
		JButton btnNewButton = new JButton("Calendar");
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 11));
		panel.add(btnNewButton);
		
		JButton btnMeeting = new JButton("Meeting");
		btnMeeting.setFont(new Font("Tahoma", Font.BOLD, 11));
		panel.add(btnMeeting);
		
		JButton btnNotification = new JButton("Notification");
		btnNotification.setFont(new Font("Tahoma", Font.BOLD, 11));
		panel.add(btnNotification);
		
		JButton btnOther = new JButton();
		btnOther.setText("<html><center>Create Meeting</center></html>");
		btnOther.setFont(new Font("Tahoma", Font.BOLD, 11));
		panel.add(btnOther);
		
		JButton btnBack = new JButton("Back");
		btnBack.setFont(new Font("Tahoma", Font.BOLD, 11));
		panel.add(btnBack);
		btnOther.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MemberSelection memslct = new MemberSelection();
				frame.dispose();
			}
		});
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ScheduleCalendar scal = new ScheduleCalendar();
				frame.dispose();
			}
		});
	}
}
