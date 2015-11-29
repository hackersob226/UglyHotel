import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.sql.*;
import javax.sql.*;

public class MakeFeedbackPanel extends JPanel {
    String city = "Atlanta";
    String rating = "Good";
    String commentHere;
    JButton ok;
    JTextField comment;
    public MakeFeedbackPanel() {
        add(new JLabel("Hotel Location: "));
        String locations[] = {"Atlanta", "Charlotte", "Savannah", "Orlando", "Miami"};
        JComboBox dropDown = new JComboBox(locations);
        dropDown.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                city = (String) dropDown.getSelectedItem();
                System.out.printf(city); //GET RID OF THIS LATER
            }
        });
        add(dropDown);

        add(new JLabel("Rating: "));
        String ratings[] = {"Excellent", "Good", "Bad", "Very Bad", "Neutral"};
        JComboBox dropDown2 = new JComboBox(ratings);
        dropDown2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                rating = (String) dropDown2.getSelectedItem();
                System.out.printf(rating); //GET RID OF THIS LATER
            }
        });
        add(dropDown2);

        add(new JLabel("Comment: "));
        comment = new JTextField(20);
        add(comment);

        ok = new JButton("Submit");
        ok.addActionListener(new ButtonListener("CustomerView"));
        add(ok);
    }

    public class ButtonListener implements ActionListener {
        private String state;

        public ButtonListener(String currState) {
            state = currState;
        }

        public void actionPerformed(ActionEvent e) {
            commentHere = comment.getText();
            JOptionPane confirm = new JOptionPane();
            confirm.showMessageDialog(null, "Feedback submitted.");
            HotelApp.currentState = state;
            HotelApp.checkState();
        }
    }
}