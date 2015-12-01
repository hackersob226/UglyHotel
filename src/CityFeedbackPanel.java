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
    Object[][] data;
    public CityFeedbackPanel(String city) {
        location = city;
        try {
            getData(HotelApp.con, HotelApp.dbname,location);
        } catch (SQLException ex) {
            System.out.println("Error");
        }
        
        add(new JLabel("Feedback for " + location));

        String[] col = {"Rating", "Comment"};

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

    public void getData(Connection con, String dbName, String city) throws SQLException {
        Statement stmt = null;
        String query = "SELECT Rating, Comment FROM HOTELREVIEW WHERE Location = \"" + city + "\"";
        
        try {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            ArrayList<String> tempList = new ArrayList<String>();
            ArrayList<String> tempList2 = new ArrayList<String>();

            while(rs.next())
            {
                tempList.add(rs.getString("Rating"));
                tempList2.add(rs.getString("Comment"));
            }

            Object[][] temp = new Object[tempList.size()][2];
            for (int i = 0; i < tempList.size(); i++) {
                temp[i][0] = tempList.get(i);
                temp[i][1] = tempList2.get(i);
            }
            data = temp;
            if (stmt != null) { 
                stmt.close();
            }
        } catch (SQLException e ) {
            System.out.println("Error");
        }
    }
}