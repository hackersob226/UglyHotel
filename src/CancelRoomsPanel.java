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
    JLabel startDate, endDate, totalCost, updateCost;
    Calendar today;
    int reserveId;
    Object[][] data;
    java.sql.Date startV;
    Double total;

    public CancelRoomsPanel(int id) {
        reserveId = id;
        startDate = new JLabel("");
        endDate = new JLabel("");
        totalCost = new JLabel("");
        updateCost = new JLabel("");
        try {
            getData(HotelApp.con, HotelApp.dbname, reserveId);
        } catch (SQLException ex) {
            System.out.println("Input Error");
        }
        today = Calendar.getInstance();
        SimpleDateFormat format1 = new SimpleDateFormat("MM-dd-yyyy");
        String[] col = {"Room Number", "Room Category", "Person Capacity",
                        "Cost per Day", "Cost of Extra Bed per Day", "Extra Bed?"};
        //Place canceled rooms in Object[][] data
        /*Object[][] data = { {new Integer(11), "Filler", new Integer(1), new Integer(111), new Integer(10), new Boolean(false)},
            {new Integer(11), "Filler", new Integer(1), new Integer(111), new Integer(10), new Boolean(false)}
        };*/
        //Insert Start/End date here.
        add(new JLabel("Start Date: "));
        add(startDate);
        add(new JLabel("End Date: "));
        add(endDate);

        table = new JTable(data, col);
        table.setPreferredScrollableViewportSize(new Dimension(HotelApp.WIDTH - 50, HotelApp.HEIGHT - 150));
        JScrollPane scrollPane = new JScrollPane(table);

        add(scrollPane);

        add(new JLabel("Total Cost of Reservation: "));
        add(totalCost);
        //Insert Total Cost here.

        add(new JLabel("Date of Cancellation: "));
        String date1 = format1.format(today.getTime());
        add(new JLabel(date1));

        calculateUpdatedCost();
        add(new JLabel("Amount to be refunded: "));
        add(updateCost);
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
            try {
                checkCancel(HotelApp.con, HotelApp.dbname, reserveId);
            } catch (SQLException ex) {
                System.out.println("Input Error");
            }
            JOptionPane confirm = new JOptionPane();
            confirm.showMessageDialog(null, "Reservation Cancelled.");
            HotelApp.currentState = state;
            HotelApp.checkState();
        }
    }

    public void calculateUpdatedCost() {
        int diff = -1;
        SimpleDateFormat format1 = new SimpleDateFormat("MM-dd-yyyy");
        String date2 = format1.format(today.getTime());
        String date1 = format1.format(startV.getTime());

        try {
            java.util.Date dateStart = format1.parse(date1);
            java.util.Date dateEnd = format1.parse(date2);
            diff = (int)Math.round((dateEnd.getTime() - dateStart.getTime()) / (double) 86400000);
            System.out.println(diff);
            //add(new JLabel("Number of days: " + diff));
        } catch (Exception e) {
            //hehe
        }
        if (diff <= 1) {
            updateCost.setText("$"+0+"");
        } else if (diff < 4) {
            updateCost.setText("$"+total.doubleValue() * 0.8+"");
        } else {
            updateCost.setText("$"+total.doubleValue()+"");
        }
    }

    public void checkCancel(Connection con, String dbName, int id) throws SQLException {
        PreparedStatement stmt = null;
        String query = "UPDATE RESERVATION SET IsCancelled = 1 WHERE ReservationID = \"" + id + "\"";
        try {
            con.setAutoCommit(false);
            stmt = con.prepareStatement(query);
            stmt.executeUpdate();
            con.commit();
        } catch (SQLException e ) {
            System.out.println("Execution Error");
        }
    }

    public void getData(Connection con, String dbName, int id) throws SQLException {
        Statement stmt = null;
        String query = "SELECT ROOM.RoomNum, ROOM.NumPersons, ROOM.RoomCategory, ROOM.CostPerDay, ROOM.CostofExtraBedPerDay, RESERVATIONHASROOM.IncludeExtraBed FROM ROOM INNER JOIN RESERVATIONHASROOM ON (ROOM.RoomNum = RESERVATIONHASROOM.RoomNum AND ROOM.Location = RESERVATIONHASROOM.Location) WHERE RESERVATIONHASROOM.ReservationID = \"" + id + "\"";
        try {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            ArrayList<Integer> roomNumList = new ArrayList<Integer>();
            ArrayList<Integer> numPersonList = new ArrayList<Integer>();
            ArrayList<String> roomCategoryList = new ArrayList<String>();
            ArrayList<Double> costPerDayList = new ArrayList<Double>();
            ArrayList<Double> costBedList = new ArrayList<Double>();
            ArrayList<Integer> extraBedList = new ArrayList<Integer>();

            while(rs.next())
            {
                roomNumList.add(rs.getInt("RoomNum"));
                numPersonList.add(rs.getInt("NumPersons"));
                roomCategoryList.add(rs.getString("RoomCategory"));
                costPerDayList.add(rs.getDouble("CostPerDay"));
                costBedList.add(rs.getDouble("CostofExtraBedPerDay"));
                extraBedList.add(rs.getInt("IncludeExtraBed"));
            }

            Object[][] temp = new Object[roomNumList.size()][6];
            for (int i = 0; i < roomNumList.size(); i++) {
                temp[i][0] = roomNumList.get(i);
                temp[i][1] = numPersonList.get(i);
                temp[i][2] = roomCategoryList.get(i);
                temp[i][3] = costPerDayList.get(i);
                temp[i][4] = costBedList.get(i);
                if (extraBedList.get(i) == 1) {
                    temp[i][5] = "Yes";
                } else {
                    temp[i][5] = "No";
                }
            }
            data = temp;
        } catch (SQLException e ) {
            System.out.println("Execution Error");
        }

        stmt = null;
        query = "SELECT RESERVATION.StartDate, RESERVATION.EndDate, RESERVATION.TotalCost FROM RESERVATION WHERE ReservationID = \"" + id + "\"";
        try {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            ArrayList<java.sql.Date> start = new ArrayList<java.sql.Date>();
            ArrayList<java.sql.Date> end = new ArrayList<java.sql.Date>();
            ArrayList<Double> cost = new ArrayList<Double>();
            //java.sql.Date start = new java.sql.Date(0), end = new java.sql.Date(0);
            //double cost = -1;
            while(rs.next())
            {
                start.add(rs.getDate("StartDate"));
                end.add(rs.getDate("EndDate"));
                cost.add(rs.getDouble("TotalCost"));
            }
            total = cost.get(0);
            startV = start.get(0);
            startDate.setText(""+start.get(0)+"");
            endDate.setText(""+end.get(0)+"");
            totalCost.setText("$"+cost.get(0)+"");
        } catch (SQLException e ) {
            System.out.println("Execution Error");
        }

        if (stmt != null) { 
            stmt.close();
        }
    }
}