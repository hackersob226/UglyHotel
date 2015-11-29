import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.sql.*;
import javax.sql.*;

public class CityFeedbackPanel extends JPanel {
    String location; //Location of feedback
    JButton ok;
    JTable table;
    public CityFeedbackPanel(String city) {
        location = city;
        add(new JLabel("Feedback for " + location));

        String[] col = {"Rating", "Comment"};
        //Place ratings in Object[][] data
        Object[][] data = { {"Excellent", "This is a filler."}, {"Neutral", "This is also a filler"}};

        table = new JTable(data, col);
        table.setPreferredScrollableViewportSize(new Dimension(HotelApp.WIDTH - 50, HotelApp.HEIGHT - 150));
        JScrollPane scrollPane = new JScrollPane(table);

        add(scrollPane);

        ok = new JButton("OK");
        ok.addActionListener(new ButtonListener("CustomerView"));
        add(ok);
    }

    public class ButtonListener implements ActionListener {
        private String state;

        public ButtonListener(String currState) {
            state = currState;
        }

        public void actionPerformed(ActionEvent e) {
            HotelApp.currentState = state;
            HotelApp.checkState();
        }
    }
}