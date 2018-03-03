package cs580;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
class JtableCellColor extends JFrame
{
private JPanel topPanel;
private JTable table;
private JScrollPane scrollPane;
private String[] columnNames=new String[3];
private String[][] dataValues=new String[3][3];
JTextField textBox=new JTextField();
public JtableCellColor()
{
setTitle("JTable Cell Color");
setSize(300,300);
setDefaultCloseOperation (EXIT_ON_CLOSE);
topPanel=new JPanel();
topPanel.setLayout(new BorderLayout());
getContentPane().add(topPanel);
columnNames=new String[] {"Column 1" ,"Column 2", "Column 3"};
dataValues=new String[][]
                                              {
                                         {"1","2","3"},
                                         {"4","5","6"},
                                         {"7","8","9"}
                                               };
TableModel  model=new myTableModel();
table=new JTable();
table.setRowHeight(50);
table.setModel(model);
TableColumn soprtColumn=table.getColumnModel().getColumn(1);
soprtColumn.setCellEditor(new DefaultCellEditor (textBox));
table.setCellSelectionEnabled(true);
scrollPane=new JScrollPane(table);
topPanel.add(scrollPane,BorderLayout.CENTER);
table.addMouseListener(new java.awt.event.MouseAdapter()  
{
public void mouseClicked(java.awt.event.MouseEvent e)
{
textBox.setBackground(Color.RED);
}
});}
public class myTableModel extends DefaultTableModel
{
                 myTableModel()
                         {
                          super(dataValues,columnNames);
                         }
               public boolean isCellEditable(int row,int cols)
                         {
                          return true;
                          }
}
public static void main(String args[])
               {
               JtableCellColor x=new JtableCellColor();
               x.setVisible(true);
                }  
}