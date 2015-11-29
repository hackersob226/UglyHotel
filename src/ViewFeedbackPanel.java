import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.sql.*;
import javax.sql.*;

public class ViewFeedbackPanel extends JPanel {
    String city = "Atlanta";
    JButton check;
    public ViewFeedbackPanel() {
        String locations[] = {"Atlanta", "Charlotte", "Savannah", "Orlando", "Miami"};
        JComboBox dropDown = new JComboBox(locations);
        dropDown.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                city = (String) dropDown.getSelectedItem();
                System.out.printf(city); //GET RID OF THIS LATER
            }
        });
        add(dropDown);

        check = new JButton("Check Review");
        check.addActionListener(new ButtonListener("CityFeedback"));
        add(check);
    }

    public class ButtonListener implements ActionListener {
        private String state;

        public ButtonListener(String currState) {
            state = currState;
        }

        public void actionPerformed(ActionEvent e) {
            HotelApp.createFeedback(city);
            HotelApp.currentState = state;
            HotelApp.checkState();
        }
    }
}