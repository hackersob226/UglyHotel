import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.sql.*;
import javax.sql.*;

public class CustomerPanel extends JPanel {
    private JButton newReserve, updateReserve, cancelReserve, newFeedback, viewFeedback;

    public CustomerPanel() {
        add(new JLabel("Welcome... "));

        newReserve = new JButton("New Reservation");
        newReserve.addActionListener(new ButtonListener("SearchRooms"));
        add(newReserve);
        
        updateReserve = new JButton("Update Reservation");
        updateReserve.addActionListener(new ButtonListener("UpdateReserve"));
        add(updateReserve);
        
        cancelReserve = new JButton("Cancel Reservation");
        cancelReserve.addActionListener(new ButtonListener("CancelReserve"));
        add(cancelReserve);
        
        newFeedback = new JButton("Provide Feedback");
        add(newFeedback);
        
        viewFeedback = new JButton("View Feedback");
        add(viewFeedback);
    }
    
    public class ButtonListener implements ActionListener {
        private String state;

        public ButtonListener(String currState) {
            state = currState;
        }

        public void actionPerformed(ActionEvent e) {
            if (state == "UpdateReserve") {
                HotelApp.createUpdateReserve();
            } else if (state == "CancelReserve") {
                HotelApp.createCancelReserve();
            }
            HotelApp.currentState = state;
            HotelApp.checkState();
        }
    }
}