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

public class ProfilePage {

	private JFrame frame;

	/**
	 * Launch the application.
	 
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ProfilePage window = new ProfilePage();
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
		
		JButton btnNewButton = new JButton("Calendar");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ScheduleCalendar scal = new ScheduleCalendar();
				scal.showFrames();
				frame.dispose();
			}
		});
		
		btnNewButton.setBounds(327, 19, 117, 29);
		frame.getContentPane().add(btnNewButton);
		
		String currentDirectory = System.getProperty("user.dir");
		
		JLabel lblNewLabel = new JLabel("Image");
		lblNewLabel.setIcon(new ImageIcon(currentDirectory + "/image/hank.png"));
		lblNewLabel.setBounds(32, 6, 137, 143);
		frame.getContentPane().add(lblNewLabel);
		
		JTextPane txtpnCaliforniaState = new JTextPane();
		txtpnCaliforniaState.setText("Hank, Tsou\nCal Poly Pomona\nMaster student\nComputer Science\n1992\n");
		txtpnCaliforniaState.setBounds(32, 161, 137, 109);
		frame.getContentPane().add(txtpnCaliforniaState);
		
		JButton btnBack = new JButton("Back");
		btnBack.setBounds(327, 241, 117, 29);
		frame.getContentPane().add(btnBack);
		
		JButton btnMeeting = new JButton("Meeting");
		btnMeeting.setBounds(327, 64, 117, 29);
		frame.getContentPane().add(btnMeeting);
		
		JButton btnNotification = new JButton("Notification");
		btnNotification.setBounds(327, 120, 117, 29);
		frame.getContentPane().add(btnNotification);
		
		JButton btnOther = new JButton("Creat Meeting");
		btnOther.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MemberSelection memslct = new MemberSelection();
				frame.dispose();
			}
		});
		btnOther.setBounds(327, 177, 117, 29);
		frame.getContentPane().add(btnOther);
	}
}
