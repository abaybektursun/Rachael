package CSCI4490_Lab1;

import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.*;
import java.awt.event.*;


public class ClientGUI extends JFrame
{
    private JLabel status;
    private JButton connect;
    private JButton submit;
    private JButton stop;
    
    public ClientGUI(String title)
    {
            int i = 0;
            this.setTitle(title);
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            JLabel emptyLabel = new JLabel("");
            emptyLabel.setPreferredSize(new Dimension(800, 500));
            this.getContentPane().add(emptyLabel, BorderLayout.CENTER);
            this.pack();

            //ADD YOUR CODE HERE TO CREATE THE STATUS JLABEL AND THE JBUTTONS
            this.setVisible(true);
    }
    
    public static void main(String[] args)
    {
        new ClientGUI(args[0]); //args[0] represents the title of the GUI
    }
    
    class EventHandler implements ActionListener{
        public void actionPerformed(ActionEvent ae){
            String text = ae.getActionCommand();
        }
    }
}