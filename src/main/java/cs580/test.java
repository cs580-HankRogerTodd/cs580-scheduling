
package cs580;

import javax.swing.*;    
import java.awt.*;    
import java.awt.event.*; 
import javax.swing.event.*;                        // for ListSelectionListener

public class test extends JFrame implements ListSelectionListener
{
  JList places;

  public test()
  {
    Container c = getContentPane(); 	
    c.setLayout(new FlowLayout());
    String names[] = {"Banglore", "Hyderabad", "Ooty", "Chennai", "Mumbai", "Delhi", "Kochi", "Darjeeling"};
    places = new JList(names) ;                    // creating JList object; pass the array as parameter
    places.setVisibleRowCount(5); 
		     
    places.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
	
    c.add(new JScrollPane(places));

    places.addListSelectionListener(this);

    setTitle("Practcing Multiple selection JList");
    setSize(300,300);
    setVisible(true);
  }
  public void valueChanged(ListSelectionEvent e)
  {
    String destinations = "";
    Object obj[ ] = places.getSelectedValues();
    for(int i = 0; i < obj.length; i++)
    {
      destinations += (String) obj[i];
    }
    
    System.out.print(destinations);

   // JOptionPane.showMessageDialog( null, "You go: " + destinations,  "Learning Multiple Selections", JOptionPane.PLAIN_MESSAGE);
  }
 
}