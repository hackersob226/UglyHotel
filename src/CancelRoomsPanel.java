import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.sql.*;
import javax.sql.*;
import javax.swing.table.DefaultTableModel;
import java.text.SimpleDateFormat;

public class CancelRoomsPanel extends JPanel {
    JButton cancel;
    JTable table;
    Calendar today;

    public CancelRoomsPanel() {
        today = Calendar.getInstance();
        SimpleDateFormat format1 = new SimpleDateFormat("MM-dd-yyyy");
        String[] col = {"Room Number", "Room Category", "Person Capacity",
                        "Cost per Day", "Cost of Extra Bed per Day", "Extra Bed?"};
        //Place canceled rooms in Object[][] data
        Object[][] data = { {new Integer(11), "Filler", new Integer(1), new Integer(111), new Integer(10), new Boolean(false)},
            {new Integer(11), "Filler", new Integer(1), new Integer(111), new Integer(10), new Boolean(false)}
        };
        //Insert Start/End date here.
        add(new JLabel("Start Date: "));
        add(new JLabel("End Date: "));

        table = new JTable(data, col);
        table.setPreferredScrollableViewportSize(new Dimension(HotelApp.WIDTH - 50, HotelApp.HEIGHT - 150));
        JScrollPane scrollPane = new JScrollPane(table);

        add(scrollPane);
        
        add(new JLabel("Total Cost of Reservation: "));
        //Insert Total Cost here.
        
        add(new JLabel("Date of Cancellation: "));
        String date1 = format1.format(today.getTime());
        add(new JLabel(date1));
        
        add(new JLabel("Amount to be refunded: "));
        //Insert Amount to be refunded based on start/end date here.
        //TODO: Logic for this....sigh.

        cancel = new JButton("Cancel Reservation");
        cancel.addActionListener(new ButtonListener("CustomerView"));
        add(cancel);
    }
    
    public class ButtonListener implements ActionListener {
        private String state;

        public ButtonListener(String currState) {
            state = currState;
        }

        public void actionPerformed(ActionEvent e) {
            JOptionPane confirm = new JOptionPane();
            confirm.showMessageDialog(null, "Reservation Cancelled.");
            HotelApp.currentState = state;
            HotelApp.checkState();
        }
    }
}