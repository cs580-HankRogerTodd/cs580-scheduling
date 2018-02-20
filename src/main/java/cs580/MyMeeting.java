package cs580;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MyMeeting {

	private JFrame frame;


	public MyMeeting() {
		initialize();
		frame.setVisible(true);
	}


	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 500, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JList list = new JList();
		list.setBounds(23, 45, 135, 166);
		frame.getContentPane().add(list);
		
		JList list_1 = new JList();
		list_1.setBounds(180, 45, 135, 166);
		frame.getContentPane().add(list_1);
		
		JTextArea textArea = new JTextArea();
		textArea.setBounds(338, 45, 135, 166);
		frame.getContentPane().add(textArea);
		
		JLabel lblAcceptMeeting = new JLabel("Accept Meeting");
		lblAcceptMeeting.setBounds(41, 17, 106, 16);
		frame.getContentPane().add(lblAcceptMeeting);
		
		JLabel lblMyMeeting = new JLabel("My Meeting");
		lblMyMeeting.setBounds(208, 17, 98, 16);
		frame.getContentPane().add(lblMyMeeting);
		
		JLabel lblMeetingDetail = new JLabel("Meeting Detail");
		lblMeetingDetail.setBounds(358, 17, 98, 16);
		frame.getContentPane().add(lblMeetingDetail);
		
		JButton btnDecline = new JButton("Decline");
		btnDecline.setBounds(30, 223, 117, 29);
		frame.getContentPane().add(btnDecline);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setBounds(377, 243, 117, 29);
		frame.getContentPane().add(btnCancel);
		
		JButton btnUpdate = new JButton("Update");
		btnUpdate.setBounds(175, 223, 79, 29);
		frame.getContentPane().add(btnUpdate);
		
		JButton btnCancel_1 = new JButton("Cancel");
		btnCancel_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnCancel_1.setBounds(248, 223, 79, 29);
		frame.getContentPane().add(btnCancel_1);
	}

}
