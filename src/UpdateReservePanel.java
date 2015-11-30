import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.sql.*;
import javax.sql.*;
import java.text.SimpleDateFormat;

public class UpdateReservePanel extends JPanel {
    JTextField reserveID, newStartDate, newEndDate;
    JButton searchID, searchAvail;
    Calendar currStart, currEnd;
    JLabel currStartDisplay, currEndDisplay;
    Calendar newStart, newEnd; //User Inputted dates stored here
    int id = -1; //User Inputted Reservation ID here
    public UpdateReservePanel(){
        currStart = Calendar.getInstance();
        currEnd = Calendar.getInstance();
        newStart = Calendar.getInstance();
        newEnd = Calendar.getInstance();
        add(new JLabel("Search ID before searching new dates. "));
        reserveID = new JTextField(5);
        newStartDate = new JTextField(10);
        newEndDate = new JTextField(10);

        add(new JLabel("Reservation ID: "));
        add(reserveID);

        searchID = new JButton("Search ID");
        searchID.addActionListener(new ButtonListener("searchID"));
        add(searchID);

        add(new JLabel("Current Start Date: "));
        currStartDisplay = new JLabel("xxx");
        add(currStartDisplay);
        add(new JLabel("Current End Date: "));
        currEndDisplay = new JLabel("xxx");
        add(currEndDisplay);

        add(new JLabel("New Start Date"));
        add(newStartDate);
        add(new JLabel("New End Date"));
        add(newEndDate);

        searchAvail = new JButton("Search Availability");
        searchAvail.addActionListener(new ButtonListener("AvailableRooms"));
        add(searchAvail);
    }

    public void searchID() {
        try{
            id = Integer.parseInt(reserveID.getText());
        } catch (Exception e) {
            JOptionPane error = new JOptionPane();
            error.showMessageDialog(null, "Please check Reservation ID");
        }
        try {
            getData(HotelApp.con, HotelApp.dbname, id);
        } catch (SQLException ex) {
            System.out.println("Input Error");
        }
    }

    public boolean searchAvail() {
        //Literally copying this code from searchRooms
        boolean flag1 = false;
        boolean flag2 = false;
        String startD = newStartDate.getText();
        int index, index2, month, day, year;
        try {
            index = startD.indexOf("/");
            month = Integer.parseInt(startD.substring(0, index));
            index2 = startD.indexOf("/", index + 1);
            day = Integer.parseInt(startD.substring(index + 1, index2));
            year = Integer.parseInt(startD.substring(index2 + 1, startD.length()));
            newStart.set(year, month - 1, day);
            flag1 = true;
        } catch (Exception e) {
            JOptionPane error = new JOptionPane();
            error.showMessageDialog(null, "Please check Start Date input.");
        }
        System.out.println("Start: " + newStart.getTime()); //GET RID OF THIS LATER

        String endD = newEndDate.getText();
        try {
            index = endD.indexOf("/");
            month = Integer.parseInt(endD.substring(0, index));
            index2 = endD.indexOf("/", index + 1);
            day = Integer.parseInt(endD.substring(index + 1, index2));
            year = Integer.parseInt(endD.substring(index2 + 1, endD.length()));
            newEnd.set(year, month - 1, day);
            flag2 = true;
        } catch (Exception e) {
            JOptionPane error = new JOptionPane();
            error.showMessageDialog(null, "Please check End Date input.");
        }
        System.out.println("End: " + newEnd.getTime()); //GET RID OF THIS LATER

        HotelApp.startSearchReserveDate = newStart; //Sends the date as metaData
        HotelApp.endSearchReserveDate = newEnd;
        if (flag1 && flag2) {
            return true;
        }
        return false;
    }

    public void getData(Connection con, String dbName, int id) throws SQLException {
        Statement stmt = null;
        String query = "SELECT RESERVATION.StartDate, RESERVATION.EndDate  FROM RESERVATION WHERE ReservationID = \"" + id + "\"";
        try {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            ArrayList<java.sql.Date> start = new ArrayList<java.sql.Date>();
            ArrayList<java.sql.Date> end = new ArrayList<java.sql.Date>();

            while(rs.next())
            {
                start.add(rs.getDate("StartDate"));
                end.add(rs.getDate("EndDate"));
            }
            currStartDisplay.setText(""+start.get(0)+"");
            currEndDisplay.setText(""+end.get(0)+"");     
        } catch (SQLException e ) {
            System.out.println("Execution Error");
        }
    }    

    public class ButtonListener implements ActionListener {
        private String state;

        public ButtonListener(String currState) {
            state = currState;
        }

        public void actionPerformed(ActionEvent e) {
            if (state == "AvailableRooms") {
                if (id != -1) {
                    if (searchAvail()) {
                        HotelApp.createAvailRooms(id);
                        HotelApp.currentState = state;
                        HotelApp.checkState();
                    }
                } else {
                    JOptionPane error = new JOptionPane();
                    error.showMessageDialog(null, "Please Search for your Reservation ID first.");
                }
            } else if (state == "searchID") {
                searchID();
            }
        }
    }
}