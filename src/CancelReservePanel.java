import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.sql.*;
import javax.sql.*;
import java.text.SimpleDateFormat;

public class CancelReservePanel extends JPanel {
    JTextField reserveID;
    JButton search;
    int id = -1; //Reservation ID Here.
    public CancelReservePanel() {
        reserveID = new JTextField(5);
        add(new JLabel("Reservation ID: "));
        add(reserveID);

        search = new JButton("Search ID");
        search.addActionListener(new ButtonListener("CancelRooms"));
        add(search);
    }

    public void searchID() {
        try{
            id = Integer.parseInt(reserveID.getText());
        } catch (Exception e) {
            JOptionPane error = new JOptionPane();
            error.showMessageDialog(null, "Please check Reservation ID");
        }
    }

    public class ButtonListener implements ActionListener {
        private String state;

        public ButtonListener(String currState) {
            state = currState;
        }

        public void actionPerformed(ActionEvent e) {
            searchID();
            if (id != -1) {
                HotelApp.createCancelRooms(id);
                HotelApp.currentState = state;
                HotelApp.checkState();
            } else {
                JOptionPane error = new JOptionPane();
                error.showMessageDialog(null, "Please Search for your Reservation ID first.");
            }
        }
    }
}