import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.sql.*;
import javax.sql.*;

public class SearchRoomsPanel extends JPanel {
    String city = "Atlanta";
    private JTextField startDate, endDate;
    private JButton search;
    static ResultSet reservationTable;
    int rows;
    Calendar start, end; //Putting Start/end date in these variables when "Search" is clicked

    public SearchRoomsPanel() {
        start = Calendar.getInstance();
        end = Calendar.getInstance();

        rows = 0;

        startDate = new JTextField(10);
        endDate = new JTextField(10);

        String locations[] = {"Atlanta", "Charlotte", "Savannah", "Orlando", "Miami"};
        JComboBox dropDown = new JComboBox(locations);
        dropDown.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                city = (String) dropDown.getSelectedItem();
                System.out.printf(city); //GET RID OF THIS LATER
            }
        });
        add(dropDown);

        add(new JLabel("Start Date (mm/dd/yyyy):"));
        add(startDate);

        add(new JLabel("End Date (mm/dd/yyyy):"));
        add(endDate);

        search = new JButton ("Search");
        search.addActionListener(new ButtonListener("MakeReservation"));
        add(search);
    }
    
    public boolean getDate() {
        boolean flag1 = false;
        boolean flag2 = false;
        String startD = startDate.getText();
        int index, index2, month, day, year;
        try {
            index = startD.indexOf("/");
            month = Integer.parseInt(startD.substring(0, index));
            index2 = startD.indexOf("/", index + 1);
            day = Integer.parseInt(startD.substring(index + 1, index2));
            year = Integer.parseInt(startD.substring(index2 + 1, startD.length()));
            start.set(year, month - 1, day);
            flag1 = true;
        } catch (Exception e) {
            JOptionPane error = new JOptionPane();
            error.showMessageDialog(null, "Please check Start Date input.");
        }
        System.out.println("Start: " + start.getTime()); //GET RID OF THIS LATER
        
        String endD = endDate.getText();
        try {
            index = endD.indexOf("/");
            month = Integer.parseInt(endD.substring(0, index));
            index2 = endD.indexOf("/", index + 1);
            day = Integer.parseInt(endD.substring(index + 1, index2));
            year = Integer.parseInt(endD.substring(index2 + 1, endD.length()));
            end.set(year, month - 1, day);
            flag2 = true;
        } catch (Exception e) {
            JOptionPane error = new JOptionPane();
            error.showMessageDialog(null, "Please check End Date input.");
        }
        System.out.println("End: " + end.getTime()); //GET RID OF THIS LATER
        
        HotelApp.startSearchReserveDate = start; //Sends the date as metaData
        HotelApp.endSearchReserveDate = end;
        if (flag1 && flag2) {
            return true;
        }
        return false;
    }

    public class ButtonListener implements ActionListener {
        private String state;

        public ButtonListener(String currState) {
            state = currState;
        }

        public void actionPerformed(ActionEvent e) {
            if (getDate()) {

                java.sql.Date sqlStartDate = new java.sql.Date(HotelApp.startSearchReserveDate.getTime().getTime());
                java.sql.Date sqlEndDate = new java.sql.Date(HotelApp.endSearchReserveDate.getTime().getTime());

                System.out.println(sqlStartDate);
                System.out.println(sqlEndDate);

                try {
                    reservationTable = findRooms(HotelApp.con, HotelApp.dbname, sqlStartDate, sqlEndDate, city);
                } catch (SQLException ex) {
                    System.out.println("Error");
                }
                HotelApp.availableTable = reservationTable;
                HotelApp.createReservation();
                HotelApp.currentState = state;
                HotelApp.checkState();
            }
        }
    }

    public ResultSet findRooms(Connection con, String dbName, java.sql.Date begin, java.sql.Date ending, String loc) throws SQLException {
        Statement stmt = null;
        String query = "SELECT * FROM ROOM INNER JOIN RESERVATIONHASROOM ON (ROOM.RoomNum = RESERVATIONHASROOM.RoomNum AND ROOM.Location = RESERVATIONHASROOM.Location) INNER JOIN RESERVATION ON RESERVATION.ReservationID = RESERVATIONHASROOM.ReservationID WHERE ROOM.Location = \"" + loc + "\" AND (RESERVATION.StartDate > \"" + endDate + "\" AND RESERVATION.EndDate > \"" + endDate + "\") OR (RESERVATION.EndDate < \"" + startDate + "\" AND RESERVATION.StartDate < \"" + startDate + "\")";
        try {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            if(!rs.isBeforeFirst())
            {
                System.out.println("Could not find any available rooms.");
            }
            while(rs.next())
            {
                rows++;
                //System.out.println(rs.getInt("RoomNum") + ", " + rs.getString("RoomCategory") + ", " + rs.getInt("NumPersons") + ", " + rs.getFloat("CostPerDay") + ", " + rs.getFloat("CostOfExtraBedPerDay"));
            }
            HotelApp.numRows = rows;
            rs.beforeFirst();
            return rs;
        } catch (SQLException e ) {
            System.out.println(e.getMessage());
        }

        if (stmt != null) { 
            stmt.close();
        }

        return null;
    }
}

/* SELECT * FROM ROOM WHERE ROOM.RoomNum = RESERVATIONHASROOM.RoomNum
 * AND ROOM.Location = RESERVATIONHASROOM.Location AND
 * RESERVATIONHASROOM.ReservationID = RESERVATION.ReservationID AND
 * (RESERVATION.StartDate > endDate AND RESERVATION.EndDate > endDate) OR (RESERVATION.EndDate < startDate AND RESERVATION.StartDate < startDate)
 */