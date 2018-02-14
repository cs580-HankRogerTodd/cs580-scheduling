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
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;
import javax.swing.JTextPane;
import javax.swing.JPanel;
import java.awt.GridLayout;
import java.awt.Font;

public class ProfilePage {

	private JFrame frame;
	private String LoginUsername;

	
	public ProfilePage(String username) {
		LoginUsername = username;
		initialize();
		frame.setVisible(true);
	}


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
		
//////// Button ////////////////////////////////////////////////////////
		JButton btnCalendar = new JButton("Calendar");
		btnCalendar.setFont(new Font("Tahoma", Font.BOLD, 11));
		panel.add(btnCalendar);
		btnCalendar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Show Calendar");
			}
		});
//////////////////////////////////
		JButton btnMeeting = new JButton("Meeting");
		btnMeeting.setFont(new Font("Tahoma", Font.BOLD, 11));
		panel.add(btnMeeting);
		btnMeeting.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Show Meeting List");
			}
		});
//////////////////////////////////		
		JButton btnNotification = new JButton("Notification");
		btnNotification.setFont(new Font("Tahoma", Font.BOLD, 11));
		panel.add(btnNotification);
		btnNotification.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Show Notification");
			}
		});
//////////////////////////////////
		JButton btnCreateMeeting = new JButton();
		btnCreateMeeting.setText("<html><center>Create Meeting</center></html>");
		btnCreateMeeting.setFont(new Font("Tahoma", Font.BOLD, 11));
		panel.add(btnCreateMeeting);
		btnCreateMeeting.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MemberSelection memslct = new MemberSelection(LoginUsername);
				frame.dispose();
			}
		});
//////////////////////////////////
		JButton btnLogout = new JButton("Logout");
		btnLogout.setFont(new Font("Tahoma", Font.BOLD, 11));
		panel.add(btnLogout);
		btnLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LoginPage login = new LoginPage();
				frame.dispose();
			}
		});
//////////////////////////////////
		
	}
}
